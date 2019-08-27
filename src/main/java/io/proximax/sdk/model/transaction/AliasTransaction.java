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
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.sdk.gen.buffers.AliasTransactionBuffer;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.alias.AliasAction;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Transaction defining alias for mosaic or for account
 */
public class AliasTransaction extends Transaction {
   private final Schema schema = new AliasTransactionSchema();

   private final Optional<MosaicId> mosaicId;
   private final Optional<Address> address;
   private final NamespaceId namespaceId;
   private final AliasAction aliasAction;

   /**
    * @param type transaction type which has to be one of {@link TransactionType#MOSAIC_ALIAS} or
    * {@link TransactionType#ADDRESS_ALIAS}
    * @param networkType network type
    * @param version transaction version. For latest {@link TransactionVersion#MOSAIC_ALIAS} or
    * {@link TransactionVersion#ADDRESS_ALIAS}
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param mosaicId optional Mosaic ID specified when mosaic alias is being created
    * @param address optional address specified when address alias is being created
    * @param namespaceId namespace Id to get an alias
    * @param aliasAction link/unlink action
    */
   public AliasTransaction(TransactionType type, NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, Optional<MosaicId> mosaicId, Optional<Address> address,
         NamespaceId namespaceId, AliasAction aliasAction) {
      super(type, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      // validate input
      Validate.notNull(mosaicId, "MosaicId must not be null");
      Validate.notNull(namespaceId, "namespaceId must not be null");
      Validate.notNull(aliasAction, "action must not be null");
      Validate.isTrue(mosaicId.isPresent() != address.isPresent(), "Address or mosaic has to be specified exclusively");
      Validate.isTrue(mosaicId.isPresent() == (type == TransactionType.MOSAIC_ALIAS),
            "Mosaic ID needs to be used with mosaic alias transaction type");
      Validate.isTrue(address.isPresent() == (type == TransactionType.ADDRESS_ALIAS),
            "Address needs to be used with address alias transaction type");
      // make assignments
      this.mosaicId = mosaicId;
      this.address = address;
      this.namespaceId = namespaceId;
      this.aliasAction = aliasAction;
   }

   /**
    * @return the mosaicId
    */
   public Optional<MosaicId> getMosaicId() {
      return mosaicId;
   }

   /**
    * @return the address
    */
   public Optional<Address> getAddress() {
      return address;
   }

   /**
    * @return the namespaceId
    */
   public NamespaceId getNamespaceId() {
      return namespaceId;
   }

   /**
    * @return the aliasAction
    */
   public AliasAction getAliasAction() {
      return aliasAction;
   }

   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      byte[] aliasIdBytes;
      if (address.isPresent()) {
         aliasIdBytes = Base32Encoder.getBytes(address.get().plain());
      } else if (mosaicId.isPresent()) {
         aliasIdBytes = UInt64Utils.getBytes(mosaicId.get().getId());
      } else {
         throw new IllegalStateException("Always has to be address or id");
      }

      // Create Vectors
      int signatureOffset = AliasTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = AliasTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = AliasTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = AliasTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));
      int namespaceIdOffset = AliasTransactionBuffer.createNamespaceIdVector(builder,
            UInt64Utils.fromBigInteger(namespaceId.getId()));
      int aliasIdOffset = AliasTransactionBuffer.createAliasIdVector(builder, aliasIdBytes);

      // header, 2 uint64 and int
      int totalSize = getSerializedSize();

      AliasTransactionBuffer.startAliasTransactionBuffer(builder);
      AliasTransactionBuffer.addDeadline(builder, deadlineOffset);
      AliasTransactionBuffer.addMaxFee(builder, feeOffset);
      AliasTransactionBuffer.addSigner(builder, signerOffset);
      AliasTransactionBuffer.addSignature(builder, signatureOffset);
      AliasTransactionBuffer.addSize(builder, totalSize);
      AliasTransactionBuffer.addType(builder, getType().getValue());
      AliasTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      AliasTransactionBuffer.addAliasId(builder, aliasIdOffset);
      AliasTransactionBuffer.addNamespaceId(builder, namespaceIdOffset);
      AliasTransactionBuffer.addActionType(builder, aliasAction.getCode());

      int codedTransaction = AliasTransactionBuffer.endAliasTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate payload size excluding header
    * 
    * @param isForAddress try if address alias is created, false otherwise
    * @return the size
    */
   public static int calculatePayloadSize(boolean isForAddress) {
      // alias is either 25 bytes of address or 8 bytes of mosaic ID
      int aliasIdSize = isForAddress ? 25 : 8;
      // alias ID + namespace ID + action
      return aliasIdSize + 8 + 1;
   }
   
   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(address.isPresent());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new AliasTransaction(getType(), getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
            Optional.of(signer), getTransactionInfo(), getMosaicId(), getAddress(), getNamespaceId(), getAliasAction());
   }
}
