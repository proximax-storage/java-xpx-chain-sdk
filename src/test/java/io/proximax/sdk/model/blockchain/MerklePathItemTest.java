/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * {@link MerklePathItem} tests
 */
class MerklePathItemTest {

   @Test
   void testConstructor() {
      MerklePathItem item = new MerklePathItem(1, "some hash");
      assertEquals(1, item.getPosition());
      assertEquals("some hash", item.getHash());
   }

   @Test
   void testFromDto() {
      io.proximax.sdk.gen.model.MerklePathItem dto = new io.proximax.sdk.gen.model.MerklePathItem();
      dto.setHash("some hash");
      dto.setPosition(1);
      MerklePathItem item = MerklePathItem.fromDto(dto);
      assertEquals(1, item.getPosition());
      assertEquals("some hash", item.getHash());   }
}
