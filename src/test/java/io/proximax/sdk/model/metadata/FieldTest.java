/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * {@link Field} tests
 */
class FieldTest {

   @Test
   void checkConstructor() {
      Field f = new Field("key", "value");
      
      assertEquals("key",  f.getKey());
      assertEquals("value", f.getValue());
      assertEquals("value", f.getValueOptional().orElse("fail test"));
      assertTrue(f.hasValue());
   }

   @Test
   void checkConstructorNoValue() {
      Field f = new Field("key");
      
      assertEquals("key",  f.getKey());
      assertThrows(RuntimeException.class, f::getValue);
      assertFalse(f.getValueOptional().isPresent());
      assertFalse(f.hasValue());
   }

   @Test
   void checkEquals() {
      Field a1 = new Field("k", "v");
      Field a2 = new Field("k", "v");
      Field b = new Field("k", "v2");
      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "othertype");
   }
   
   @Test
   void checkHashCode() {
      Field a1 = new Field("k", "v");
      Field a2 = new Field("k", "v");
      Field b = new Field("k", "v2");
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }
}
