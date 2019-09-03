/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;

/**
 * {@link MosaicDefinitionTransactionBuilder} tests
 */
class MosaicDefinitionTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private MosaicDefinitionTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new MosaicDefinitionTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void testNoDuration() {
      MosaicNonce nonce = MosaicNonce.createRandom();
      MosaicId mosid = new MosaicId("CAFECAFECAFECAFE");
      MosaicProperties props = new MosaicProperties(true, true, 5, Optional.empty());
      MosaicDefinitionTransaction trans = builder.nonce(nonce).mosaicId(mosid).mosaicProperties(props).build();
      
      assertEquals(nonce, trans.getNonce());
      assertEquals(mosid, trans.getMosaicId());
      assertEquals(props, trans.getMosaicProperties());
   }

   @Test
   void testDuration() {
      MosaicNonce nonce = MosaicNonce.createRandom();
      MosaicId mosid = new MosaicId("CAFECAFECAFECAFE");
      MosaicProperties props = new MosaicProperties(true, true, 5, Optional.of(BigInteger.ONE));
      MosaicDefinitionTransaction trans = builder.nonce(nonce).mosaicId(mosid).mosaicProperties(props).build();
      
      assertEquals(nonce, trans.getNonce());
      assertEquals(mosid, trans.getMosaicId());
      assertEquals(props, trans.getMosaicProperties());
      assertEquals(Optional.of(BigInteger.ONE), trans.getMosaicProperties().getDuration());
   }

}
