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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.core.utils.StringUtils;
import io.proximax.sdk.gen.buffers.MetadataModificationBuffer;
import io.proximax.sdk.gen.buffers.ModifyMetadataTransactionBuffer;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.metadata.Field;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataModificationType;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Transaction requesting modification of metadata
 */
public class ModifyMetadataTransaction extends Transaction {
   private final Schema schema = new ModifyMetadataTransactionSchema();

   private final MetadataType metadataType;
   private final Optional<UInt64Id> metadataId;
   private final Optional<Address> address;
   private final List<MetadataModification> modifications;

   /**
    * @param type
    * @param networkType
    * @param version
    * @param deadline
    * @param maxFee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param metadataType
    * @param metadataId
    * @param address
    * @param modifications
    */
   public ModifyMetadataTransaction(EntityType type, NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, MetadataType metadataType, Optional<UInt64Id> metadataId,
         Optional<Address> address, List<MetadataModification> modifications) {
      super(type, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      // validations
      Validate.notNull(metadataType, "metadataType can not be null");
      Validate.isTrue(address.isPresent() != metadataId.isPresent(), "Has to be either address or metadata");
      Validate.notNull(modifications, "modifications can not be null");
      // assignments
      this.metadataType = metadataType;
      this.metadataId = metadataId;
      this.address = address;
      this.modifications = Collections.unmodifiableList(modifications);
   }

   /**
    * @return the metadataType
    */
   public MetadataType getMetadataType() {
      return metadataType;
   }

   /**
    * @return the metadataId
    */
   public Optional<UInt64Id> getMetadataId() {
      return metadataId;
   }

   /**
    * @return the address
    */
   public Optional<Address> getAddress() {
      return address;
   }

   /**
    * @return the modifications
    */
   public List<MetadataModification> getModifications() {
      return modifications;
   }

   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      byte[] metadataIdBytes;
      if (address.isPresent()) {
         metadataIdBytes = Base32Encoder.getBytes(address.get().plain());
      } else if (metadataId.isPresent()) {
         metadataIdBytes = UInt64Utils.getBytes(metadataId.get().getId());
      } else {
         throw new IllegalStateException("Always has to be address or id");
      }

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
      int feeOffset = ModifyMetadataTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int metadataIdOffset = ModifyMetadataTransactionBuffer.createMetadataIdVector(builder, metadataIdBytes);
      int modificationsOffset = ModifyMetadataTransactionBuffer.createModificationsVector(builder, modificationOffsets);

      // add size of stuff with constant size and size of metadata id
      int totalSize = getSerializedSize();

      ModifyMetadataTransactionBuffer.startModifyMetadataTransactionBuffer(builder);
      ModifyMetadataTransactionBuffer.addDeadline(builder, deadlineOffset);
      ModifyMetadataTransactionBuffer.addMaxFee(builder, feeOffset);
      ModifyMetadataTransactionBuffer.addSigner(builder, signerOffset);
      ModifyMetadataTransactionBuffer.addSignature(builder, signatureOffset);
      ModifyMetadataTransactionBuffer.addSize(builder, totalSize);
      ModifyMetadataTransactionBuffer.addType(builder, getType().getValue());
      ModifyMetadataTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      ModifyMetadataTransactionBuffer.addMetadataId(builder, metadataIdOffset);
      ModifyMetadataTransactionBuffer.addMetadataType(builder, metadataType.getCode());
      ModifyMetadataTransactionBuffer.addModifications(builder, modificationsOffset);

      int codedTransaction = ModifyMetadataTransactionBuffer.endModifyMetadataTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate size of modification
    * 
    * @param mod the modification
    * @return the size
    */
   private static int calculateModSize(MetadataModification mod) {
      int keySize = StringUtils.getBytes(mod.getField().getKey()).length;
      // remove does not have value
      int valueSize = mod.getType() == MetadataModificationType.REMOVE ? 0
            : StringUtils.getBytes(mod.getField().getValue()).length;
      // compute number of bytes: size + modType + keySize + valueSize + key + value
      return 4 + 1 + 1 + 2 + keySize + valueSize;
   }

   /**
    * calculate payload size
    * 
    * @param isAddressMetadata true if address, false otherwise
    * @param mods modifications
    * 
    * @return size
    */
   public static int calculatePayloadSize(boolean isAddressMetadata, List<MetadataModification> mods) {
      // 25 bytes for address 8 bytes for uint64
      int metaIdSize = isAddressMetadata ? 25 : 8;
      int modsSize = mods.stream().mapToInt(ModifyMetadataTransaction::calculateModSize).sum();
      // id + mod size + mods
      return metaIdSize + 1 + modsSize;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getAddress().isPresent(), getModifications());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new ModifyMetadataTransaction(getType(), getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getMetadataType(), getMetadataId(), getAddress(),
            getModifications());
   }
}
