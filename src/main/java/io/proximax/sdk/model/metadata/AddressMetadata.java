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

import io.proximax.sdk.model.account.Address;

/**
 * Metadata for address. The ID here is the address representing the address
 */
public class AddressMetadata extends Metadata {
   private final Address address;

   /**
    * @param fields metadata fields associated with the address
    * @param address the address
    */
   public AddressMetadata(List<Field> fields, Address address) {
      super(OldMetadataType.ADDRESS, fields);
      this.address = address;
   }

   /**
    * @return the address
    */
   public Address getAddress() {
      return address;
   }

   /**
    * get String ID of the metadata
    * 
    * @return the string ID
    */
   public String getId() {
      return getIdFromAddress(address);
   }
   /**
    * return the address metadata ID used for requests to APIs
    * 
    * @param address metadata address
    * @return the string representation of address
    */
   public static String getIdFromAddress(Address address) {
      return address.plain();
   }
}
