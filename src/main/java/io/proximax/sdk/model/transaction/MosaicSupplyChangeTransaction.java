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

import io.proximax.sdk.gen.buffers.MosaicSupplyChangeTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicSupplyType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * In case a mosaic has the flag 'supplyMutable' set to true, the creator of the mosaic can change the supply, i.e.
 * increase or decrease the supply.
 */
public class MosaicSupplyChangeTransaction extends Transaction {
   private final Schema schema = new MosaicSupplyChangeTransactionSchema();

   private final MosaicId mosaicId;
   private final MosaicSupplyType mosaicSupplyType;
   private final BigInteger delta;

   /**
    * @param networkType
    * @param version
    * @param deadline
    * @param maxFee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param mosaicId
    * @param mosaicSupplyType
    * @param delta
    */
   public MosaicSupplyChangeTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, MosaicId mosaicId, MosaicSupplyType mosaicSupplyType,
         BigInteger delta) {
      super(EntityType.MOSAIC_SUPPLY_CHANGE, networkType, version, deadline, maxFee, signature, signer,
            transactionInfo);
      // validations
      Validate.notNull(mosaicId, "MosaicId must not be null");
      Validate.notNull(mosaicSupplyType, "MosaicSupplyType must not be null");
      Validate.notNull(delta, "Delta must not be null");
      // assignments
      this.mosaicId = mosaicId;
      this.mosaicSupplyType = mosaicSupplyType;
      this.delta = delta;
   }

   /**
    * Returns mosaic id.
    *
    * @return BigInteger
    */
   public MosaicId getMosaicId() {
      return mosaicId;
   }

   /**
    * Returns mosaic supply type.
    *
    * @return {@link MosaicSupplyType}
    */
   public MosaicSupplyType getMosaicSupplyType() {
      return mosaicSupplyType;
   }

   /**
    * Returns amount of mosaics added or removed.
    *
    * @return BigInteger
    */
   public BigInteger getDelta() {
      return delta;
   }

   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Vectors
      int signatureVector = MosaicSupplyChangeTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = MosaicSupplyChangeTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = MosaicSupplyChangeTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = MosaicSupplyChangeTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int mosaicIdVector = MosaicSupplyChangeTransactionBuffer.createMosaicIdVector(builder,
            UInt64Utils.fromBigInteger(mosaicId.getId()));
      int deltaVector = MosaicSupplyChangeTransactionBuffer.createDeltaVector(builder,
            UInt64Utils.fromBigInteger(delta));

      int size = getSerializedSize();

      MosaicSupplyChangeTransactionBuffer.startMosaicSupplyChangeTransactionBuffer(builder);
      MosaicSupplyChangeTransactionBuffer.addSize(builder, size);
      MosaicSupplyChangeTransactionBuffer.addSignature(builder, signatureVector);
      MosaicSupplyChangeTransactionBuffer.addSigner(builder, signerVector);
      MosaicSupplyChangeTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      MosaicSupplyChangeTransactionBuffer.addType(builder, getType().getValue());
      MosaicSupplyChangeTransactionBuffer.addMaxFee(builder, feeVector);
      MosaicSupplyChangeTransactionBuffer.addDeadline(builder, deadlineVector);

      MosaicSupplyChangeTransactionBuffer.addMosaicId(builder, mosaicIdVector);
      MosaicSupplyChangeTransactionBuffer.addDirection(builder, mosaicSupplyType.getValue());
      MosaicSupplyChangeTransactionBuffer.addDelta(builder, deltaVector);

      int codedTransaction = MosaicSupplyChangeTransactionBuffer.endMosaicSupplyChangeTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   public static int calculatePayloadSize() {
      // mosaic id, supply type, delta
      return 8 + 1 + 8;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize();
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new MosaicSupplyChangeTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getMosaicId(), getMosaicSupplyType(),
            getDelta());
   }
}
