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

import io.proximax.sdk.gen.buffers.RemoveExchangeOfferBuffer;
import io.proximax.sdk.gen.buffers.RemoveExchangeOfferTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.exchange.RemoveExchangeOffer;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Remove exchange offer transaction used to remove standing offer
 */
public class ExchangeOfferRemoveTransaction extends Transaction {
   private final Schema schema = new ExchangeOfferRemoveTransactionSchema();

   private final List<RemoveExchangeOffer> offers;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#EXCHANGE_OFFER_REMOVE} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param offers list of offers
    */
   public ExchangeOfferRemoveTransaction(NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, List<RemoveExchangeOffer> offers) {
      super(EntityType.EXCHANGE_OFFER_REMOVE, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(offers, "Offers cannot be null");
      this.offers = Collections.unmodifiableList(offers);
   }

   /**
    * @return the offers
    */
   public List<RemoveExchangeOffer> getOffers() {
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
         RemoveExchangeOffer offer = getOffers().get(i);
         // prepare vectors for collections
         int mosaicIdOffset = RemoveExchangeOfferBuffer.createMosaicIdVector(builder,
               UInt64Utils.fromBigInteger(offer.getMosaicId().getId()));

         // populate flat-buffer
         RemoveExchangeOfferBuffer.startRemoveExchangeOfferBuffer(builder);
         RemoveExchangeOfferBuffer.addMosaicId(builder, mosaicIdOffset);
         RemoveExchangeOfferBuffer.addType(builder, offer.getType().getCode());
         // add the offset
         offerOffsets[i] = RemoveExchangeOfferBuffer.endRemoveExchangeOfferBuffer(builder);
      }

      // create vectors
      int signatureOffset = RemoveExchangeOfferTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = RemoveExchangeOfferTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = RemoveExchangeOfferTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = RemoveExchangeOfferTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int offersOffset = RemoveExchangeOfferTransactionBuffer.createOffersVector(builder, offerOffsets);

      // add size of the transaction
      int totalSize = getSerializedSize();

      RemoveExchangeOfferTransactionBuffer.startRemoveExchangeOfferTransactionBuffer(builder);
      RemoveExchangeOfferTransactionBuffer.addDeadline(builder, deadlineOffset);
      RemoveExchangeOfferTransactionBuffer.addMaxFee(builder, feeOffset);
      RemoveExchangeOfferTransactionBuffer.addSigner(builder, signerOffset);
      RemoveExchangeOfferTransactionBuffer.addSignature(builder, signatureOffset);
      RemoveExchangeOfferTransactionBuffer.addSize(builder, totalSize);
      RemoveExchangeOfferTransactionBuffer.addType(builder, getType().getValue());
      RemoveExchangeOfferTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      RemoveExchangeOfferTransactionBuffer.addOffersCount(builder, offerOffsets.length);
      RemoveExchangeOfferTransactionBuffer.addOffers(builder, offersOffset);

      int codedTransaction = RemoveExchangeOfferTransactionBuffer.endRemoveExchangeOfferTransactionBuffer(builder);
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
      // offer count + offer count * (id, type)
      return 1 + offerCount * (8 + 1);
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getOffers().size());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new ExchangeOfferRemoveTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getOffers());
   }
}
