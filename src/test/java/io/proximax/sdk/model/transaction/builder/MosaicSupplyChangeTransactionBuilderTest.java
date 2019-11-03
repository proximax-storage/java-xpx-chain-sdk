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
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicSupplyType;
import io.proximax.sdk.model.transaction.MosaicSupplyChangeTransaction;

/**
 * {@link MosaicSupplyChangeTransactionBuilder} tests
 */
class MosaicSupplyChangeTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private MosaicSupplyChangeTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new MosaicSupplyChangeTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void increaseFor() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(1234567890l));
      MosaicSupplyChangeTransaction trans = builder.increaseSupplyFor(mosid).delta(BigInteger.TEN).build();
      
      assertEquals(mosid, trans.getMosaicId());
      assertEquals(MosaicSupplyType.INCREASE, trans.getMosaicSupplyType());
      assertEquals(BigInteger.TEN, trans.getDelta());
   }


   @Test
   void decreaseFor() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(1234567890l));
      MosaicSupplyChangeTransaction trans = builder.decreaseSupplyFor(mosid).delta(BigInteger.TEN).build();
      
      assertEquals(mosid, trans.getMosaicId());
      assertEquals(MosaicSupplyType.DECREASE, trans.getMosaicSupplyType());
      assertEquals(BigInteger.TEN, trans.getDelta());
   }

   @Test
   void increase() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(1234567890l));
      MosaicSupplyChangeTransaction trans = builder.increase(mosid, BigInteger.TEN).build();
      
      assertEquals(mosid, trans.getMosaicId());
      assertEquals(MosaicSupplyType.INCREASE, trans.getMosaicSupplyType());
      assertEquals(BigInteger.TEN, trans.getDelta());
   }


   @Test
   void decrease() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(1234567890l));
      MosaicSupplyChangeTransaction trans = builder.decrease(mosid, BigInteger.TEN).build();
      
      assertEquals(mosid, trans.getMosaicId());
      assertEquals(MosaicSupplyType.DECREASE, trans.getMosaicSupplyType());
      assertEquals(BigInteger.TEN, trans.getDelta());
   }

}
