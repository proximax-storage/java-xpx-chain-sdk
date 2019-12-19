/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.AddExchangeOfferBuffer;
import io.proximax.sdk.gen.buffers.AddExchangeOfferTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.exchange.AddExchangeOffer;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Exchange offer transaction
 */
public class ExchangeOfferAddTransaction extends Transaction {
   private final Schema schema = new ExchangeOfferAddTransactionSchema();

   private final List<AddExchangeOffer> offers;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#EXCHANGE_OFFER_ADD} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param offers list of offers
    */
   public ExchangeOfferAddTransaction(NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, List<AddExchangeOffer> offers) {
      super(EntityType.EXCHANGE_OFFER_ADD, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(offers, "Offers cannot be null");
      this.offers = Collections.unmodifiableList(offers);
   }

   /**
    * @return the offers
    */
   public List<AddExchangeOffer> getOffers() {
      return offers;
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // load modifications
      int[] offerOffsets = new int[getOffers().size()];
      for (int i = 0; i < offerOffsets.length; i++) {
         AddExchangeOffer offer = getOffers().get(i);
         // prepare vectors for collections
         int mosaicIdOffset = AddExchangeOfferBuffer.createMosaicIdVector(builder,
               UInt64Utils.fromBigInteger(offer.getMosaicId().getId()));
         int mosaicAmountOffset = AddExchangeOfferBuffer.createMosaicAmountVector(builder,
               UInt64Utils.fromBigInteger(offer.getMosaicAmount()));
         int costOffset = AddExchangeOfferBuffer.createCostVector(builder, UInt64Utils.fromBigInteger(offer.getCost()));
         int durationOffset = AddExchangeOfferBuffer.createDurationVector(builder, UInt64Utils.fromBigInteger(offer.getDuration()));

         // populate flat-buffer
         AddExchangeOfferBuffer.startAddExchangeOfferBuffer(builder);
         AddExchangeOfferBuffer.addMosaicId(builder, mosaicIdOffset);
         AddExchangeOfferBuffer.addMosaicAmount(builder, mosaicAmountOffset);
         AddExchangeOfferBuffer.addCost(builder, costOffset);
         AddExchangeOfferBuffer.addType(builder, offer.getType().getCode());
         AddExchangeOfferBuffer.addDuration(builder, durationOffset);
         // add the offset
         offerOffsets[i] = AddExchangeOfferBuffer.endAddExchangeOfferBuffer(builder);
      }

      // create vectors
      int signatureOffset = AddExchangeOfferTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = AddExchangeOfferTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = AddExchangeOfferTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = AddExchangeOfferTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int offersOffset = AddExchangeOfferTransactionBuffer.createOffersVector(builder, offerOffsets);

      // add size of the transaction
      int totalSize = getSerializedSize();

      AddExchangeOfferTransactionBuffer.startAddExchangeOfferTransactionBuffer(builder);
      AddExchangeOfferTransactionBuffer.addDeadline(builder, deadlineOffset);
      AddExchangeOfferTransactionBuffer.addMaxFee(builder, feeOffset);
      AddExchangeOfferTransactionBuffer.addSigner(builder, signerOffset);
      AddExchangeOfferTransactionBuffer.addSignature(builder, signatureOffset);
      AddExchangeOfferTransactionBuffer.addSize(builder, totalSize);
      AddExchangeOfferTransactionBuffer.addType(builder, getType().getValue());
      AddExchangeOfferTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      AddExchangeOfferTransactionBuffer.addOffersCount(builder, offerOffsets.length);
      AddExchangeOfferTransactionBuffer.addOffers(builder, offersOffset);

      int codedTransaction = AddExchangeOfferTransactionBuffer.endAddExchangeOfferTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * @param offerCount number of offer items
    * @return the size of the payload
    */
   public static int calculatePayloadSize(int offerCount) {
      // offer count + offer count * (id, amount, cost, type, duration)
      return 1 + offerCount * (8 + 8 + 8 + 1 + 8);
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getOffers().size());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new ExchangeOfferAddTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getOffers());
   }
}
