/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.gsonfire.builders.JsonArrayBuilder;

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
   
   @Test
   void checkMapToObject() {
      JsonObject obj = GsonUtils.mapToJsonObject("{\"hi\":\"there\"}");
      assertEquals("there", obj.get("hi").getAsString());
   }
   
   @Test
   void checkMapToArray() {
      JsonArray arr = GsonUtils.mapToJsonArray("[{\"hi\":\"there\"}]");
      assertEquals(1, arr.size());
      assertEquals("there", arr.get(0).getAsJsonObject().get("hi").getAsString());
   }
   
   @Test
   void checkGetJsonArray() {
      List<String> items = Arrays.asList("hi", "there");
      JsonArray arr = GsonUtils.getJsonArray(items);
      assertEquals(items.size(), arr.size());
   }
   
   @Test
   void checkGetJsonArrayMapped() {
      List<Integer> items = Arrays.asList(1, 7);
      JsonArray arr = GsonUtils.getJsonArray(items, num -> "num"+num);
      assertEquals(items.size(), arr.size());
      assertEquals("num1", arr.get(0).getAsString());
      assertEquals("num7", arr.get(1).getAsString());
   }
   
   @Test
   void checkToBigInteger() {
      JsonArray arr = JsonArrayBuilder.start().add(1).add(2).build();
      assertEquals(BigInteger.valueOf(8589934593l), GsonUtils.getBigInteger(arr));
      arr.add(8);
      assertThrows(IllegalArgumentException.class, () -> GsonUtils.getBigInteger(arr));
   }
   
   @Test
   void checkJsonPrimitive() {
      JsonPrimitive val = new JsonPrimitive("primiteve value");
      assertEquals(val, GsonUtils.getJsonPrimitive(val.toString()));
   }
   
   @Test
   void checkFieldOfObject() {
      JsonObject parent = new JsonObject();
      JsonObject obj = new JsonObject();
      parent.add("objectname", obj);
      obj.addProperty("somekey", "property value");
      assertEquals("property value", GsonUtils.getFieldOfObject(parent, "objectname", "somekey").getAsString());
   }
   
   @Test
   void checkStreamArray() {
      JsonArray arr = JsonArrayBuilder.start().add(1).add(2).build();
      assertEquals(2, GsonUtils.stream(arr).count());
   }
}
