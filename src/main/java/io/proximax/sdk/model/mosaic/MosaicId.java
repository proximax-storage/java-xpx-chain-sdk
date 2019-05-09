/*
 * Copyright 2018 NEM
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

package io.proximax.sdk.model.mosaic;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.transaction.IdGenerator;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * The mosaic id structure describes mosaic id
 *
 * @since 1.0
 */
public class MosaicId implements UInt64Id {
   private final BigInteger id;
   private final Optional<MosaicNonce> nonce;
   private final Optional<String> fullName = Optional.empty();

   /**
    * Create mosaic from the random nonce and public key of the owner
    * 
    * @param nonce random integer
    * @param ownerPublicKeyHex hexadecimal representation of owner's public key
    */
   public MosaicId(MosaicNonce nonce, String ownerPublicKeyHex) {
      this.id = IdGenerator.generateMosaicId(nonce.getNonceAsInt(), ownerPublicKeyHex);
      this.nonce = Optional.of(nonce);
   }

   public MosaicId(String hex) {
      if (!hex.matches("^[0-9A-Fa-f]{16}$")) {
         throw new IllegalIdentifierException("invalid hex string");
      }
      this.id = new BigInteger(hex, 16);
      this.nonce = Optional.empty();
   }

   /**
    * Create MosaicId from BigInteger id
    *
    * @param id id of the mosaic
    */
   public MosaicId(BigInteger id) {
      this.id = id;
      this.nonce = Optional.empty();
   }

   /**
    * @return the id
    */
   public BigInteger getId() {
      return id;
   }

   /**
    * @return the nonce
    */
   public Optional<MosaicNonce> getNonce() {
      return nonce;
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
   
   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      MosaicId other = (MosaicId) obj;
      return Objects.equals(id, other.id);
   }

   @Override
   public String toString() {
      return "MosaicId [id=" + id + ", nonce=" + nonce + "]";
   }
}
