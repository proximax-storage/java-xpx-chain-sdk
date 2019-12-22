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

import io.proximax.sdk.model.mosaic.MosaicId;

/**
 * {@link RemoveExchangeOffer} tests
 */
class RemoveExchangeOfferTest {
   private static final MosaicId MOSAIC_ID = new MosaicId(BigInteger.valueOf(1234567890l));

   @Test
   void testConstructor() {
      ExchangeOfferType type = ExchangeOfferType.SELL;
      
      RemoveExchangeOffer offer = new RemoveExchangeOffer(MOSAIC_ID, type);
      
      assertEquals(MOSAIC_ID, offer.getMosaicId());
      assertEquals(type, offer.getType());

   }

   @Test
   void checkEquals() {
      ExchangeOfferType type = ExchangeOfferType.SELL;
      
      RemoveExchangeOffer a1 = new RemoveExchangeOffer(MOSAIC_ID, type);
      RemoveExchangeOffer a2 = new RemoveExchangeOffer(MOSAIC_ID, type);
      RemoveExchangeOffer b = new RemoveExchangeOffer(MOSAIC_ID, ExchangeOfferType.BUY);
      
      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "whatever");
   }
   
   @Test
   void checkHashcode() {
      ExchangeOfferType type = ExchangeOfferType.SELL;
      
      RemoveExchangeOffer a1 = new RemoveExchangeOffer(MOSAIC_ID, type);
      RemoveExchangeOffer a2 = new RemoveExchangeOffer(MOSAIC_ID, type);
      RemoveExchangeOffer b = new RemoveExchangeOffer(MOSAIC_ID, ExchangeOfferType.BUY);
      
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }
   
   @Test
   void testToString() {
      ExchangeOfferType type = ExchangeOfferType.SELL;
      
      RemoveExchangeOffer offer = new RemoveExchangeOffer(MOSAIC_ID, type);
      
      assertTrue(offer.toString().startsWith("RemoveExchangeOffer"));
   }
}
