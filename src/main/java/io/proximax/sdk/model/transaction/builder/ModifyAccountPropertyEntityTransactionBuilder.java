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

/**
 * <p>
 * Builder for account address property modification
 * </p>
 * <p>
 * Standard use: call {@link #allowed(List)} or {@link #blocked(List)} to manage the allowed and blocked addresses
 * </p>
 */
public class ModifyAccountPropertyEntityTransactionBuilder extends ModifyAccountPropertyTransactionBuilder<EntityType> {

   public ModifyAccountPropertyEntityTransactionBuilder() {
      super(EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE, EntityVersion.ACCOUNT_PROPERTIES_ENTITY_TYPE.getValue());
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

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * convenience call to manage allowed entity types for account
    * 
    * @param modifications modifications to be made to the list of allowed entity types
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<EntityType> allowed(
         List<AccountPropertyModification<EntityType>> modifications) {
      return propertyType(AccountPropertyType.ALLOW_TRANSACTION).modifications(modifications);
   }

   /**
    * convenience call to manage blocked entity types for account
    * 
    * @param modifications modifications to be made to the list of blocked entity types
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<EntityType> blocked(
         List<AccountPropertyModification<EntityType>> modifications) {
      return propertyType(AccountPropertyType.BLOCK_TRANSACTION).modifications(modifications);
   }
}
