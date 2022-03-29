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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class TransactionInfoTest {

    @Test void createATransactionInfoWithStaticConstructorCreateForTransactionsGetUsingListener() {
        TransactionInfo transactionInfo = TransactionInfo.create(new BigInteger("121855"),
                "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F");

        assertEquals(new BigInteger("121855"), transactionInfo.getHeight());
        assertFalse(transactionInfo.getIndex().isPresent());
        assertFalse(transactionInfo.getId().isPresent());
        assertFalse(transactionInfo.getAggregateHash().isPresent());
        assertFalse(transactionInfo.getAggregateId().isPresent());
        assertTrue(transactionInfo.getHash().isPresent());
        assertTrue(transactionInfo.getMerkleComponentHash().isPresent());

        assertEquals("B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", transactionInfo.getHash().get());
        assertEquals("B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", transactionInfo.getMerkleComponentHash().get());
    }

    @Test void createATransactionInfoWithStaticConstructorCreateForStandaloneTransactions() {
        TransactionInfo transactionInfo = TransactionInfo.create(new BigInteger("121855"),1, "5A3D23889CD1E800015929A9",
                "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F");

        assertEquals(new BigInteger("121855"), transactionInfo.getHeight());
        assertTrue(transactionInfo.getIndex().isPresent());
        assertTrue(transactionInfo.getId().isPresent());
        assertFalse(transactionInfo.getAggregateHash().isPresent());
        assertFalse(transactionInfo.getAggregateId().isPresent());
        assertTrue(transactionInfo.getHash().isPresent());
        assertTrue(transactionInfo.getMerkleComponentHash().isPresent());
        assertEquals(1, transactionInfo.getIndex().get());
        assertEquals("5A3D23889CD1E800015929A9", transactionInfo.getId().get());
        assertEquals("B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", transactionInfo.getHash().get());
        assertEquals("B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", transactionInfo.getMerkleComponentHash().get());
    }


    @Test void createATransactionInfoWithStaticConstructorCreateForAggregateInnerTransactions() {
        TransactionInfo transactionInfo = TransactionInfo.createAggregate(new BigInteger("130996"),0, "6191E454A4E1A152670C2654",
                "1E9BD8D5D13D85466555D799298A397CF1399AA6389363027BEBA97604212B2A", "6191E454A4E1A152670C2653",
                "86C47C9D2CF804EF8B910B7AEF5E81F12F8503BF84CD11243941E4913409216F");

        assertEquals(new BigInteger("130996"), transactionInfo.getHeight());
        assertTrue(transactionInfo.getIndex().isPresent());
        assertTrue(transactionInfo.getId().isPresent());
        assertFalse(transactionInfo.getHash().isPresent());
        assertFalse(transactionInfo.getMerkleComponentHash().isPresent());
        assertTrue(transactionInfo.getAggregateHash().isPresent());
        assertTrue(transactionInfo.getAggregateId().isPresent());
        assertEquals(0, transactionInfo.getIndex().get());
        assertEquals("6191E454A4E1A152670C2654", transactionInfo.getId().get());
        assertEquals("1E9BD8D5D13D85466555D799298A397CF1399AA6389363027BEBA97604212B2A", transactionInfo.getAggregateHash().get());
        assertEquals("6191E454A4E1A152670C2653", transactionInfo.getAggregateId().get());
        assertEquals("86C47C9D2CF804EF8B910B7AEF5E81F12F8503BF84CD11243941E4913409216F", transactionInfo.getUniqueAggregateHash().get());
    }

    @Test
    void testSupportingMethods() {
       TransactionInfo transactionInfo1 = TransactionInfo.create(new BigInteger("121855"),1, "5A3D23889CD1E800015929A9",
             "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F");
       TransactionInfo transactionInfo2 = TransactionInfo.create(new BigInteger("121855"),1, "5A3D23889CD1E800015929A9",
             "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2F", "B6C7648A3DDF71415650805E9E7801424FE03BBEE7D21F9C57B60220D3E95B2C");
       // tostring
       assertTrue(transactionInfo1.toString().startsWith("TransactionInfo "));
       // equals
       assertEquals(transactionInfo1, transactionInfo1);
       assertNotEquals(transactionInfo1, transactionInfo2);
       assertNotEquals(transactionInfo1, null);
       assertNotEquals(transactionInfo1, "hello");
       // hashcode
       assertEquals(transactionInfo1.hashCode(), transactionInfo1.hashCode());
       assertNotEquals(transactionInfo1.hashCode(), transactionInfo2.hashCode());
    }
}