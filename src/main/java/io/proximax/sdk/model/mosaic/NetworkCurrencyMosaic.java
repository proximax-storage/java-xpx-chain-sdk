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
import java.util.Optional;

import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * XPX Mosaic
 */
public class NetworkCurrencyMosaic extends Mosaic {
   /**
    * Namespace with alias to the network currency mosaic
    */
   public static final String MOSAIC_NAMESPACE = "prx.xpx";
   
   /**
    * ID of network currency
    */
   public static final UInt64Id ID = new NamespaceId(MOSAIC_NAMESPACE);
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

   /** factory that can be used to create mosaic instances */
   public static final MosaicFactory FACTORY = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY, Optional.empty());
   
   /** one of network currency taking the divisibility into account */
   public static final NetworkCurrencyMosaic ONE = new NetworkCurrencyMosaic(FACTORY.getAbsoluteAmount(BigDecimal.ONE));
   /** ten of network currency taking the divisibility into account */
   public static final NetworkCurrencyMosaic TEN = new NetworkCurrencyMosaic(FACTORY.getAbsoluteAmount(BigDecimal.TEN));
   
   /**
    * create specified amount of micro XPX
    * 
    * @param amount amount of micro XPX
    */
   public NetworkCurrencyMosaic(BigInteger amount) {
      super(ID, amount);
   }

   /**
    * Create XPX with using XPX as unit.
    *
    * @param amount amount to send
    * @return a XPX instance
    */
   public static NetworkCurrencyMosaic createRelative(BigDecimal amount) {
      return new NetworkCurrencyMosaic(FACTORY.getAbsoluteAmount(amount));
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
