/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import java.math.BigInteger;

import io.proximax.sdk.gen.model.UpgradeDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Blockchain upgrade information
 */
public class BlockchainUpgrade {
   private final BigInteger height;
   private final BlockchainVersion version;
   
   /**
    * @param height the height at which specified version became required
    * @param version the node version
    */
   public BlockchainUpgrade(BigInteger height, BlockchainVersion version) {
      this.height = height;
      this.version = version;
   }

   /**
    * @return the height
    */
   public BigInteger getHeight() {
      return height;
   }

   /**
    * @return the version
    */
   public BlockchainVersion getVersion() {
      return version;
   }
   
   /**
    * retrieve upgrade information from the service DTO
    * 
    * @param dto the DTO provided by the service
    * @return the 
    */
   public static BlockchainUpgrade fromDto(UpgradeDTO dto) {
      return new BlockchainUpgrade(
            UInt64Utils.toBigInt(dto.getHeight()),
            BlockchainVersion.fromVersionValue(
                  UInt64Utils.toBigInt(dto.getCatapultVersion())));
   }
}
