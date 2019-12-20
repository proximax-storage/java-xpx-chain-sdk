/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import java.util.Objects;

import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * Descriptor for exchange offer removal. Can be used to cancel offer which was created via {@link AddExchangeOffer}
 */
public class RemoveExchangeOffer {

   private final UInt64Id mosaicId;
   private final ExchangeOfferType type;

   /**
    * @param mosaicId ID of the mosaic for which the offer is standing
    * @param type type of the offer indicating whether mosaic is being bought or sold
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

   @Override
   public int hashCode() {
      return Objects.hash(mosaicId, type);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      RemoveExchangeOffer other = (RemoveExchangeOffer) obj;
      return Objects.equals(mosaicId, other.mosaicId) && type == other.type;
   }

   @Override
   public String toString() {
      return "RemoveExchangeOffer [mosaicId=" + mosaicId + ", type=" + type + "]";
   }

}
