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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;

class LockFundsTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
      LockFundsTransaction tx = new LockFundsTransaction(NetworkType.MIJIN, 23, new FakeDeadline(), BigInteger.ONE,
            NetworkCurrencyMosaic.TEN, BigInteger.valueOf(100), signedTransaction,
            "signaturestring", new PublicAccount(new KeyPair().getPublicKey().getHexString(), NetworkType.MIJIN),
            TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash"));
      assertEquals(NetworkCurrencyMosaic.createRelative(BigDecimal.valueOf(10)), tx.getMosaic());
      assertEquals(BigInteger.valueOf(100), tx.getDuration());
      assertEquals(signedTransaction, tx.getSignedTransaction());
   }

   @Test
   void staticConstructor() {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
      LockFundsTransaction tx = LockFundsTransaction.create(new FakeDeadline(),
            NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100),
            signedTransaction,
            NetworkType.MIJIN_TEST);
      assertEquals(NetworkCurrencyMosaic.TEN, tx.getMosaic());
      assertEquals(BigInteger.valueOf(100), tx.getDuration());
      assertEquals(signedTransaction, tx.getSignedTransaction());
   }

   @Test
   void serialization() throws IOException {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
      LockFundsTransaction lockFundstx = LockFundsTransaction.create(new FakeDeadline(),
            NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100),
            signedTransaction,
            NetworkType.MIJIN_TEST);
      byte[] actual = lockFundstx.generateBytes();
      assertArrayEquals(loadBytes("lock_funds"), actual);
   }

   @Test
   void shouldThrowExceptionWhenSignedTransactionIsNotTypeAggregateBonded() {

      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.TRANSFER);
      assertThrows(IllegalArgumentException.class,
            () -> LockFundsTransaction.create(new FakeDeadline(),
                  NetworkCurrencyMosaic.TEN,
                  BigInteger.valueOf(100),
                  signedTransaction,
                  NetworkType.MIJIN_TEST),
            "Signed transaction must be Aggregate Bonded Transaction");
   }

   @Test
   void checkToStringIsImplemented() {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
      LockFundsTransaction tx = LockFundsTransaction.create(new FakeDeadline(),
            NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100),
            signedTransaction,
            NetworkType.MIJIN_TEST);
      assertTrue(tx.toString().startsWith("LockFundsTransaction "));
   }
}
