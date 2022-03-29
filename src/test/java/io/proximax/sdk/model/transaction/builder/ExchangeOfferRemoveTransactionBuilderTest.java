/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.exchange.RemoveExchangeOffer;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.ExchangeOfferRemoveTransaction;

/**
 * {@link ExchangeOfferRemoveTransactionBuilder} tests
 */
class ExchangeOfferRemoveTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;
   private static final MosaicId MOSAIC_ID = new MosaicId(BigInteger.valueOf(1234567890l));

   private ExchangeOfferRemoveTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new ExchangeOfferRemoveTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   @Test
   void test() {
      ExchangeOfferType type = ExchangeOfferType.SELL;
      
      RemoveExchangeOffer offer = new RemoveExchangeOffer(MOSAIC_ID, type);
      
      ExchangeOfferRemoveTransaction trans = builder.offers(offer).build();
      
      assertEquals(Arrays.asList(offer), trans.getOffers());
   }

}
