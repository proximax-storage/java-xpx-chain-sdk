/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link TransactionBuilderFactory} tests
 */
class TransactionFactoryTest {
   private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;
   
   @Test
   void prepareFactory() {
      TransactionBuilderFactory fac = new TransactionBuilderFactory();
      fac.setNetworkType(NETWORK_TYPE);
      fac.setDeadlineMillis(BigInteger.valueOf(60_000));
      fac.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);

      assertEquals(NETWORK_TYPE, fac.getNetworkType());
      assertEquals(BigInteger.valueOf(60_000), fac.getDeadlineMillis());
      assertEquals(FeeCalculationStrategy.MEDIUM, fac.getFeeCalculationStrategy());
   }
   
}
