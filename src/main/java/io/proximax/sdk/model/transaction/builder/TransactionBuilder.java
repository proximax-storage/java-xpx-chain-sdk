/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.Optional;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.DeadlineRaw;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionDeadline;
import io.proximax.sdk.model.transaction.TransactionInfo;
import io.proximax.sdk.model.transaction.TransactionType;

/**
 * Transaction builder is base class implementing builder pattern to construct instance of transaction. It is bases of
 * specialized transaction builders for all types of transactions
 */
public abstract class TransactionBuilder<B extends TransactionBuilder<B, T>, T extends Transaction> {

   private TransactionType type;
   private Integer version;
   private NetworkType networkType;
   private Optional<BigInteger> maxFee;
   private Optional<String> signature;
   private Optional<PublicAccount> signer;
   private Optional<TransactionInfo> transactionInfo;
   private Optional<FeeCalculationStrategy> feeCalculationStrategy;
   // fixed deadline
   private Optional<TransactionDeadline> deadline;
   // duration to generate deadline from the time of call to build
   private Optional<BigInteger> deadlineMillis;

   /**
    * @param type
    * @param version
    */
   public TransactionBuilder(TransactionType type, Integer version) {
      this.type = type;
      this.version = version;
      // initialize defaults
      maxFee = Optional.empty();
      signature = Optional.empty();
      signer = Optional.empty();
      transactionInfo = Optional.empty();
      feeCalculationStrategy = Optional.empty();
      deadline = Optional.empty();
      deadlineMillis = Optional.empty();
   }

   /**
    * every builder needs to return instance of itself so that type-safe builder can be constructed
    * 
    * @return reference to itself
    */
   protected abstract B self();

   /**
    * every builder needs to provide build method that creates instance of the transaction
    * 
    * @return the instance of the transaction
    */
   public abstract T build();

   /**
    * calculate maxFee using fee calculation strategy or return 0 maxFee if no strategy was provided
    * 
    * @param transactionPayloadSize size of transaction in bytes excluding header
    * @return the maxFee
    */
   protected BigInteger getMaxFeeCalculation(int transactionPayloadSize) {
      return feeCalculationStrategy.orElse(FeeCalculationStrategy.ZERO).calculateFee(Transaction.HEADER_SIZE + transactionPayloadSize);
   }

   // ----------------------------------------- setters --------------------------------------//

   /**
    * @param version the version to set
    */
   public B version(Integer version) {
      this.version = version;
      return self();
   }

   /**
    * @param networkType the networkType to set
    */
   public B networkType(NetworkType networkType) {
      this.networkType = networkType;
      return self();
   }

   /**
    * @param deadline the fixed deadline to use
    */
   public B deadline(TransactionDeadline deadline) {
      this.deadline = Optional.of(deadline);
      return self();
   }

   /**
    * @param deadlineMillis the deadline specified as duration in milliseconds after {@link TransactionBuilder#build()}
    * is invoked
    */
   public B deadlineDuration(BigInteger deadlineMillis) {
      this.deadlineMillis = Optional.of(deadlineMillis);
      return self();
   }

   /**
    * @param maxFee the maxFee to set
    */
   public B maxFee(BigInteger maxFee) {
      this.maxFee = Optional.of(maxFee);
      return self();
   }

   /**
    * @param signature the signature to set
    */
   public B signature(Optional<String> signature) {
      this.signature = signature;
      return self();
   }

   /**
    * @param signer the signer to set
    */
   public B signer(Optional<PublicAccount> signer) {
      this.signer = signer;
      return self();
   }

   /**
    * @param transactionInfo the transactionInfo to set
    */
   public B transactionInfo(Optional<TransactionInfo> transactionInfo) {
      this.transactionInfo = transactionInfo;
      return self();
   }

   /**
    * @param feeCalculationStrategy the feeCalculationStrategy to set
    */
   public B feeCalculationStrategy(FeeCalculationStrategy feeCalculationStrategy) {
      this.feeCalculationStrategy = Optional.of(feeCalculationStrategy);
      return self();
   }

   // -------------------------------------- getters ---------------------------------------//

   /**
    * @return the type
    */
   public TransactionType getType() {
      return type;
   }

   /**
    * @return the version
    */
   public Integer getVersion() {
      return version;
   }

   /**
    * @return the networkType
    */
   public NetworkType getNetworkType() {
      return networkType;
   }

   /**
    * @return the maxFee
    */
   public Optional<BigInteger> getMaxFee() {
      return maxFee;
   }

   /**
    * @return the signature
    */
   public Optional<String> getSignature() {
      return signature;
   }

   /**
    * @return the signer
    */
   public Optional<PublicAccount> getSigner() {
      return signer;
   }

   /**
    * @return the transactionInfo
    */
   public Optional<TransactionInfo> getTransactionInfo() {
      return transactionInfo;
   }

   /**
    * @return the feeCalculationStrategy
    */
   public Optional<FeeCalculationStrategy> getFeeCalculationStrategy() {
      return feeCalculationStrategy;
   }

   /**
    * @return the deadline
    */
   public TransactionDeadline getDeadline() {
      final Optional<BigInteger> duration = deadlineMillis;
      return deadline.orElseGet(() -> DeadlineRaw.startNow(
            duration.orElseThrow(() -> new IllegalStateException("fixed or millis deadline needs to be specified"))));
   }

}
