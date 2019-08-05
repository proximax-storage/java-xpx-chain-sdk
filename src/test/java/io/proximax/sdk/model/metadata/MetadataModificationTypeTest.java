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
 * {@link MetadataModificationType} tests
 */
class MetadataModificationTypeTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, MetadataModificationType.ADD.getCode());
   }

   @Test
   void checkGetByCode() {
      for (MetadataModificationType item: MetadataModificationType.values()) {
         assertEquals(item, MetadataModificationType.getByCode(item.getCode()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> MetadataModificationType.getByCode(-8));
   }
}
