/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.proximax.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;

/**
 * builder for {@link ModifyMultisigAccountTransaction}
 */
public class ModifyMultisigAccountTransactionBuilder
      extends TransactionBuilder<ModifyMultisigAccountTransactionBuilder, ModifyMultisigAccountTransaction> {

   private int minApprovalDelta;
   private int minRemovalDelta;
   private List<MultisigCosignatoryModification> modifications;

   public ModifyMultisigAccountTransactionBuilder() {
      super(EntityType.MODIFY_MULTISIG_ACCOUNT, EntityVersion.MODIFY_MULTISIG_ACCOUNT.getValue());
      // defaults
      minApprovalDelta = 0;
      minRemovalDelta = 0;
      modifications = new ArrayList<>();
   }

   @Override
   protected ModifyMultisigAccountTransactionBuilder self() {
      return this;
   }

   @Override
   public ModifyMultisigAccountTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(ModifyMultisigAccountTransaction.calculatePayloadSize(getModifications().size())));
      // create transaction instance
      return new ModifyMultisigAccountTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getMinApprovalDelta(), getMinRemovalDelta(), getModifications());
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * change in minimum number of cosignatory approvals of transactions
    * 
    * @param minApprovalDelta change in min approvals
    * @return self
    */
   public ModifyMultisigAccountTransactionBuilder minApprovalDelta(int minApprovalDelta) {
      this.minApprovalDelta = minApprovalDelta;
      return self();
   }

   /**
    * change in minimum number of cosignatory approvals for removal of cosignatory
    * @param minRemovalDelta change in minimum removals
    * @return self
    */
   public ModifyMultisigAccountTransactionBuilder minRemovalDelta(int minRemovalDelta) {
      this.minRemovalDelta = minRemovalDelta;
      return self();
   }

   /**
    * changes in list of cosignatories for the account
    * 
    * @param modifications list of changes
    * @return self
    */
   public ModifyMultisigAccountTransactionBuilder modifications(List<MultisigCosignatoryModification> modifications) {
      this.modifications = modifications;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the minApprovalDelta
    */
   public int getMinApprovalDelta() {
      return minApprovalDelta;
   }

   /**
    * @return the minRemovalDelta
    */
   public int getMinRemovalDelta() {
      return minRemovalDelta;
   }

   /**
    * @return the modifications
    */
   public List<MultisigCosignatoryModification> getModifications() {
      return modifications;
   }

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * changes in list of cosignatories for the account
    * 
    * @param modifications changes
    * @return self
    */
   public ModifyMultisigAccountTransactionBuilder modifications(MultisigCosignatoryModification ... modifications) {
      return modifications(Arrays.asList(modifications));
   }

}
