/*
 * Copyright 2019 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.sdk;

import java.math.BigInteger;
import java.util.Arrays;
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
import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.transaction.ModifyContractTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * E2E tests that demonstrate contracts
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EContractTest extends E2EBaseTest {

   private Account customer;
   private Account contract;
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
      contract = new Account(new KeyPair(), getNetworkType());
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
      signup(contract.getAddress());
   }

   @Test
   void test01ChangeContractAccountToMultisig() {

   }

   @Test
   void test02CreateContract() {
      // prepre transaction
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
      SignedTransaction signedTrans = contract.sign(trans);
      // announce the transaction
      transactionHttp.announce(signedTrans).blockingFirst();
      listener.confirmed(contract.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
   }

   @Test
   void test03ChangeExistingContract() {

   }
}
