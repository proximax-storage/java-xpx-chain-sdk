/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import java.math.BigInteger;
import java.util.Objects;

import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * Descriptor for exchange offer used to add new offer to the blockchain. Other parties can then make offers to fulfill
 * this offer via {@link ExchangeOffer}. Such offer can also be removed via {@link RemoveExchangeOffer}
 */
public class AddExchangeOffer {

   private final UInt64Id mosaicId;
   private final BigInteger mosaicAmount;
   private final BigInteger cost;
   private final ExchangeOfferType type;
   private final BigInteger duration;

   /**
    * @param mosaicId ID of the mosaic that is being either bought or sold for network currency
    * @param mosaicAmount absolute amount of the mosaic
    * @param cost absolute amount of network currency that is being exchanged for the mosaic. NOTE this is absolute
    * amount i.e. 1 XPX with divisibility 6 is represented as 1_000_000
    * @param type type of he offer indicating whether mosaic is being sold or bought
    * @param duration duration of the offer validity in number of blocks
    */
   public AddExchangeOffer(UInt64Id mosaicId, BigInteger mosaicAmount, BigInteger cost, ExchangeOfferType type,
         BigInteger duration) {
      this.mosaicId = mosaicId;
      this.mosaicAmount = mosaicAmount;
      this.cost = cost;
      this.type = type;
      this.duration = duration;
   }

   /**
    * @param mosaic mosaic that is being bought or sold
    * @param cost absolute amount of network currency that is being exchanged for the mosaic. NOTE this is absolute
    * amount i.e. 1 XPX with divisibility 6 is represented as 1_000_000
    * @param type type of he offer indicating whether mosaic is being sold or bought
    * @param duration duration of the offer validity in number of blocks
    */
   public AddExchangeOffer(Mosaic mosaic, BigInteger cost, ExchangeOfferType type, BigInteger duration) {
      this(mosaic.getId(), mosaic.getAmount(), cost, type, duration);
   }

   /**
    * @return the mosaicId
    */
   public UInt64Id getMosaicId() {
      return mosaicId;
   }

   /**
    * @return the mosaicAmount
    */
   public BigInteger getMosaicAmount() {
      return mosaicAmount;
   }

   /**
    * @return the cost
    */
   public BigInteger getCost() {
      return cost;
   }

   /**
    * @return the type
    */
   public ExchangeOfferType getType() {
      return type;
   }

   /**
    * @return the duration
    */
   public BigInteger getDuration() {
      return duration;
   }

   @Override
   public int hashCode() {
      return Objects.hash(cost, duration, mosaicAmount, mosaicId, type);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AddExchangeOffer other = (AddExchangeOffer) obj;
      return Objects.equals(cost, other.cost) && Objects.equals(duration, other.duration)
            && Objects.equals(mosaicAmount, other.mosaicAmount) && Objects.equals(mosaicId, other.mosaicId)
            && type == other.type;
   }

   @Override
   public String toString() {
      return "AddExchangeOffer [mosaicId=" + mosaicId + ", mosaicAmount=" + mosaicAmount + ", cost=" + cost + ", type="
            + type + ", duration=" + duration + "]";
   }

}
