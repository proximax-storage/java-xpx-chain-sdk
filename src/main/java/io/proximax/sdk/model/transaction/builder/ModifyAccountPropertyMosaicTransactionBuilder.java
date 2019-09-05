/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * Builder for account address property modification
 */
public class ModifyAccountPropertyMosaicTransactionBuilder extends ModifyAccountPropertyTransactionBuilder<UInt64Id> {

   public ModifyAccountPropertyMosaicTransactionBuilder() {
      super(EntityType.ACCOUNT_PROPERTIES_ADDRESS, EntityVersion.ACCOUNT_PROPERTIES_ADDRESS.getValue());
   }

   @Override
   public ModifyAccountPropertyTransaction<UInt64Id> build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            ModifyAccountPropertyTransaction.MosaicModification.calculatePayloadSize(getModifications().size())));
      // create transaction instance
      return new ModifyAccountPropertyTransaction.MosaicModification(getNetworkType(), getVersion(), getDeadline(),
            maxFee, getSignature(), getSigner(), getTransactionInfo(), getPropertyType(), getModifications());
   }

}
