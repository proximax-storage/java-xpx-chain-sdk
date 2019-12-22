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

import io.proximax.sdk.model.exchange.RemoveExchangeOffer;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ExchangeOfferRemoveTransaction;

/**
 * <p>
 * builder for {@link ExchangeOfferRemoveTransaction}
 * </p>
 * <p>
 * Standard use: to place offers call {@link #offers(RemoveExchangeOffer...)}
 * </p>
 */
public class ExchangeOfferRemoveTransactionBuilder extends TransactionBuilder<ExchangeOfferRemoveTransactionBuilder, ExchangeOfferRemoveTransaction> {
   private List<RemoveExchangeOffer> offers;

   public ExchangeOfferRemoveTransactionBuilder() {
      super(EntityType.EXCHANGE_OFFER_REMOVE, EntityVersion.EXCHANGE_OFFER_REMOVE.getValue());
      // defaults
      offers = new ArrayList<>();
   }

   @Override
   protected ExchangeOfferRemoveTransactionBuilder self() {
      return this;
   }

   @Override
   public ExchangeOfferRemoveTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(ExchangeOfferRemoveTransaction.calculatePayloadSize(getOffers().size())));
      // create transaction instance
      return new ExchangeOfferRemoveTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(),
            getTransactionInfo(), getOffers());
   }

   // ------------------------------------ setters ------------------------------------//

   /**
    * specify offers to be announced
    * 
    * @param offers list of offers
    * @return self
    */
   public ExchangeOfferRemoveTransactionBuilder offers(List<RemoveExchangeOffer> offers) {
      this.offers = offers;
      return this;
   }

   // ------------------------------------------- getters ------------------------------------------//

   /**
    * @return the mosaics
    */
   public List<RemoveExchangeOffer> getOffers() {
      return offers;
   }

   // ----------------------------------------- convenience methods -------------------------------------//

   /**
    * @param offers the offers to set
    * @return self
    */
   public ExchangeOfferRemoveTransactionBuilder offers(RemoveExchangeOffer... offers) {
      return offers(Arrays.asList(offers));
   }

}
