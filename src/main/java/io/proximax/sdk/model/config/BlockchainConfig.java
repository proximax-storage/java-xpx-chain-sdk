/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import io.proximax.sdk.gen.model.ConfigDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Network configuration
 */
public class BlockchainConfig {

   private final BigInteger height;
   private final String config;
   private final String supportedEntityVersions;
   
   /**
    * @param height height at which the configuration was valid
    * @param config the configuration
    * @param supportedEntityVersions the entity versions that are allowed
    */
   public BlockchainConfig(BigInteger height, String config, String supportedEntityVersions) {
      this.height = height;
      this.config = config;
      this.supportedEntityVersions = supportedEntityVersions;
   }

   /**
    * @return the height
    */
   public BigInteger getHeight() {
      return height;
   }

   /**
    * @return the blockChainConfig
    */
   public String getConfig() {
      return config;
   }

   /**
    * @return the supportedEntityVersions
    */
   public String getSupportedEntityVersions() {
      return supportedEntityVersions;
   }

   /**
    * get blockchain configuration as properties instance
    * 
    * @return Properties initialized with the config contents
    * @throws IOException when configuration can not be loaded
    */
   public Properties getConfigProperties() throws IOException {
      Properties props = new Properties();
      props.load(new ByteArrayInputStream(getConfig().getBytes(StandardCharsets.UTF_8)));
      return props;
   }
   
   /**
    * convert from the DTO
    * @param dto the DTO from the service response
    * @return the network configuration instance
    */
   public static BlockchainConfig fromDto(ConfigDTO dto) {
      return new BlockchainConfig(
            UInt64Utils.toBigInt(new ArrayList<>(dto.getHeight())),
            dto.getNetworkConfig(),
            dto.getSupportedEntityVersions());
   }
}
