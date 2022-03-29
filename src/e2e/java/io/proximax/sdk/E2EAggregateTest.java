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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.proximax.sdk.model.transaction.CosignatureTransaction;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * E2E tests that demonstrate non-multisig agrregate transactions
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EAggregateTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EAggregateTest.class);

   private final Account alice = new Account(new KeyPair(), getNetworkType());
   private final Account bob = new Account(new KeyPair(), getNetworkType());
   private final Account mike = new Account(new KeyPair(), getNetworkType());

   private final MosaicId mosaicX = new MosaicId(UInt64Utils.fromLongArray(new long[] { 1173232007, 1792145974 }));
   private final MosaicId mosaicY = new MosaicId(UInt64Utils.fromLongArray(new long[] { 519256100, 642862634 }));

   @BeforeAll
   void addListener() {
      disposables.add(
            listener.status(alice.getAddress()).subscribe(err -> logger.error("Operation failed for alice: {}", err),
                  t -> logger.error("exception thrown", t)));
      disposables
            .add(listener.status(bob.getAddress()).subscribe(err -> logger.error("Operation failed for bob: {}", err),
                  t -> logger.error("exception thrown", t)));
      disposables
            .add(listener.status(mike.getAddress()).subscribe(err -> logger.error("Operation failed for mike: {}", err),
                  t -> logger.error("exception thrown", t)));
      // send funds to alice and bob
      logger.info("Mosaic X: {}", mosaicX.getIdAsHex());
      logger.info("Mosaic Y: {}", mosaicY.getIdAsHex());
      logger.info("Network currency is: {}", NetworkCurrencyMosaic.ID.getIdAsHex());
      // signup for all events for addresses
      signup(alice.getAddress());
      signup(bob.getAddress());
      signup(mike.getAddress());
   }

   @AfterAll
   void closeDown() {
      logger.info("cleaning up after tests");
      // return all back to seed account
      returnAllToSeed(alice);
      returnAllToSeed(bob);
      returnAllToSeed(mike);
   }

   @Test
   void escrowBetweenTwoParties() {
      returnAllToSeed(alice);
      returnAllToSeed(bob);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.TEN);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.ONE);
      sendMosaic(seedAccount, bob.getAddress(), new Mosaic(mosaicY, BigInteger.TEN));
      logger.info("Escrow between {} and {}", alice, bob);
      // send mosaic X from alice to bob
      TransferTransaction aliceToBob = transact.transfer().mosaics(NetworkCurrencyMosaic.ONE).to(bob.getAddress())
            .build();
      // send mosaic Y from bob to alice
      TransferTransaction bobToAlice = transact.transfer().mosaics(new Mosaic(mosaicY, BigInteger.TEN))
            .to(alice.getAddress()).build();
      // aggregate bonded with the 2 transactions - escrow
      AggregateTransaction escrow = transact.aggregateBonded()
            .innerTransactions(aliceToBob.toAggregate(alice.getPublicAccount()),
                  bobToAlice.toAggregate(bob.getPublicAccount()))
            .build();
      // alice sign the escrow trans
      SignedTransaction signedEscrow = api.sign(escrow, alice);
      // lock funds for escrow
      LockFundsTransaction lock = transact.lockFunds().mosaic(NetworkCurrencyMosaic.TEN)
            .duration(BigInteger.valueOf(480)).signedTransaction(signedEscrow).build();
      // alice sign and announce the lock
      logger.info("announcing {}", lock);
      SignedTransaction signedLock = api.sign(lock, alice);
      logger.info("SignedLock hash {}", signedLock.getHash());

      transactionHttp.announce(signedLock).blockingFirst();
      // wait for lock confirmation
      logger.info("got confirmation: {}",
            listener.confirmed(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
      // announce escrow
      logger.info("announcing {}", escrow);
      transactionHttp.announceAggregateBonded(signedEscrow).blockingFirst();
      // wait for escrow confirmation
      listener.aggregateBondedAdded(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();

      // bob sign the escrow
      AggregateTransaction pendingEscrow = accountHttp.aggregateBondedTransactions(bob.getPublicAccount())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst().get(0);
      CosignatureSignedTransaction signedCosig = CosignatureTransaction.create(pendingEscrow).signWith(bob);
      // bob announce the cosignature
      logger.info("announcing escrow");
      transactionHttp.announceAggregateBondedCosignature(signedCosig).blockingFirst();
      // bob wait for cosignature
      listener.confirmed(bob.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // test that alice has 10M X and 10 Y
      sleepForAWhile();
      List<Mosaic> aliceMosaics = accountHttp.getAccountInfo(alice.getAddress()).blockingFirst().getMosaics();
      assertEquals(2, aliceMosaics.size());
      aliceMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(10_000_000), mosaic.getAmount());
         } else if (mosaic.getId().getId().equals(mosaicY.getId())) {
            assertEquals(BigInteger.TEN, mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
      // test that bob got the 1M X
      List<Mosaic> bobMosaics = accountHttp.getAccountInfo(bob.getAddress()).blockingFirst().getMosaics();
      assertEquals(1, bobMosaics.size());
      bobMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(1_000_000), mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
   }

   @Test
   void escrowBetweenTwoPartiesComplete() {
      returnAllToSeed(alice);
      returnAllToSeed(bob);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.TEN);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.ONE);
      sendMosaic(seedAccount, bob.getAddress(), new Mosaic(mosaicY, BigInteger.TEN));
      logger.info("Escrow between {} and {}", alice, bob);
      // send mosaic X from alice to bob
      TransferTransaction aliceToBob = transact.transfer().mosaics(NetworkCurrencyMosaic.ONE).to(bob.getAddress())
            .build();
      // send mosaic Y from bob to alice
      TransferTransaction bobToAlice = transact.transfer().mosaics(new Mosaic(mosaicY, BigInteger.TEN))
            .to(alice.getAddress()).build();
      // aggregate bonded with the 2 transactions - escrow
      AggregateTransaction escrow = transact.aggregateComplete()
            .innerTransactions(aliceToBob.toAggregate(alice.getPublicAccount()),
                  bobToAlice.toAggregate(bob.getPublicAccount()))
            .build();
      // alice sign the escrow trans
      SignedTransaction signedEscrow = api.signWithCosigners(escrow, alice, Arrays.asList(bob));
      // announce escrow
      logger.info("announcing {}", escrow);
      transactionHttp.announce(signedEscrow).blockingFirst();
      // wait for escrow confirmation
      listener.confirmed(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // test that alice has 10M X and 10 Y
      sleepForAWhile();
      List<Mosaic> aliceMosaics = accountHttp.getAccountInfo(alice.getAddress()).blockingFirst().getMosaics();
      assertEquals(2, aliceMosaics.size());
      aliceMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(10_000_000), mosaic.getAmount());
         } else if (mosaic.getId().getId().equals(mosaicY.getId())) {
            assertEquals(BigInteger.TEN, mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
      // test that bob got the 1M X
      List<Mosaic> bobMosaics = accountHttp.getAccountInfo(bob.getAddress()).blockingFirst().getMosaics();
      assertEquals(1, bobMosaics.size());
      bobMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(1_000_000), mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
   }

   @Test
   void escrowBetweenThreeParties() {
      returnAllToSeed(alice);
      returnAllToSeed(bob);
      returnAllToSeed(mike);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.TEN);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.ONE);
      sendMosaic(seedAccount, bob.getAddress(), new Mosaic(mosaicY, BigInteger.TEN));
      sendMosaic(seedAccount, mike.getAddress(), NetworkCurrencyMosaic.ONE);
      sleepForAWhile();
      logger.info("Escrow between {}, {} and {}", alice, bob, mike);
      // send mosaic X from alice to bob
      TransferTransaction aliceToBob = transact.transfer().mosaics(NetworkCurrencyMosaic.ONE).to(bob.getAddress())
            .build();
      // send mosaic Y from bob to alice
      TransferTransaction bobToAlice = transact.transfer().mosaics(new Mosaic(mosaicY, BigInteger.TEN))
            .to(alice.getAddress()).build();
      // send mosaic Y from bob to alice
      TransferTransaction mikeToAlice = transact.transfer().mosaics(NetworkCurrencyMosaic.ONE).to(alice.getAddress())
            .build();
      // aggregate bonded with the 3 transactions - escrow
      AggregateTransaction escrow = transact.aggregateBonded()
            .innerTransactions(aliceToBob.toAggregate(alice.getPublicAccount()),
                  bobToAlice.toAggregate(bob.getPublicAccount()),
                  mikeToAlice.toAggregate(mike.getPublicAccount()))
            .build();
      // alice sign the escrow trans
      SignedTransaction signedEscrow = api.sign(escrow, alice);
      // lock funds for escrow
      LockFundsTransaction lock = transact.lockFunds().mosaic(NetworkCurrencyMosaic.TEN)
            .duration(BigInteger.valueOf(480)).signedTransaction(signedEscrow).build();
      // alice sign and announce the lock
      logger.info("announcing {}", lock);
      SignedTransaction signedLock = api.sign(lock, alice);
      logger.info("SignedLock hash {}", signedLock.getHash());
      transactionHttp.announce(signedLock).blockingFirst();
      // wait for lock confirmation
      listener.confirmed(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      sleepForAWhile();
      // announce escrow
      logger.info("announcing {}", escrow);
      transactionHttp.announceAggregateBonded(signedEscrow).blockingFirst();
      // wait for escrow confirmation
      listener.aggregateBondedAdded(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();

      // bob sign the escrow
      AggregateTransaction pendingEscrowBob = accountHttp.aggregateBondedTransactions(bob.getPublicAccount())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst().get(0);
      CosignatureSignedTransaction signedCosigBob = CosignatureTransaction.create(pendingEscrowBob).signWith(bob);
      // bob announce the cosignature
      logger.info("announcing cosig bob");
      transactionHttp.announceAggregateBondedCosignature(signedCosigBob).blockingFirst();
      // wait for cosig event
      listener.cosignatureAdded(bob.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();

      // mike sign the escrow
      AggregateTransaction pendingEscrowMike = accountHttp.aggregateBondedTransactions(mike.getPublicAccount())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst().get(0);
      CosignatureSignedTransaction signedCosigMike = CosignatureTransaction.create(pendingEscrowMike).signWith(mike);
      // mike announce the cosignature
      logger.info("announcing cosig mike");
      transactionHttp.announceAggregateBondedCosignature(signedCosigMike).blockingFirst();
      // wait for cosig event
      listener.cosignatureAdded(mike.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();

      logger.info("waiting for transaction confirmation");
      // wait for the transaction confirmation
      listener.confirmed(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      sleepForAWhile();
      // test that alice has 11M X and 10 Y
      List<Mosaic> aliceMosaics = accountHttp.getAccountInfo(alice.getAddress()).blockingFirst().getMosaics();
      assertEquals(2, aliceMosaics.size());
      aliceMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(11_000_000), mosaic.getAmount());
         } else if (mosaic.getId().getId().equals(mosaicY.getId())) {
            assertEquals(BigInteger.TEN, mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
      // test that bob got the 1M X
      List<Mosaic> bobMosaics = accountHttp.getAccountInfo(bob.getAddress()).blockingFirst().getMosaics();
      assertEquals(1, bobMosaics.size());
      bobMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(1_000_000), mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
      // test that mike has nothing
      List<Mosaic> mikeMosaics = accountHttp.getAccountInfo(mike.getAddress()).blockingFirst().getMosaics();
      assertTrue(mikeMosaics.isEmpty());
   }

   @Test
   void askforMoney() {
      returnAllToSeed(alice);
      returnAllToSeed(bob);
      sendMosaic(seedAccount, alice.getAddress(), NetworkCurrencyMosaic.TEN);
      sendMosaic(seedAccount, bob.getAddress(), new Mosaic(mosaicY, BigInteger.TEN));
      logger.info("Alice asks for money");
      // send mosaic X from alice to bob
      TransferTransaction aliceToBob = transact.transfer().to(bob.getAddress())
            .message(PlainMessage.create("send me 10 Y")).build();
      // send mosaic Y from bob to alice
      TransferTransaction bobToAlice = transact.transfer().mosaics(new Mosaic(mosaicY, BigInteger.TEN))
            .to(alice.getAddress()).build();
      // aggregate bonded with the 2 transactions - escrow
      AggregateTransaction escrow = transact.aggregateBonded()
            .innerTransactions(aliceToBob.toAggregate(alice.getPublicAccount()),
                  bobToAlice.toAggregate(bob.getPublicAccount()))
            .build();
      // alice sign the escrow trans
      SignedTransaction signedEscrow = api.sign(escrow, alice);
      // lock funds for escrow
      LockFundsTransaction lock = transact.lockFunds().mosaic(NetworkCurrencyMosaic.TEN)
            .duration(BigInteger.valueOf(480)).signedTransaction(signedEscrow).build();
      // alice sign and announce the lock
      logger.info("announcing {}", lock);
      SignedTransaction signedLock = api.sign(lock, alice);
      logger.info("SignedLock hash {}", signedLock.getHash());
      transactionHttp.announce(signedLock).blockingFirst();
      // wait for lock confirmation
      listener.confirmed(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      sleepForAWhile();
      // announce escrow
      logger.info("announcing {}", escrow);
      transactionHttp.announceAggregateBonded(signedEscrow).blockingFirst();
      // wait for escrow confirmation
      listener.aggregateBondedAdded(alice.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // bob sign the escrow
      AggregateTransaction pendingEscrow = accountHttp.aggregateBondedTransactions(bob.getPublicAccount())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst().get(0);
      CosignatureSignedTransaction signedCosig = CosignatureTransaction.create(pendingEscrow).signWith(bob);
      // bob announce the cosignature
      logger.info("announcing escrow");
      transactionHttp.announceAggregateBondedCosignature(signedCosig).blockingFirst();
      // bob wait for cosignature
      listener.confirmed(bob.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // test that alice has 10M X and 10 Y
      sleepForAWhile();
      List<Mosaic> aliceMosaics = accountHttp.getAccountInfo(alice.getAddress()).blockingFirst().getMosaics();
      assertEquals(2, aliceMosaics.size());
      aliceMosaics.forEach(mosaic -> {
         if (mosaic.getId().getId().equals(mosaicX.getId())) {
            assertEquals(BigInteger.valueOf(10_000_000), mosaic.getAmount());
         } else if (mosaic.getId().getId().equals(mosaicY.getId())) {
            assertEquals(BigInteger.TEN, mosaic.getAmount());
         } else {
            fail("unexpected mosaic " + mosaic);
         }
      });
      // test that bob got the 1M X
      List<Mosaic> bobMosaics = accountHttp.getAccountInfo(bob.getAddress()).blockingFirst().getMosaics();
      assertEquals(0, bobMosaics.size());
   }
}
