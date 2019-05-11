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
import io.proximax.sdk.infrastructure.model.UInt64DTO;
import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.alias.AliasAction;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.AliasTransaction;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
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
      account = new Account(new KeyPair(), NETWORK_TYPE);
      mosaicNonce = MosaicNonce.createRandom();
      mosaicId = new MosaicId(mosaicNonce, seedAccount.getPublicKey());
      signup(seedAccount.getAddress());
      UInt64DTO dto = new UInt64DTO();
      dto.add(244048598l);
      dto.add(73533943l);
      System.out.println(new MosaicId(UInt64Utils.toBigInt(dto)).getIdAsHex());
//      fail();
   }

   @AfterAll
   void closeDown() {

   }

   @Test
   void test01PrepareData() {
      NamespaceId rootId = new NamespaceId(ROOT_NAME);
      logger.info("Going to create namespace {}", rootId);
      // create root namespace
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction
            .createRootNamespace(getDeadline(), ROOT_NAME, BigInteger.valueOf(200), NETWORK_TYPE);
      SignedTransaction signedTransaction = seedAccount.sign(registerNamespaceTransaction);
      transactionHttp.announce(signedTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            ROOT_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
      // create child namespace for mosaic
      RegisterNamespaceTransaction registerChildNamespaceTransaction = RegisterNamespaceTransaction
            .createSubNamespace(getDeadline(), CHILD1_NAME, rootId, NETWORK_TYPE);
      SignedTransaction signedChildTransaction = seedAccount.sign(registerChildNamespaceTransaction);
      transactionHttp.announce(signedChildTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            CHILD1_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
      // create child namespace for account
      RegisterNamespaceTransaction accNamespace = RegisterNamespaceTransaction
            .createSubNamespace(getDeadline(), CHILD2_NAME, rootId, NETWORK_TYPE);
      SignedTransaction accNamespaceSigned = seedAccount.sign(accNamespace);
      transactionHttp.announce(accNamespaceSigned).blockingFirst();
      logger.info("Registered namespace {}. {}",
            CHILD2_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
      // create mosaic
      logger.info("Creating new mosaic");
      SignedTransaction mdt = MosaicDefinitionTransaction.create(mosaicNonce,
            mosaicId,
            getDeadline(),
            new MosaicProperties(true, true, false, 6, BigInteger.valueOf(200)),
            NETWORK_TYPE).signWith(seedAccount);
      Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS);
      transactionHttp.announce(mdt).blockingFirst();
      logger.info("Mosaic created. {}", confirmation.blockingFirst());
   }

   @Test
   void test02CreateMosaicAlias() {
      AliasTransaction alias = AliasTransaction.create(mosaicId, mosaNamespaceId, AliasAction.LINK, getDeadline(), NETWORK_TYPE);
      SignedTransaction signed = seedAccount.sign(alias);
      transactionHttp.announce(signed).blockingFirst();
      logger.info("created alias. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
   }

   @Test
   void test03CreateAccountAlias() {
      sendSomeCash(seedAccount, account.getAddress(), 1);
      sendSomeCash(account, seedAccount.getAddress(), 1);
      logger.info("creating alias for address");
      AliasTransaction alias = AliasTransaction.create(account.getAddress(), accNamespaceId, AliasAction.LINK, getDeadline(), NETWORK_TYPE);
      SignedTransaction signed = seedAccount.sign(alias);
      transactionHttp.announce(signed).blockingFirst();
      logger.info("created alias. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
   }

}
