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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * XPX Mosaic
 */
public class NetworkCurrencyMosaic extends Mosaic {
   /**
    * ID of network currency
    */
   // TODO had to replace this by hard-coded mosaic ID because alias to currency namespace is not in the dev catapult
//   public static final NamespaceId ID = new NamespaceId("prx.xpx");
   public static final MosaicId ID = new MosaicId(new BigInteger("0DC67FBE1CAD29E3", 16));
   /**
    * Divisibility
    */
   public static final int DIVISIBILITY = 6;
   /**
    * Initial supply
    */
   public static final BigInteger INITIALSUPPLY = BigInteger.valueOf(9000000000000000l);
   /**
    * Is transferable
    */
   public static final boolean TRANSFERABLE = true;
   /**
    * Is supply mutable
    */
   public static final boolean SUPPLYMUTABLE = false;
   /**
    * Is levy mutable
    */
   public static final boolean LEVYMUTABLE = false;

   /**
    * create specified amount of micro XPX
    * 
    * @param amount amount of micro XPX
    */
   public NetworkCurrencyMosaic(BigInteger amount) {
      super(NetworkCurrencyMosaic.ID, amount);
   }

   /**
    * Create XPX with using XPX as unit.
    *
    * @param amount amount to send
    * @return a XPX instance
    */
   public static NetworkCurrencyMosaic createRelative(BigInteger amount) {
      BigInteger relativeAmount = BigDecimal.valueOf(Math.pow(10, NetworkCurrencyMosaic.DIVISIBILITY)).toBigInteger().multiply(amount);
      return new NetworkCurrencyMosaic(relativeAmount);
   }

   /**
    * Create XPX with using micro XPX as unit, 1 XPX = 1000000 micro XPX.
    *
    * @param amount amount to send
    * @return a XPX instance
    */
   public static NetworkCurrencyMosaic createAbsolute(BigInteger amount) {
      return new NetworkCurrencyMosaic(amount);
   }
}
