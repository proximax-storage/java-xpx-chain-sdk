/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.exchange.AddExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link ExchangeOfferAddTransaction} tests
 */
class ExchangeOfferAddTransactionTest extends ResourceBasedTest {
   private static final MosaicId MOSAIC_ID = new MosaicId(BigInteger.valueOf(1234567890l));

   @Test
   void testConstructor() throws IOException {
      AddExchangeOffer offer = new AddExchangeOffer(MOSAIC_ID, BigInteger.ZERO, BigInteger.ONE, ExchangeOfferType.SELL,
            BigInteger.TEN);
      ExchangeOfferAddTransaction trans = new ExchangeOfferAddTransaction(NetworkType.TEST_NET, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), Arrays.asList(offer));

      assertEquals(Arrays.asList(offer), trans.getOffers());
   }

   @Test
   void serialization() throws IOException {
      AddExchangeOffer offer = new AddExchangeOffer(MOSAIC_ID, BigInteger.ZERO, BigInteger.ONE, ExchangeOfferType.SELL,
            BigInteger.TEN);
      ExchangeOfferAddTransaction trans = new ExchangeOfferAddTransaction(NetworkType.TEST_NET, 
                  EntityVersion.EXCHANGE_OFFER_ADD.getValue(), new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), Arrays.asList(offer));


      byte[] actual = trans.generateBytes();
//      saveBytes("exchange_add_transaction", actual);
      assertArrayEquals(loadBytes("exchange_add_transaction"), actual);
   }

   @Test
   void checkCopyToSigner() throws IOException {
      PublicAccount remoteAccount = new Account(new KeyPair(), NetworkType.TEST_NET).getPublicAccount();

      AddExchangeOffer offer = new AddExchangeOffer(MOSAIC_ID, BigInteger.ZERO, BigInteger.ONE, ExchangeOfferType.SELL,
            BigInteger.TEN);
      ExchangeOfferAddTransaction trans = new ExchangeOfferAddTransaction(NetworkType.TEST_NET, 4, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), Arrays.asList(offer));

      Transaction t = trans.copyForSigner(remoteAccount);
      assertEquals(Optional.of(remoteAccount), t.getSigner());
   }
}
