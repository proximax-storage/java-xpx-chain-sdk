/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.proximax.sdk.model.exchange.ExchangeOffer;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ExchangeOfferTransaction;

/**
 * <p>
 * builder for {@link ExchangeOfferTransaction}
 * </p>
 * <p>
 * Standard use: to place offers call {@link #offers(ExchangeOffer...)}
 * </p>
 */
public class ExchangeOfferTransactionBuilder extends TransactionBuilder<ExchangeOfferTransactionBuilder, ExchangeOfferTransaction> {
   private List<ExchangeOffer> offers;

   public ExchangeOfferTransactionBuilder() {
      super(EntityType.EXCHANGE_OFFER_REMOVE, EntityVersion.EXCHANGE_OFFER_REMOVE.getValue());
      // defaults
      offers = new ArrayList<>();
   }

   @Override
   protected ExchangeOfferTransactionBuilder self() {
      return this;
   }

   @Override
   public ExchangeOfferTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(ExchangeOfferTransaction.calculatePayloadSize(getOffers().size())));
      // create transaction instance
      return new ExchangeOfferTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(),
            getTransactionInfo(), getOffers());
   }

   // ------------------------------------ setters ------------------------------------//

   /**
    * specify offers to be announced
    * 
    * @param offers list of offers
    * @return self
    */
   public ExchangeOfferTransactionBuilder offers(List<ExchangeOffer> offers) {
      this.offers = offers;
      return this;
   }

   // ------------------------------------------- getters ------------------------------------------//

   /**
    * @return the mosaics
    */
   public List<ExchangeOffer> getOffers() {
      return offers;
   }

   // ----------------------------------------- convenience methods -------------------------------------//

   /**
    * @param offers the offers to set
    * @return self
    */
   public ExchangeOfferTransactionBuilder offers(ExchangeOffer... offers) {
      return offers(Arrays.asList(offers));
   }

}
