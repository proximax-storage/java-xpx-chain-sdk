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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import io.proximax.core.crypto.Hashes;

class HashTypeTest {

   @Test
   void rawValueOf() {
      assertEquals(HashType.SHA3_256, HashType.rawValueOf(0));
      assertEquals(0, HashType.SHA3_256.getValue());
      assertThrows(IllegalArgumentException.class, () -> HashType.rawValueOf(-521));
   }
   
   @Test
   void hashValue() {
      byte[] originalBytes = new byte[] {1, 2, 3, 4, 5, 6};
      byte[] hashedBytes = HashType.SHA3_256.hashValue(originalBytes);
      assertArrayEquals(Hex.decode("af5c1f42d55c60d12918a9517fe4cba62259668577a2b7814a5cc5a38fc19ead"), hashedBytes);
   }
   
   @Test
   void SHA3_256ShouldBeExactly64CharactersLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.sha3_256(secretBytes);
      String secret = Hex.toHexString(result);

      assertTrue(HashType.SHA3_256.validate(secret));
   }

   @Test
   void SHA3_256ShouldReturnFalseIfItIsNot64CharsLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.sha3_512(secretBytes);
      String secret = Hex.toHexString(result);

      assertFalse(HashType.SHA3_256.validate(secret));
   }

   @Test
   void SHA3_256ShouldReturnFalseIfItIsNotAValidHash() {
      String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
      assertFalse(HashType.SHA3_256.validate(secret));

   }

   @Test
   void KECCAK_256ShouldBeExactly64CharactersLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.keccak256(secretBytes);
      String secret = Hex.toHexString(result);

      assertTrue(HashType.KECCAK_256.validate(secret));
   }

   @Test
   void KECCAK_256ShouldReturnFalseIfItIsNot64CharsLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.ripemd160(secretBytes);
      String secret = Hex.toHexString(result);

      assertFalse(HashType.KECCAK_256.validate(secret));
   }

   @Test
   void KECCAK_256ShouldReturnFalseIfItIsNotAValidHash() {
      String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
      assertFalse(HashType.KECCAK_256.validate(secret));
   }

   @Test
   void HASH_160ShouldHaveValidLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.hash160(secretBytes);
      String secret = Hex.toHexString(result);

      assertTrue(HashType.HASH_160.validate(secret));
   }

   @Test
   void HASH_160ShouldReturnFalseIfItIsNot40CharsLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.hash160(secretBytes);
      String secret = Hex.toHexString(result);

      assertFalse(HashType.HASH_160.validate(secret + "00"));
   }

   @Test
   void HASH_160ShouldReturnFalseIfItIsNotAValidHash() {
      String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
      assertFalse(HashType.HASH_160.validate(secret));
   }

   @Test
   void HASH_256ShouldBeExactly64CharactersLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.hash256(secretBytes);
      String secret = Hex.toHexString(result);

      assertTrue(HashType.HASH_256.validate(secret));
   }

   @Test
   void HASH_256ShouldReturnFalseIfItIsNot64CharsLength() {
      byte[] secretBytes = new byte[20];
      new Random().nextBytes(secretBytes);
      byte[] result = Hashes.ripemd160(secretBytes);
      String secret = Hex.toHexString(result);

      assertFalse(HashType.HASH_256.validate(secret));
   }

   @Test
   void HASH_256ShouldReturnFalseIfItIsNotAValidHash() {
      String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
      assertFalse(HashType.HASH_256.validate(secret));
   }
}
