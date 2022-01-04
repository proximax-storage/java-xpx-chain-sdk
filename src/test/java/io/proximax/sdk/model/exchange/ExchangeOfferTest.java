/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link ExchangeOffer} tests
 */
class ExchangeOfferTest {
   private static final MosaicId MOSAIC_ID = new MosaicId(BigInteger.valueOf(1234567890l));

   @Test
   void testConstructor() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      PublicAccount owner = new PublicAccount("ABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);

      ExchangeOffer offer = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);

      assertEquals(MOSAIC_ID, offer.getMosaicId());
      assertEquals(amount, offer.getMosaicAmount());
      assertEquals(cost, offer.getCost());
      assertEquals(type, offer.getType());
      assertEquals(owner, offer.getOwner());

   }

   @Test
   void testMosaicConstructor() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      PublicAccount owner = new PublicAccount("ABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);

      ExchangeOffer offer = new ExchangeOffer(new Mosaic(MOSAIC_ID, amount), cost, type, owner);

      assertEquals(MOSAIC_ID, offer.getMosaicId());
      assertEquals(amount, offer.getMosaicAmount());
      assertEquals(cost, offer.getCost());
      assertEquals(type, offer.getType());
      assertEquals(owner, offer.getOwner());

   }

   @Test
   void checkEquals() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      PublicAccount owner = new PublicAccount("ABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);
      PublicAccount owner2 = new PublicAccount("AAAAEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);

      ExchangeOffer a1 = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);
      ExchangeOffer a2 = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);
      ExchangeOffer b = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner2);

      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "whatever");
   }

   @Test
   void checkHashcode() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      PublicAccount owner = new PublicAccount("ABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);
      PublicAccount owner2 = new PublicAccount("AAAAEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);

      ExchangeOffer a1 = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);
      ExchangeOffer a2 = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);
      ExchangeOffer b = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner2);

      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }

   @Test
   void testToString() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      PublicAccount owner = new PublicAccount("ABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFABABCDEFAB",
            NetworkType.TEST_NET);

      ExchangeOffer offer = new ExchangeOffer(MOSAIC_ID, amount, cost, type, owner);

      assertTrue(offer.toString().startsWith("ExchangeOffer"));
   }
}
