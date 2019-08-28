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

import io.proximax.sdk.model.transaction.ModifyContractTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link ModifyContractTransaction}
 */
public class ModifyContractTransactionBuilder
      extends TransactionBuilder<ModifyContractTransactionBuilder, ModifyContractTransaction> {

   private BigInteger durationDelta;
   private String contentHash;
   private List<MultisigCosignatoryModification> customersModifications;
   private List<MultisigCosignatoryModification> executorsModifications;
   private List<MultisigCosignatoryModification> verifiersModifications;

   public ModifyContractTransactionBuilder() {
      super(TransactionType.MODIFY_CONTRACT, TransactionVersion.MODIFY_CONTRACT.getValue());
      // defaults
      customersModifications = new ArrayList<>();
      executorsModifications = new ArrayList<>();
      verifiersModifications = new ArrayList<>();
   }

   @Override
   protected ModifyContractTransactionBuilder self() {
      return this;
   }

   @Override
   public ModifyContractTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(ModifyContractTransaction.calculatePayloadSize(getContentHash(),
                  getCustomersModifications().size(),
                  getExecutorsModifications().size(),
                  getVerifiersModifications().size())));
      // create transaction instance
      return new ModifyContractTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getDurationDelta(), getContentHash(), getCustomersModifications(),
            getExecutorsModifications(), getVerifiersModifications());
   }

   // ------------------------------------- setters ---------------------------------------------//

   public ModifyContractTransactionBuilder durationDelta(BigInteger durationDelta) {
      this.durationDelta = durationDelta;
      return self();
   }

   public ModifyContractTransactionBuilder contentHash(String contentHash) {
      this.contentHash = contentHash;
      return self();
   }

   public ModifyContractTransactionBuilder customersModifications(
         List<MultisigCosignatoryModification> customersModifications) {
      this.customersModifications = customersModifications;
      return self();
   }

   public ModifyContractTransactionBuilder executorsModifications(
         List<MultisigCosignatoryModification> executorsModifications) {
      this.executorsModifications = executorsModifications;
      return self();
   }

   public ModifyContractTransactionBuilder verifiersModifications(
         List<MultisigCosignatoryModification> verifiersModifications) {
      this.verifiersModifications = verifiersModifications;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the durationDelta
    */
   public BigInteger getDurationDelta() {
      return durationDelta;
   }

   /**
    * @return the contentHash
    */
   public String getContentHash() {
      return contentHash;
   }

   /**
    * @return the customersModifications
    */
   public List<MultisigCosignatoryModification> getCustomersModifications() {
      return customersModifications;
   }

   /**
    * @return the executorsModifications
    */
   public List<MultisigCosignatoryModification> getExecutorsModifications() {
      return executorsModifications;
   }

   /**
    * @return the verifiersModifications
    */
   public List<MultisigCosignatoryModification> getVerifiersModifications() {
      return verifiersModifications;
   }

   // -------------------------------------- convenience --------------------------------------------//

   public ModifyContractTransactionBuilder customersModifications(
         MultisigCosignatoryModification... customersModifications) {
      return customersModifications(Arrays.asList(customersModifications));
   }

   public ModifyContractTransactionBuilder executorsModifications(
         MultisigCosignatoryModification... executorsModifications) {
      return executorsModifications(Arrays.asList(executorsModifications));
   }

   public ModifyContractTransactionBuilder verifiersModifications(
         MultisigCosignatoryModification... verifiersModifications) {
      return verifiersModifications(Arrays.asList(verifiersModifications));
   }
}
