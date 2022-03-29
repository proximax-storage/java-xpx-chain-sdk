/*
 * Copyright 2022 ProximaX
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

/**
 * Type of the metadata
 */
public enum MetadataType {

   ADDRESS(0), MOSAIC(1), NAMESPACE(2);

   private final int code;

   /**
    * @param code
    */
   private MetadataType(int code) {
      this.code = code;
   }

   /**
    * @return the code
    */
   public int getCode() {
      return code;
   }

   /**
    * retrieve metadata type by the code
    * 
    * @param code of the metadata type
    * @return metadata type
    */
   public static MetadataType getByCode(int code) {
      for (MetadataType type : MetadataType.values()) {
         if (code == type.code) {
            return type;
         }
      }
      throw new IllegalArgumentException("Unsupported metadata type code " + code);
   }
}
