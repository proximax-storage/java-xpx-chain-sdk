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
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.network.NetworkType;

class LockFundsTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", EntityType.AGGREGATE_BONDED);
      LockFundsTransaction tx = new LockFundsTransaction(NetworkType.TEST_NET, 23, new FakeDeadline(), BigInteger.ONE,
            Optional.of("signaturestring"),
            Optional.of(new PublicAccount(new KeyPair().getPublicKey().getHexString(), NetworkType.TEST_NET)),
            Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100), signedTransaction);

      assertEquals(NetworkCurrencyMosaic.createRelative(BigDecimal.valueOf(10)), tx.getMosaic());
      assertEquals(BigInteger.valueOf(100), tx.getDuration());
      assertEquals(signedTransaction, tx.getSignedTransaction());
   }

   @Test
   void serialization() throws IOException {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", EntityType.AGGREGATE_BONDED);
      LockFundsTransaction lockFundstx = new LockFundsTransaction(NetworkType.TEST_NET, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100), signedTransaction);
      byte[] actual = lockFundstx.generateBytes();
//      saveBytes("lock_funds", actual);
      assertArrayEquals(loadBytes("lock_funds"), actual);
   }

   @Test
   void shouldThrowExceptionWhenSignedTransactionIsNotTypeAggregateBonded() {

      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", EntityType.TRANSFER);
      assertThrows(IllegalArgumentException.class,
            () -> new LockFundsTransaction(NetworkType.TEST_NET, 1, new FakeDeadline(),
                  BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN,
                  BigInteger.valueOf(100), signedTransaction),
            "Signed transaction must be Aggregate Bonded Transaction");
   }

   @Test
   void checkToStringIsImplemented() {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", EntityType.AGGREGATE_BONDED);
      LockFundsTransaction tx = new LockFundsTransaction(NetworkType.TEST_NET, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100), signedTransaction);
      assertTrue(tx.toString().startsWith("LockFundsTransaction "));
   }
   
   @Test
   void checkCopyToSigner() throws IOException {
      PublicAccount remoteAccount = new Account(new KeyPair(), NetworkType.TEST_NET).getPublicAccount();
      
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", EntityType.AGGREGATE_BONDED);
      LockFundsTransaction trans = new LockFundsTransaction(NetworkType.TEST_NET, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100), signedTransaction);

      Transaction t = trans.copyForSigner(remoteAccount);
      assertEquals(Optional.of(remoteAccount), t.getSigner());
   }
}
