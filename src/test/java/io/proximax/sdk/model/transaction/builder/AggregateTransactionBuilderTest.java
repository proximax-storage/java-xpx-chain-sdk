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
import io.proximax.sdk.model.blockchain.NetworkType;
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

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

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
      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST)),
            Arrays.asList(), PlainMessage.Empty);

      AggregateTransaction agg = completeBuilder.innerTransactions(transferTx).build();
      
      assertEquals(1, agg.getInnerTransactions().size());
      assertEquals(transferTx, agg.getInnerTransactions().get(0));
      assertEquals(EntityType.AGGREGATE_COMPLETE, agg.getType());
      assertEquals(EntityVersion.AGGREGATE_COMPLETE.getValue(), agg.getVersion());
   }

   @Test
   void testBondedNoCosig() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST)),
            Arrays.asList(), PlainMessage.Empty);

      AggregateTransaction agg = bondedBuilder.innerTransactions(transferTx).build();
      
      assertEquals(1, agg.getInnerTransactions().size());
      assertEquals(transferTx, agg.getInnerTransactions().get(0));
      assertEquals(EntityType.AGGREGATE_BONDED, agg.getType());
      assertEquals(EntityVersion.AGGREGATE_BONDED.getValue(), agg.getVersion());

   }

   @Test
   void testCompleteWithCosig() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST)),
            Arrays.asList(), PlainMessage.Empty);
      
      PublicAccount puba = new PublicAccount("9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24",
            NetworkType.MIJIN_TEST);
      AggregateTransactionCosignature cosig = new AggregateTransactionCosignature("signature",
            puba);

      AggregateTransaction agg = completeBuilder.innerTransactions(transferTx).cosignatures(cosig).build();
      
      assertEquals(1, agg.getCosignatures().size());
      assertEquals("signature", agg.getCosignatures().get(0).getSignature());
      assertEquals(puba, agg.getCosignatures().get(0).getSigner());
   }


}
