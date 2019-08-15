/*
 * Copyright 2018 NEM
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

package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;

class RegisterNamespaceTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      RegisterNamespaceTransaction tx = new RegisterNamespaceTransaction(NetworkType.MIJIN, 23, new FakeDeadline(), BigInteger.ONE, 
            "prx", new NamespaceId(new BigInteger("4635294387305441662")), NamespaceType.SubNamespace, Optional.of(BigInteger.ONE), Optional.of(new NamespaceId(BigInteger.TEN)), 
            "signaturestring", new PublicAccount(new KeyPair().getPublicKey().getHexString(), NetworkType.MIJIN),
            TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash"));
      assertEquals("prx", tx.getNamespaceName());
      assertEquals(new NamespaceId(new BigInteger("4635294387305441662")), tx.getNamespaceId());
      assertEquals(NamespaceType.SubNamespace, tx.getNamespaceType());
      assertEquals(Optional.of(BigInteger.ONE), tx.getDuration());
      assertEquals(Optional.of(new NamespaceId(BigInteger.TEN)), tx.getParentId());
   }

   @Test
   void createANamespaceCreationRootNamespaceTransactionViaStaticConstructor() {
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createRootNamespace(
            new Deadline(2, ChronoUnit.HOURS),
            "prx",
            BigInteger.valueOf(2000),
            NetworkType.MIJIN_TEST);

      assertEquals(NetworkType.MIJIN_TEST, registerNamespaceTransaction.getNetworkType());
      assertEquals(2, registerNamespaceTransaction.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < registerNamespaceTransaction.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), registerNamespaceTransaction.getFee());
      assertEquals("prx", registerNamespaceTransaction.getNamespaceName());
      assertEquals(NamespaceType.RootNamespace, registerNamespaceTransaction.getNamespaceType());
      assertEquals(new BigInteger("-5661737225685060674"), registerNamespaceTransaction.getNamespaceId().getId());
      assertEquals(BigInteger.valueOf(2000), registerNamespaceTransaction.getDuration().get());
   }

   @Test
   void createANamespaceCreationSubNamespaceTransactionViaStaticConstructor() {
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createSubNamespace(
            new Deadline(2, ChronoUnit.HOURS),
            "newnamespace",
            new NamespaceId(new BigInteger("4635294387305441662")),
            NetworkType.MIJIN_TEST);

      assertEquals(NetworkType.MIJIN_TEST, registerNamespaceTransaction.getNetworkType());
      assertEquals(2, registerNamespaceTransaction.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < registerNamespaceTransaction.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), registerNamespaceTransaction.getFee());
      assertEquals("newnamespace", registerNamespaceTransaction.getNamespaceName());
      assertEquals(NamespaceType.SubNamespace, registerNamespaceTransaction.getNamespaceType());
      assertEquals(new BigInteger("-7487193294859220686"), registerNamespaceTransaction.getNamespaceId().getId());
      assertEquals(new BigInteger("4635294387305441662"), registerNamespaceTransaction.getParentId().get().getId());
   }

   @Test
   void serializationRootNamespace() throws IOException {
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction
            .createRootNamespace(new FakeDeadline(), "newnamespace", BigInteger.valueOf(10000), NetworkType.MIJIN_TEST);

      byte[] actual = registerNamespaceTransaction.generateBytes();
//      saveBytes("register_namespace_root", actual);
      assertArrayEquals(loadBytes("register_namespace_root"), actual);
   }

   @Test
   void serializationSubNamespace() throws IOException {
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createSubNamespace(
            new FakeDeadline(),
            "subnamespace",
            new NamespaceId(new BigInteger("4635294387305441662")),
            NetworkType.MIJIN_TEST);

      byte[] actual = registerNamespaceTransaction.generateBytes();
//      saveBytes("register_namespace_child", actual);
      assertArrayEquals(loadBytes("register_namespace_child"), actual);
   }
}
