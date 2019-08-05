/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.mosaic.IllegalIdentifierException;

/**
 * {@link MetadataId} tests
 */
class MetadataIdTest {

   @Test
   void constructorBigint() {
      MetadataId id = new MetadataId(BigInteger.ONE);
      assertEquals(1l, id.getIdAsLong());
      assertEquals(BigInteger.ONE, id.getId());
      assertEquals("0000000000000001", id.getIdAsHex());
      assertFalse(id.getFullName().isPresent());
   }

   @Test
   void constructorHexString() {
      MetadataId id = new MetadataId("0000000000000001");
      assertEquals(1l, id.getIdAsLong());
      assertEquals(BigInteger.ONE, id.getId());
      assertEquals("0000000000000001", id.getIdAsHex());
      assertFalse(id.getFullName().isPresent());
   }

   @Test
   void throwOnBadHex() {
      assertThrows(IllegalIdentifierException.class, () -> new MetadataId("000000000g000001"));
   }

}
