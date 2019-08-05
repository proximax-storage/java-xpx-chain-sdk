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

package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.utils.dto.UInt64Utils;

class MosaicIdTest {
   private static final MosaicNonce NONCE = new MosaicNonce(new byte[4]);
   private static final String PUB_KEY = "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF";
   private static final BigInteger ID = UInt64Utils.fromIntArray(new int[] { 481110499, 231112638 });

   @Test
   void createAMosaicIdFromMosaicNameViaConstructor() {
      MosaicId mosaicId = new MosaicId(NONCE, PUB_KEY);
      assertEquals(ID, mosaicId.getId());
      assertEquals("0dc67fbe1cad29e3", mosaicId.getIdAsHex());
      assertEquals(NONCE, mosaicId.getNonce().orElse(new MosaicNonce(new byte[] { 1, 2, 3, 4 })));
      assertEquals(992621222383397347l, mosaicId.getIdAsLong());
      assertFalse(mosaicId.getFullName().isPresent());
   }

   @Test
   void createAMosaicIdFromHexString() {
      MosaicId mosaicId = new MosaicId("0dc67fbe1cad29e3");
      assertEquals(ID, mosaicId.getId());
      assertEquals("0dc67fbe1cad29e3", mosaicId.getIdAsHex());
      assertFalse(mosaicId.getNonce().isPresent());
      assertEquals(992621222383397347l, mosaicId.getIdAsLong());
      assertFalse(mosaicId.getFullName().isPresent());
   }

   @Test
   void badHexForConstructor() {
      assertThrows(IllegalIdentifierException.class, () -> new MosaicId("not a hash"));
   }

   @Test
   void createAMosaicIdFromIdViaConstructor() {
      MosaicId mosaicId = new MosaicId(new BigInteger("-8884663987180930485"));
      assertEquals(new BigInteger("-8884663987180930485"), mosaicId.getId());
   }

   @Test
   void shouldCompareMosaicIdsForEquality() {
      MosaicId mosaicId = new MosaicId(new BigInteger("-8884663987180930485"));
      MosaicId mosaicId2 = new MosaicId(new BigInteger("-8884663987180930485"));
      assertTrue(mosaicId.equals(mosaicId2));
   }

   @Test
   void someMoreData() {
      MosaicId mosaicId = new MosaicId(MosaicNonce.createFromHex("B76FE378"),
            "4AFF7B4BA8C1C26A7917575993346627CB6C80DE62CD92F7F9AEDB7064A3DE62");
      MosaicId mosaicId2 = new MosaicId(new BigInteger("3AD842A8C0AFC518", 16));
      assertTrue(mosaicId.equals(mosaicId2));
   }
   
   @Test
   void checkToStringWorks() {
      assertTrue(new MosaicId("0dc67fbe1cad29e3").toString().startsWith("MosaicId "));
   }
   
   @Test
   void checkEquals() {
      MosaicId a1 = new MosaicId("0dc67fbe1cad29e3");
      MosaicId a2 = new MosaicId("0dc67fbe1cad29e3");
      MosaicId b = new MosaicId("0dc67fbe1cad29ed");
      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "othertype");
   }
   
   @Test
   void checkHashCode() {
      MosaicId a1 = new MosaicId("0dc67fbe1cad29e3");
      MosaicId a2 = new MosaicId("0dc67fbe1cad29e3");
      MosaicId b = new MosaicId("0dc67fbe1cad29ed");
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }
}
