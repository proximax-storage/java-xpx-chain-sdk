/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.RemoveMosaicLevyTransaction;

/**
 * <p>
 * builder for {@link RemoveMosaicLevyTransaction}
 * </p>
 * <p>
 * Standard use when remove mosaic levy: call {@link #create(MosaicId)}
 * </p>
 */
public class RemoveMosaicLevyTransactionBuilder extends TransactionBuilder<RemoveMosaicLevyTransactionBuilder, RemoveMosaicLevyTransaction> {
    private MosaicId mosaicID;
public RemoveMosaicLevyTransactionBuilder() {
        super(EntityType.REMOVE_MOSAIC_LEVY, EntityVersion.REMOVE_MOSAIC_LEVY.getValue());

    }
    
    @Override
    protected RemoveMosaicLevyTransactionBuilder self() {
        return this;
    }

    @Override
    public RemoveMosaicLevyTransaction build() {
        // use or calculate maxFee
        BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(RemoveMosaicLevyTransaction.calculatePayloadSize()));
        // create transaction instance
        return new RemoveMosaicLevyTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(), getTransactionInfo(), getMosaicId());
    }
   
    // ------------------------------------- setters ---------------------------------------------//

    /**
     * define mosaic ID
     * 
     * @param mosaicID desired mosaic ID
     * @return self
     */
    public RemoveMosaicLevyTransactionBuilder mosaicId(MosaicId mosaicID) {
        this.mosaicID = mosaicID;
        return self();
    }
    // -------------------------------------- getters --------------------------------------------//

    /**
     * @return the mosaic Id
     */
    public MosaicId getMosaicId() {
        return mosaicID;
    }

    // ----------------------------------------- convenience methods -------------------------------------//
    /**
     * @param mosaicID the mosaid Id to set
     * @return self
     */
    public RemoveMosaicLevyTransactionBuilder create(MosaicId mosaicID) {
        return mosaicId(mosaicID);
    }
}
