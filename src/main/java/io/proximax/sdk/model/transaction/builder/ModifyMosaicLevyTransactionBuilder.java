/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicLevy;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ModifyMosaicLevyTransaction;

/**
 * <p>
 * builder for {@link ModifyMosaicLevyTransaction}
 * </p>
 * <p>
 * Standard use when modify mosaic levy: call {@link #create(MosaicLevy, MosaicId)}
 * </p>
 */
public class ModifyMosaicLevyTransactionBuilder
        extends TransactionBuilder<ModifyMosaicLevyTransactionBuilder, ModifyMosaicLevyTransaction> {

    private MosaicLevy mosaicLevy;
    private MosaicId mosaicId;

    public ModifyMosaicLevyTransactionBuilder() {
        super(EntityType.MODIFY_MOSAIC_LEVY, EntityVersion.MODIFY_MOSAIC_LEVY.getValue());

    }

    @Override
    protected ModifyMosaicLevyTransactionBuilder self() {
        return this;
    }

    @Override
    public ModifyMosaicLevyTransaction build() {
        // use or calculate maxFee
        BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
                ModifyMosaicLevyTransaction.calculatePayloadSize()));
        // create transaction instance
        return new ModifyMosaicLevyTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(), getTransactionInfo(), getMosaicLevy(), getMosaicId());
    }

    // ------------------------------------- setters ---------------------------------------------//

    /**
    * define mosaic ID
    * 
    * @param mosaicId desired mosaic ID
    * @return self
    */
    public ModifyMosaicLevyTransactionBuilder mosaicId(MosaicId mosaicId) {
        this.mosaicId = mosaicId;
        return self();
    }

    /**
    * define mosaic levy
    * 
    * @param mosaicLevy mosaic levy 
    * @return self
    */
    public ModifyMosaicLevyTransactionBuilder mosaicLevy(MosaicLevy mosaicLevy) {
        this.mosaicLevy = mosaicLevy;
        return self();
    }

    // -------------------------------------- getters --------------------------------------------//

    /**
    * @return the mosaic Id
    */
    public MosaicId getMosaicId() {
        return mosaicId;
    }

    /**
    * @return the mosaic levy
    */
    public MosaicLevy getMosaicLevy() {
        return mosaicLevy;
    }

    // -------------------------------------- convenience --------------------------------------------//
    /**
    * convenience call to {@link ModifyMosaicLevyTransactionBuilder#mosaicLevy(MosaicLevy)}
    * 
    * @param mosaicLevy the mosaic levy to set
    * @return self
    */
    public ModifyMosaicLevyTransactionBuilder create(MosaicLevy mosaicLevy, MosaicId mosaicId) {
        return mosaicLevy(mosaicLevy).mosaicId(mosaicId);
    }

    //    /**
    //     * @param mosaicsid the mosaics id to set
    //     * @return self
    //     */
    //    public ModifyMosaicLevyTransactionBuilder mosaicsID(MosaicId mosaicsid) {
    //        return mosaicId(mosaicsid);
    //    }


}
