/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.Hashes;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PublicKey;
import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.contract.Contract;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.ModifyContractTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * E2E tests that demonstrate contracts
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EContractTest extends E2EBaseTest {

   private Account customer;
   private Account accContract;
   private Account executor1;
   private Account executor2;
   private Account validator1;
   private Account validator2;
   private String contentHash;

   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EContractTest.class);

   @BeforeAll
   void initStuff() {
      customer = new Account(new KeyPair(), getNetworkType());
      accContract = new Account(new KeyPair(), getNetworkType());
      executor1 = new Account(new KeyPair(), getNetworkType());
      executor2 = new Account(new KeyPair(), getNetworkType());
      validator1 = new Account(new KeyPair(), getNetworkType());
      validator2 = new Account(new KeyPair(), getNetworkType());
      // create random content and hash it
      contentHash = HexEncoder.getString(Hashes.sha3_256(RandomUtils.nextBytes(20)));
   }

   @AfterAll
   void closeDown() {

   }

   @Test
   void prepare() {
      signup(accContract.getAddress());
      logger.info("Going to create contract for {}", accContract);
      logger.info("customers {}", customer);
      logger.info("executors {}, {}", executor1, executor2);
      logger.info("validators {}, {}", validator1, validator2);
   }

   @Test
   void test02CreateContract() {
      logger.info("Creating contract");
      // prepare transaction
      ModifyContractTransaction trans = ModifyContractTransaction.create(getDeadline(),
            BigInteger.ZERO,
            BigInteger.valueOf(100l),
            contentHash,
            Arrays.asList(MultisigCosignatoryModification.add(customer.getPublicAccount())),
            Arrays.asList(
                  MultisigCosignatoryModification.add(executor1.getPublicAccount()),
                  MultisigCosignatoryModification.add(executor2.getPublicAccount())),
            Arrays.asList(
                  MultisigCosignatoryModification.add(validator1.getPublicAccount()),
                  MultisigCosignatoryModification.add(validator2.getPublicAccount())),
            getNetworkType());
      // sign the transaction
      SignedTransaction signedTrans = api.sign(trans, accContract);
      // announce the transaction
      transactionHttp.announce(signedTrans).blockingFirst();
      ModifyContractTransaction contractConfirmation = (ModifyContractTransaction)listener.confirmed(accContract.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      sleepForAWhile();
      logger.info("Got contract confirmed: {}", contractConfirmation);
      // do couple sanity checks
      assertEquals(1, contractConfirmation.getCustomersModifications().size());
      assertEquals(2, contractConfirmation.getExecutorsModifications().size());
      assertEquals(2, contractConfirmation.getVerifiersModifications().size());
      assertEquals(contentHash.toLowerCase(), contractConfirmation.getContentHash().toLowerCase());
      assertEquals(BigInteger.valueOf(100l), contractConfirmation.getDurationDelta());
      // now retrieve the contract from the account
      sleepForAWhile();
      Contract contract = contractHttp.getContract(accContract.getAddress()).blockingFirst();
      // check that contract meets expectations
      assertEquals(1, contract.getCustomers().size());
      assertEquals(2, contract.getExecutors().size());
      assertEquals(2, contract.getVerifiers().size());
      assertEquals(contentHash.toLowerCase(), contract.getContentHash().toLowerCase());
      assertEquals(BigInteger.valueOf(100l), contract.getDuration());
   }

   @Test
   void test03ChangeExistingContract() {
      logger.info("Changing contract");
      // prepare transaction - add one block to duration, move executor2 to verifiers
      ModifyContractTransaction trans = ModifyContractTransaction.create(getDeadline(),
            BigInteger.ZERO,
            BigInteger.ONE,
            contentHash,
            Arrays.asList(),
            Arrays.asList(
                  MultisigCosignatoryModification.remove(executor2.getPublicAccount())),
            Arrays.asList(
                  MultisigCosignatoryModification.add(executor2.getPublicAccount())),
            getNetworkType());
      // create aggregate transaction for the multisig
      AggregateTransaction aggregateTrans = AggregateTransaction.createComplete(getDeadline(),
            Arrays.asList(trans.toAggregate(accContract.getPublicAccount())),
            getNetworkType());
      // sign the transaction
      SignedTransaction signedTrans = validator1.signTransactionWithCosignatories(aggregateTrans, api.getNetworkGenerationHash(),
            Arrays.asList(validator2));
      // announce the transaction
      transactionHttp.announce(signedTrans).blockingFirst();
      listener.confirmed(accContract.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      sleepForAWhile();
      // now retrieve the contract from the account
      Contract contract = contractHttp.getContract(accContract.getAddress()).blockingFirst();
      // check that contract meets expectations
      assertEquals(1, contract.getCustomers().size());
      assertEquals(1, contract.getExecutors().size());
      assertEquals(3, contract.getVerifiers().size());
      assertEquals(contentHash.toLowerCase(), contract.getContentHash().toLowerCase());
      assertEquals(BigInteger.valueOf(101l), contract.getDuration());
   }
   
   @Test
   void test04CompareContractEndpoints() {
      // prepare public key
      PublicKey pubKey = PublicKey.fromHexString(accContract.getPublicKey());
      // make requests
      Contract contractByAddress = contractHttp.getContract(accContract.getAddress()).blockingFirst();
      Contract contractByPubKey = contractHttp.getContract(pubKey).blockingFirst();
      List<Contract> contractsByAddresses = contractHttp.getContracts(accContract.getAddress()).blockingFirst();
      List<Contract> contractsByPubKeys = contractHttp.getContracts(pubKey).blockingFirst();
      // expect one contract for one address
      assertEquals(1, contractsByAddresses.size());
      assertEquals(1, contractsByPubKeys.size());
      // check for response equality
      assertEquals(contractByAddress, contractByPubKey);
      assertEquals(contractByAddress, contractsByAddresses.get(0));
      assertEquals(contractByAddress, contractsByPubKeys.get(0));
      
   }
}
