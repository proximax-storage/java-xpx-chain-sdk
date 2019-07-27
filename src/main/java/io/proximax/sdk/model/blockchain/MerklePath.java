/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.MerkleProofInfo;

/**
 * merkle path is list of merkle path items
 */
public class MerklePath {
   private final List<MerklePathItem> items;

   /**
    * create new instance
    * 
    * @param items list of items of the path
    */
   public MerklePath(List<MerklePathItem> items) {
      this.items = Collections.unmodifiableList(items);
   }

   /**
    * @return the merkle path items
    */
   public List<MerklePathItem> getItems() {
      return items;
   }

   /**
    * create new merkle path from the DTO as provided by the server
    * 
    * @param dto DTO from the server
    * @return the path
    */
   public static MerklePath fromDto(MerkleProofInfo dto) {
      return new MerklePath(
            dto.getMerklePath().stream().map(MerklePathItem::fromDto).collect(Collectors.toList()));
   }
}
