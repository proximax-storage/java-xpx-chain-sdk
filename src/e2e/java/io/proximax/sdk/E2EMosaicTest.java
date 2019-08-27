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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.MosaicSupplyChangeTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.reactivex.Observable;

/**
 * E2E tests that demonstrate transfers
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EMosaicTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EMosaicTest.class);

   private MosaicNonce nonce;
   private MosaicId id;

   @BeforeAll
   void prepareMosaicInfo() {
   }

   @AfterAll
   void closeDown() {
   }

   @Test
   void test00PrepareMosaicId() {
      logger.info("Generating secure mosaic ID");
      nonce = MosaicNonce.createRandom();
      id = new MosaicId(nonce, seedAccount.getPublicKey());
      signup(seedAccount.getAddress());
   }

   @Test
   void test01CreateMosaic() {
      logger.info("Creating new mosaic");
      SignedTransaction mdt = transact.mosaicDefinition().nonce(nonce).mosaicId(id)
            .mosaicProperties(new MosaicProperties(true, true, 6, Optional.of(BigInteger.valueOf(20)))).build()
            .signWith(seedAccount, api.getNetworkGenerationHash());
      Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(),
            TimeUnit.SECONDS);
      transactionHttp.announce(mdt).blockingFirst();
      logger.info("Mosaic created. {}", confirmation.blockingFirst());
      sleepForAWhile();
      // verify that mosaic looks fine
      MosaicInfo info = mosaicHttp.getMosaic(id).blockingFirst();
      assertEquals(BigInteger.ZERO, info.getSupply());
      // check properties
      assertEquals(true, info.isSupplyMutable());
      assertEquals(true, info.isTransferable());
      assertEquals(6, info.getDivisibility());
      assertEquals(Optional.of(BigInteger.valueOf(20)), info.getDuration());
   }

   @Test
   void test02ChangeSupply() {
      logger.info("Changing supply");
      SignedTransaction supplychange = transact.mosaicSupplyChange().increaseSupplyFor(id).delta(BigInteger.TEN).build()
            .signWith(seedAccount, api.getNetworkGenerationHash());
      transactionHttp.announce(supplychange).blockingFirst();
      logger.info("Supply changed. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      // verify that mosaic looks fine
      sleepForAWhile();
      MosaicInfo info = mosaicHttp.getMosaic(id).blockingFirst();
      assertEquals(BigInteger.TEN, info.getSupply());
   }

   @Test
   void test03DecreaseSupply() {
      logger.info("Changing supply");
      SignedTransaction supplychange = transact.mosaicSupplyChange().decreaseSupplyFor(id).delta(BigInteger.ONE).build()
            .signWith(seedAccount, api.getNetworkGenerationHash());
      transactionHttp.announce(supplychange).blockingFirst();
      logger.info("Supply changed. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      // verify that mosaic looks fine
      sleepForAWhile();
      MosaicInfo info = mosaicHttp.getMosaic(id).blockingFirst();
      assertEquals(BigInteger.valueOf(9), info.getSupply());
   }

   @Test
   void test04AggregateMosaic() {
      logger.info("Creating aggregate mosaic");
      MosaicNonce aNonce = MosaicNonce.createRandom();
      MosaicId aId = new MosaicId(aNonce, seedAccount.getPublicKey());
      // create mosaic
      MosaicDefinitionTransaction create = transact.mosaicDefinition().nonce(aNonce).mosaicId(aId)
            .mosaicProperties(new MosaicProperties(true, true, 6, Optional.of(BigInteger.valueOf(20)))).build();
      // add supply of 10
      MosaicSupplyChangeTransaction increaseSupply = transact.mosaicSupplyChange().increaseSupplyFor(aId)
            .delta(BigInteger.TEN).build();
      // remove supply of 1
      MosaicSupplyChangeTransaction decreaseSupply = transact.mosaicSupplyChange().decreaseSupplyFor(aId)
            .delta(BigInteger.ONE).build();
      // create aggregate transaction
      AggregateTransaction aggregateTransaction = transact.aggregateComplete()
            .innerTransactions(create.toAggregate(seedAccount.getPublicAccount()),
                  increaseSupply.toAggregate(seedAccount.getPublicAccount()),
                  decreaseSupply.toAggregate(seedAccount.getPublicAccount()))
            .build();
      // sign the transaction
      SignedTransaction signedTransaction = api.sign(aggregateTransaction, seedAccount);
      // announce the request
      transactionHttp.announce(signedTransaction).blockingFirst();
      // wait for acceptance
      logger.info("Aggregate mosaic done. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      sleepForAWhile();
      // verify that mosaic looks fine
      MosaicInfo info = mosaicHttp.getMosaic(aId).blockingFirst();
      assertEquals(BigInteger.valueOf(9), info.getSupply());
   }

   @Test
   void test05CreateMosaicWithoutDuration() {
      MosaicNonce aNonce = MosaicNonce.createRandom();
      MosaicId aId = new MosaicId(aNonce, seedAccount.getPublicKey());
      logger.info("Creating new mosaic {}", aId.getIdAsHex());
      SignedTransaction mdt = transact.mosaicDefinition().nonce(aNonce).mosaicId(aId)
            .mosaicProperties(new MosaicProperties(true, true, 6, Optional.empty())).build()
            .signWith(seedAccount, api.getNetworkGenerationHash());
      Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(),
            TimeUnit.SECONDS);
      transactionHttp.announce(mdt).blockingFirst();
      logger.info("Mosaic created. {}", confirmation.blockingFirst());
      sleepForAWhile();
      // verify that mosaic looks fine
      MosaicInfo info = mosaicHttp.getMosaic(aId).blockingFirst();
      assertEquals(BigInteger.ZERO, info.getSupply());
      // check properties
      assertEquals(true, info.isSupplyMutable());
      assertEquals(true, info.isTransferable());
      assertEquals(6, info.getDivisibility());
      assertEquals(Optional.empty(), info.getDuration());
   }

}
