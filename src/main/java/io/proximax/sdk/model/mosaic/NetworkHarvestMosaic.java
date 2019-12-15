/*
 * Copyright 2019 ProximaX
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

/**
 * NetworkHarvestMosaic mosaic
 *
 * This represents the per-network harvest mosaic. This mosaicId is aliased with namespace name `cat.harvest`.
 *
 * @since 0.10.2
 */
public class NetworkHarvestMosaic extends Mosaic {

   /**
    * ID of `harvest` namespace.
    */
   public static final NamespaceId ID = new NamespaceId("cat.harvest");
   /**
    * Divisibility
    */
   public static final int DIVISIBILITY = 3;
   /**
    * Initial supply
    */
   public static final BigInteger INITIALSUPPLY = BigInteger.valueOf(15000000l);
   /**
    * Is transferable
    */
   public static final boolean TRANSFERABLE = true;
   /**
    * Is supply mutable
    */
   public static final boolean SUPPLYMUTABLE = true;

   /** factory that can be used to create mosaic instances */
   public static final MosaicFactory FACTORY = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY, Optional.empty());

   /** one of network harvest mosaic taking the divisibility into account */
   public static final NetworkHarvestMosaic ONE = new NetworkHarvestMosaic(FACTORY.getAbsoluteAmount(BigDecimal.ONE));
   /** ten of network harvest mosaic taking the divisibility into account */
   public static final NetworkHarvestMosaic TEN = new NetworkHarvestMosaic(FACTORY.getAbsoluteAmount(BigDecimal.TEN));

   /**
    * create new instance of network harvest mosaic of specified amount
    * 
    * @param amount amount of mosaic irrespective of divisibility
    */
   public NetworkHarvestMosaic(BigInteger amount) {
      super(ID, amount);
   }

   /**
    * Create xem with using xem as unit.
    *
    * @param amount amount to send
    * @return a NetworkCurrencyMosaic instance
    */
   public static NetworkHarvestMosaic createRelative(BigDecimal amount) {
      return new NetworkHarvestMosaic(FACTORY.getAbsoluteAmount(amount));
   }

   /**
    * Create xem with using micro xem as unit, 1 NetworkCurrencyMosaic = 1000000 micro NetworkCurrencyMosaic.
    *
    * @param amount amount to send
    * @return a NetworkCurrencyMosaic instance
    */
   public static NetworkHarvestMosaic createAbsolute(BigInteger amount) {
      return new NetworkHarvestMosaic(amount);
   }
}