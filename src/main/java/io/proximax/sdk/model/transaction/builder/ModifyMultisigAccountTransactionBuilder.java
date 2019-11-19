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
import java.util.stream.Collectors;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;

/**
 * <p>
 * builder for {@link ModifyMultisigAccountTransaction}
 * </p>
 * <p>
 * Standard use: When converting account to multisig you will need to specify {@link #minApprovalDelta(int)} and
 * {@link #minRemovalDelta(int)} together with list of cosignatory modifications
 * </p>
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
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            ModifyMultisigAccountTransaction.calculatePayloadSize(getModifications().size())));
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
    * 
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
   public ModifyMultisigAccountTransactionBuilder modifications(MultisigCosignatoryModification... modifications) {
      return modifications(Arrays.asList(modifications));
   }

   /**
    * <p>
    * change account to mutisig account by adding specified cosigners and specifying minimum approvals and minimum
    * removals
    * </p>
    * <p>
    * NOTE that if account has already been multisig then cosigners will be added and min approvals and min removals
    * will be changed by specified deltas
    * </p>
    * 
    * @param cosigners list of cosigners to be added to the account
    * @param minApprovals change in min approvals
    * @param minRemovals change in min removals
    * 
    * @return self
    */
   public ModifyMultisigAccountTransactionBuilder changeToMultisig(List<PublicAccount> cosigners, int minApprovals,
         int minRemovals) {
      return minApprovalDelta(minApprovals).minRemovalDelta(minRemovals)
            .modifications(cosigners.stream().map(MultisigCosignatoryModification::add).collect(Collectors.toList()));
   }
}
