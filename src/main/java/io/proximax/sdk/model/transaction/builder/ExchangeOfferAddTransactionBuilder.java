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

import io.proximax.sdk.model.exchange.AddExchangeOffer;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ExchangeOfferAddTransaction;

/**
 * <p>
 * builder for {@link ExchangeOfferAddTransaction}
 * </p>
 * <p>
 * Standard use: to place offers call {@link #offers(AddExchangeOffer...)}
 * </p>
 */
public class ExchangeOfferAddTransactionBuilder extends TransactionBuilder<ExchangeOfferAddTransactionBuilder, ExchangeOfferAddTransaction> {
   private List<AddExchangeOffer> offers;

   public ExchangeOfferAddTransactionBuilder() {
      super(EntityType.EXCHANGE_OFFER_ADD, EntityVersion.EXCHANGE_OFFER_ADD.getValue());
      // defaults
      offers = new ArrayList<>();
   }

   @Override
   protected ExchangeOfferAddTransactionBuilder self() {
      return this;
   }

   @Override
   public ExchangeOfferAddTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(ExchangeOfferAddTransaction.calculatePayloadSize(getOffers().size())));
      // create transaction instance
      return new ExchangeOfferAddTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(),
            getTransactionInfo(), getOffers());
   }

   // ------------------------------------ setters ------------------------------------//

   /**
    * specify offers to be announced
    * 
    * @param offers list of offers
    * @return self
    */
   public ExchangeOfferAddTransactionBuilder offers(List<AddExchangeOffer> offers) {
      this.offers = offers;
      return this;
   }

   // ------------------------------------------- getters ------------------------------------------//

   /**
    * @return the mosaics
    */
   public List<AddExchangeOffer> getOffers() {
      return offers;
   }

   // ----------------------------------------- convenience methods -------------------------------------//

   /**
    * @param offers the offers to set
    * @return self
    */
   public ExchangeOfferAddTransactionBuilder offers(AddExchangeOffer... offers) {
      return offers(Arrays.asList(offers));
   }

}
