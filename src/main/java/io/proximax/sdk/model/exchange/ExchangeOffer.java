/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import java.math.BigInteger;

import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * Descriptor for exchange offer
 */
public class ExchangeOffer {
   
   private final UInt64Id mosaicId;
   private final BigInteger mosaicAmount;
   private final BigInteger cost;
   private final ExchangeOfferType type;
   private final String owner;
   
   /**
    * @param mosaicId
    * @param mosaicAmount
    * @param cost
    * @param type
    * @param owner
    */
   public ExchangeOffer(UInt64Id mosaicId, BigInteger mosaicAmount, BigInteger cost, ExchangeOfferType type,
         String owner) {
      this.mosaicId = mosaicId;
      this.mosaicAmount = mosaicAmount;
      this.cost = cost;
      this.type = type;
      this.owner = owner;
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
    * @return the owner
    */
   public String getOwner() {
      return owner;
   }
   
}
