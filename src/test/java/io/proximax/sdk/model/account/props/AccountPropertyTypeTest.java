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
 * AccountPropertyTypeTest tests
 */
class AccountPropertyTypeTest {

   @Test
   void testGetByCode() {
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, AccountPropertyType.getByCode(0x01));
   }
   
   @Test
   void testGetCode() {
      assertEquals(0x01, AccountPropertyType.ALLOW_ADDRESS.getCode());
   }
   
   @Test
   void testGetByInvalidCode() {
      assertThrows(IllegalArgumentException.class, () -> AccountPropertyType.getByCode(-7));
   }

   @Test
   void getByStringCodeNull() {
      assertThrows(NullPointerException.class, () -> AccountPropertyType.getByCode(null));
   }

   @Test
   void getByStringCodeInvalid() {
      assertThrows(NumberFormatException.class, () -> AccountPropertyType.getByCode("hello"));
   }

   @Test
   void getByStringCode() {
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, AccountPropertyType.getByCode("01"));
   }
   
   @Test
   void getByStringCodeBad() {
      assertThrows(IllegalArgumentException.class, () -> AccountPropertyType.getByCode("FFFFFFFFFF"));
   }
}
