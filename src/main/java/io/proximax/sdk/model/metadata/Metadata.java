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

import java.util.List;

/**
 * Generic metadata having type and key-value fields
 */
public class Metadata {
   private MetadataType type;
   private List<Field> fields;
   
   /**
    * @param type
    * @param fields
    */
   public Metadata(MetadataType type, List<Field> fields) {
      this.type = type;
      this.fields = fields;
   }
   
   /**
    * @return the type
    */
   public MetadataType getType() {
      return type;
   }
   /**
    * @param type the type to set
    */
   public void setType(MetadataType type) {
      this.type = type;
   }
   /**
    * @return the fields
    */
   public List<Field> getFields() {
      return fields;
   }
   /**
    * @param fields the fields to set
    */
   public void setFields(List<Field> fields) {
      this.fields = fields;
   }
}
