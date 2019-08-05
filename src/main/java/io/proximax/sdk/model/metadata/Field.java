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
package io.proximax.sdk.model.metadata;

import java.util.Objects;
import java.util.Optional;

/**
 * Metadata field representing key and value pair
 */
public class Field {

   private final String key;
   private final Optional<String> value;
   
   /**
    * @param key metadata key
    * @param value metadata value
    */
   public Field(String key, String value) {
      this.key = key;
      this.value = Optional.of(value);
   }   
   
   /**
    * @param key metadata key
    */
   public Field(String key) {
      this.key = key;
      this.value = Optional.empty();
   }
   
   /**
    * @return the key metadata key
    */
   public String getKey() {
      return key;
   }
   
   /**
    * check whether this field defines value
    * 
    * @return true or false indication whether value is present
    */
   public boolean hasValue() {
      return value.isPresent();
   }
   
   /**
    * get value or throw runtime exception. Use {@link #hasValue()} to check for presence of value or
    * use {@link #getValueOptional()} to retrieve optional
    * 
    * @return the value
    */
   public String getValue() {
      return value.orElseThrow(() -> new RuntimeException("Value not available"));
   }
   
   /**
    * get optional value
    * 
    * @return the optional value
    */
   public Optional<String> getValueOptional() {
      return value;
   }

   @Override
   public int hashCode() {
      return Objects.hash(key, value);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Field other = (Field) obj;
      return Objects.equals(key, other.key) && Objects.equals(value, other.value);
   }
   
   
}
