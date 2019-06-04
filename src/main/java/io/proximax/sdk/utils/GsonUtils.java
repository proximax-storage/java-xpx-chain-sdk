/*
 * Copyright 2019 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.sdk.utils;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.Validate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * TODO add proper description
 */
public class GsonUtils {

   /**
    * no public constructor for utility class
    */
   private GsonUtils() {
      // hidden constructor
   }

   public static JsonObject mapToJsonObject(String str) {
      return new Gson().fromJson(str, JsonObject.class);
   }
   
   public static JsonArray mapToJsonArray(String str) {
      return new Gson().fromJson(str, JsonArray.class);
   }
   
   public static JsonArray getJsonArray(List<String> elements) {
      JsonArray bodyArr = new JsonArray(elements.size());
      elements.stream().forEachOrdered(str -> bodyArr.add(str));
      return bodyArr;
   }
   
   public static <T> JsonArray getJsonArray(List<T> elements, Function<T, String> mapper) {
      JsonArray bodyArr = new JsonArray(elements.size());
      elements.stream().map(mapper).forEachOrdered(str -> bodyArr.add(str));
      return bodyArr;
   }
   
   public static JsonPrimitive getJsonPrimitive(String str) {
      return new Gson().fromJson(str, JsonPrimitive.class);
   }
   
   public static JsonElement getFieldOfObject(JsonObject parent, String objectName, String fieldName) {
      return parent.get(objectName).getAsJsonObject().get(fieldName);
   }
   /**
    * convert JSON array to stream of JSON elements
    * 
    * @param jsonArr array with elements
    * @return stream of elements
    */
   public static Stream<JsonElement> stream(JsonArray jsonArr) {
      return StreamSupport.stream(jsonArr.spliterator(), false);
   }
   
   /**
    * deserialize array of 2 uint32 values into BigInteger
    * 
    * @param uint64Value array of 2 uint32 values
    * @return big integer representing the JSON array
    */
   public static BigInteger getBigInteger(JsonArray uint64Value) {
      final int expectedSize = 2;
      Validate.isTrue(uint64Value.size() == expectedSize, "Uint64 json array is expected to contain %d values but had %d", expectedSize, uint64Value.size());
      return UInt64Utils.fromLongArray(new long[] {uint64Value.get(0).getAsLong(), uint64Value.get(1).getAsLong()});
   }
}
