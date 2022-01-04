/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.MosaicMetadataTransaction;
import io.proximax.sdk.utils.MetadataCalculationUtils;

/**
 * <p>
 * builder for {@link MosaicMetadataTransaction}
 * </p>
 * <p>
 * Standard use when create mosaic levy: call
 * {@link #create(PublicAccount targetPublicKey, BigInteger scopedMetadataKey,MosaicId targetMosaicId, Short valueSizeDelta, Integer valueSize, String value, String oldValue)}
 * </p>
 */
public class MosaicMetadataTransactionBuilder
        extends TransactionBuilder<MosaicMetadataTransactionBuilder, MosaicMetadataTransaction> {

    private PublicAccount targetPublicKey;
    private BigInteger scopedMetadataKey;
    private MosaicId targetMosaicId;
    private Short valueSizeDelta;
    private Short valueSize;
    private String value;
    private String oldValue;

    public MosaicMetadataTransactionBuilder() {
        super(EntityType.MOSAIC_METADATA_V2, EntityVersion.MOSAIC_METADATA_V2.getValue());
    }

    @Override
    protected MosaicMetadataTransactionBuilder self() {
        return this;
    }

    @Override
    public MosaicMetadataTransaction build() {
        // use or calculate maxFee
        BigInteger maxFee = getMaxFee()
                .orElseGet(() -> getMaxFeeCalculation(MosaicMetadataTransaction.calculatePayloadSize(valueSize.intValue())));

        // create transaction instance
        return new MosaicMetadataTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
                getSigner(), getTransactionInfo(), getTargetPublicKey(), getScopedMetadataKey(), 
                getTargetMosaicId(),
                getValueSizeDelta(), getValueSize(), getOldValue(), getValue());
    }
    // ------------------------------------- setters
    // ---------------------------------------------//

    /**
     * define target public Key
     * 
     * @param targetPublicKey target public Key
     * @return self
     */
    public MosaicMetadataTransactionBuilder targetPublicKey(PublicAccount targetPublicKey) {
        this.targetPublicKey = targetPublicKey;
        return self();
    }

    /**
     * define target metadata key
     * 
     * @param scopedMetadataKey scoped metadata key
     * @return self
     */
    public MosaicMetadataTransactionBuilder scopedMetadataKey(BigInteger scopedMetadataKey) {
        this.scopedMetadataKey = scopedMetadataKey;
        return self();
    }

    /**
     * define target mosaic id
     * 
     * @param targetMosaicId target mosaic id
     * @return self
     */
    public MosaicMetadataTransactionBuilder targetMosaicId(MosaicId targetMosaicId) {
        this.targetMosaicId = targetMosaicId;
        return self();
    }

    /**
     * define value size delta
     * 
     * @param valueSizeDelta value size delta
     * @return self
     */
    public MosaicMetadataTransactionBuilder valueSizeDelta(Short valueSizeDelta) {
        this.valueSizeDelta = valueSizeDelta;
        return self();
    }

    /**
     * define value size
     * 
     * @param valueSize value size
     * @return self
     */
    public MosaicMetadataTransactionBuilder valueSize(Short valueSize) {
        this.valueSize = valueSize;
        return self();
    }

    /**
     * define value
     * 
     * @param value value
     * @return self
     */
    public MosaicMetadataTransactionBuilder value(String value) {
        this.value = value;
        return self();
    }

    /**
     * define old value
     * 
     * @param old value value
     * @return self
     */
    public MosaicMetadataTransactionBuilder oldvalue(String oldValue) {
        this.oldValue = oldValue;
        return self();
    }
    // -------------------------------------- getters --------------------------------------------//

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
     * @return the old value
     */
    public String getOldValue() {
        return oldValue;
    }

    // -------------------------------------- convenience
    // --------------------------------------------//
    /**
     * convenience call to
     * {@link MosaicMetadataTransactionBuilder#deadline(deadline).targetPublicKey(targetPublicKey).targetMosaicId(targetMosaicId).scopedMetadataKey(scopedMetadataKey).value(value).oldValue(oldValue).maxFee(maxFeeValue)}
     * 
     * @param targetPublicKey   target public key
     * @param scopedMetadataKey scoped metadata key
     * @param targetMosaicId    target mosaicId
     * @param valueSizeDelta    value size delta
     * @param valueSize         value size
     * @param value             value
     * @param oldValue          old value
     * @param valueDifferences  value differences
     * @param maxfee            max fee
     * @return self
     */
    public MosaicMetadataTransactionBuilder create(PublicAccount targetPublicKey,
            MosaicId targetMosaicId, String scopedMetadataKey, String value, String oldValue) {
        var valueSizeDeltaValue = MetadataCalculationUtils.getValueSizeDeltaValue(value, oldValue);
        var ValueSize = MetadataCalculationUtils.getValueSize(value, oldValue);
        var ScopedMetadataKey = MetadataCalculationUtils.getScopedMetadataKey(scopedMetadataKey);
      

        // byte[] ScopedKey = StringUtils.getBytes(scopedMetadataKey);
        // byte[] scopeMetadatakey = Hashes.sha3_256(ScopedKey);
        // BigInteger ScopedMetadataKey = new BigInteger(scopeMetadatakey);

        // byte[] OldValue = StringUtils.getBytes(oldValue);
        // byte[] NewValue = StringUtils.getBytes(value);
        // Integer ValueSize = Math.max(OldValue.length, NewValue.length);

        // Integer valueSizeDelta = (OldValue.length) - (NewValue.length);
        // Short valueSizeDeltaValue = valueSizeDelta.shortValue();

        // byte[] valueDifferenceBytes = new byte[ValueSize];
        // byte[] newValueByteArray = new byte[ValueSize];
        // byte[] oldValueByteArray = new byte[ValueSize];

        // System.arraycopy(NewValue, 0, newValueByteArray, 0, NewValue.length);
        // System.arraycopy(OldValue, 0, oldValueByteArray, 0, OldValue.length);

        // for (int i = 0; i < ValueSize; i++) {

        //     valueDifferenceBytes[i] = (byte) (newValueByteArray[i] ^ oldValueByteArray[i]);
        // }
        return targetPublicKey(targetPublicKey).targetMosaicId(targetMosaicId).value(value).valueSizeDelta(valueSizeDeltaValue).valueSize(
                ValueSize).scopedMetadataKey(
                ScopedMetadataKey).oldvalue(oldValue);
    }
}
