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
 * Metadata for address. The ID here is String representing the address
 */
public class AddressMetadata extends Metadata {
   private String address;

   /**
    * @param fields
    * @param address
    */
   public AddressMetadata(List<Field> fields, String address) {
      super(MetadataType.ADDRESS, fields);
      this.address = address;
   }

   /**
    * @return the address
    */
   public String getAddress() {
      return address;
   }

   /**
    * @param address the address to set
    */
   public void setAddress(String address) {
      this.address = address;
   }

}
