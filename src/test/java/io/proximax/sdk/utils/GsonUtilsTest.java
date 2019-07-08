/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * {@link GsonUtils} tests
 */
class GsonUtilsTest {
   private static final String HELLO = "hello";
   @Test
   void ensureLongsStringTest() {
      List<Object> items = new LinkedList<>();
      items.add(HELLO);
      List<Object> result = GsonUtils.ensureLongs(items);
      assertEquals(items, result);
   }

   @Test
   void ensureLongsEmptyTest() {
      List<Object> items = new LinkedList<>();
      List<Object> result = GsonUtils.ensureLongs(items);
      assertEquals(items, result);
   }

   @Test
   void ensureLongsDoubleTest() {
      List<Object> items = new LinkedList<>();
      items.add(1d);
      items.add(15d);
      List<Object> result = GsonUtils.ensureLongs(items);
      assertEquals(1l, (Long)result.get(0));
      assertEquals(15l, (Long)result.get(1));
   }

   @Test
   void ensureLongsMixedTest() {
      List<Object> items = new LinkedList<>();
      items.add(1d);
      items.add(15l);
      items.add(HELLO);
      items.add(5);
      List<Object> result = GsonUtils.ensureLongs(items);
      assertEquals(1l, (Long)result.get(0));
      assertEquals(15l, (Long)result.get(1));
      assertEquals(HELLO, result.get(2));
      assertEquals(5l, (Long)result.get(3));
   }

   @Test
   void ensureLongsLongTest() {
      List<Object> items = new LinkedList<>();
      items.add(1l);
      items.add(15l);
      List<Object> result = GsonUtils.ensureLongs(items);
      assertEquals(items, result);
   }
}
