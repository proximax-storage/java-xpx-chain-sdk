/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.contract;

import java.math.BigInteger;
import java.util.Objects;

import io.proximax.sdk.gen.model.ContractHashRecordDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * record in the history of contract content hashes
 */
public class ContractContentHashRecord {
   private final String contentHash;
   private final BigInteger blockHeight;
   
   /**
    * @param contentHash
    * @param blockHeight
    */
   public ContractContentHashRecord(String contentHash, BigInteger blockHeight) {
      this.contentHash = contentHash;
      this.blockHeight = blockHeight;
   }

   /**
    * @return the contentHash
    */
   public String getContentHash() {
      return contentHash;
   }

   /**
    * @return the blockHeight
    */
   public BigInteger getBlockHeight() {
      return blockHeight;
   }

   @Override
   public int hashCode() {
      return Objects.hash(blockHeight, contentHash);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ContractContentHashRecord other = (ContractContentHashRecord) obj;
      return Objects.equals(blockHeight, other.blockHeight) && Objects.equals(contentHash, other.contentHash);
   }
   
   /**
    * create new instance of content hash record from the DTO
    * 
    * @param dto the DTO as provided by the server
    * @return instance of the hash record
    */
   public static ContractContentHashRecord fromDto(ContractHashRecordDTO dto) {
      return new ContractContentHashRecord(dto.getHash(), UInt64Utils.toBigInt(dto.getHeight()));
   }
}
