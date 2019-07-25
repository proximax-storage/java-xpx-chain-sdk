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
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.MosaicDefinitionTransactionBuffer;
import io.proximax.sdk.gen.buffers.MosaicProperty;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.mosaic.MosaicPropertyId;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Before a mosaic can be created or transferred, a corresponding definition of the mosaic has to be created and
 * published to the network. This is done via a mosaic definition transaction.
 *
 * @since 1.0
 */
public class MosaicDefinitionTransaction extends Transaction {
   private final MosaicNonce nonce;
   private final MosaicId mosaicId;
   private final MosaicProperties mosaicProperties;
   private final Schema schema = new MosaicDefinitionTransactionSchema();

   public MosaicDefinitionTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee,
         MosaicNonce nonce, MosaicId mosaicId, MosaicProperties mosaicProperties, String signature, PublicAccount signer,
         TransactionInfo transactionInfo) {
      this(networkType, version, deadline, fee, nonce, mosaicId, mosaicProperties, Optional.of(signature), Optional.of(signer),
            Optional.of(transactionInfo));
   }

   public MosaicDefinitionTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee,
         MosaicNonce nonce, MosaicId mosaicId, MosaicProperties mosaicProperties) {
      this(networkType, version, deadline, fee, nonce, mosaicId, mosaicProperties, Optional.empty(), Optional.empty(),
            Optional.empty());
   }

   private MosaicDefinitionTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee,
         MosaicNonce nonce, MosaicId mosaicId, MosaicProperties mosaicProperties, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
      super(TransactionType.MOSAIC_DEFINITION, networkType, version, deadline, fee, signature, signer, transactionInfo);
      Validate.notNull(nonce, "nonce must not be null");
      Validate.notNull(mosaicId, "MosaicId must not be null");
      Validate.notNull(mosaicProperties, "MosaicProperties must not be null");
      this.mosaicId = mosaicId;
      this.mosaicProperties = mosaicProperties;
      this.nonce = nonce;
   }

   /**
    * Create a mosaic creation transaction object.
    *
    * @param nonce mosaic nonce allowing one account to have multiple mosaics
    * @param mosaicId id of the mosaic to be created
    * @param deadline The deadline to include the transaction.
    * @param mosaicProperties The mosaic properties.
    * @param networkType The network type.
    * @return {@link MosaicDefinitionTransaction}
    */
   public static MosaicDefinitionTransaction create(MosaicNonce nonce, MosaicId mosaicId, TransactionDeadline deadline,
         MosaicProperties mosaicProperties, NetworkType networkType) {
      Validate.notNull(mosaicId, "MosaicId must not be null");
      return new MosaicDefinitionTransaction(networkType, TransactionVersion.MOSAIC_DEFINITION.getValue(), deadline,
            BigInteger.valueOf(0), nonce, mosaicId, mosaicProperties);
   }

   
   /**
    * Returns mosaic id generated from namespace name and mosaic name.
    *
    * @return MosaicId
    */
   public MosaicId getMosaicId() {
      return mosaicId;
   }

   /**
    * Get the nonce that was used to generate mosaic ID
    * 
    * @return the nonce
    */
   public MosaicNonce getNonce() {
      return nonce;
   }

   /**
    * Returns mosaic properties defining mosaic.
    *
    * @return {@link MosaicProperties}
    */
   public MosaicProperties getMosaicProperties() {
      return mosaicProperties;
   }

   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      int version = (int) Long
            .parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);
      // get value for flags field
      int flags = 0;
      if (mosaicProperties.isSupplyMutable()) {
         flags |= MosaicProperties.FLAG_SUPPLY_MUTABLE;
      }
      if (mosaicProperties.isTransferable()) {
         flags |= MosaicProperties.FLAG_TRANSFERABLE;
      }

      // get array of optional properties
      ArrayList<Pair<MosaicPropertyId, BigInteger>> propertyList = new ArrayList<>();
      Optional<BigInteger> duration = mosaicProperties.getDuration();
      if (duration.isPresent()) {
         propertyList.add(new ImmutablePair<>(MosaicPropertyId.DURATION, duration.get()));
      }
      int[] optinalPropertiesVector = new int[propertyList.size()];
      for (int i = 0; i < propertyList.size(); i++) {
         Pair<MosaicPropertyId, BigInteger> property = propertyList.get(i);
         // prepare offset for the value
         int valueOffset = MosaicProperty.createValueVector(builder, UInt64Utils.fromBigInteger(property.getValue()));
         // increase size - id + value
         MosaicProperty.startMosaicProperty(builder);
         MosaicProperty.addMosaicPropertyId(builder, property.getKey().getCode());
         MosaicProperty.addValue(builder, valueOffset);
         optinalPropertiesVector[i] = MosaicProperty.endMosaicProperty(builder);
      }
      // Create Vectors
      int signatureVector = MosaicDefinitionTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = MosaicDefinitionTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = MosaicDefinitionTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = MosaicDefinitionTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getFee()));
      int mosaicIdVector = MosaicDefinitionTransactionBuffer.createMosaicIdVector(builder,
            UInt64Utils.fromBigInteger(mosaicId.getId()));
      int optionalPropertiesVector = MosaicDefinitionTransactionBuffer.createOptionalPropertiesVector(builder,
            optinalPropertiesVector);

      // header + nonce + id + numOptProp + flags + divisibility + (id + value)*numOptProp
      int size = 120 + 4 + 8 + 1 + 1 + 1 + (1 + 8) * optinalPropertiesVector.length;

      MosaicDefinitionTransactionBuffer.startMosaicDefinitionTransactionBuffer(builder);
      MosaicDefinitionTransactionBuffer.addSize(builder, size);
      MosaicDefinitionTransactionBuffer.addSignature(builder, signatureVector);
      MosaicDefinitionTransactionBuffer.addSigner(builder, signerVector);
      MosaicDefinitionTransactionBuffer.addVersion(builder, version);
      MosaicDefinitionTransactionBuffer.addType(builder, getType().getValue());
      MosaicDefinitionTransactionBuffer.addMaxFee(builder, feeVector);
      MosaicDefinitionTransactionBuffer.addDeadline(builder, deadlineVector);

      MosaicDefinitionTransactionBuffer.addMosaicNonce(builder, nonce.getNonceAsInt());
      MosaicDefinitionTransactionBuffer.addMosaicId(builder, mosaicIdVector);
      MosaicDefinitionTransactionBuffer.addNumOptionalProperties(builder, optinalPropertiesVector.length);
      MosaicDefinitionTransactionBuffer.addFlags(builder, flags);
      MosaicDefinitionTransactionBuffer.addDivisibility(builder, mosaicProperties.getDivisibility());
      MosaicDefinitionTransactionBuffer.addOptionalProperties(builder, optionalPropertiesVector);

      int codedTransaction = MosaicDefinitionTransactionBuffer.endMosaicDefinitionTransactionBuffer(builder);
      builder.finish(codedTransaction);

      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized form has incorrect length");
      return output;
   }
}
