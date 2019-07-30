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
 * {@link MetadataModification} tests
 */
class MetadataModificationTest {

   @Test
   void checkConstructor() {
      Field field = new Field("key", "value");
      MetadataModification mod = new MetadataModification(MetadataModificationType.ADD, field);
      
      assertEquals(MetadataModificationType.ADD, mod.getType());
      assertEquals(field, mod.getField());
   }
   
   @Test
   void checkNullConstructorChecks() {
      assertThrows(NullPointerException.class, () -> new MetadataModification(null, null));
      assertThrows(NullPointerException.class, () -> new MetadataModification(null, new Field("key")));
      assertThrows(NullPointerException.class, () -> new MetadataModification(MetadataModificationType.ADD, null));
   }

   @Test
   void checkAdd() {
      MetadataModification mod = MetadataModification.add("key", "value");
      
      assertEquals(MetadataModificationType.ADD, mod.getType());
      assertEquals(new Field("key", "value"), mod.getField());
   }
   
   @Test
   void checkRemove() {
      MetadataModification mod = MetadataModification.remove("key");
      
      assertEquals(MetadataModificationType.REMOVE, mod.getType());
      assertEquals(new Field("key"), mod.getField());
   }
}
