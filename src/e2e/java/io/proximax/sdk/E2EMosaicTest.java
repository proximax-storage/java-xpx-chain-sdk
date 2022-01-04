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

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicLevy;
import io.proximax.sdk.model.mosaic.MosaicLevyInfo;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.ModifyMosaicLevyTransaction;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.MosaicSupplyChangeTransaction;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.RemoveMosaicLevyTransaction;
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
            id = new MosaicId(
                        nonce,
                        PublicAccount.createFromPublicKey(seedAccount.getPublicKey(), getNetworkType()));
            signup(seedAccount.getAddress());
      }

      @Test
      void test01CreateMosaic() {
            logger.info("Creating new mosaic " + id.getIdAsHex() + " " + id.getIdAsLong() + " " + id.getId());
            nonce = MosaicNonce.createRandom();
            id = new MosaicId(
                        nonce,
                        PublicAccount.createFromPublicKey(seedAccount.getPublicKey(), getNetworkType()));
            SignedTransaction mdt = transact.mosaicDefinition()
                        .init(nonce, id, new MosaicProperties(true, true, 6, Optional.of(BigInteger.ZERO))).build()
                        .signWith(seedAccount, api.getNetworkGenerationHash());
            logger.info("mdt Payload. {}", mdt.getPayload());

            Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress()).timeout(
                        getTimeoutSeconds(),
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
            assertEquals(Optional.empty(), info.getDuration());
      }

      @Test
      void test02ChangeSupply() {
            logger.info("Changing supply");
            SignedTransaction supplychange = transact.mosaicSupplyChange().increase(id, BigInteger.TEN).build()
                        .signWith(seedAccount, api.getNetworkGenerationHash());
            transactionHttp.announce(supplychange).blockingFirst();
            logger.info("supplychange Payload. {}", supplychange.getPayload());

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
            SignedTransaction supplychange = transact.mosaicSupplyChange().decrease(id, BigInteger.ONE).build()
                        .signWith(seedAccount, api.getNetworkGenerationHash());
            transactionHttp.announce(supplychange).blockingFirst();
            logger.info("supplychange Payload. {}", supplychange.getPayload());

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
            MosaicNonce aNonce = MosaicNonce.createRandom();
            logger.info("Creating aggregate mosaic");
            MosaicId aId = new MosaicId(
                        aNonce, PublicAccount.createFromPublicKey(seedAccount.getPublicKey(), getNetworkType()));
            // create mosaic
            MosaicDefinitionTransaction create = transact.mosaicDefinition()
                        .init(aNonce, aId, new MosaicProperties(true, true, 6, Optional.of(BigInteger.valueOf(20))))
                        .build();
            // add supply of 10
            MosaicSupplyChangeTransaction increaseSupply = transact.mosaicSupplyChange().increase(aId, BigInteger.TEN)
                        .build();
            // remove supply of 1
            MosaicSupplyChangeTransaction decreaseSupply = transact.mosaicSupplyChange().decrease(aId, BigInteger.ONE)
                        .build();
            // create aggregate transaction
            AggregateTransaction aggregateTransaction = transact.aggregateComplete()
                        .innerTransactions(create.toAggregate(seedAccount.getPublicAccount()),
                                    increaseSupply.toAggregate(seedAccount.getPublicAccount()),
                                    decreaseSupply.toAggregate(seedAccount.getPublicAccount()))
                        .build();
            // sign the transaction
            SignedTransaction signedTransaction = api.sign(aggregateTransaction, seedAccount);
            logger.info("signedTransaction Payload. {}", signedTransaction.getPayload());

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
            MosaicId aId = new MosaicId(
                        aNonce,
                        PublicAccount.createFromPublicKey(seedAccount.getPublicKey(), getNetworkType()));
            logger.info("Creating new mosaic {}", aId.getIdAsHex());
            SignedTransaction mdt = transact.mosaicDefinition()
                        .init(aNonce, aId, new MosaicProperties(true, true, 6, Optional.empty())).build()
                        .signWith(seedAccount, api.getNetworkGenerationHash());
            Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress())
                        .timeout(getTimeoutSeconds(), TimeUnit.SECONDS);
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

      @Test
      void test06MosaicLevy() {
            MosaicNonce aNonce = MosaicNonce.createRandom();
            MosaicId aId = new MosaicId(
                        aNonce,
                        PublicAccount.createFromPublicKey(seedAccount.getPublicKey(), getNetworkType()));

            logger.info("Creating mosaic " + aId.getIdAsHex());

            // create mosaic
            MosaicDefinitionTransaction mosaicDefinition = transact.mosaicDefinition()
                        .init(aNonce, aId, new MosaicProperties(true, true, 0, Optional.of(BigInteger.valueOf(100))))
                        .build();
            // add supply of 10000
            MosaicSupplyChangeTransaction increaseSupply = transact.mosaicSupplyChange()
                        .increase(aId, BigInteger.valueOf(
                                    100))
                        .build();

            // create aggregate transaction
            AggregateTransaction aggregateTransaction = transact.aggregateComplete()
                        .innerTransactions(mosaicDefinition.toAggregate(seedAccount.getPublicAccount()),
                                    increaseSupply.toAggregate(
                                                seedAccount.getPublicAccount()))
                        .build();
            // sign the transaction
            SignedTransaction signedAggregateTransaction = api.sign(aggregateTransaction, seedAccount);
            // announce the request
            transactionHttp.announce(signedAggregateTransaction).blockingFirst();
            // wait for acceptance
            logger.info("SignedAggregateTransaction done {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();
            Recipient receipient = Recipient
                        .from(seedAccount.getAddress());
            var levy = MosaicLevy.createWithAbsoluteFee(receipient, mosaicDefinition.getMosaicId(), 100);
            ModifyMosaicLevyTransaction mosaicLevyTransaction = transact.modifyMosaicLevy()
                        .create(levy, mosaicDefinition.getMosaicId()).build();

            // .signWith(seedAccount, api.getNetworkGenerationHash());
            SignedTransaction signed = api.sign(mosaicLevyTransaction, seedAccount);
            logger.info("Mosaic Levy hash: {}", signed.getHash());

            // announce the request
            transactionHttp.announce(signed).blockingFirst();

            // wait for acceptance
            logger.info("Mosaic Levy Transaction done: {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();
            // verify that mosaic looks fine
            MosaicLevyInfo info = mosaicHttp.getMosaicLevyInfo(mosaicDefinition.getMosaicId()).blockingFirst();
            assertEquals(receipient.getAddress(), info.getRecipient().getAddress());
            sleepForAWhile();
            // ---------------------------------------remove mosaic levy---------------------------------------//
            RemoveMosaicLevyTransaction removeMosaicLevyTransaction = transact.removeMosaicLevy().create(aId).build();
            SignedTransaction signedTransaction = api.sign(removeMosaicLevyTransaction, seedAccount);
            transactionHttp.announce(signedTransaction).blockingFirst();
            logger.info("Remove Mosaic Levy getHash: {}", signedTransaction.getHash());
            sleepForAWhile();
            logger.info("Remove Mosaic Levy Transaction done: {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
      }
}
