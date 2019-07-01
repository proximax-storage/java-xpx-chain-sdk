/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.MerkleProofInfo;

/**
 * {@link MerklePath} tests
 */
class MerklePathTest {

   @Test
   void testConstructor() {
      MerklePath path = new MerklePath(new LinkedList<>());
      assertEquals(0, path.getItems().size());
   }

   @Test
   void testFromDto() {
      MerkleProofInfo dto = new MerkleProofInfo();
      dto.setMerklePath(new LinkedList<>());
      MerklePath path = MerklePath.fromDto(dto);
      assertEquals(0, path.getItems().size());
   }
}
