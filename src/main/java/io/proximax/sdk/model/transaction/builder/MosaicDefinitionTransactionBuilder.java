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
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link MosaicDefinitionTransaction}
 */
public class MosaicDefinitionTransactionBuilder
      extends TransactionBuilder<MosaicDefinitionTransactionBuilder, MosaicDefinitionTransaction> {

   private MosaicNonce nonce;
   private MosaicId mosaicId;
   private MosaicProperties mosaicProperties;

   public MosaicDefinitionTransactionBuilder() {
      super(TransactionType.MOSAIC_DEFINITION, TransactionVersion.MOSAIC_DEFINITION.getValue());
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