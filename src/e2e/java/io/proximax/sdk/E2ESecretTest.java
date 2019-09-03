/*
 * Copyright 2018 NEM
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
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.HashType;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.SecretLockTransaction;
import io.proximax.sdk.model.transaction.SecretProofTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * Tests for secure lock and secure proof that can be used for cross-chain swaps
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2ESecretTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ESecretTest.class);

   private final Account simpleAccount = new Account(new KeyPair(), getNetworkType());

   @BeforeAll
   void addListener() {
      logger.info("Will be sending transactions to {}", simpleAccount);
      listener.status(simpleAccount.getAddress()).doOnNext(err -> logger.info("Error on recipient: {}", err))
            .doOnError(exception -> logger.error("Failure on recipient: {}", exception))
            .doOnComplete(() -> logger.info("done with recipient {}", simpleAccount));
   }

   @Test
   void standaloneSecretLockAndProofTransaction_SHA3_256() throws ExecutionException, InterruptedException {
      standaloneSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.SHA3_256);
   }

   @Test
   void standaloneSecretLockAndProofTransaction_KECCAK_256() throws ExecutionException, InterruptedException {
      standaloneSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.KECCAK_256);
   }

   @Test
   void standaloneSecretLockAndProofTransaction_HASH_160() throws ExecutionException, InterruptedException {
      standaloneSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.HASH_160);
   }

   @Test
   void standaloneSecretLockAndProofTransaction_HASH_256() throws ExecutionException, InterruptedException {
      standaloneSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.HASH_256);
   }

   void standaloneSecretLockAndProofTransaction(Account from, Address to, HashType hashType) {
      logger.info("Creating standalone lock and proof for {}", hashType);
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = hashType.hashValue(secretBytes);
      String secret = Hex.toHexString(result);
      String proof = Hex.toHexString(secretBytes);
      // make a secret lock moving mosaic to the target account
      SecretLockTransaction secretLocktx = transact.secretLock().mosaic(NetworkCurrencyMosaic.ONE)
            .duration(BigInteger.valueOf(10)).hashType(hashType).secret(secret).recipient(to).build();
      SignedTransaction secretLockTransactionSigned = api.sign(secretLocktx, from);
      transactionHttp.announce(secretLockTransactionSigned).blockingFirst();
      logger.info("Lock confirmed: {}",
            listener.confirmed(from.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
      SecretProofTransaction secretProoftx = transact.secretProof().hashType(hashType).secret(secret).proof(proof)
            .recipient(Recipient.from(to)).build();
      SignedTransaction secretProoftxSigned = api.sign(secretProoftx, from);
      transactionHttp.announce(secretProoftxSigned).blockingFirst();
      logger.info("Proof confirmed: {}",
            listener.confirmed(from.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
   }

   @Test
   void aggregateSecretLockAndProofTransaction_SHA3_256() {
      aggregateSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.SHA3_256);
   }

   @Test
   void aggregateSecretLockandProofTransaction_KECCAK_256() {
      aggregateSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.KECCAK_256);
   }

   @Test
   void aggregateSecretLockAndProofTransaction_HASH_160() {
      aggregateSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.HASH_160);
   }

   @Test
   void aggregateSecretLockAndProofTransaction_HASH_256() {
      aggregateSecretLockAndProofTransaction(seedAccount, simpleAccount.getAddress(), HashType.HASH_256);
   }

   void aggregateSecretLockAndProofTransaction(Account from, Address to, HashType hashType) {
      logger.info("Creating aggregate lock and proof for {}", hashType);
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = hashType.hashValue(secretBytes);
      String secret = Hex.toHexString(result);
      String proof = Hex.toHexString(secretBytes);
      // make a secret lock moving mosaic to the target account
      SecretLockTransaction secretLocktx = transact.secretLock().mosaic(NetworkCurrencyMosaic.ONE)
            .duration(BigInteger.valueOf(10)).hashType(hashType).secret(secret).recipient(to).build();
      SignedTransaction lockFundsTransactionSigned = api.sign(secretLocktx, from);
      transactionHttp.announce(lockFundsTransactionSigned).blockingFirst();
      logger.info("Lock confirmed: {}",
            listener.confirmed(from.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();

      // create aggregate proof
      SecretProofTransaction secretProoftx = transact.secretProof().hashType(hashType).secret(secret).proof(proof)
            .recipient(Recipient.from(to)).build();
      AggregateTransaction secretProofAggregatetx = transact.aggregateComplete()
            .innerTransactions(secretProoftx.toAggregate(from.getPublicAccount())).build();
      SignedTransaction secretProofTransactionSigned = api.sign(secretProofAggregatetx, from);
      transactionHttp.announce(secretProofTransactionSigned).blockingFirst();

      logger.info("Proof confirmed: {}",
            listener.confirmed(from.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
   }
}
