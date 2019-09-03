/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.BlockchainConfigTransaction;

/**
 * {@link BlockchainConfigTransactionBuilder} tests
 */
class BlockchainConfigTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private BlockchainConfigTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new BlockchainConfigTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   @Test
   void test() {
      BlockchainConfigTransaction trans = builder.applyHeightDelta(BigInteger.ONE).blockchainConfig("config").supportedEntityVersions("entity versions").build();
      
      assertEquals(BigInteger.ONE, trans.getApplyHeightDelta());
      assertEquals("config", trans.getBlockchainConfig());
      assertEquals("entity versions", trans.getSupportedEntityVersions());
   }

}
