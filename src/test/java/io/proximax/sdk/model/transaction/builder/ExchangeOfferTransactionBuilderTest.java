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
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.exchange.ExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.ExchangeOfferTransaction;

/**
 * {@link ExchangeOfferTransactionBuilder} tests
 */
class ExchangeOfferTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;
   private static final MosaicId MOSAIC_ID = new MosaicId(BigInteger.valueOf(1234567890l));

   private ExchangeOfferTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new ExchangeOfferTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   @Test
   void test() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      PublicAccount owner = new PublicAccount("ABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.MIJIN);
      
      ExchangeOffer offer = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);
      
      ExchangeOfferTransaction trans = builder.offers(offer).build();
      
      assertEquals(Arrays.asList(offer), trans.getOffers());
   }

}
