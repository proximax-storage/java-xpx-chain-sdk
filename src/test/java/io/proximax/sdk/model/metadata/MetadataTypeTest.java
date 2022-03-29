/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link OldMetadataType} tests
 */
class MetadataTypeTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(1, OldMetadataType.ADDRESS.getCode());
   }

   @Test
   void checkGetByCode() {
      for (OldMetadataType item: OldMetadataType.values()) {
         assertEquals(item, OldMetadataType.getByCode(item.getCode()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(RuntimeException.class, () -> OldMetadataType.getByCode(-8));
   }

}
