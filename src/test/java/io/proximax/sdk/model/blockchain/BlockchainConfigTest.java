/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.ConfigDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * {@link BlockchainConfig} tests
 */
class BlockchainConfigTest {

   @Test
   void testConstructor() throws IOException {
      BlockchainConfig conf = new BlockchainConfig(BigInteger.TEN, "a=b\nc=d", "entities");
      // assertions
      assertEquals(BigInteger.TEN, conf.getHeight());
      assertEquals("a=b\nc=d", conf.getConfig());
      assertEquals("entities", conf.getSupportedEntityVersions());
      // check parsing to properties
      assertEquals("b", conf.getConfigProperties().getProperty("a"));
   }

   @Test
   void testFromDto() {
      ConfigDTO confDto = new ConfigDTO();
      confDto.setHeight(UInt64Utils.dtoFromBigInt(BigInteger.TEN));
      confDto.setBlockChainConfig("config");
      confDto.setSupportedEntityVersions("supent");
      
      // make the conversion
      BlockchainConfig bcc = BlockchainConfig.fromDto(confDto);
      
      // make assertions
      assertEquals(BigInteger.TEN, bcc.getHeight());
      assertEquals("config", bcc.getConfig());
      assertEquals("supent", bcc.getSupportedEntityVersions());
      
   }
}
