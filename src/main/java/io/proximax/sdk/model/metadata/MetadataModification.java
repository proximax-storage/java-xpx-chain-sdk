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

import org.apache.commons.lang.Validate;

/**
 * Metadata modification
 */
public class MetadataModification {
   private final MetadataModificationType type;
   private final Field field;

   /**
    * @param type type of the modification
    * @param field key-value pair
    */
   public MetadataModification(MetadataModificationType type, Field field) {
      Validate.notNull(type, "type can not be null");
      Validate.notNull(field, "field can not be null");
      this.type = type;
      this.field = field;
   }

   /**
    * create modification to add specified key-value pair
    * 
    * @param key key to be added
    * @param value value to be added
    * @return metadata modification instance
    */
   public static MetadataModification add(String key, String value) {
      Validate.notNull(key, "key can not be null");
      Validate.notNull(value, "value can not be null");
      return new MetadataModification(MetadataModificationType.ADD, new Field(key, value));
   }
   
   /**
    * create modification to remove specified key
    * 
    * @param key key to be removed
    * @return metadata modification instance
    */
   public static MetadataModification remove(String key) {
      Validate.notNull(key, "key can not be null");
      return new MetadataModification(MetadataModificationType.REMOVE, new Field(key));
   }
   
   /**
    * @return the type
    */
   public MetadataModificationType getType() {
      return type;
   }

   /**
    * @return the fields
    */
   public Field getField() {
      return field;
   }

}
