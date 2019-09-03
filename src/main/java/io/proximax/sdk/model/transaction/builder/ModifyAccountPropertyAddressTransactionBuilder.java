/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;

/**
 * Builder for account address property modification
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

}
