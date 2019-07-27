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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
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

import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.namespace.NamespaceName;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.RegisterNamespaceTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.reactivex.schedulers.Schedulers;

/**
 * E2E tests that demonstrate transfers
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2ENamespaceTest extends E2EBaseTest {
   private static final String ROOT_NAME = "test-root-namespace-"
         + new Double(Math.floor(Math.random() * 10000)).intValue();
   private static final String CHILD1_NAME = "c1";

   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ENamespaceTest.class);

   @BeforeAll
   void initStuff() {
   }

   @AfterAll
   void closeDown() {

   }

   @Test
   void test01CreateRootNamespace() {
      NamespaceId rootId = new NamespaceId(ROOT_NAME);
      logger.info("Going to create namespace {}", rootId);
      // create root namespace
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction
            .createRootNamespace(getDeadline(), ROOT_NAME, BigInteger.valueOf(100), getNetworkType());
      SignedTransaction signedTransaction = api.sign(registerNamespaceTransaction, seedAccount);
      transactionHttp.announce(signedTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            ROOT_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
      // check the namespace
      checkNamespace(ROOT_NAME, Optional.empty(), 100);
   }

   @Test
   void test02CreateChildNamespace() {
      NamespaceId rootId = new NamespaceId(ROOT_NAME);
      NamespaceId childId = new NamespaceId(ROOT_NAME + "." + CHILD1_NAME);
      logger.info("Going to create child namespace {}", childId);
      // create root namespace
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction
            .createSubNamespace(getDeadline(), CHILD1_NAME, rootId, getNetworkType());
      SignedTransaction signedTransaction = api.sign(registerNamespaceTransaction, seedAccount);
      transactionHttp.announce(signedTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            CHILD1_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
      // check the namespace
      checkNamespace(CHILD1_NAME, Optional.of(ROOT_NAME), 100);
   }

   @Test
   void test03CreateAggregateRootAndChild() {
      String aggRootName = "a" + ROOT_NAME;
      NamespaceId rootId = new NamespaceId(aggRootName);
      NamespaceId childId = new NamespaceId(aggRootName + "." + CHILD1_NAME);
      logger.info("Going to create aggregate root and child namespace {}", childId);
      // create root namespace
      RegisterNamespaceTransaction registerRootTransaction = RegisterNamespaceTransaction
            .createRootNamespace(getDeadline(), aggRootName, BigInteger.valueOf(100), getNetworkType());
      RegisterNamespaceTransaction registerChildTransaction = RegisterNamespaceTransaction
            .createSubNamespace(getDeadline(), CHILD1_NAME, rootId, getNetworkType());
      // prepare aggregate transaction for both namespaces
      AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(getDeadline(),
            Arrays.asList(registerRootTransaction.toAggregate(seedAccount.getPublicAccount()),
                  registerChildTransaction.toAggregate(seedAccount.getPublicAccount())),
            getNetworkType());
      // sign the aggregate transaction
      SignedTransaction signedTransaction = api.sign(aggregateTransaction, seedAccount);
      transactionHttp.announce(signedTransaction).blockingFirst();
      logger.info("Registered namespaces {}. {}",
            CHILD1_NAME,
            listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
      // check the namespaces
      checkNamespace(aggRootName, Optional.empty(), 100);
      checkNamespace(CHILD1_NAME, Optional.of(aggRootName), 100);
   }

   @Test
   void test04RetrieveFromAccount() {
      NamespaceId nsId = new NamespaceId(ROOT_NAME);
      // check that the seed account has the namespace as expected
      long nsCount = namespaceHttp.getNamespacesFromAccount(seedAccount.getAddress()).flatMapIterable(list -> list)
            .filter(info -> nsId.getId().equals(info.getId().getId()))
            .count().blockingGet();
      assertEquals(1, nsCount);
   }
   
   @Test
   void test05RetrieveFromAccounts() {
      NamespaceId nsId = new NamespaceId(ROOT_NAME);
      // check that the seed account has the namespace as expected
      long nsCount = namespaceHttp.getNamespacesFromAccounts(Arrays.asList(seedAccount.getAddress(), Account.generateNewAccount(getNetworkType()).getAddress())).flatMapIterable(list -> list)
            .filter(info -> nsId.getId().equals(info.getId().getId()))
            .count().blockingGet();
      assertEquals(1, nsCount);
   }
   
   @Test
   void throwExceptionWhenNamespaceDoesNotExists() {
      namespaceHttp.getNamespace(new NamespaceId("nonregisterednamespace")).subscribeOn(Schedulers.single()).test()
            .awaitDone(2, TimeUnit.SECONDS).assertFailure(RuntimeException.class);
   }
   
   /**
    * service method that checks for existence of a namespace
    * 
    * @param name name of the namespace
    * @param parentName name of parent or null if this is root namespace
    * @param duration number of blocks that this namespace will exist
    */
   private void checkNamespace(String name, Optional<String> parentName, long duration) {
      NamespaceId nsId;
      if (parentName.isPresent()) {
         nsId = new NamespaceId(parentName.get() + "." + name);
      } else {
         nsId = new NamespaceId(name);
      }
      logger.info("Checking namespace {}", nsId);
      // retrieve the namespace and check it is OK
      NamespaceInfo namespace = namespaceHttp.getNamespace(nsId).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // check for namespace properties
      assertEquals(BigInteger.valueOf(duration), namespace.getEndHeight().subtract(namespace.getStartHeight()));
      assertEquals(nsId.getId(), namespace.getId().getId());
      // try to check name
      NamespaceName nsName = namespaceHttp.getNamespaceNames(Arrays.asList(nsId)).flatMapIterable(list -> list)
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(name, nsName.getName());
      assertEquals(nsId.getId(), nsName.getNamespaceId().getId());
      if (!parentName.isPresent()) {
         // we expect to be root
         assertFalse(nsName.getParentId().isPresent());
      } else {
         NamespaceId parentId = new NamespaceId(parentName.get());
         assertTrue(nsName.getParentId().isPresent());
         assertEquals(parentId.getId(), nsName.getParentId().get().getId());
      }
   }
}
