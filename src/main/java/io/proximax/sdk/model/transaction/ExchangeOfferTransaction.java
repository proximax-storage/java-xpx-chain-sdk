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

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.ExchangeOfferBuffer;
import io.proximax.sdk.gen.buffers.ExchangeOfferTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.exchange.ExchangeOffer;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Exchange offer transaction. Offer to fulfill someone else's offer
 */
public class ExchangeOfferTransaction extends Transaction {
   private final Schema schema = new ExchangeOfferTransactionSchema();

   private final List<ExchangeOffer> offers;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#EXCHANGE_OFFER} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param offers list of offers
    */
   public ExchangeOfferTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, List<ExchangeOffer> offers) {
      super(EntityType.EXCHANGE_OFFER, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(offers, "Offers cannot be null");
      this.offers = Collections.unmodifiableList(offers);
   }

   /**
    * @return the offers
    */
   public List<ExchangeOffer> getOffers() {
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
         ExchangeOffer offer = getOffers().get(i);
         // prepare vectors for collections
         int mosaicIdOffset = ExchangeOfferBuffer.createMosaicIdVector(builder,
               UInt64Utils.fromBigInteger(offer.getMosaicId().getId()));
         int mosaicAmountOffset = ExchangeOfferBuffer.createMosaicAmountVector(builder,
               UInt64Utils.fromBigInteger(offer.getMosaicAmount()));
         int costOffset = ExchangeOfferBuffer.createCostVector(builder, UInt64Utils.fromBigInteger(offer.getCost()));
         int ownerOffset = ExchangeOfferBuffer.createOwnerVector(builder,
               HexEncoder.getBytes(offer.getOwner().getPublicKey()));

         // populate flat-buffer
         ExchangeOfferBuffer.startExchangeOfferBuffer(builder);
         ExchangeOfferBuffer.addMosaicId(builder, mosaicIdOffset);
         ExchangeOfferBuffer.addMosaicAmount(builder, mosaicAmountOffset);
         ExchangeOfferBuffer.addCost(builder, costOffset);
         ExchangeOfferBuffer.addType(builder, offer.getType().getCode());
         ExchangeOfferBuffer.addOwner(builder, ownerOffset);
         // add the offset
         offerOffsets[i] = ExchangeOfferBuffer.endExchangeOfferBuffer(builder);
      }

      // create vectors
      int signatureOffset = ExchangeOfferTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = ExchangeOfferTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = ExchangeOfferTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = ExchangeOfferTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int offersOffset = ExchangeOfferTransactionBuffer.createOffersVector(builder, offerOffsets);

      // add size of the transaction
      int totalSize = getSerializedSize();

      ExchangeOfferTransactionBuffer.startExchangeOfferTransactionBuffer(builder);
      ExchangeOfferTransactionBuffer.addDeadline(builder, deadlineOffset);
      ExchangeOfferTransactionBuffer.addMaxFee(builder, feeOffset);
      ExchangeOfferTransactionBuffer.addSigner(builder, signerOffset);
      ExchangeOfferTransactionBuffer.addSignature(builder, signatureOffset);
      ExchangeOfferTransactionBuffer.addSize(builder, totalSize);
      ExchangeOfferTransactionBuffer.addType(builder, getType().getValue());
      ExchangeOfferTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      ExchangeOfferTransactionBuffer.addOffersCount(builder, offerOffsets.length);
      ExchangeOfferTransactionBuffer.addOffers(builder, offersOffset);

      int codedTransaction = ExchangeOfferTransactionBuffer.endExchangeOfferTransactionBuffer(builder);
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
      // offer count + offer count * (id, amount, cost, type, owner)
      return 1 + offerCount * (8 + 8 + 8 + 1 + 32);
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getOffers().size());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new ExchangeOfferTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
            Optional.of(signer), getTransactionInfo(), getOffers());
   }
}
