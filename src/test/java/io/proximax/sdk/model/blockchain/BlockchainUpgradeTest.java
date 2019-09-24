/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.UpgradeDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * {@link BlockchainUpgrade} tests
 */
class BlockchainUpgradeTest {

   @Test
   void testConstructor() {
      BlockchainUpgrade up = new BlockchainUpgrade(BigInteger.TEN, new BlockchainVersion(1, 2, 3, 4));
      
      assertEquals(BigInteger.TEN, up.getHeight());
      assertEquals(new BlockchainVersion(1, 2, 3, 4), up.getVersion());
   }

   @Test
   void testFromDto() {
      UpgradeDTO dto = new UpgradeDTO();
      dto.setBlockChainVersion(UInt64Utils.dtoFromBigInt(BigInteger.valueOf(123456789l)));
      dto.setHeight(UInt64Utils.dtoFromBigInt(BigInteger.TEN));
      
      BlockchainUpgrade up = BlockchainUpgrade.fromDto(dto);
      
      assertEquals(BigInteger.valueOf(123456789l), up.getVersion().getVersionValue());
      assertEquals(BigInteger.TEN, up.getHeight());
   }
}
