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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.metadata.Field;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataModificationType;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;

/**
 * Transaction requesting modification of metadata
 */
public class ModifyMetadataTransaction extends Transaction {
   private final MetadataType metadataType;
   private final Optional<UInt64Id> metadataId;
   private final Optional<Address> address;
   private final List<MetadataModification> modifications;
   private final Schema schema = new ModifyMetadataTransactionSchema();

   public ModifyMetadataTransaction(TransactionType transactionType, NetworkType networkType,
         Integer transactionVersion, Deadline deadline, BigInteger fee, Optional<UInt64Id> metadataId,
         Optional<Address> address, MetadataType metadataType, List<MetadataModification> modifications, String signature,
         PublicAccount signer, TransactionInfo transactionInfo) {
      this(transactionType, networkType, transactionVersion, deadline, fee, metadataId, address,
            metadataType, modifications, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
   }
   
   private ModifyMetadataTransaction(TransactionType transactionType, NetworkType networkType, Deadline deadline,
         Optional<UInt64Id> metadataId, Optional<Address> address, MetadataType metadataType,
         List<MetadataModification> modifications) {
      this(transactionType, networkType, TransactionVersion.METADATA_MODIFICATION.getValue(), deadline, BigInteger.ZERO,
            metadataId, address, metadataType, modifications, Optional.empty(), Optional.empty(), Optional.empty());
   }

   private ModifyMetadataTransaction(TransactionType transactionType, NetworkType networkType, Integer version,
         Deadline deadline, BigInteger fee, Optional<UInt64Id> metadataId, Optional<Address> address,
         MetadataType metadataType, List<MetadataModification> modifications, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
      super(transactionType, networkType, version, deadline, fee, signature, signer, transactionInfo);
      Validate.notNull(metadataType, "metadataType must not be null");
      Validate.notNull(metadataId, "metadataId must not be null");
      Validate.notNull(address, "address must not be null");
      Validate.notNull(modifications, "modifications must not be null");
      Validate.isTrue(metadataId.isPresent() != address.isPresent(), "specify metadata id or address exclusively");
      this.metadataId = metadataId;
      this.address = address;
      this.metadataType = metadataType;
      this.modifications = modifications;
   }

   public static ModifyMetadataTransaction createForMosaic(Deadline deadline, MosaicId mosaicId,
         List<MetadataModification> modifications, NetworkType networkType) {
      return new ModifyMetadataTransaction(TransactionType.MODIFY_MOSAIC_METADATA, networkType, deadline,
            Optional.of(mosaicId), Optional.empty(), MetadataType.MOSAIC, modifications);
   }

   public static ModifyMetadataTransaction createForNamespace(Deadline deadline, NamespaceId namespaceId,
         List<MetadataModification> modifications, NetworkType networkType) {
      return new ModifyMetadataTransaction(TransactionType.MODIFY_NAMESPACE_METADATA, networkType, deadline,
            Optional.of(namespaceId), Optional.empty(), MetadataType.NAMESPACE, modifications);
   }

   public static ModifyMetadataTransaction createForAddress(Deadline deadline, Address address,
         List<MetadataModification> modifications, NetworkType networkType) {
      return new ModifyMetadataTransaction(TransactionType.MODIFY_ADDRESS_METADATA, networkType, deadline,
            Optional.empty(), Optional.of(address), MetadataType.ADDRESS, modifications);
   }

   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      int version = (int) Long
            .parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);
      byte[] metadataIdBytes;
      if (address.isPresent()) {
         metadataIdBytes = new Base32().decode(address.get().plain().getBytes(StandardCharsets.UTF_8));
      } else if (metadataId.isPresent()) {
         metadataIdBytes = UInt64Utils.getBytes(metadataId.get().getId());
      } else {
         throw new IllegalStateException("Always has to be address or id");
      }

      // track the size of the whole metadata modification
      int totalSize = 0;
      // load modifications
      int[] modificationOffsets = new int[modifications.size()];
      for (int i = 0; i < modificationOffsets.length; i++) {
         MetadataModification mod = modifications.get(i);
         Field modField = mod.getField();
         MetadataModificationType modType = mod.getType();
         // prepare intermediate data
         byte[] keyBytes = modField.getKey().getBytes(StandardCharsets.UTF_8);
         byte[] valueBytes = modType == MetadataModificationType.REMOVE ? new byte[0]
               : modField.getValue().getBytes(StandardCharsets.UTF_8);
         // value size needs special handling to make sure 0 is serialized as 4 bytes, not as 1
         byte[] valueSizeBytes = new byte[2];
         ByteBuffer.wrap(valueSizeBytes).order(ByteOrder.LITTLE_ENDIAN).putShort((short) valueBytes.length);

         // prepare vectors for collections
         int keyOffset = MetadataModificationBuffer.createKeyVector(builder, keyBytes);
         int valueOffset = MetadataModificationBuffer.createValueVector(builder, valueBytes);
         int valueSizeOffset = MetadataModificationBuffer.createValueSizeVector(builder, valueSizeBytes);

         // compute number of bytes: size + modType + keySize + valueSize + key + value
         int modSize = 4 + 1 + 1 + 2 + keyBytes.length + valueBytes.length;
         // increase total size
         totalSize += modSize;

         // populate flat-buffer
         MetadataModificationBuffer.startMetadataModificationBuffer(builder);
         MetadataModificationBuffer.addSize(builder, modSize);
         MetadataModificationBuffer.addKeySize(builder, keyBytes.length);
         MetadataModificationBuffer.addKey(builder, keyOffset);
         MetadataModificationBuffer.addValueSize(builder, valueSizeOffset);
         MetadataModificationBuffer.addValue(builder, valueOffset);
         MetadataModificationBuffer.addModificationType(builder, modType.getCode());
         modificationOffsets[i] = MetadataModificationBuffer.endMetadataModificationBuffer(builder);
      }

      // create vectors
      int signatureOffset = ModifyMetadataTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = ModifyMetadataTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = ModifyMetadataTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = ModifyMetadataTransactionBuffer.createFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      int metadataIdOffset = ModifyMetadataTransactionBuffer.createMetadataIdVector(builder, metadataIdBytes);
      int modificationsOffset = ModifyMetadataTransactionBuffer.createModificationsVector(builder, modificationOffsets);

      // add size of stuff with constant size and size of metadata id
      totalSize += 120 + metadataIdBytes.length + 1;

      ModifyMetadataTransactionBuffer.startModifyMetadataTransactionBuffer(builder);
      ModifyMetadataTransactionBuffer.addDeadline(builder, deadlineOffset);
      ModifyMetadataTransactionBuffer.addFee(builder, feeOffset);
      ModifyMetadataTransactionBuffer.addSigner(builder, signerOffset);
      ModifyMetadataTransactionBuffer.addSignature(builder, signatureOffset);
      ModifyMetadataTransactionBuffer.addSize(builder, totalSize);
      ModifyMetadataTransactionBuffer.addType(builder, getType().getValue());
      ModifyMetadataTransactionBuffer.addVersion(builder, version);

      ModifyMetadataTransactionBuffer.addMetadataId(builder, metadataIdOffset);
      ModifyMetadataTransactionBuffer.addMetadataType(builder, metadataType.getCode());
      ModifyMetadataTransactionBuffer.addModifications(builder, modificationsOffset);

      int codedTransaction = ModifyMetadataTransactionBuffer.endModifyMetadataTransactionBuffer(builder);
      builder.finish(codedTransaction);

      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized form has incorrect length");
      return output;
   }
}
