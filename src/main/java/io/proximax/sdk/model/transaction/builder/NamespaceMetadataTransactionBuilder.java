/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.NamespaceMetadataTransaction;
import io.proximax.sdk.utils.MetadataCalculationUtils;

public class NamespaceMetadataTransactionBuilder
        extends TransactionBuilder<NamespaceMetadataTransactionBuilder, NamespaceMetadataTransaction> {
    private PublicAccount targetPublicKey;
    private BigInteger scopedMetadataKey;
    private NamespaceId targetNamespaceId;
    private Short valueSizeDelta;
    private Short valueSize;
    private String value;
    private String oldValue;

    public NamespaceMetadataTransactionBuilder() {
        super(EntityType.NAMESPACE_METADATA_V2, EntityVersion.NAMESPACE_METADATA_V2.getValue());
    }

    @Override
    protected NamespaceMetadataTransactionBuilder self() {
        return this;
    }

    @Override
    public NamespaceMetadataTransaction build() {
        // use or calculate maxFee
        BigInteger maxFee = getMaxFee()
                .orElseGet(() -> getMaxFeeCalculation(NamespaceMetadataTransaction.calculatePayloadSize(valueSize.intValue())));

        // create transaction instance
        return new NamespaceMetadataTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
                getSigner(), getTransactionInfo(), getTargetPublicKey(), getScopedMetadataKey(), getTargetNamespaceId(),
                getValueSizeDelta(), getValueSize(), getOldValue(), getValue());
    }
    // ------------------------------------- setters ---------------------------------------------//

    /**
     * define target public Key
     * 
     * @param targetPublicKey target public Key
     * @return self
     */
    public NamespaceMetadataTransactionBuilder targetPublicKey(PublicAccount targetPublicKey) {
        this.targetPublicKey = targetPublicKey;
        return self();
    }

    /**
     * define target metadata key
     * 
     * @param scopedMetadataKey scoped metadata key
     * @return self
     */
    public NamespaceMetadataTransactionBuilder scopedMetadataKey(BigInteger scopedMetadataKey) {
        this.scopedMetadataKey = scopedMetadataKey;
        return self();
    }

    /**
     * define target mosaic id
     * 
     * @param targetNamespaceId target mosaic id
     * @return self
     */
    public NamespaceMetadataTransactionBuilder targetNamespaceId(NamespaceId targetNamespaceId) {
        this.targetNamespaceId = targetNamespaceId;
        return self();
    }

    /**
     * define value size delta
     * 
     * @param valueSizeDelta value size delta
     * @return self
     */
    public NamespaceMetadataTransactionBuilder valueSizeDelta(Short valueSizeDelta) {
        this.valueSizeDelta = valueSizeDelta;
        return self();
    }

    /**
     * define value size
     * 
     * @param valueSize value size
     * @return self
     */
    public NamespaceMetadataTransactionBuilder valueSize(Short valueSize) {
        this.valueSize = valueSize;
        return self();
    }

    /**
     * define value
     * 
     * @param value value
     * @return self
     */
    public NamespaceMetadataTransactionBuilder value(String value) {
        this.value = value;
        return self();
    }

    /**
     * define value
     * 
     * @param oldValue
     * @return self
     */
    public NamespaceMetadataTransactionBuilder oldvalue(String oldValue) {
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
     * @return the targetNamespaceId
     */
    public NamespaceId getTargetNamespaceId() {
        return targetNamespaceId;
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
     * {@link NamespaceMetadataTransactionBuilder#create targetPublicKey(targetPublicKey).targetNamespaceId(targetNamespaceId).scopedMetadataKey(scopedMetadataKey).value(value).oldValue(oldValue).maxFee(maxFeeValue)}
     * 
     * @param targetPublicKey   target public key
     * @param scopedMetadataKey scoped metadata key
     * @param targetNamespaceId target NamespaceId
     * @param value             value
     * @param oldValue          old value
     * @return self
     */
    public NamespaceMetadataTransactionBuilder create(PublicAccount targetPublicKey,
            NamespaceId targetNamespaceId, String scopedMetadataKey, String value, String oldValue) {
        var valueSizeDeltaValue = MetadataCalculationUtils.getValueSizeDeltaValue(value, oldValue);
        var ValueSize = MetadataCalculationUtils.getValueSize(value, oldValue);
        var ScopedMetadataKey = MetadataCalculationUtils.getScopedMetadataKey(scopedMetadataKey);

       
        return targetPublicKey(targetPublicKey).targetNamespaceId(targetNamespaceId).value(value)
                .valueSizeDelta(valueSizeDeltaValue).valueSize(
                        ValueSize).oldvalue(oldValue).scopedMetadataKey(
                        ScopedMetadataKey);
    }

    
}
