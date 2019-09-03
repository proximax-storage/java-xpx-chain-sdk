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
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.EntityType;

/**
 * {@link LockFundsTransactionBuilder} tests
 */
class LockFundsTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private LockFundsTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new LockFundsTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   @Test
   void test() {
      SignedTransaction signedTransaction = new SignedTransaction("payload",
            "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", EntityType.AGGREGATE_BONDED);
      LockFundsTransaction trans = builder.aggregate(BigInteger.ONE).signedTransaction(signedTransaction).build();
      
      assertEquals(BigInteger.ONE, trans.getDuration());
      assertEquals(NetworkCurrencyMosaic.TEN, trans.getMosaic());
      assertEquals(signedTransaction, trans.getSignedTransaction());
   }

}
