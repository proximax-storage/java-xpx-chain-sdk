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

import org.apache.commons.lang.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicProperties;

/**
 * Before a mosaic can be created or transferred, a corresponding definition of the mosaic has to be created and
 * published to the network. This is done via a mosaic definition transaction.
 *
 * @since 1.0
 */
public class MosaicDefinitionTransaction extends Transaction {
   private final MosaicId mosaicId;
   private final MosaicProperties mosaicProperties;
   private final Schema schema = new MosaicDefinitionTransactionSchema();

   public MosaicDefinitionTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee,
         MosaicId mosaicId, MosaicProperties mosaicProperties, String signature, PublicAccount signer,
         TransactionInfo transactionInfo) {
      this(networkType, version, deadline, fee, mosaicId, mosaicProperties, Optional.of(signature), Optional.of(signer),
            Optional.of(transactionInfo));
   }

   public MosaicDefinitionTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee,
         MosaicId mosaicId, MosaicProperties mosaicProperties) {
      this(networkType, version, deadline, fee, mosaicId, mosaicProperties, Optional.empty(), Optional.empty(),
            Optional.empty());
   }

   private MosaicDefinitionTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee,
         MosaicId mosaicId, MosaicProperties mosaicProperties, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
      super(TransactionType.MOSAIC_DEFINITION, networkType, version, deadline, fee, signature, signer, transactionInfo);
      Validate.notNull(mosaicId, "MosaicId must not be null");
      Validate.notNull(mosaicProperties, "MosaicProperties must not be null");
      this.mosaicId = mosaicId;
      this.mosaicProperties = mosaicProperties;
   }

   /**
    * Create a mosaic creation transaction object.
    *
    * @param deadline The deadline to include the transaction.
    * @param mosaicName The mosaic name ex: xem.
    * @param namespaceName The namespace where mosaic will be included ex: nem.
    * @param mosaicProperties The mosaic properties.
    * @param networkType The network type.
    * @return {@link MosaicDefinitionTransaction}
    */
   public static MosaicDefinitionTransaction create(MosaicId mosaicId, Deadline deadline,
         MosaicProperties mosaicProperties, NetworkType networkType) {
      Validate.notNull(mosaicId, "MosaicId must not be null");
      return new MosaicDefinitionTransaction(networkType, TransactionVersion.MOSAIC_DEFINITION.getValue(), deadline,
            BigInteger.valueOf(0), mosaicId, mosaicProperties);
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
      int[] fee = new int[] { 0, 0 };
      int version = (int) Long
            .parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

      int flags = 0;

      if (mosaicProperties.isSupplyMutable()) {
         flags += 1;
      }

      if (mosaicProperties.isTransferable()) {
         flags += 2;
      }

      if (mosaicProperties.isLevyMutable()) {
         flags += 4;
      }

      // Create Vectors
      int signatureVector = MosaicDefinitionTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = MosaicDefinitionTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = MosaicDefinitionTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = MosaicDefinitionTransactionBuffer.createFeeVector(builder, fee);
      int mosaicIdVector = MosaicDefinitionTransactionBuffer.createMosaicIdVector(builder,
            UInt64Utils.fromBigInteger(mosaicId.getId()));
      int durationVector = MosaicDefinitionTransactionBuffer.createDurationVector(builder,
            UInt64Utils.fromBigInteger(mosaicProperties.getDuration()));

      MosaicDefinitionTransactionBuffer.startMosaicDefinitionTransactionBuffer(builder);
      MosaicDefinitionTransactionBuffer.addSignature(builder, signatureVector);
      MosaicDefinitionTransactionBuffer.addSigner(builder, signerVector);
      MosaicDefinitionTransactionBuffer.addVersion(builder, version);
      MosaicDefinitionTransactionBuffer.addType(builder, getType().getValue());
      MosaicDefinitionTransactionBuffer.addFee(builder, feeVector);
      MosaicDefinitionTransactionBuffer.addDeadline(builder, deadlineVector);

      MosaicDefinitionTransactionBuffer.addMosaicId(builder, mosaicIdVector);
      MosaicDefinitionTransactionBuffer.addNumOptionalProperties(builder, 1);
      MosaicDefinitionTransactionBuffer.addFlags(builder, flags);

      MosaicDefinitionTransactionBuffer.addDivisibility(builder, mosaicProperties.getDivisibility());

      MosaicDefinitionTransactionBuffer.addIndicateDuration(builder, 2);
      MosaicDefinitionTransactionBuffer.addDuration(builder, durationVector);

      int codedTransaction = MosaicDefinitionTransactionBuffer.endMosaicDefinitionTransactionBuffer(builder);
      builder.finish(codedTransaction);

      return schema.serialize(builder.sizedByteArray());
   }
}
