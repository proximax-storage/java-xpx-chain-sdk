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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;

class RegisterNamespaceTransactionTest {

    @Test
    void createANamespaceCreationRootNamespaceTransactionViaStaticConstructor() {
        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createRootNamespace(
                new Deadline(2, ChronoUnit.HOURS),
                "prx",
                BigInteger.valueOf(2000),
                NetworkType.MIJIN_TEST
        );

        assertEquals(NetworkType.MIJIN_TEST, registerNamespaceTransaction.getNetworkType());
        assertTrue(2 == registerNamespaceTransaction.getVersion());
        assertTrue(LocalDateTime.now().isBefore(registerNamespaceTransaction.getDeadline().getLocalDateTime()));
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
                NetworkType.MIJIN_TEST
        );

        assertEquals(NetworkType.MIJIN_TEST, registerNamespaceTransaction.getNetworkType());
        assertTrue(2 == registerNamespaceTransaction.getVersion());
        assertTrue(LocalDateTime.now().isBefore(registerNamespaceTransaction.getDeadline().getLocalDateTime()));
        assertEquals(BigInteger.valueOf(0), registerNamespaceTransaction.getFee());
        assertEquals("newnamespace", registerNamespaceTransaction.getNamespaceName());
        assertEquals(NamespaceType.SubNamespace, registerNamespaceTransaction.getNamespaceType());
        assertEquals(new BigInteger("-7487193294859220686"), registerNamespaceTransaction.getNamespaceId().getId());
        assertEquals(new BigInteger("4635294387305441662"), registerNamespaceTransaction.getParentId().get().getId());
    }

    @Test
    @DisplayName("Serialization root namespace")
    @Disabled
    void serializationRootNamespace() {
        // Generated at nem2-library-js/test/transactions/RegisterNamespaceTransaction.spec.js
        byte[] expected = new byte[]{(byte)150,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                2, (byte)144, 78, 65, 0, 0, 0, 0, 0, 0, 0, 0,1,0,0,0,0,0,0,0,0,16,39,0,0,0,0,0,0,126,(byte)233,(byte)179,(byte)184,(byte)175,(byte)223,83,64,12,110,101,119,110,97,109,101,115,112,97,99,101};

        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createRootNamespace(
                new FakeDeadline(),
                "newnamespace",
                BigInteger.valueOf(10000),
                NetworkType.MIJIN_TEST
        );

        byte[] actual = registerNamespaceTransaction.generateBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("Serialization sub namespace")
    @Disabled
    void serializationSubNamespace() {
        // Generated at nem2-library-js/test/transactions/RegisterNamespaceTransaction.spec.js
        byte[] expected = new byte[]{(byte)150,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                2,(byte)144,78,65,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,126,(byte)233,(byte)179,(byte)184,(byte)175,(byte)223,83,64,3,18,(byte)152,27,120,121,(byte)163,113,12,115,117,98,110,97,109,101,115,112,97,99,101};

        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createSubNamespace(
                new FakeDeadline(),
                "subnamespace",
                new NamespaceId(new BigInteger("4635294387305441662")),
                NetworkType.MIJIN_TEST
        );

        byte[] actual = registerNamespaceTransaction.generateBytes();
        assertArrayEquals(expected, actual);
    }
}
