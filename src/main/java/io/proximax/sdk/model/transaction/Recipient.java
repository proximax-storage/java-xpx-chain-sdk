/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Representation of recipient. This can be either address or namespace which is aliased to the account
 */
public class Recipient {
   private final Optional<Address> address;
   private final Optional<NamespaceId> namespaceId;
   
   /**
    * Create new instance of recipient. Always either address or namespace ID has to be specified
    * 
    * @param address recipient address
    * @param namespaceId recipient namespace ID which is expected to have an alias to an account
    */
   private Recipient(Optional<Address> address, Optional<NamespaceId> namespaceId) {
      Validate.isTrue(address.isPresent() != namespaceId.isPresent(), "exactly one of address and namespaceId has to be present");
      this.address = address;
      this.namespaceId = namespaceId;
   }
   
   /**
    * create new recipient from the address
    * 
    * @param address recipient address
    */
   public Recipient(Address address) {
      this(Optional.of(address), Optional.empty());
   }

   /**
    * create new recipient from the namespace ID
    * 
    * @param namespaceId recipient namespace ID which is expected to have an alias to an account
    */
   public Recipient(NamespaceId namespaceId) {
      this(Optional.empty(), Optional.of(namespaceId));
   }

   /**
    * create new recipient from the address
    * 
    * @param address recipient address
    * @return the recipient
    */
   public static Recipient from(Address address) {
      return new Recipient(address);
   }
   
   /**
    * create new recipient from the namespace ID
    * 
    * @param namespaceId recipient namespace ID which is expected to have an alias to an account
    * @return the recipient
    */
   public static Recipient from(NamespaceId namespaceId) {
      return new Recipient(namespaceId);
   }
   
   /**
    * @return the address
    */
   public Optional<Address> getAddress() {
      return address;
   }

   /**
    * @return the namespaceId
    */
   public Optional<NamespaceId> getNamespaceId() {
      return namespaceId;
   }

   /**
    * get 25 bytes of serialized recipient value
    * 
    * @return 25 bytes of address or 25 bytes of namespace ID followed by 0s
    */
   public byte[] getBytes() {
      if (address.isPresent()) {
         // simply return the address as 25 bytes
         return Base32Encoder.getBytes(address.get().plain());
      } else if (namespaceId.isPresent()) {
         // namespaceId is uint64 (8 bytes) so append 0s to create 25 bytes
         return ByteBuffer.allocate(25).put((byte)0x91).put(UInt64Utils.getBytes(namespaceId.get().getId())).array();
      }
      // this should never happen
      throw new IllegalStateException("recipient not specified");
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(address, namespaceId);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Recipient other = (Recipient) obj;
      return Objects.equals(address, other.address) && Objects.equals(namespaceId, other.namespaceId);
   }

}
