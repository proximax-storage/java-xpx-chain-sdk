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
import io.proximax.sdk.gen.buffers.MetadataAccountTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.MetadataCalculationUtils;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Create/ modify account metadata transaction contains information about
 * metadata.
 */
public class AccountMetadataTransaction extends Transaction {
    private final AccountMetadataTransactionSchema schema = new AccountMetadataTransactionSchema();

    private final PublicAccount targetPublicKey;
    private final BigInteger scopedMetadataKey;
    private final Short valueSizeDelta;
    private final Short valueSize;
    private final String value;
    private final String oldValue;

    /**
     * @param networkType       network type
     * @param version           transaction version. Use
     *                          {@link EntityVersion#ACCOUNT_METADATA_V2} for
     *                          current
     *                          version
     * @param deadline          transaction deadline
     * @param maxFee            transaction fee
     * @param signature         optional signature
     * @param signer            optional signer
     * @param targetPublicKey   target public key
     * @param scopedMetadataKey scoped metadata key
     * @param targetMosaicId    target mosaicId
     * @param valueSizeDelta    value size delta
     * @param valueSize         value size
     * @param value             value
     * @param oldValue          old value
     */
    public AccountMetadataTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
            BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
            Optional<TransactionInfo> transactionInfo, PublicAccount targetPublicKey, BigInteger scopedMetadataKey,
            Short valueSizeDelta,
            Short valueSize, String oldValue, String value) {
        super(EntityType.ACCOUNT_METADATA_V2, networkType, version, deadline, maxFee, signature, signer,
                transactionInfo);
        Validate.notNull(targetPublicKey, "targetPublicKey has to be specified");
        Validate.notNull(scopedMetadataKey, "scopedMetadataKey has to be specified");
        Validate.notNull(valueSizeDelta, "valueSizeDelta has to be specified");
        Validate.notNull(valueSize, "valueSize has to be specified");
        Validate.notNull(value, "value has to be specified");

        this.targetPublicKey = targetPublicKey;
        this.scopedMetadataKey = scopedMetadataKey;
        this.valueSizeDelta = valueSizeDelta;
        this.valueSize = valueSize;
        this.value = value;
        this.oldValue = oldValue;
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
     * @return the oldValue
     */
    public String getOldValue() {
        return oldValue;
    }

    @Override
    protected byte[] generateBytes() {
        var valueDifferences = MetadataCalculationUtils.getValueDifferenceBytes(value, oldValue);
        FlatBufferBuilder builder = new FlatBufferBuilder();
        // prepare data
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        // Create Vectors
        int signatureVector = MetadataAccountTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = MetadataAccountTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = MetadataAccountTransactionBuffer.createDeadlineVector(builder,
                UInt64Utils.fromBigInteger(deadlineBigInt));
        int feeVector = MetadataAccountTransactionBuffer.createMaxFeeVector(builder,
                UInt64Utils.fromBigInteger(getMaxFee()));

        var targetKeyVector = MetadataAccountTransactionBuffer.createTargetKeyVector(builder,
                HexEncoder.getBytes(targetPublicKey.getPublicKey().toString()));
        var scopedMetadataKeyVector = MetadataAccountTransactionBuffer.createScopedMetadataKeyVector(builder,
                UInt64Utils.fromBigInteger(scopedMetadataKey));

        var valueVector = MetadataAccountTransactionBuffer.createValueVector(builder, valueDifferences);
        // total size of transaction
        int size = getSerializedSize();
        // flatbuffer serialization
        MetadataAccountTransactionBuffer.startMetadataAccountTransactionBuffer(builder);
        MetadataAccountTransactionBuffer.addSize(builder, size);
        MetadataAccountTransactionBuffer.addSignature(builder, signatureVector);
        MetadataAccountTransactionBuffer.addSigner(builder, signerVector);
        MetadataAccountTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
        MetadataAccountTransactionBuffer.addType(builder, getType().getValue());
        MetadataAccountTransactionBuffer.addMaxFee(builder, feeVector);
        MetadataAccountTransactionBuffer.addDeadline(builder, deadlineVector);
        MetadataAccountTransactionBuffer.addTargetKey(builder, targetKeyVector);
        MetadataAccountTransactionBuffer.addScopedMetadataKey(builder, scopedMetadataKeyVector);
        MetadataAccountTransactionBuffer.addValueSizeDelta(builder, valueSizeDelta);
        MetadataAccountTransactionBuffer.addValueSize(builder, valueSize);
        MetadataAccountTransactionBuffer.addValue(builder, valueVector);
        int codedTransaction = MetadataAccountTransactionBuffer.endMetadataAccountTransactionBuffer(builder);
        builder.finish(codedTransaction);

        System.out.println("size " + size);


        // validate size
        byte[] output = schema.serialize(builder.sizedByteArray());
        System.out.println("output " + output.length);

        Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
        return output;
    }

    public static int calculatePayloadSize(Integer valueSize) {
        System.out.println("calculatePayloadSize_valueSize: " + valueSize);

        // scopedMetadataKey + targetPublicKey + mosaicId + valueDeltaSize + value size
        return 8 + 32 + 2 + 2 + valueSize;
    }

    @Override
    protected int getPayloadSerializedSize() {
        int value_Size;
        if (getValueSizeDelta() == 0) {
            value_Size = valueSize.intValue() - 1;
        } else {
            value_Size = valueSize.intValue();
        }
        System.out.println("getPayloadSerializedSize_valueSize: "+valueSize);
        return calculatePayloadSize(value_Size);
    }

    @Override
    protected Transaction copyForSigner(PublicAccount signer) {
        return new AccountMetadataTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
                getSignature(),
                Optional.of(signer), getTransactionInfo(), getTargetPublicKey(), getScopedMetadataKey(),
                getValueSizeDelta(), getValueSize(), getValue(),
                getOldValue());
    }
}
