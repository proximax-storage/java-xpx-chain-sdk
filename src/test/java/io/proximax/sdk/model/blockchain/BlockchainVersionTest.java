/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

/**
 * {@link BlockchainVersion} tests
 */
class BlockchainVersionTest {

   @Test
   void testConstructor() {
      BlockchainVersion ver = new BlockchainVersion(1, 2, 3, 4);
      
      assertEquals(1, ver.getMajor());
      assertEquals(2, ver.getMinor());
      assertEquals(3, ver.getRevision());
      assertEquals(4, ver.getBuild());
      
      assertEquals(BigInteger.valueOf(281483566841860l), ver.getVersionValue());
      assertEquals(ver, BlockchainVersion.fromVersionValue(BigInteger.valueOf(281483566841860l)));
   }
   
   @Test
   void testConstructorRangeZero() {
      BlockchainVersion ver = new BlockchainVersion(0, 0, 0, 0);
      
      assertEquals(0, ver.getMajor());
      assertEquals(0, ver.getMinor());
      assertEquals(0, ver.getRevision());
      assertEquals(0, ver.getBuild());

      assertEquals(BigInteger.valueOf(0l), ver.getVersionValue());
      assertEquals(ver, BlockchainVersion.fromVersionValue(BigInteger.valueOf(0l)));
   }
   
   @Test
   void testConstructorRangeMax() {
      BlockchainVersion ver = new BlockchainVersion(65535, 65535, 65535, 65535);
      
      assertEquals(65535, ver.getMajor());
      assertEquals(65535, ver.getMinor());
      assertEquals(65535, ver.getRevision());
      assertEquals(65535, ver.getBuild());

      assertEquals(new BigInteger("18446744073709551615"), ver.getVersionValue());
      assertEquals(ver, BlockchainVersion.fromVersionValue(new BigInteger("18446744073709551615")));
   }
   
   @Test
   void testConstructorRangeViolation() {
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(-1, 2, 3, 4));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(1, -1, 3, 4));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(1, 2, -1, 4));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(1, 2, 3, -1));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(65536, 2, 3, 4));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(1, 65536, 3, 4));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(1, 2, 65536, 4));
      assertThrows(IllegalArgumentException.class, () -> new BlockchainVersion(1, 2, 3, 65536));
   }
   
   @Test
   void testBigIntegerConversionsAreSymmetrical() {
      BlockchainVersion ver = BlockchainVersion.fromVersionValue(BigInteger.valueOf(1234567890l));
      assertEquals(BigInteger.valueOf(1234567890l), ver.getVersionValue());
   }

   @Test
   void hashCodeEquals() {
      BlockchainVersion a1 = new BlockchainVersion(2, 3, 4, 5);
      BlockchainVersion a2 = new BlockchainVersion(2, 3, 4, 5);
      BlockchainVersion b = new BlockchainVersion(99, 3, 4, 5);
      
      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "otherclass");
      
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
      assertNotEquals(a1.hashCode(), "otherclass".hashCode());
   }
}
