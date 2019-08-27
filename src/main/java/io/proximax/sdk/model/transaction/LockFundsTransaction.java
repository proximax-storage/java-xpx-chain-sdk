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
import org.spongycastle.util.encoders.Hex;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.LockFundsTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Lock funds transaction is used before sending an Aggregate bonded transaction, as a deposit to announce the
 * transaction. When aggregate bonded transaction is confirmed funds are returned to LockFundsTransaction signer.
 *
 * @since 1.0
 */
public class LockFundsTransaction extends Transaction {
   private final Schema schema = new LockFundsTransactionSchema();

   private final Mosaic mosaic;
   private final BigInteger duration;
   private final SignedTransaction signedTransaction;

   /**
    * @param networkType
    * @param version
    * @param deadline
    * @param maxFee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param mosaic
    * @param duration
    * @param signedTransaction
    */
   public LockFundsTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, Mosaic mosaic, BigInteger duration,
         SignedTransaction signedTransaction) {
      super(TransactionType.LOCK, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(mosaic, "Mosaic must not be null");
      Validate.notNull(duration, "Duration must not be null");
      Validate.notNull(signedTransaction, "Signed transaction must not be null");
      Validate.isTrue(signedTransaction.getType() == TransactionType.AGGREGATE_BONDED,
            "Signed transaction must be Aggregate Bonded Transaction");
      this.mosaic = mosaic;
      this.duration = duration;
      this.signedTransaction = signedTransaction;
   }

   /**
    * Returns locked mosaic.
    *
    * @return locked mosaic.
    */
   public Mosaic getMosaic() {
      return mosaic;
   }

   /**
    * Returns funds lock duration in number of blocks.
    *
    * @return funds lock duration in number of blocks.
    */
   public BigInteger getDuration() {
      return duration;
   }

   /**
    * Returns signed transaction for which funds are locked.
    *
    * @return signed transaction for which funds are locked.
    */
   public SignedTransaction getSignedTransaction() {
      return signedTransaction;
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Vectors
      int signatureVector = LockFundsTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = LockFundsTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = LockFundsTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = LockFundsTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));
      int mosaicIdVector = LockFundsTransactionBuffer.createMosaicIdVector(builder,
            UInt64Utils.fromBigInteger(mosaic.getId().getId()));
      int mosaicAmountVector = LockFundsTransactionBuffer.createMosaicAmountVector(builder,
            UInt64Utils.fromBigInteger(mosaic.getAmount()));
      int durationVector = LockFundsTransactionBuffer.createDurationVector(builder,
            UInt64Utils.fromBigInteger(duration));

      int hashVector = LockFundsTransactionBuffer.createHashVector(builder, Hex.decode(signedTransaction.getHash()));

      int size = getSerializedSize();

      LockFundsTransactionBuffer.startLockFundsTransactionBuffer(builder);
      LockFundsTransactionBuffer.addSize(builder, size);
      LockFundsTransactionBuffer.addSignature(builder, signatureVector);
      LockFundsTransactionBuffer.addSigner(builder, signerVector);
      LockFundsTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      LockFundsTransactionBuffer.addType(builder, getType().getValue());
      LockFundsTransactionBuffer.addMaxFee(builder, feeVector);
      LockFundsTransactionBuffer.addDeadline(builder, deadlineVector);

      LockFundsTransactionBuffer.addMosaicId(builder, mosaicIdVector);
      LockFundsTransactionBuffer.addMosaicAmount(builder, mosaicAmountVector);
      LockFundsTransactionBuffer.addDuration(builder, durationVector);
      LockFundsTransactionBuffer.addHash(builder, hashVector);

      int codedLockFunds = LockFundsTransactionBuffer.endLockFundsTransactionBuffer(builder);
      builder.finish(codedLockFunds);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   @Override
   public String toString() {
      return "LockFundsTransaction [mosaic=" + mosaic + ", duration=" + duration + ", signedTransaction="
            + signedTransaction + ", schema=" + schema + "]";
   }

   public static int calculatePayloadSize() {
      // mosaic id, amount, duration, hash
      return 8 + 8 + 8 + 32;
   }
   
   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize();
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new LockFundsTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getMosaic(),
            getDuration(), getSignedTransaction());
   }
}
