/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.account.props;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * AccountPropertyModificationType tests
 */
class AccountPropertyModificationTypeTest {

   @Test
   void testGetByCode() {
      assertEquals(AccountPropertyModificationType.ADD, AccountPropertyModificationType.getByCode(0));
   }
   
   @Test
   void testGetCode() {
      assertEquals(0, AccountPropertyModificationType.ADD.getCode());
   }
   
   @Test
   void testGetByInvalidCode() {
      assertThrows(IllegalArgumentException.class, () -> AccountPropertyModificationType.getByCode(-7));
   }

   @Test
   void getByStringCodeNull() {
      assertThrows(NullPointerException.class, () -> AccountPropertyModificationType.getByCode(null));
   }

   @Test
   void getByStringCodeInvalid() {
      assertThrows(NumberFormatException.class, () -> AccountPropertyModificationType.getByCode("hello"));
   }

   @Test
   void getByStringCode() {
      assertEquals(AccountPropertyModificationType.ADD, AccountPropertyModificationType.getByCode("00"));
   }
   
   @Test
   void getByStringCodeBad() {
      assertThrows(IllegalArgumentException.class, () -> AccountPropertyModificationType.getByCode("FFFFFFFFFF"));
   }
}
