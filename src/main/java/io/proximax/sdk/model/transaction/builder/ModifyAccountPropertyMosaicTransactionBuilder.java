/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.List;

import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * <p>
 * Builder for account address property modification
 * </p>
 * <p>
 * Standard use: call {@link #allowed(List)} or {@link #blocked(List)} to manage the allowed and blocked addresses
 * </p>
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

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * convenience call to manage allowed mosaics for account
    * 
    * @param modifications modifications to be made to the list of allowed mosaics
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<UInt64Id> allowed(
         List<AccountPropertyModification<UInt64Id>> modifications) {
      return propertyType(AccountPropertyType.ALLOW_MOSAIC).modifications(modifications);
   }

   /**
    * convenience call to manage blocked mosaics for account
    * 
    * @param modifications modifications to be made to the list of blocked mosaics
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<UInt64Id> blocked(
         List<AccountPropertyModification<UInt64Id>> modifications) {
      return propertyType(AccountPropertyType.BLOCK_MOSAIC).modifications(modifications);
   }
}
