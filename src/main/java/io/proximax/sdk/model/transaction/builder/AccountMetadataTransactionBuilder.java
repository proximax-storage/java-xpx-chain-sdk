/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.transaction.AccountMetadataTransaction;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.utils.MetadataCalculationUtils;

public class AccountMetadataTransactionBuilder
        extends TransactionBuilder<AccountMetadataTransactionBuilder, AccountMetadataTransaction> {
    private PublicAccount targetPublicKey;
    private BigInteger scopedMetadataKey;
    private Short valueSizeDelta;
    private Short valueSize;
    private String value;
    private String oldValue;

    public AccountMetadataTransactionBuilder() {
        super(EntityType.ACCOUNT_METADATA_V2, EntityVersion.ACCOUNT_METADATA_V2.getValue());
    }

    @Override
    protected AccountMetadataTransactionBuilder self() {
        return this;
    }

    @Override
    public AccountMetadataTransaction build() {
        // use or calculate maxFee
        BigInteger maxFee = getMaxFee()
                .orElseGet(() -> getMaxFeeCalculation(AccountMetadataTransaction.calculatePayloadSize(valueSize.intValue())));

        // create transaction instance
        return new AccountMetadataTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
                getSigner(), getTransactionInfo(), getTargetPublicKey(), getScopedMetadataKey(), getValueSizeDelta(),
                getValueSize(), getOldValue(), getValue());
    }
    // ------------------------------------- setters ---------------------------------------------//

    /**
     * define target public Key
     * 
     * @param targetPublicKey target public Key
     * @return self
     */
    public AccountMetadataTransactionBuilder targetPublicKey(PublicAccount targetPublicKey) {
        this.targetPublicKey = targetPublicKey;
        return self();
    }

    /**
     * define target metadata key
     * 
     * @param scopedMetadataKey scoped metadata key
     * @return self
     */
    public AccountMetadataTransactionBuilder scopedMetadataKey(BigInteger scopedMetadataKey) {
        this.scopedMetadataKey = scopedMetadataKey;
        return self();
    }

    /**
     * define value size delta
     * 
     * @param valueSizeDelta value size delta
     * @return self
     */
    public AccountMetadataTransactionBuilder valueSizeDelta(Short valueSizeDelta) {
        this.valueSizeDelta = valueSizeDelta;
        return self();
    }

    /**
     * define value size
     * 
     * @param valueSize value size
     * @return self
     */
    public AccountMetadataTransactionBuilder valueSize(Short valueSize) {
        this.valueSize = valueSize;
        return self();
    }

    /**
     * define value
     * 
     * @param value value
     * @return self
     */
    public AccountMetadataTransactionBuilder value(String value) {
        this.value = value;
        return self();
    }

    /**
     * define old value
     * 
     * @param value value
     * @return self
     */
    public AccountMetadataTransactionBuilder oldValue(String oldValue) {
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

    // -------------------------------------- convenience --------------------------------------------//
    /**
     * convenience call to
     * {@link AccountMetadataTransactionBuilder#deadline(deadline).targetPublicKey(targetPublicKey).scopedMetadataKey(scopedMetadataKey).value(value).oldValue(oldValue).maxFee(maxFeeValue)}
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
    public AccountMetadataTransactionBuilder create(PublicAccount targetPublicKey,
            String scopedMetadataKey, String value, String oldValue) {
        var valueSizeDeltaValue = MetadataCalculationUtils.getValueSizeDeltaValue(value, oldValue);
        var ValueSize = MetadataCalculationUtils.getValueSize(value, oldValue);
        var ScopedMetadataKey = MetadataCalculationUtils.getScopedMetadataKey(scopedMetadataKey);
        return targetPublicKey(targetPublicKey).value(value)
                .valueSizeDelta(valueSizeDeltaValue).valueSize(
                        ValueSize)
                .scopedMetadataKey(
                        ScopedMetadataKey)
                .oldValue(oldValue);
    }
}
