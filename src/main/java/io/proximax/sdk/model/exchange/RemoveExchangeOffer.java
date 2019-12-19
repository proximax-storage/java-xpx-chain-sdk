/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * Descriptor for exchange offer
 */
public class RemoveExchangeOffer {
   
   private final UInt64Id mosaicId;
   private final ExchangeOfferType type;

   /**
    * @param mosaicId
    * @param type
    */
   public RemoveExchangeOffer(UInt64Id mosaicId, ExchangeOfferType type) {
      this.mosaicId = mosaicId;
      this.type = type;
   }
   
   /**
    * @return the mosaicId
    */
   public UInt64Id getMosaicId() {
      return mosaicId;
   }
   /**
    * @return the type
    */
   public ExchangeOfferType getType() {
      return type;
   }
}
