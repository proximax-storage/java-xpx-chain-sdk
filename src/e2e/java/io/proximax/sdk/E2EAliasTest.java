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
import java.util.Arrays;
import java.util.List;
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

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNames;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.AliasTransaction;
import io.proximax.sdk.model.transaction.RegisterNamespaceTransaction;
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
public class E2EAliasTest extends E2EBaseTest {
   private static final String ROOT_NAME = "test-root-alias-"
         + new Double(Math.floor(Math.random() * 10000)).intValue();
   private static final String CHILD1_NAME = "mos";
   private static final String CHILD2_NAME = "acc";

   private Account account;
   private MosaicNonce mosaicNonce;
   private MosaicId mosaicId;
   private NamespaceId mosaNamespaceId = new NamespaceId(ROOT_NAME + "." + CHILD1_NAME);
   private NamespaceId accNamespaceId = new NamespaceId(ROOT_NAME + "." + CHILD2_NAME);

   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EAliasTest.class);

   @BeforeAll
   void initStuff() {
      account = new Account(new KeyPair(), getNetworkType());
      mosaicNonce = MosaicNonce.createRandom();
      mosaicId = new MosaicId(mosaicNonce, seedAccount.getPublicKey());
      signup(seedAccount.getAddress());
   }

   @AfterAll
   void closeDown() {
      returnAllToSeed(account);
   }

   @Test
   void test01PrepareData() {
      NamespaceId rootId = new NamespaceId(ROOT_NAME);
      logger.info("Going to create namespace {}", rootId);
      // create root namespace
      RegisterNamespaceTransaction registerNamespaceTransaction = transact.registerNamespace().rootNamespace(ROOT_NAME)
            .duration(BigInteger.valueOf(200)).build();
      SignedTransaction signedTransaction = api.sign(registerNamespaceTransaction, seedAccount);
      transactionHttp.announce(signedTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            ROOT_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      sleepForAWhile();
      // create child namespace for mosaic
      RegisterNamespaceTransaction registerChildNamespaceTransaction = transact.registerNamespace()
            .subNamespace(rootId, CHILD1_NAME).duration(BigInteger.valueOf(200)).build();
      SignedTransaction signedChildTransaction = api.sign(registerChildNamespaceTransaction, seedAccount);
      transactionHttp.announce(signedChildTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            CHILD1_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      sleepForAWhile();
      // create child namespace for account
      RegisterNamespaceTransaction accNamespace = transact.registerNamespace().subNamespace(rootId, CHILD2_NAME)
            .duration(BigInteger.valueOf(200)).build();
      SignedTransaction accNamespaceSigned = api.sign(accNamespace, seedAccount);
      transactionHttp.announce(accNamespaceSigned).blockingFirst();
      logger.info("Registered namespace {}. {}",
            CHILD2_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      sleepForAWhile();
      // create mosaic
      logger.info("Creating new mosaic");
      SignedTransaction mdt = transact.mosaicDefinition().nonce(mosaicNonce).mosaicId(mosaicId)
            .mosaicProperties(new MosaicProperties(true, true, 6, Optional.of(BigInteger.valueOf(200)))).build()
            .signWith(seedAccount, api.getNetworkGenerationHash());
      Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(),
            TimeUnit.SECONDS);
      sleepForAWhile();
      transactionHttp.announce(mdt).blockingFirst();
      logger.info("Mosaic created. {}", confirmation.blockingFirst());
   }

   @Test
   void test02CreateMosaicAlias() {
      AliasTransaction alias = transact.aliasMosaic().link(mosaicId).namespaceId(mosaNamespaceId).build();
      SignedTransaction signed = api.sign(alias, seedAccount);
      transactionHttp.announce(signed).blockingFirst();
      logger.info("created alias. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      sleepForAWhile();
   }

   @Test
   void test03CreateAccountAlias() {
      sendSomeCash(seedAccount, account.getAddress(), 1);
      sendSomeCash(account, seedAccount.getAddress(), 1);
      logger.info("creating alias for address");
      AliasTransaction alias = transact.aliasAddress().link(account.getAddress()).namespaceId(accNamespaceId).build();
      SignedTransaction signed = api.sign(alias, seedAccount);
      transactionHttp.announce(signed).blockingFirst();
      logger.info("created alias. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
      sleepForAWhile();
   }

   @Test
   void test04RetrieveMosaicName() {
      List<MosaicNames> names = mosaicHttp.getMosaicNames(Arrays.asList(mosaicId)).blockingFirst();
      MosaicNames name = names.get(0);
      assertEquals(ROOT_NAME + "." + CHILD1_NAME, name.getNames().get(0));
   }

   @Test
   void test05TransferUsingAliases() {
      transactionHttp.announce(transact.transfer()
            .mosaic(new Mosaic(new NamespaceId(NetworkCurrencyMosaic.MOSAIC_NAMESPACE), BigInteger.valueOf(1_000_000)))
            .to(accNamespaceId).build().signWith(seedAccount, api.getNetworkGenerationHash())).blockingFirst();
      logger.info("made transfer. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                  .blockingFirst());
   }
}
