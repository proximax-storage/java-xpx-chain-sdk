/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.alias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link AliasAction} tests
 */
class AliasActionTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, AliasAction.LINK.getCode());
   }

   @Test
   void checkGetByCode() {
      for (AliasAction item: AliasAction.values()) {
         assertEquals(item, AliasAction.getByCode(item.getCode()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> AliasAction.getByCode(-8));
   }

}
