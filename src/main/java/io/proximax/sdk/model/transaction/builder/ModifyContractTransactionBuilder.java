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

import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ModifyContractTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;

/**
 * <p>
 * builder for {@link ModifyContractTransaction}
 * </p>
 * <p>
 * Standard use: contract needs to be identified by the contentHash. To create contract call
 * {@link #init(String, BigInteger)} followed by definition of customers, executors and verifiers
 * </p>
 */
public class ModifyContractTransactionBuilder
      extends TransactionBuilder<ModifyContractTransactionBuilder, ModifyContractTransaction> {

   private BigInteger durationDelta;
   private String contentHash;
   private List<MultisigCosignatoryModification> customersModifications;
   private List<MultisigCosignatoryModification> executorsModifications;
   private List<MultisigCosignatoryModification> verifiersModifications;

   public ModifyContractTransactionBuilder() {
      super(EntityType.MODIFY_CONTRACT, EntityVersion.MODIFY_CONTRACT.getValue());
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

   // ------------------------------------ checked API ------------------------------------------//

   /**
    * initialize builder with parameters that are mandatory to create new contract. also can be used to change duration
    * of the contract
    * 
    * @param contentHash hash of the contract content
    * @param durationDelta duration of the contract in blocks for new contract and change in duration for existing
    * contract
    * @return self
    */
   public ModifyContractTransactionBuilder init(String contentHash, BigInteger durationDelta) {
      return contentHash(contentHash).durationDelta(durationDelta);
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
