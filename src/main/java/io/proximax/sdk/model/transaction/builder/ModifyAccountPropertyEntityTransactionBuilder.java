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

/**
 * Builder for account address property modification
 */
public class ModifyAccountPropertyEntityTransactionBuilder
      extends ModifyAccountPropertyTransactionBuilder<EntityType> {

   public ModifyAccountPropertyEntityTransactionBuilder() {
      super(EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE,
            EntityVersion.ACCOUNT_PROPERTIES_ENTITY_TYPE.getValue());
   }

   @Override
   public ModifyAccountPropertyTransaction<EntityType> build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            ModifyAccountPropertyTransaction.EntityTypeModification.calculatePayloadSize(getModifications().size())));
      // create transaction instance
      return new ModifyAccountPropertyTransaction.EntityTypeModification(getNetworkType(), getVersion(), getDeadline(),
            maxFee, getSignature(), getSigner(), getTransactionInfo(), getPropertyType(), getModifications());
   }
}
