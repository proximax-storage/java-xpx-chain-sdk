/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.gen.buffers.MessageBuffer;
import io.proximax.sdk.gen.buffers.MosaicBuffer;
import io.proximax.sdk.gen.buffers.TransferTransactionBuffer;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * The transfer transactions object contain data about transfers of mosaics and message to another account.
 *
 * @since 1.0
 */
public class TransferTransaction extends Transaction {
   private final Recipient recipient;
   private final List<Mosaic> mosaics;
   private final Message message;
   private final Schema schema = new TransferTransactionSchema();

   /**
    * private raw all-argument constructor
    * 
    * @param networkType type of the network
    * @param version transaction version
    * @param deadline transaction deadline
    * @param maxFee optional maxFee declaring maximum allowed fee for transaction
    * @param recipient transfer recipient
    * @param mosaics list of mosaic values that is to be transferred
    * @param message message
    * @param signature transaction signature
    * @param signer transaction signer
    * @param transactionInfo transaction info
    * @param feeCalculationStrategy fee calculation strategy used when fee is not specified
    */
   public TransferTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         Optional<BigInteger> maxFee, Recipient recipient, List<Mosaic> mosaics, Message message,
         Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo,
         Optional<FeeCalculationStrategy> feeCalculationStrategy) {
      super(TransactionType.TRANSFER, networkType, version, deadline, maxFee, signature, signer, transactionInfo,
            feeCalculationStrategy);
      Validate.notNull(recipient, "Recipient must not be null");
      Validate.notNull(mosaics, "Mosaics must not be null");
      Validate.notNull(message, "Message must not be null");
      this.recipient = recipient;
      this.mosaics = Collections.unmodifiableList(mosaics);
      this.message = message;
   }

   /**
    * Create generic transfer transaction with unspecified recipient
    * 
    * @param deadline transaction deadline
    * @param recipient transfer recipient
    * @param mosaics list of mosaic values that is to be transferred
    * @param message message
    * @param maxFee optional maxFee declaring maximum allowed fee for transaction
    * @param networkType type of the network
    * @param feeCalculationStrategy fee calculation strategy used when fee is not specified
    * @return the transaction instance
    */
   public static TransferTransaction create(TransactionDeadline deadline, Recipient recipient, List<Mosaic> mosaics,
         Message message, Optional<BigInteger> maxFee, NetworkType networkType,
         Optional<FeeCalculationStrategy> feeCalculationStrategy) {
      return new TransferTransaction(networkType, TransactionVersion.TRANSFER.getValue(), deadline, maxFee, recipient,
            mosaics, message, Optional.empty(), Optional.empty(), Optional.empty(), feeCalculationStrategy);
   }

   /**
    * Create transfer transaction with recipient specified as address
    * 
    * @param deadline transaction deadline
    * @param address transfer recipient
    * @param mosaics list of mosaic values that is to be transferred
    * @param message message
    * @param maxFee optional maxFee declaring maximum allowed fee for transaction
    * @param networkType type of the network
    * @param feeCalculationStrategy fee calculation strategy used when fee is not specified
    * @return the transaction instance
    */
   public static TransferTransaction create(TransactionDeadline deadline, Address address, List<Mosaic> mosaics,
         Message message, Optional<BigInteger> maxFee, NetworkType networkType,
         Optional<FeeCalculationStrategy> feeCalculationStrategy) {
      return new TransferTransaction(networkType, TransactionVersion.TRANSFER.getValue(), deadline, maxFee, Recipient.from(address),
            mosaics, message, Optional.empty(), Optional.empty(), Optional.empty(), feeCalculationStrategy);
   }

   /**
    * Create transfer transaction with recipient specified as namespace ID
    * 
    * @param deadline transaction deadline
    * @param namespaceId transfer recipient
    * @param mosaics list of mosaic values that is to be transferred
    * @param message message
    * @param maxFee optional maxFee declaring maximum allowed fee for transaction
    * @param networkType type of the network
    * @param feeCalculationStrategy fee calculation strategy used when fee is not specified
    * @return the transaction instance
    */
   public static TransferTransaction create(TransactionDeadline deadline, NamespaceId namespaceId, List<Mosaic> mosaics,
         Message message, Optional<BigInteger> maxFee, NetworkType networkType,
         Optional<FeeCalculationStrategy> feeCalculationStrategy) {
      return new TransferTransaction(networkType, TransactionVersion.TRANSFER.getValue(), deadline, maxFee, Recipient.from(namespaceId),
            mosaics, message, Optional.empty(), Optional.empty(), Optional.empty(), feeCalculationStrategy);
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new TransferTransaction(getNetworkType(), getVersion(), getDeadline(), Optional.of(getMaxFee()), getRecipient(),
            getMosaics(), getMessage(), getSignature(), Optional.of(signer), getTransactionInfo(), getFeeCalculationStrategy());
   }
   
   /**
    * Returns the recipient.
    *
    * @return recipient
    */
   public Recipient getRecipient() {
      return recipient;
   }

   /**
    * Returns list of mosaic objects.
    *
    * @return list of mosaics in the transfer
    */
   public List<Mosaic> getMosaics() {
      return mosaics;
   }

   /**
    * Returns transaction message.
    *
    * @return Message
    */
   public Message getMessage() {
      return message;
   }

   /**
    * get message payload serialized to byte array
    * 
    * @return payload byte array
    */
   private byte[] getPayloadBytes() {
      return getMessage().getEncodedPayload();
   }

   @Override
   protected int getPayloadSerializedSize() {
      byte[] payloadBytes = getPayloadBytes();
      return
            // recipient is always 25 bytes
            25 +
            // message size is short
            2 +
            // message type byte
            1 +
            // number of mosaics
            1 +
            // each mosaic has id and amount, both 8byte uint64
            ((8 + 8) * mosaics.size()) +
            // number of message bytes
            payloadBytes.length;
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Message
      byte[] payloadBytes = getPayloadBytes();
      int payload = MessageBuffer.createPayloadVector(builder, payloadBytes);
      MessageBuffer.startMessageBuffer(builder);
      MessageBuffer.addType(builder, message.getTypeCode());
      MessageBuffer.addPayload(builder, payload);
      int messageVector = MessageBuffer.endMessageBuffer(builder);

      // Create Mosaics
      int[] mosaicBuffers = new int[mosaics.size()];
      for (int i = 0; i < mosaics.size(); ++i) {
         Mosaic mosaic = mosaics.get(i);
         int id = MosaicBuffer.createIdVector(builder, UInt64Utils.fromBigInteger(mosaic.getId().getId()));
         int amount = MosaicBuffer.createAmountVector(builder, UInt64Utils.fromBigInteger(mosaic.getAmount()));
         MosaicBuffer.startMosaicBuffer(builder);
         MosaicBuffer.addId(builder, id);
         MosaicBuffer.addAmount(builder, amount);
         mosaicBuffers[i] = MosaicBuffer.endMosaicBuffer(builder);
      }

      // serialize the recipient
      byte[] recipientBytes = recipient.getBytes();
      // Create Vectors
      int signatureVector = TransferTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = TransferTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = TransferTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = TransferTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));
      int recipientVector = TransferTransactionBuffer.createRecipientVector(builder, recipientBytes);
      int mosaicsVector = TransferTransactionBuffer.createMosaicsVector(builder, mosaicBuffers);

      // total size of transaction
      int size = getSerializedSize();

      TransferTransactionBuffer.startTransferTransactionBuffer(builder);
      TransferTransactionBuffer.addSize(builder, size);
      TransferTransactionBuffer.addSignature(builder, signatureVector);
      TransferTransactionBuffer.addSigner(builder, signerVector);
      TransferTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      TransferTransactionBuffer.addType(builder, getType().getValue());
      TransferTransactionBuffer.addMaxFee(builder, feeVector);
      TransferTransactionBuffer.addDeadline(builder, deadlineVector);

      TransferTransactionBuffer.addRecipient(builder, recipientVector);
      TransferTransactionBuffer.addNumMosaics(builder, mosaics.size());
      TransferTransactionBuffer.addMessageSize(builder, payloadBytes.length + 1);
      TransferTransactionBuffer.addMessage(builder, messageVector);
      TransferTransactionBuffer.addMosaics(builder, mosaicsVector);

      int codedTransfer = TransferTransactionBuffer.endTransferTransactionBuffer(builder);
      builder.finish(codedTransfer);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }
   
}
