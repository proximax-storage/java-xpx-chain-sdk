/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.DeadlineRaw;

/**
 * {@link TransactionBuilderFactory} tests
 */
class TransactionBuilderFactoryTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;
   private static final BigInteger DELAY = BigInteger.valueOf(60_000);
   
   @Test
   void test() {
      TransactionBuilderFactory fac = new TransactionBuilderFactory();
      fac.setNetworkType(NETWORK_TYPE);
      fac.setDeadlineMillis(DELAY);
      fac.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
      
      testDefaults(fac.accountLink());
      testDefaults(fac.accountPropAddress());
      testDefaults(fac.accountPropEntityType());
      testDefaults(fac.accountPropMosaic());
      testDefaults(fac.aggregateBonded());
      testDefaults(fac.aggregateComplete());
      testDefaults(fac.aliasAddress());
      testDefaults(fac.aliasMosaic());
      testDefaults(fac.blockchainConfig());
      testDefaults(fac.blockchainUpgrade());
      testDefaults(fac.lockFunds());
      testDefaults(fac.modifyContract());
      testDefaults(fac.modifyMetadata());
      testDefaults(fac.mosaicDefinition());
      testDefaults(fac.mosaicSupplyChange());
      testDefaults(fac.multisigModification());
      testDefaults(fac.registerNamespace());
      testDefaults(fac.secretLock());
      testDefaults(fac.secretProof());
      testDefaults(fac.transfer());

   }

   public void testDefaults(TransactionBuilder<?, ?> builder) {
      assertEquals(NETWORK_TYPE, builder.getNetworkType());
      assertTrue(Math.abs(DeadlineRaw.startNow(DELAY).getInstant() - builder.getDeadline().getInstant()) < 500);
      assertEquals(Optional.of(FeeCalculationStrategy.MEDIUM), builder.getFeeCalculationStrategy());
   }
}
