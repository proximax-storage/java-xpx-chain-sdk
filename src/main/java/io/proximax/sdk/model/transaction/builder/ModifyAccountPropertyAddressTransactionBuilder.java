/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.List;

import io.proximax.sdk.model.account.Address;
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
public class ModifyAccountPropertyAddressTransactionBuilder extends ModifyAccountPropertyTransactionBuilder<Address> {

   public ModifyAccountPropertyAddressTransactionBuilder() {
      super(EntityType.ACCOUNT_PROPERTIES_ADDRESS, EntityVersion.ACCOUNT_PROPERTIES_ADDRESS.getValue());
   }

   @Override
   public ModifyAccountPropertyTransaction<Address> build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            ModifyAccountPropertyTransaction.AddressModification.calculatePayloadSize(getModifications().size())));
      // create transaction instance
      return new ModifyAccountPropertyTransaction.AddressModification(getNetworkType(), getVersion(), getDeadline(),
            maxFee, getSignature(), getSigner(), getTransactionInfo(), getPropertyType(), getModifications());
   }

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * convenience call to manage allowed addresses for account
    * 
    * @param modifications modifications to be made to the list of allowed addresses
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<Address> allowed(
         List<AccountPropertyModification<Address>> modifications) {
      return propertyType(AccountPropertyType.ALLOW_ADDRESS).modifications(modifications);
   }

   /**
    * convenience call to manage blocked addresses for account
    * 
    * @param modifications modifications to be made to the list of blocked addresses
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<Address> blocked(
         List<AccountPropertyModification<Address>> modifications) {
      return propertyType(AccountPropertyType.BLOCK_ADDRESS).modifications(modifications);
   }
}
