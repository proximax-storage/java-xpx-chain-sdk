/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.AggregateTransactionCosignature;
import io.proximax.sdk.model.transaction.Deadline;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * {@link AggregateTransactionBuilder} tests
 */
class AggregateTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;

   private AggregateTransactionBuilder completeBuilder;
   private AggregateTransactionBuilder bondedBuilder;
   
   @BeforeEach
   void setUp() {
      completeBuilder = initBuilder(AggregateTransactionBuilder.createComplete());
      bondedBuilder = initBuilder(AggregateTransactionBuilder.createBonded());

   }
   
   private static AggregateTransactionBuilder initBuilder(AggregateTransactionBuilder builder) {
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
      return builder;
   }
   
   @Test
   void testCompleteNoCosig() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET)),
            Arrays.asList(), PlainMessage.EMPTY);

      AggregateTransaction agg = completeBuilder.innerTransactions(transferTx).build();
      
      assertEquals(1, agg.getInnerTransactions().size());
      assertEquals(transferTx, agg.getInnerTransactions().get(0));
      assertEquals(EntityType.AGGREGATE_COMPLETE, agg.getType());
      assertEquals(EntityVersion.AGGREGATE_COMPLETE.getValue(), agg.getVersion());
   }

   @Test
   void testBondedNoCosig() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET)),
            Arrays.asList(), PlainMessage.EMPTY);

      AggregateTransaction agg = bondedBuilder.innerTransactions(transferTx).build();
      
      assertEquals(1, agg.getInnerTransactions().size());
      assertEquals(transferTx, agg.getInnerTransactions().get(0));
      assertEquals(EntityType.AGGREGATE_BONDED, agg.getType());
      assertEquals(EntityVersion.AGGREGATE_BONDED.getValue(), agg.getVersion());

   }

   @Test
   void testCompleteWithCosig() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET)),
            Arrays.asList(), PlainMessage.EMPTY);
      
      PublicAccount puba = new PublicAccount("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
            NetworkType.TEST_NET);
      AggregateTransactionCosignature cosig = new AggregateTransactionCosignature("signature",
            puba);

      AggregateTransaction agg = completeBuilder.innerTransactions(transferTx).cosignatures(cosig).build();
      
      assertEquals(1, agg.getCosignatures().size());
      assertEquals("signature", agg.getCosignatures().get(0).getSignature());
      assertEquals(puba, agg.getCosignatures().get(0).getSigner());
   }


}
