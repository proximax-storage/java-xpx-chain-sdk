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

import java.math.BigInteger;
import java.util.Optional;

import io.proximax.sdk.model.mosaic.IllegalIdentifierException;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Numeric representation of metadata ID for mosaic and namespace
 */
public class MetadataId implements UInt64Id {
   private final BigInteger id;
   private final Optional<String> fullName = Optional.empty();
   
   /**
    * create new metadata id using provided id
    * @param id id of the metadata
    */
   public MetadataId(BigInteger id) {
      this.id = id;
   }

   /**
    * create ID based on hexadecimal string
    * 
    * @param hex uint64 ID encoded as hexadecimal string
    */
   public MetadataId(String hex) {
      if (!hex.matches("^[0-9A-Fa-f]{16}$")) {
         throw new IllegalIdentifierException("invalid hex string");
      }
      this.id = new BigInteger(hex, 16);

   }

   @Override
   public BigInteger getId() {
      return id;
   }

   @Override
   public long getIdAsLong() {
      return id.longValue();
   }

   @Override
   public String getIdAsHex() {
      return UInt64Utils.bigIntegerToHex(id);
   }

   @Override
   public Optional<String> getFullName() {
      return fullName;
   }

}
