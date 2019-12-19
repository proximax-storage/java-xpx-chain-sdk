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
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.ExchangeOfferAddTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

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

   @BeforeAll
   void prepareForTest() {
      logger.info("Using account {}", simpleAccount);
      signup(simpleAccount.getAddress());
   }

   @AfterAll
   void cleanupAferTest() {
   }

   @Test
   void test01BidExchange() throws InterruptedException, ExecutionException {
      // make sure we have funds
      sendSomeCash(getSeedAccount(), simpleAccount.getAddress(), 10000);
      // now make offer on the blockchain
      AddExchangeOffer offer = new AddExchangeOffer(NetworkCurrencyMosaic.ID, BigInteger.valueOf(1000),
            BigInteger.valueOf(500), ExchangeOfferType.BUY, BigInteger.valueOf(1000));
      ExchangeOfferAddTransaction addTrans = transact.exchangeAdd().offers(offer).build();
      SignedTransaction signedAddTrans = api.sign(addTrans, simpleAccount);
      logger.info("Announced offer add. {}", transactionHttp.announce(signedAddTrans).blockingFirst());
      logger.info("Announcement confirmed: {}",
            listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
   }

}
