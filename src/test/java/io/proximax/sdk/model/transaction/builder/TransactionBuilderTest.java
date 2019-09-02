/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.DeadlineRaw;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.TransactionInfo;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * {@link TransactionBuilder} tests based on the {@link TransferTransactionBulder}
 */
class TransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private TransferTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new TransferTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }   
   @Test
   void testEmptyTransferToRecipient() {
      // prepare data
      Account acc = new Account(new KeyPair(), NETWORK_TYPE);
      TransactionInfo transInfo = TransactionInfo.create(BigInteger.ONE, 4, "hello", "CAFE", "CAFECAFE");
      Recipient recipient = Recipient.from(acc.getAddress());
      // create transaction
      TransferTransaction trans = builder.to(recipient)
            .version(7)
            .type(TransactionType.ACCOUNT_LINK)
            .maxFee(BigInteger.TEN)
            .deadline(DeadlineRaw.startNow(BigInteger.valueOf(50000)))
            .signature("CAFE")
            .signer(acc.getPublicAccount())
            .transactionInfo(transInfo)
            .build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      // type of result will be different from the specified
      assertEquals(TransactionType.TRANSFER, trans.getType());
      assertEquals(TransactionType.ACCOUNT_LINK, builder.getType());
      assertEquals(7, trans.getVersion());
      assertEquals(BigInteger.TEN, trans.getMaxFee());
      assertEquals(Optional.of(FeeCalculationStrategy.MEDIUM), builder.getFeeCalculationStrategy());
      assertTrue(Math.abs(DeadlineRaw.startNow(BigInteger.valueOf(50_000)).getInstant() - trans.getDeadline().getInstant())<500);
      assertEquals(Optional.of("CAFE"), trans.getSignature());
      assertEquals(Optional.of(acc.getPublicAccount()), trans.getSigner());
      assertEquals(Optional.of(transInfo), trans.getTransactionInfo());
   }
   
   @Test
   void testDeadline() {
      TransactionBuilder bld = new TransferTransactionBuilder();
      
      assertThrows(IllegalStateException.class, () -> bld.getDeadline());
   }
}
