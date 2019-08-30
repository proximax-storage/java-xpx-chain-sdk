/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.TransactionType;

/**
 * builder for {@link ModifyAccountPropertyTransaction}
 */
public abstract class ModifyAccountPropertyTransactionBuilder<T> extends TransactionBuilder<ModifyAccountPropertyTransactionBuilder<T>, ModifyAccountPropertyTransaction<T>> {
   
   private AccountPropertyType propertyType;
   private List<AccountPropertyModification<T>> modifications;

   public ModifyAccountPropertyTransactionBuilder(TransactionType type, Integer version) {
      super(type, version);
      // provide empty lists as defaults
      this.modifications = new ArrayList<>();
   }

   @Override
   protected ModifyAccountPropertyTransactionBuilder<T> self() {
      return this;
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * @param propertyType the propertyType to set
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<T> propertyType(AccountPropertyType propertyType) {
      this.propertyType = propertyType;
      return self();
   }

   /**
    * @param modifications the modifications to set
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<T> modifications(List<AccountPropertyModification<T>> modifications) {
      this.modifications = modifications;
      return self();
   }

   // ------------------------------------- getters ---------------------------------------------//

   /**
    * @return the propertyType
    */
   public AccountPropertyType getPropertyType() {
      return propertyType;
   }

   /**
    * @return the modifications
    */
   public List<AccountPropertyModification<T>> getModifications() {
      return modifications;
   }

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * @param modifications the modifications to set
    * @return self
    */
   public ModifyAccountPropertyTransactionBuilder<T> modifications(AccountPropertyModification<T> ... modifications) {
      return modifications(Arrays.asList(modifications));
   }
}
