/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.account.props;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * AccountPropertyModification tests
 */
class AccountPropertyModificationTest {
   private static final String HELLO = "hello";
   
   @Test
   void testConstructor() {
      AccountPropertyModification<String> mod = new AccountPropertyModification<>(AccountPropertyModificationType.ADD, HELLO);
      assertEquals(AccountPropertyModificationType.ADD, mod.getType());
      assertEquals(HELLO, mod.getValue());
   }

   @Test
   void testStaticAdd() {
      AccountPropertyModification<String> modAdd = AccountPropertyModification.add(HELLO);
      assertEquals(AccountPropertyModificationType.ADD, modAdd.getType());
      assertEquals(HELLO, modAdd.getValue());
   }
   
   @Test
   void testStaticRemove() {
      AccountPropertyModification<String> modRemove = AccountPropertyModification.remove(HELLO);
      assertEquals(AccountPropertyModificationType.REMOVE, modRemove.getType());
      assertEquals(HELLO, modRemove.getValue());
   }
}
