/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.proximax.sdk.model.transaction.builder.TransactionFactory;
import io.proximax.sdk.model.transaction.builder.TransferTransactionBuilder;

/**
 * {@link TransferTransactionBuilder} tests
 */
class TransferTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;
   
   @Test
   void testEmptyTransfer() {
      // prepare factory
      TransactionFactory fac = new TransactionFactory();
      fac.setNetworkType(NETWORK_TYPE);
      fac.setDeadlineMillis(BigInteger.valueOf(60_000));
      fac.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
      // prepare data
      Recipient recipient = Recipient.from(new Account(new KeyPair(), NETWORK_TYPE).getAddress());
      // create transaction
      TransferTransaction trans = fac.transfer().to(recipient).build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(Optional.of(FeeCalculationStrategy.MEDIUM), trans.getFeeCalculationStrategy());
      assertTrue(trans.getMosaics().isEmpty());
      assertEquals(PlainMessage.Empty, trans.getMessage());
      assertEquals(recipient, trans.getRecipient());
   }
   
   @Test
   void testMosaicTransferWithMessage() {
      // prepare factory
      TransactionFactory fac = new TransactionFactory();
      fac.setNetworkType(NETWORK_TYPE);
      fac.setDeadlineMillis(BigInteger.valueOf(60_000));
      fac.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
      // prepare data
      Recipient recipient = Recipient.from(new Account(new KeyPair(), NETWORK_TYPE).getAddress());
      // create transaction
      TransferTransaction trans = fac.transfer().addMosaic(NetworkCurrencyMosaic.TEN).to(recipient).message(PlainMessage.create("hello world")).build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(Optional.of(FeeCalculationStrategy.MEDIUM), trans.getFeeCalculationStrategy());
      assertEquals(Arrays.asList(NetworkCurrencyMosaic.TEN), trans.getMosaics());
      assertEquals("hello world", trans.getMessage().getPayload());
      assertEquals(recipient, trans.getRecipient());
   }
}
