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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.exchange.AddExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.exchange.RemoveExchangeOffer;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.ExchangeOfferAddTransaction;
import io.proximax.sdk.model.transaction.ExchangeOfferRemoveTransaction;
import io.proximax.sdk.model.transaction.ExchangeOfferTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Multisig integration tests
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EExchangeTest extends E2EBaseTest {
   /** slf4j logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EExchangeTest.class);

   private final Account simpleAccount = new Account(new KeyPair(), getNetworkType());
   private final MosaicId EXCHANGED_MOSAIC = new MosaicId(UInt64Utils.fromLongArray(new long[] { 145530229,1818060917 }));
   
   @BeforeAll
   void prepareForTest() {
      logger.info("Using account {}", simpleAccount);
      signup(simpleAccount.getAddress());
   }

   @AfterAll
   void cleanupAferTest() {
   }

   @Test
   void test01BidAndRemove() throws InterruptedException, ExecutionException {
      // make sure we have funds
      sendSomeCash(getSeedAccount(), simpleAccount.getAddress(), 10000);
      // now make offer on the blockchain
      AddExchangeOffer offer = new AddExchangeOffer(EXCHANGED_MOSAIC, BigInteger.valueOf(1000),
            BigInteger.valueOf(500), ExchangeOfferType.BUY, BigInteger.valueOf(1000));
      ExchangeOfferAddTransaction addTrans = transact.exchangeAdd().offers(offer).build();
      SignedTransaction signedAddTrans = api.sign(addTrans, simpleAccount);
      logger.info("Announced offer add. {}", transactionHttp.announce(signedAddTrans).blockingFirst());
      logger.info("Announcement confirmed: {}",
            listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      // remove the offer
      RemoveExchangeOffer removeOffer = new RemoveExchangeOffer(EXCHANGED_MOSAIC, ExchangeOfferType.BUY);
      ExchangeOfferRemoveTransaction removeTrans = transact.exchangeRemove().offers(removeOffer).build();
      SignedTransaction signedRemoveTrans = api.sign(removeTrans, simpleAccount);
      logger.info("Announced offer remove. {}", transactionHttp.announce(signedRemoveTrans).blockingFirst());
      logger.info("Announcement confirmed: {}",
            listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
   }

   @Test
   void testBidAndOffer() throws InterruptedException, ExecutionException {
      // make sure we have funds
      sendSomeCash(getSeedAccount(), simpleAccount.getAddress(), 10000);
      {
         // now add offer on the blockchain - buy 1000 harvest mosaic for 500 network currency
         AddExchangeOffer addOffer = new AddExchangeOffer(EXCHANGED_MOSAIC, BigInteger.valueOf(1000),
               BigInteger.valueOf(500), ExchangeOfferType.BUY, BigInteger.valueOf(1000));
         ExchangeOfferAddTransaction addTrans = transact.exchangeAdd().offers(addOffer).build();
         SignedTransaction signedAddTrans = api.sign(addTrans, simpleAccount);
         logger.info("Announced offer add. {}", transactionHttp.announce(signedAddTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
      {
         // seed account can offer 500 harvest mosaic for 250 network currency
         ExchangeOffer offer = new ExchangeOffer(EXCHANGED_MOSAIC, BigInteger.valueOf(500),
               BigInteger.valueOf(250), ExchangeOfferType.BUY, simpleAccount.getPublicKey());
         ExchangeOfferTransaction offerTrans = transact.exchangeOffer().offers(offer).build();
         SignedTransaction signedOfferTrans = api.sign(offerTrans, getSeedAccount());
         logger.info("Announced offer for half. {}", transactionHttp.announce(signedOfferTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(getSeedAccount().getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
      {
         // remove the offer
         RemoveExchangeOffer removeOffer = new RemoveExchangeOffer(EXCHANGED_MOSAIC, ExchangeOfferType.BUY);
         ExchangeOfferRemoveTransaction removeTrans = transact.exchangeRemove().offers(removeOffer).build();
         SignedTransaction signedRemoveTrans = api.sign(removeTrans, simpleAccount);
         logger.info("Announced offer remove. {}", transactionHttp.announce(signedRemoveTrans).blockingFirst());
         logger.info("Announcement confirmed: {}",
               listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                     .blockingFirst());
      }
   }

}
