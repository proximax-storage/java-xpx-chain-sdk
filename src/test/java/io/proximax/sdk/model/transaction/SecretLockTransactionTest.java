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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;

public class SecretLockTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
      SecretLockTransaction tx = new SecretLockTransaction(NetworkType.MIJIN, 23, new FakeDeadline(), BigInteger.ONE,
            Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN, BigInteger.valueOf(100),
            HashType.SHA3_256, secret, Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"));

      // make assertions
      assertEquals(NetworkCurrencyMosaic.TEN, tx.getMosaic());
      assertEquals(BigInteger.valueOf(100), tx.getDuration());
      assertEquals(HashType.SHA3_256, tx.getHashType());
      assertEquals(secret, tx.getSecret());
      assertEquals(Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"), tx.getRecipient());
   }

   @Test
   void serialization() throws IOException {
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
      SecretLockTransaction secretLocktx = new SecretLockTransaction(NetworkType.MIJIN_TEST, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN,
            BigInteger.valueOf(100), HashType.SHA3_256, secret,
            Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"));

      byte[] actual = secretLocktx.generateBytes();
//      saveBytes("secret_lock", actual);
      assertArrayEquals(loadBytes("secret_lock"), actual);
   }

   @Test
   void shouldThrowErrorWhenSecretIsNotValid() {
      assertThrows(IllegalArgumentException.class, () -> {
         SecretLockTransaction secretLocktx = new SecretLockTransaction(NetworkType.MIJIN_TEST, 1, new FakeDeadline(),
               BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), NetworkCurrencyMosaic.TEN,
               BigInteger.valueOf(100), HashType.SHA3_256, "this is not valid",
               Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"));
      }, "not a valid secret");
   }
}
