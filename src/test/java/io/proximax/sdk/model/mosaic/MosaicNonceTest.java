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
package io.proximax.sdk.model.mosaic;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Nonce tests
 */
class MosaicNonceTest {

   @Test
   void keepsValue() {
      byte[] bytes = new byte[] {1,2,3,4};
      byte[] nonceBytes = new MosaicNonce(bytes).getNonce();
      assertEquals(bytes, nonceBytes);
   }
   
   @Test
   void invalidNonces() {
      assertThrows(IllegalArgumentException.class, () -> {
         new MosaicNonce(new byte[4]);
      });
      assertThrows(IllegalArgumentException.class, () -> {
         byte[] bytes = new byte[] {1,2,3,4,5};
         new MosaicNonce(bytes);
      });
      assertThrows(IllegalArgumentException.class, () -> {
         new MosaicNonce(null);
      });
   }

   @Test
   void getRandom() {
      // note that this test can fail sometimes but with 1000 attempts the chance of randomness collision should be low
      // and definitely not consistent
      final Set<MosaicNonce> nonces = new HashSet<>();
      Stream.generate(MosaicNonce::createRandom).limit(1000).forEach(nonce -> {
         if (nonces.contains(nonce)) {
            fail("nonce needs to be unique");
         }
         nonces.add(nonce);
      });
   }

   @Test
   void getInt() {
      byte[] bytes = new byte[] {0,0,0,1};
      assertEquals(1, new MosaicNonce(bytes).getNonceAsInt());
   }
   
   @Test
   void fromBigInt() {
      assertEquals(new MosaicNonce(new byte[] {1,0,0,0}), MosaicNonce.createFromBigInteger(BigInteger.ONE));
   }
   
   @Test
   void fromHex() {
      assertThrows(IllegalIdentifierException.class, () -> {
         MosaicNonce.createFromHex("FF");
      });
      assertThrows(IllegalIdentifierException.class, () -> {
         MosaicNonce.createFromHex("this bad");
      });
      assertThrows(IllegalIdentifierException.class, () -> {
         MosaicNonce.createFromHex("FFFF");
      });
      assertThrows(IllegalIdentifierException.class, () -> {
         MosaicNonce.createFromHex("FF0000");
      });
      assertThrows(IllegalIdentifierException.class, () -> {
         MosaicNonce.createFromHex("FFaaaaa");
      });
      assertEquals(1, MosaicNonce.createFromHex("00000001").getNonceAsInt());
      assertEquals(new MosaicNonce(new byte[] {0,0,0,1}), MosaicNonce.createFromHex("00000001"));
   }
   
   @Test
   void standardToString() {
      assertTrue(MosaicNonce.createRandom().toString().startsWith("MosaicNonce ["));
   }
   
   @Test
   void equalityContract() {
      MosaicNonce n1 = MosaicNonce.createRandom();
      MosaicNonce n2 = MosaicNonce.createRandom();
      MosaicNonce n3 = new MosaicNonce(n1.getNonce());
      assertEquals(n1, n3);
      assertEquals(n1, n1);
      assertNotEquals(n1,  n2);
      assertNotEquals(n1,  null);
      assertNotEquals(n1,  "weird");
   }
}
