/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link MultisigCosignatoryModificationType} tests
 */
class MultisigCosignatoryModificationTypeTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, MultisigCosignatoryModificationType.ADD.getValue());
   }

   @Test
   void checkGetByCode() {
      for (MultisigCosignatoryModificationType item: MultisigCosignatoryModificationType.values()) {
         assertEquals(item, MultisigCosignatoryModificationType.rawValueOf(item.getValue()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> MultisigCosignatoryModificationType.rawValueOf(-8));
   }

}
