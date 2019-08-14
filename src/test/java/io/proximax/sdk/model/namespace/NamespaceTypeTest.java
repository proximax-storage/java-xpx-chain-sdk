/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.namespace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link NamespaceType} tests
 */
class NamespaceTypeTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, NamespaceType.RootNamespace.getValue());
   }

   @Test
   void checkGetByCode() {
      for (NamespaceType item: NamespaceType.values()) {
         assertEquals(item, NamespaceType.rawValueOf(item.getValue()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> NamespaceType.rawValueOf((byte)-8));
   }

}
