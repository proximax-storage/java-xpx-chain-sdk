/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;

/**
 * <p>
 * builder for {@link MosaicDefinitionTransaction}
 * </p>
 * <p>
 * Standard use: call {@link #init(MosaicNonce, MosaicId, MosaicProperties)} to specify all of the required parameters
 * </p>
 */
public class MosaicDefinitionTransactionBuilder
      extends TransactionBuilder<MosaicDefinitionTransactionBuilder, MosaicDefinitionTransaction> {

   private MosaicNonce nonce;
   private MosaicId mosaicId;
   private MosaicProperties mosaicProperties;

   public MosaicDefinitionTransactionBuilder() {
      super(EntityType.MOSAIC_DEFINITION, EntityVersion.MOSAIC_DEFINITION.getValue());
      // defaults
   }

   @Override
   protected MosaicDefinitionTransactionBuilder self() {
      return this;
   }

   @Override
   public MosaicDefinitionTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            MosaicDefinitionTransaction.calculatePayloadSize(getMosaicProperties().getDuration().isPresent() ? 1 : 0)));
      // create transaction instance
      return new MosaicDefinitionTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getNonce(), getMosaicId(), getMosaicProperties());
   }

   // ------------------------------------ checked API ------------------------------------------//

   /**
    * initialize builder with mandatory parameters
    * 
    * @param nonce mosaic nonce
    * @param mosaicId mosaic ID
    * @param mosaicProperties mosaic properties
    * @return self
    */
   public MosaicDefinitionTransactionBuilder init(MosaicNonce nonce, MosaicId mosaicId,
         MosaicProperties mosaicProperties) {
      return nonce(nonce).mosaicId(mosaicId).mosaicProperties(mosaicProperties);
   }

   // -------------------------------------- setters --------------------------------------------//

   /**
    * define nonce. Nonce is random number ensuring the uniqueness of the mosaic ID
    * 
    * @param nonce the nonce
    * @return self
    */
   public MosaicDefinitionTransactionBuilder nonce(MosaicNonce nonce) {
      this.nonce = nonce;
      return self();
   }

   /**
    * define mosaic ID
    * 
    * @param mosaicId desired mosaic ID
    * @return self
    */
   public MosaicDefinitionTransactionBuilder mosaicId(MosaicId mosaicId) {
      this.mosaicId = mosaicId;
      return self();
   }

   /**
    * define mosaic properties
    * 
    * @param mosaicProperties the mosaic properties
    * @return self
    */
   public MosaicDefinitionTransactionBuilder mosaicProperties(MosaicProperties mosaicProperties) {
      this.mosaicProperties = mosaicProperties;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the nonce
    */
   public MosaicNonce getNonce() {
      return nonce;
   }

   /**
    * @return the mosaicId
    */
   public MosaicId getMosaicId() {
      return mosaicId;
   }

   /**
    * @return the mosaicProperties
    */
   public MosaicProperties getMosaicProperties() {
      return mosaicProperties;
   }

   // -------------------------------------- convenience --------------------------------------------//

}