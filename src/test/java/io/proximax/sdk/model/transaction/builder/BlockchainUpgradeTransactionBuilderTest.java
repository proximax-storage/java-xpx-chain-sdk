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
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.BlockchainUpgradeTransaction;

/**
 * {@link BlockchainUpgradeTransactionBuilder} tests
 */
class BlockchainUpgradeTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private BlockchainUpgradeTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new BlockchainUpgradeTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   @Test
   void test() {
      BlockchainVersion version = new BlockchainVersion(1, 2, 3, 4);
      BlockchainUpgradeTransaction trans = builder.newVersion(version).upgradePeriod(BigInteger.TEN).build();
      
      assertEquals(version, trans.getNewVersion());
      assertEquals(BigInteger.TEN, trans.getUpgradePeriod());
   }

}
