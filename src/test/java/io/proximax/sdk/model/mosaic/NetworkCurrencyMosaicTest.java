/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class NetworkCurrencyMosaicTest {

   @Test
   void shouldCreateViaConstructor() {
      NetworkCurrencyMosaic mosaic = new NetworkCurrencyMosaic(BigInteger.valueOf(0));
      assertEquals(BigInteger.valueOf(0), mosaic.getAmount());
      assertEquals(NetworkCurrencyMosaic.ID, mosaic.getId());
   }

   @Test
   void createRelativeDecimal() {
      NetworkCurrencyMosaic mosaic = NetworkCurrencyMosaic.createRelative(BigDecimal.valueOf(15, 1));
      assertEquals(BigInteger.valueOf(1500000), mosaic.getAmount());
      assertEquals(NetworkCurrencyMosaic.ID, mosaic.getId());

   }

   @Test
   void createRelativeDecimalWhole() {
      NetworkCurrencyMosaic mosaic = NetworkCurrencyMosaic.createRelative(BigDecimal.valueOf(15));
      assertEquals(BigInteger.valueOf(15000000), mosaic.getAmount());
      assertEquals(NetworkCurrencyMosaic.ID, mosaic.getId());

   }

   @Test
   void shouldCreateAbsolute() {
      NetworkCurrencyMosaic mosaic = NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1));
      assertEquals(BigInteger.valueOf(1), mosaic.getAmount());
      assertEquals(NetworkCurrencyMosaic.ID, mosaic.getId());
   }
   
   @Test
   void testConstants() {
      assertEquals(1000000l, NetworkCurrencyMosaic.ONE.getAmount().longValue());
      assertEquals(10000000l, NetworkCurrencyMosaic.TEN.getAmount().longValue());
   }
}