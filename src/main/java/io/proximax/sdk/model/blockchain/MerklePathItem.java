/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

/**
 * an item on the merkle path
 */
public class MerklePathItem {

   private final Integer position;
   private final String hash;
   
   /**
    * create new instance
    * 
    * @param position position of the item on the path
    * @param hash hash code
    */
   public MerklePathItem(Integer position, String hash) {
      this.position = position;
      this.hash = hash;
   }

   
   /**
    * @return the position
    */
   public Integer getPosition() {
      return position;
   }


   /**
    * @return the hash
    */
   public String getHash() {
      return hash;
   }

   /**
    * create merkle path item from a DTO
    * 
    * @param dto the DTO as provided by the server
    * @return the merkle path item
    */
   public static MerklePathItem fromDto(io.proximax.sdk.gen.model.MerklePathItem dto) {
      return new MerklePathItem(dto.getPosition(), dto.getHash());
   }
 }
