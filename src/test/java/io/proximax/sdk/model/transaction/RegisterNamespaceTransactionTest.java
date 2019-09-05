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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;

class RegisterNamespaceTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      RegisterNamespaceTransaction tx = new RegisterNamespaceTransaction(NetworkType.MIJIN, 23, new FakeDeadline(),
            BigInteger.ONE, Optional.empty(), Optional.empty(), Optional.empty(), "prx",
            new NamespaceId(new BigInteger("4635294387305441662")), Optional.of(BigInteger.ONE),
            Optional.of(new NamespaceId(BigInteger.TEN)), NamespaceType.SUB_NAMESPACE);

      assertEquals("prx", tx.getNamespaceName());
      assertEquals(new NamespaceId(new BigInteger("4635294387305441662")), tx.getNamespaceId());
      assertEquals(NamespaceType.SUB_NAMESPACE, tx.getNamespaceType());
      assertEquals(Optional.of(BigInteger.ONE), tx.getDuration());
      assertEquals(Optional.of(new NamespaceId(BigInteger.TEN)), tx.getParentId());
   }

   @Test
   void serializationRootNamespace() throws IOException {
      RegisterNamespaceTransaction registerNamespaceTransaction = new RegisterNamespaceTransaction(
            NetworkType.MIJIN_TEST, 2, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), "newnamespace", new NamespaceId(IdGenerator.generateNamespaceId("newnamespace")),
            Optional.of(BigInteger.valueOf(10000)), Optional.empty(), NamespaceType.ROOT_NAMESPACE);

      byte[] actual = registerNamespaceTransaction.generateBytes();
//      saveBytes("register_namespace_root", actual);
      assertArrayEquals(loadBytes("register_namespace_root"), actual);
   }

   @Test
   void serializationSubNamespace() throws IOException {
      RegisterNamespaceTransaction registerNamespaceTransaction = new RegisterNamespaceTransaction(
            NetworkType.MIJIN_TEST, 2, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), "subnamespace",
            new NamespaceId(IdGenerator.generateSubNamespaceIdFromParentId(new BigInteger("4635294387305441662"),
                  "subnamespace")),
            Optional.empty(), Optional.of(new NamespaceId(new BigInteger("4635294387305441662"))),
            NamespaceType.SUB_NAMESPACE);

      byte[] actual = registerNamespaceTransaction.generateBytes();
//      saveBytes("register_namespace_child", actual);
      assertArrayEquals(loadBytes("register_namespace_child"), actual);
   }
}
