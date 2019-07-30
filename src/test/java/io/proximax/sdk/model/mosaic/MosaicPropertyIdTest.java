/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link MosaicPropertyId} tests
 */
class MosaicPropertyIdTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, MosaicPropertyId.FLAGS.getCode());
   }

   @Test
   void checkGetByCode() {
      for (MosaicPropertyId item: MosaicPropertyId.values()) {
         assertEquals(item, MosaicPropertyId.getByCode(item.getCode()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> MosaicPropertyId.getByCode((byte)-8));
   }

}
