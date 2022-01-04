/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link MultisigCosignatoryModification} tests
 */
class MultisigCosignatoryModificationTest {

   @Test
   void testAdd() {
      PublicAccount pubAcct = new Account(new KeyPair(), NetworkType.MAIN_NET).getPublicAccount();
      MultisigCosignatoryModification mod = MultisigCosignatoryModification.add(pubAcct);
      assertEquals(pubAcct, mod.getCosignatoryPublicAccount());
      assertEquals(MultisigCosignatoryModificationType.ADD, mod.getType());
   }

   @Test
   void testRemove() {
      PublicAccount pubAcct = new Account(new KeyPair(), NetworkType.MAIN_NET).getPublicAccount();
      MultisigCosignatoryModification mod = MultisigCosignatoryModification.remove(pubAcct);
      assertEquals(pubAcct, mod.getCosignatoryPublicAccount());
      assertEquals(MultisigCosignatoryModificationType.REMOVE, mod.getType());
   }

   @Test
   void testServiceMethods() {
      PublicAccount pubAcct = new Account(new KeyPair(), NetworkType.MAIN_NET).getPublicAccount();
      MultisigCosignatoryModification mod1 = MultisigCosignatoryModification.remove(pubAcct);
      MultisigCosignatoryModification mod1b = MultisigCosignatoryModification.remove(pubAcct);
      MultisigCosignatoryModification mod2 = MultisigCosignatoryModification.add(pubAcct);
      // equals
      assertEquals(mod1, mod1);
      assertEquals(mod1, mod1b);
      assertNotEquals(mod1, mod2);
      assertNotEquals(mod1, null);
      assertNotEquals(mod1, "wrongtype");
      // hashCode
      assertEquals(mod1.hashCode(), mod1.hashCode());
      assertEquals(mod1.hashCode(), mod1b.hashCode());
      assertNotEquals(mod1.hashCode(), mod2.hashCode());
   }
}
