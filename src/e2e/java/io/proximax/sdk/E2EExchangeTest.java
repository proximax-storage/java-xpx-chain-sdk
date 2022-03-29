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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.exchange.AddExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.exchange.RemoveExchangeOffer;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.ExchangeOfferAddTransaction;
import io.proximax.sdk.model.transaction.ExchangeOfferRemoveTransaction;
import io.proximax.sdk.model.transaction.ExchangeOfferTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * Exchange integration tests
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EExchangeTest extends E2EBaseTest {
   /** slf4j logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EExchangeTest.class);

   //private final MosaicId createMosaic() = new MosaicId("0DE5F42A0A3B4200");
   private Account simpleAccount;
   
   @BeforeEach
   void prepareForTest() {
      simpleAccount = createAccount();
   }

   @AfterEach
   void cleanupAferTest() {
      returnAllToSeed(simpleAccount);
      simpleAccount = null;
   }
   MosaicId createMosaic(){
      MosaicNonce aNonce = MosaicNonce.createRandom();
      MosaicId aId = new MosaicId(
            aNonce,
            PublicAccount.createFromPublicKey(
                  "42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                  getNetworkType()));

      logger.info("Creating mosaic " + aId.getIdAsHex());
      return aId;
   }
    
   Account createAccount() {
      Account simpleAccount = new Account(new KeyPair(), getNetworkType());
      logger.info("Using account {}", simpleAccount);
      signup(simpleAccount.getAddress());
      return simpleAccount;
   }
   
   @Test
   void testBidAndRemove() throws InterruptedException, ExecutionException {
      // make sure we have funds
      sendSomeCash(getSeedAccount(), simpleAccount.getAddress(), 100);
      // now make offer on the blockchain
      AddExchangeOffer offer = new AddExchangeOffer(createMosaic(), BigInteger.valueOf(1000),
            BigInteger.valueOf(500), ExchangeOfferType.BUY, BigInteger.valueOf(1000));
      ExchangeOfferAddTransaction addTrans = transact.exchangeAdd().offers(offer).build();
      SignedTransaction signedAddTrans = api.sign(addTrans, simpleAccount);
      logger.info("Announced offer add. {}", transactionHttp.announce(signedAddTrans).blockingFirst());
      logger.info("Announcement confirmed: {}",
            listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      // remove the offer
      RemoveExchangeOffer removeOffer = new RemoveExchangeOffer(createMosaic(), ExchangeOfferType.BUY);
      ExchangeOfferRemoveTransaction removeTrans = transact.exchangeRemove().offers(removeOffer).build();
      SignedTransaction signedRemoveTrans = api.sign(removeTrans, simpleAccount);
      logger.info("Announced offer remove. {}", transactionHttp.announce(signedRemoveTrans).blockingFirst());
      logger.info("Announcement confirmed: {}",
            listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
   }

   @Test
   void testBidOfferRemove() throws InterruptedException, ExecutionException {
      // make sure we have funds
      sendSomeCash(getSeedAccount(), simpleAccount.getAddress(), 100);
      {
         // now add offer on the blockchain - buy 1000 harvest mosaic for 500 network currency
         AddExchangeOffer addOffer = new AddExchangeOffer(createMosaic(), BigInteger.valueOf(1000),
               BigInteger.valueOf(500), ExchangeOfferType.BUY, BigInteger.valueOf(1000));
         ExchangeOfferAddTransaction addTrans = transact.exchangeAdd().offers(addOffer).build();
         SignedTransaction signedAddTrans = api.sign(addTrans, simpleAccount);
         logger.info("Announced offer add. {}", transactionHttp.announce(signedAddTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
      {
         // seed account can offer
         ExchangeOffer offer = new ExchangeOffer(createMosaic(), BigInteger.valueOf(500),
               BigInteger.valueOf(250), ExchangeOfferType.BUY, simpleAccount.getPublicAccount());
         ExchangeOfferTransaction offerTrans = transact.exchangeOffer().offers(offer).build();
         SignedTransaction signedOfferTrans = api.sign(offerTrans, getSeedAccount());
         logger.info("Announced offer for half. {}", transactionHttp.announce(signedOfferTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(getSeedAccount().getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
      {
         // remove the offer
         RemoveExchangeOffer removeOffer = new RemoveExchangeOffer(createMosaic(), ExchangeOfferType.BUY);
         ExchangeOfferRemoveTransaction removeTrans = transact.exchangeRemove().offers(removeOffer).build();
         SignedTransaction signedRemoveTrans = api.sign(removeTrans, simpleAccount);
         logger.info("Announced offer remove. {}", transactionHttp.announce(signedRemoveTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
   }

   @Test
   void testBidAndOffer() throws InterruptedException, ExecutionException {
      final int initialFunds = 4321;
      final MosaicId desiredMosaic = createMosaic();
      final int desiredAmount = 1234;
      final int offeredPrice = 567;
      // make sure we have funds
      sendMosaic(getSeedAccount(), simpleAccount.getAddress(), NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(initialFunds)));
      {
         // now add offer on the blockchain - buy 1000 harvest mosaic for 500 network currency
         AddExchangeOffer addOffer = new AddExchangeOffer(desiredMosaic, BigInteger.valueOf(desiredAmount),
               BigInteger.valueOf(offeredPrice), ExchangeOfferType.BUY, BigInteger.valueOf(1000));
         ExchangeOfferAddTransaction addTrans = transact.exchangeAdd().offers(addOffer).build();
         SignedTransaction signedAddTrans = api.sign(addTrans, simpleAccount);
         logger.info("Announced offer add. {}", transactionHttp.announce(signedAddTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
      {
         // seed account can offer 500 harvest mosaic for 250 network currency
         ExchangeOffer offer = new ExchangeOffer(desiredMosaic, BigInteger.valueOf(desiredAmount),
               BigInteger.valueOf(offeredPrice), ExchangeOfferType.BUY, simpleAccount.getPublicAccount());
         ExchangeOfferTransaction offerTrans = transact.exchangeOffer().offers(offer).build();
         SignedTransaction signedOfferTrans = api.sign(offerTrans, getSeedAccount());
         logger.info("Announced offer: {}", transactionHttp.announce(signedOfferTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(getSeedAccount().getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
      {
         sleepForAWhile();
         AccountInfo acc = accountHttp.getAccountInfo(simpleAccount.getAddress()).blockingFirst();
         List<Mosaic> mosaics = acc.getMosaics();
         assertEquals(2, mosaics.size());
         for (Mosaic mosaic: mosaics) {
            if (mosaic.getId().equals(createMosaic())) {
               assertEquals(desiredAmount, mosaic.getAmount().intValue());
            } else {
               // do not check network currency by ID as it is different
               assertEquals(initialFunds - offeredPrice, mosaic.getAmount().intValue());
            }
         }
      }
   }

}
