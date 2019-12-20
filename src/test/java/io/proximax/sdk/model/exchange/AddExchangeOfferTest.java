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

import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;

/**
 * {@link AddExchangeOffer} tests
 */
class AddExchangeOfferTest {
   private static final MosaicId MOSAIC_ID = new MosaicId(BigInteger.valueOf(1234567890l));

   @Test
   void testConstructor() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      BigInteger duration = BigInteger.ZERO;
      
      AddExchangeOffer offer = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, duration);
      
      assertEquals(MOSAIC_ID, offer.getMosaicId());
      assertEquals(amount, offer.getMosaicAmount());
      assertEquals(cost, offer.getCost());
      assertEquals(type, offer.getType());
      assertEquals(duration, offer.getDuration());

   }

   @Test
   void testMosaicConstructor() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      BigInteger duration = BigInteger.ZERO;
      
      AddExchangeOffer offer = new AddExchangeOffer(new Mosaic(MOSAIC_ID, amount), cost, type, duration);
      
      assertEquals(MOSAIC_ID, offer.getMosaicId());
      assertEquals(amount, offer.getMosaicAmount());
      assertEquals(cost, offer.getCost());
      assertEquals(type, offer.getType());
      assertEquals(duration, offer.getDuration());

   }

   @Test
   void checkEquals() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      BigInteger duration = BigInteger.ZERO;
      
      AddExchangeOffer a1 = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, duration);
      AddExchangeOffer a2 = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, duration);
      AddExchangeOffer b = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, BigInteger.valueOf(1234));
      
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
      BigInteger duration = BigInteger.ZERO;
      
      AddExchangeOffer a1 = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, duration);
      AddExchangeOffer a2 = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, duration);
      AddExchangeOffer b = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, BigInteger.valueOf(1234));
      
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }
   
   @Test
   void testToString() {
      BigInteger amount = BigInteger.TEN;
      BigInteger cost = BigInteger.ONE;
      ExchangeOfferType type = ExchangeOfferType.SELL;
      BigInteger duration = BigInteger.ZERO;
      
      AddExchangeOffer offer = new AddExchangeOffer(MOSAIC_ID, amount, cost, type, duration);
      
      assertTrue(offer.toString().startsWith("AddExchangeOffer "));
   }
}
