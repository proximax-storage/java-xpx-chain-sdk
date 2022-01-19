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

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.MetadataTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.MetadataCalculationUtils;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Create/ modify a mosaic metadata transaction contains information about
 * metadata .
 */
public class MosaicMetadataTransaction extends Transaction {
    private final MetadataTransactionSchema schema = new MetadataTransactionSchema();

    private final PublicAccount targetPublicKey;
    private final BigInteger scopedMetadataKey;
    private final MosaicId targetMosaicId;
    private final Short valueSizeDelta;
    private final Short valueSize;
    private final String value;
    private final String oldValue;

    /**
     * @param networkType       network type
     * @param version           transaction version. Use
     *                          {@link EntityVersion#MOSAIC_METADATA_V2} for current
     *                          version
     * @param deadline          transaction deadline
     * @param maxFee            transaction fee
     * @param signature         optional signature
     * @param signer            optional signer
     * @param transactionInfo   optional transaction info
     * @param targetPublicKey   target public key
     * @param scopedMetadataKey scoped metadata key
     * @param targetMosaicId    target mosaicId
     * @param valueSizeDelta    value size delta
     * @param valueSize         value size
     * @param oldValue          old value
     * @param value             value
     */
    public MosaicMetadataTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
            BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
            Optional<TransactionInfo> transactionInfo, PublicAccount targetPublicKey, BigInteger scopedMetadataKey,
            MosaicId targetMosaicId, Short valueSizeDelta,
            Short valueSize, String oldValue, String value) {
        super(EntityType.MOSAIC_METADATA_V2, networkType, version, deadline, maxFee, signature, signer,
                transactionInfo);
        Validate.notNull(targetPublicKey, "targetPublicKey has to be specified");
        Validate.notNull(scopedMetadataKey, "scopedMetadataKey has to be specified");
        Validate.notNull(targetMosaicId, "targetMosaicId has to be specified");
        Validate.notNull(valueSizeDelta, "valueSizeDelta has to be specified");
        Validate.notNull(valueSize, "valueSize has to be specified");
        Validate.notNull(value, "value has to be specified");
        //Validate.notNull(valueDifferences, "valueDifferences has to be specified");

        this.targetPublicKey = targetPublicKey;
        this.scopedMetadataKey = scopedMetadataKey;
        this.targetMosaicId = targetMosaicId;
        this.valueSizeDelta = valueSizeDelta;
        this.valueSize = valueSize;
        this.value = value;
        this.oldValue = oldValue;

        //this.valueDifferences = valueDifferences;

    }

    /**
     * @return the targetPublicKey
     */
    public PublicAccount getTargetPublicKey() {
        return targetPublicKey;
    }

    /**
     * @return the scopedMetadataKey
     */
    public BigInteger getScopedMetadataKey() {
        return scopedMetadataKey;
    }

    /**
     * @return the targetMosaicId
     */
    public MosaicId getTargetMosaicId() {
        return targetMosaicId;
    }

    /**
     * @return the valueSizeDelta
     */
    public Short getValueSizeDelta() {
        return valueSizeDelta;
    }

    /**
     * @return the valueSize
     */
    public Short getValueSize() {
        return valueSize;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the old value - for valueDifferences calculation
     */
    public String getOldValue() {
        return oldValue;
    }


    @Override
    protected byte[] generateBytes() {

        var valueDifferences = MetadataCalculationUtils.getValueDifferenceBytes(value, getOldValue());

        FlatBufferBuilder builder = new FlatBufferBuilder();
        // prepare data
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        // Create Vectors
        int signatureVector = MetadataTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = MetadataTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = MetadataTransactionBuffer.createDeadlineVector(builder,
                UInt64Utils.fromBigInteger(deadlineBigInt));
        int feeVector = MetadataTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));

        var targetKeyVector = MetadataTransactionBuffer.createTargetKeyVector(builder,
                HexEncoder.getBytes(targetPublicKey.getPublicKey().toString()));
        var scopedMetadataKeyVector = MetadataTransactionBuffer.createScopedMetadataKeyVector(builder,
                UInt64Utils.fromBigInteger(scopedMetadataKey));
        var targetIdVector = MetadataTransactionBuffer.createTargetIdVector(builder,
                UInt64Utils.fromBigInteger(targetMosaicId.getId()));

        var valueVector = MetadataTransactionBuffer.createValueVector(builder, valueDifferences);
        // total size of transaction
        int size = getSerializedSize();
        // flatbuffer serialization
        MetadataTransactionBuffer.startMetadataTransactionBuffer(builder);
        MetadataTransactionBuffer.addSize(builder, size);
        MetadataTransactionBuffer.addSignature(builder, signatureVector);
        MetadataTransactionBuffer.addSigner(builder, signerVector);
        MetadataTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
        MetadataTransactionBuffer.addType(builder, getType().getValue());
        MetadataTransactionBuffer.addMaxFee(builder, feeVector);
        MetadataTransactionBuffer.addDeadline(builder, deadlineVector);
        MetadataTransactionBuffer.addTargetKey(builder, targetKeyVector);
        MetadataTransactionBuffer.addScopedMetadataKey(builder, scopedMetadataKeyVector);
        MetadataTransactionBuffer.addTargetId(builder, targetIdVector);
        MetadataTransactionBuffer.addValueSizeDelta(builder, valueSizeDelta);
        MetadataTransactionBuffer.addValueSize(builder, valueSize);
        MetadataTransactionBuffer.addValue(builder, valueVector);
        int codedTransaction = MetadataTransactionBuffer.endMetadataTransactionBuffer(builder);
        builder.finish(codedTransaction);

        // validate size
        byte[] output = schema.serialize(builder.sizedByteArray());
        Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
        return output;
    }

    public static int calculatePayloadSize(Integer valueSize) {
        // scopedMetadataKey + targetPublicKey + mosaicId + valueDeltaSize + value size
        return 8 + 32 + 8 + 2 + 2 + valueSize;
    }

    @Override
    protected int getPayloadSerializedSize() {
        int value_Size;
        if (getValueSizeDelta() == 0) {
            value_Size = valueSize.intValue() - 1;
        } else {
            value_Size = valueSize.intValue();
        }
        return calculatePayloadSize(value_Size);
    }

    @Override
    protected Transaction copyForSigner(PublicAccount signer) {
        return new MosaicMetadataTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
                Optional.of(signer), getTransactionInfo(), getTargetPublicKey(), getScopedMetadataKey(),
                getTargetMosaicId(), getValueSizeDelta(), getValueSize(), null, getValue());
    }
}
