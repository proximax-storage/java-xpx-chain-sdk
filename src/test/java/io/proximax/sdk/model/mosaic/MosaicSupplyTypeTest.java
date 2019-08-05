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
 * {@link MosaicSupplyType} tests
 */
class MosaicSupplyTypeTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, MosaicSupplyType.DECREASE.getValue());
   }

   @Test
   void checkGetByCode() {
      for (MosaicSupplyType item: MosaicSupplyType.values()) {
         assertEquals(item, MosaicSupplyType.rawValueOf(item.getValue()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> MosaicSupplyType.rawValueOf(-8));
   }
}
