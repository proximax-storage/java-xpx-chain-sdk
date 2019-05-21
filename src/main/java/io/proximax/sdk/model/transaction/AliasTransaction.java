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
   private final Optional<MosaicId> mosaicId;
   private final Optional<Address> address;
   private final NamespaceId namespaceId;
   private final AliasAction aliasAction;
   private final Schema schema = new AliasTransactionSchema();

   public AliasTransaction(TransactionType transactionType, NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee,
         Optional<MosaicId> mosaicId, Optional<Address> address, NamespaceId namespaceId, AliasAction action, String signature, PublicAccount signer,
         TransactionInfo transactionInfo) {
      this(transactionType, networkType, version, deadline, fee, mosaicId, address, namespaceId, action, Optional.of(signature),
            Optional.of(signer), Optional.of(transactionInfo));
   }

   public AliasTransaction(TransactionType transactionType, NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee,
         Optional<MosaicId> mosaicId, Optional<Address> address, NamespaceId namespaceId, AliasAction action) {
      this(transactionType, networkType, version, deadline, fee, mosaicId, address, namespaceId, action, Optional.empty(), Optional.empty(),
            Optional.empty());
   }

   private AliasTransaction(TransactionType transactionType, NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee,
         Optional<MosaicId> mosaicId, Optional<Address> address, NamespaceId namespaceId, AliasAction action, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
      super(transactionType, networkType, version, deadline, fee, signature, signer, transactionInfo);
      Validate.notNull(mosaicId, "MosaicId must not be null");
      Validate.notNull(namespaceId, "namespaceId must not be null");
      Validate.notNull(action, "action must not be null");
      Validate.isTrue(mosaicId.isPresent() != address.isPresent(), "Address or mosaic has to be specified exclusively");
      this.mosaicId = mosaicId;
      this.address = address;
      this.namespaceId = namespaceId;
      this.aliasAction = action;
   }

   /**
    * Create a mosaic creation transaction object.
    *
    * @param mosaicId id of the mosaic
    * @param namespaceId id of the namespace to be aliased
    * @param action alias action to perform
    * @param deadline The deadline to include the transaction.
    * @param networkType The network type.
    * @return {@link AliasTransaction}
    */
   public static AliasTransaction create(MosaicId mosaicId, NamespaceId namespaceId, AliasAction action,
         TransactionDeadline deadline, NetworkType networkType) {
      Validate.notNull(mosaicId, "mosaicId must not be null");
      Validate.notNull(namespaceId, "namespaceId must not be null");
      return new AliasTransaction(TransactionType.MOSAIC_ALIAS, networkType, TransactionVersion.MOSAIC_ALIAS.getValue(), deadline,
            BigInteger.valueOf(0), Optional.of(mosaicId), Optional.empty(), namespaceId, action);
   }

   public static AliasTransaction create(Address address, NamespaceId namespaceId, AliasAction action,
         TransactionDeadline deadline, NetworkType networkType) {
      Validate.notNull(address, "address must not be null");
      Validate.notNull(namespaceId, "namespaceId must not be null");
      return new AliasTransaction(TransactionType.ADDRESS_ALIAS, networkType, TransactionVersion.ADDRESS_ALIAS.getValue(), deadline,
            BigInteger.valueOf(0), Optional.empty(), Optional.of(address), namespaceId, action);
   }

   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      
      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      int version = (int) Long
            .parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

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
      int feeOffset = AliasTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      int namespaceIdVector = AliasTransactionBuffer.createNamespaceIdVector(builder,
            UInt64Utils.fromBigInteger(namespaceId.getId()));
      int aliasIdOffset = AliasTransactionBuffer.createAliasIdVector(builder, aliasIdBytes);
      
      // header, 2 uint64 and int
      int totalSize = 120 + aliasIdBytes.length + 8 + 1;

      AliasTransactionBuffer.startAliasTransactionBuffer(builder);
      AliasTransactionBuffer.addDeadline(builder, deadlineOffset);
      AliasTransactionBuffer.addMaxFee(builder, feeOffset);
      AliasTransactionBuffer.addSigner(builder, signerOffset);
      AliasTransactionBuffer.addSignature(builder, signatureOffset);
      AliasTransactionBuffer.addSize(builder, totalSize);
      AliasTransactionBuffer.addType(builder, getType().getValue());
      AliasTransactionBuffer.addVersion(builder, version);

      AliasTransactionBuffer.addAliasId(builder, aliasIdOffset);
      AliasTransactionBuffer.addNamespaceId(builder, namespaceIdVector);
      AliasTransactionBuffer.addActionType(builder, aliasAction.getCode());
      
      int codedTransaction = AliasTransactionBuffer.endAliasTransactionBuffer(builder);
      builder.finish(codedTransaction);

      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized form has incorrect length ", output.length);
      return output;
   }
}
