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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * {@link TransferTransactionBuilder} tests
 */
class TransferTransactionBuilderTest {

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
      Recipient recipient = Recipient.from(new Account(new KeyPair(), NETWORK_TYPE).getAddress());
      // create transaction
      TransferTransaction trans = builder.to(recipient).build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertTrue(trans.getMosaics().isEmpty());
      assertEquals(PlainMessage.Empty, trans.getMessage());
      assertEquals(recipient, trans.getRecipient());
   }
   
   @Test
   void testEmptyTransferToAddress() {
      // prepare data
      Address recipient = new Account(new KeyPair(), NETWORK_TYPE).getAddress();
      // create transaction
      TransferTransaction trans = builder.to(recipient).build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertTrue(trans.getMosaics().isEmpty());
      assertEquals(PlainMessage.Empty, trans.getMessage());
      assertEquals(Recipient.from(recipient), trans.getRecipient());
   }
   
   @Test
   void testEmptyTransferToNamespace() {
      // prepare data
      NamespaceId recipient = new NamespaceId("john.wick");
      // create transaction
      TransferTransaction trans = builder.to(recipient).build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertTrue(trans.getMosaics().isEmpty());
      assertEquals(PlainMessage.Empty, trans.getMessage());
      assertEquals(Recipient.from(recipient), trans.getRecipient());
   }
   
   @Test
   void testMosaicTransferWithMessage() {
      // prepare data
      Recipient recipient = Recipient.from(new Account(new KeyPair(), NETWORK_TYPE).getAddress());
      // create transaction
      TransferTransaction trans = builder.mosaics(NetworkCurrencyMosaic.TEN).to(recipient).message(PlainMessage.create("hello world")).build();
      // check transaction
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(Arrays.asList(NetworkCurrencyMosaic.TEN), trans.getMosaics());
      assertEquals("hello world", trans.getMessage().getPayload());
      assertEquals(recipient, trans.getRecipient());
   }
}
