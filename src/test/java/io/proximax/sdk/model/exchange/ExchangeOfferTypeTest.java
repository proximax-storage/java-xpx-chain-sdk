/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link ExchangeOfferType} tests
 */
class ExchangeOfferTypeTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals(0, ExchangeOfferType.SELL.getCode());
   }

   @Test
   void checkGetByCode() {
      for (ExchangeOfferType item: ExchangeOfferType.values()) {
         assertEquals(item, ExchangeOfferType.getByCode(item.getCode()));
      }
   }
   
   @Test
   void testBadCode() {
      assertThrows(IllegalArgumentException.class, () -> ExchangeOfferType.getByCode(-8));
   }
}
