/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Optional;

import com.google.flatbuffers.FlatBufferBuilder;

import org.apache.commons.lang3.Validate;

import io.proximax.sdk.gen.buffers.RemoveMosaicLevyTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Accounts can stop the collection of taxes from mosaics transfers. This can be
 * done via RemoveMosaicLevyTransaction
 */
public class RemoveMosaicLevyTransaction extends Transaction{
    private final RemoveMosaicLevyTransactionSchema schema = new RemoveMosaicLevyTransactionSchema();
    private final MosaicId mosaicId;

    /**
     * @param networkType     network type
     * @param version         transaction version. Use
     *                        {@link EntityVersion#MOSAIC_MODIFY_LEVY} for
     *                        current version
     * @param deadline        transaction deadline
     * @param maxFee          transaction fee
     * @param signature       optional signature
     * @param signer          optional signer
     * @param transactionInfo optional transaction info
     * @param mosaicID        ID of mosaic that will change
     * 
     */
 public RemoveMosaicLevyTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, MosaicId mosaicId) {
     super(EntityType.REMOVE_MOSAIC_LEVY, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
     Validate.notNull(mosaicId, "mosaicId has to be specified");

     this.mosaicId = mosaicId;
 }
   
 /**
  * Returns mosaic id.
  *
  * @return BigInteger
  */
 public MosaicId getMosaicId() {
     return mosaicId;
 }

 @Override
 protected byte[] generateBytes() {
     FlatBufferBuilder builder = new FlatBufferBuilder();
     // prepare data
     BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

     // Create Vectors
     int signatureVector = RemoveMosaicLevyTransactionBuffer.createSignatureVector(builder, new byte[64]);
     int signerVector = RemoveMosaicLevyTransactionBuffer.createSignerVector(builder, new byte[32]);
     int deadlineVector = RemoveMosaicLevyTransactionBuffer.createDeadlineVector(builder,
             UInt64Utils.fromBigInteger(deadlineBigInt));
     int feeVector = RemoveMosaicLevyTransactionBuffer.createMaxFeeVector(builder,
             UInt64Utils.fromBigInteger(getMaxFee()));
     int mosaicIdVector = RemoveMosaicLevyTransactionBuffer.createMosaicIdVector(builder,
             UInt64Utils.fromBigInteger(mosaicId.getId()));

     int size = getSerializedSize();

     // flatbuffer serialization
     RemoveMosaicLevyTransactionBuffer.startRemoveMosaicLevyTransactionBuffer(builder);
     RemoveMosaicLevyTransactionBuffer.addSize(builder, size);
     RemoveMosaicLevyTransactionBuffer.addSignature(builder, signatureVector);
     RemoveMosaicLevyTransactionBuffer.addSigner(builder, signerVector);
     RemoveMosaicLevyTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
     RemoveMosaicLevyTransactionBuffer.addType(builder, getType().getValue());
     RemoveMosaicLevyTransactionBuffer.addMaxFee(builder, feeVector);
     RemoveMosaicLevyTransactionBuffer.addDeadline(builder, deadlineVector);

     RemoveMosaicLevyTransactionBuffer.addMosaicId(builder, mosaicIdVector);

     int codedTransaction = RemoveMosaicLevyTransactionBuffer.endRemoveMosaicLevyTransactionBuffer(builder);
     builder.finish(codedTransaction);

     // validate size
     byte[] output = schema.serialize(builder.sizedByteArray());
     Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
     return output;
 }
 
 public static int calculatePayloadSize() {
     // MosaicId
     return 8;
 }
 
 @Override
 protected int getPayloadSerializedSize() {
     return calculatePayloadSize();
 }
 
 @Override
 protected Transaction copyForSigner(PublicAccount signer) {
     return new RemoveMosaicLevyTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(), Optional.of(signer), getTransactionInfo(), getMosaicId());
 }

}
