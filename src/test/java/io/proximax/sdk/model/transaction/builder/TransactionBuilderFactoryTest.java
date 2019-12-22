/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
   void testDefaults() {
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
      testDefaults(fac.contract());
      testDefaults(fac.modifyContract());
      testDefaults(fac.modifyMetadata());
      testDefaults(fac.mosaicDefinition());
      testDefaults(fac.mosaicSupplyChange());
      testDefaults(fac.multisigModification());
      testDefaults(fac.registerNamespace());
      testDefaults(fac.secretLock());
      testDefaults(fac.secretProof());
      testDefaults(fac.transfer());
      testDefaults(fac.exchangeAdd());
      testDefaults(fac.exchangeRemove());
      testDefaults(fac.exchangeOffer());

      assertEquals(NETWORK_TYPE, fac.getNetworkType());
      assertEquals(DELAY, fac.getDeadlineMillis());
      assertEquals(FeeCalculationStrategy.MEDIUM, fac.getFeeCalculationStrategy());
   }

   @Test
   void testNoDefaults() {
      TransactionBuilderFactory fac = new TransactionBuilderFactory();
      
      testNoDefaults(fac.accountLink());
      testNoDefaults(fac.accountPropAddress());
      testNoDefaults(fac.accountPropEntityType());
      testNoDefaults(fac.accountPropMosaic());
      testNoDefaults(fac.aggregateBonded());
      testNoDefaults(fac.aggregateComplete());
      testNoDefaults(fac.aliasAddress());
      testNoDefaults(fac.aliasMosaic());
      testNoDefaults(fac.blockchainConfig());
      testNoDefaults(fac.blockchainUpgrade());
      testNoDefaults(fac.lockFunds());
      testNoDefaults(fac.contract());
      testNoDefaults(fac.modifyContract());
      testNoDefaults(fac.modifyMetadata());
      testNoDefaults(fac.mosaicDefinition());
      testNoDefaults(fac.mosaicSupplyChange());
      testNoDefaults(fac.multisigModification());
      testNoDefaults(fac.registerNamespace());
      testNoDefaults(fac.secretLock());
      testNoDefaults(fac.secretProof());
      testNoDefaults(fac.transfer());
      testNoDefaults(fac.exchangeAdd());
      testNoDefaults(fac.exchangeRemove());
      testNoDefaults(fac.exchangeOffer());

      assertNull(fac.getNetworkType());
      assertNull(fac.getDeadlineMillis());
      assertNull(fac.getFeeCalculationStrategy());
   }

   public void testDefaults(TransactionBuilder<?, ?> builder) {
      assertEquals(NETWORK_TYPE, builder.getNetworkType());
      assertTrue(Math.abs(DeadlineRaw.startNow(DELAY).getInstant() - builder.getDeadline().getInstant()) < 500);
      assertEquals(Optional.of(FeeCalculationStrategy.MEDIUM), builder.getFeeCalculationStrategy());
   }
   
   public void testNoDefaults(TransactionBuilder<?, ?> builder) {
      assertNull(builder.getNetworkType());
      assertThrows(IllegalStateException.class, () -> builder.getDeadline());
      assertEquals(Optional.empty(), builder.getFeeCalculationStrategy());
   }
}
