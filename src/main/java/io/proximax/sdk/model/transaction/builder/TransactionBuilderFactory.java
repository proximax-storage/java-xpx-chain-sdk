/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * Central factory to instantiate transaction builders for all transaction types
 */
public class TransactionBuilderFactory {
   private NetworkType networkType;
   private BigInteger deadlineMillis;
   private FeeCalculationStrategy feeCalculationStrategy;

   /**
    * @return the networkType
    */
   public NetworkType getNetworkType() {
      return networkType;
   }

   /**
    * @param networkType the networkType to set
    * @return this factory
    */
   public TransactionBuilderFactory setNetworkType(NetworkType networkType) {
      this.networkType = networkType;
      return this;
   }

   /**
    * @return the deadline in milliseconds
    */
   public BigInteger getDeadlineMillis() {
      return deadlineMillis;
   }

   /**
    * set default builder transaction deadline specified as milliseconds elapsed from the time when
    * {@link TransactionBuilder#build()} is invoked
    * 
    * @param deadlineMillis the deadlineMillis to set
    * @return this factory
    */
   public TransactionBuilderFactory setDeadlineMillis(BigInteger deadlineMillis) {
      this.deadlineMillis = deadlineMillis;
      return this;
   }

   /**
    * @return the default fee calculation strategy
    */
   public FeeCalculationStrategy getFeeCalculationStrategy() {
      return feeCalculationStrategy;
   }

   /**
    * @param feeCalculationStrategy the feeCalculationStrategy to set
    * @return this factory
    */
   public TransactionBuilderFactory setFeeCalculationStrategy(FeeCalculationStrategy feeCalculationStrategy) {
      this.feeCalculationStrategy = feeCalculationStrategy;
      return this;
   }

   /**
    * initialize default values for the builder
    * 
    * @param builder
    */
   protected void initDefaults(TransactionBuilder<?, ?> builder) {
      builder.networkType(networkType);
      if (feeCalculationStrategy != null) {
         builder.feeCalculationStrategy(feeCalculationStrategy);
      }
      if (deadlineMillis != null) {
         builder.deadlineDuration(deadlineMillis);
      }
   }

   // ------------------------------ retrieve specific transaction builders ------------------------------- //
   /**
    * create builder for transfer transaction
    * 
    * @return the builder
    */
   public TransferTransactionBuilder transfer() {
      TransferTransactionBuilder builder = new TransferTransactionBuilder();
      initDefaults(builder);
      return builder;
   }
   
   /**
    * create builder for account link transaction
    * 
    * @return the builder
    */
   public AccountLinkTransactionBuilder accountLink() {
      AccountLinkTransactionBuilder builder = new AccountLinkTransactionBuilder();
      initDefaults(builder);
      return builder;
   }
   
   /**
    * create builder for aggregate bonded transaction
    * 
    * @return the builder
    */
   public AggregateTransactionBuilder aggregateBonded() {
      AggregateTransactionBuilder builder = AggregateTransactionBuilder.createBonded();
      initDefaults(builder);
      return builder;      
   }
   
   /**
    * create builder for aggregate complete transaction
    * 
    * @return the builder
    */
   public AggregateTransactionBuilder aggregateComplete() {
      AggregateTransactionBuilder builder = AggregateTransactionBuilder.createComplete();
      initDefaults(builder);
      return builder;      
   }
   
   /**
    * create builder for mosaic alias transaction
    * 
    * @return the builder
    */
   public AliasTransactionBuilder aliasMosaic() {
      AliasTransactionBuilder builder = AliasTransactionBuilder.createForMosaic();
      initDefaults(builder);
      return builder;      
   }
   
   /**
    * create builder for address alias transaction
    * 
    * @return the builder
    */
   public AliasTransactionBuilder aliasAddress() {
      AliasTransactionBuilder builder = AliasTransactionBuilder.createForAddress();
      initDefaults(builder);
      return builder;      
   }
   
   /**
    * create builder for lock funds transaction
    * 
    * @return the builder
    */
   public LockFundsTransactionBuilder lockFunds() {
      LockFundsTransactionBuilder builder = new LockFundsTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for register namespace transaction
    * 
    * @return the builder
    */
   public RegisterNamespaceTransactionBuilder registerNamespace() {
      RegisterNamespaceTransactionBuilder builder = new RegisterNamespaceTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for mosaic definition transaction
    * 
    * @return the builder
    */
   public MosaicDefinitionTransactionBuilder mosaicDefinition() {
      MosaicDefinitionTransactionBuilder builder = new MosaicDefinitionTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for mosaic supply change transaction
    * 
    * @return the builder
    */
   public MosaicSupplyChangeTransactionBuilder mosaicSupplyChange() {
      MosaicSupplyChangeTransactionBuilder builder = new MosaicSupplyChangeTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for blockchain config transaction
    * 
    * @return the builder
    */
   public BlockchainConfigTransactionBuilder blockchainConfig() {
      BlockchainConfigTransactionBuilder builder = new BlockchainConfigTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for blockchain upgrade transaction
    * 
    * @return the builder
    */
   public BlockchainUpgradeTransactionBuilder blockchainUpgrade() {
      BlockchainUpgradeTransactionBuilder builder = new BlockchainUpgradeTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }
   
   /**
    * create builder for blockchain upgrade transaction
    * 
    * @return the builder
    */
   public ModifyContractTransactionBuilder modifyContract() {
      ModifyContractTransactionBuilder builder = new ModifyContractTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }
   
   /**
    * create builder for multisig account modification transaction
    * 
    * @return the builder
    */
   public ModifyMultisigAccountTransactionBuilder multisigModification() {
      ModifyMultisigAccountTransactionBuilder builder = new ModifyMultisigAccountTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }
   
   /**
    * create builder for secret lock transaction
    * 
    * @return the builder
    */
   public SecretLockTransactionBuilder secretLock() {
      SecretLockTransactionBuilder builder = new SecretLockTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }
   
   /**
    * create builder for secret proof transaction
    * 
    * @return the builder
    */
   public SecretProofTransactionBuilder secretProof() {
      SecretProofTransactionBuilder builder = new SecretProofTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for address account property modification transaction
    * 
    * @return the builder
    */
   public ModifyAccountPropertyAddressTransactionBuilder accountPropAddress() {
      ModifyAccountPropertyAddressTransactionBuilder builder = new ModifyAccountPropertyAddressTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for entity type account property modification transaction
    * 
    * @return the builder
    */
   public ModifyAccountPropertyEntityTransactionBuilder accountPropEntityType() {
      ModifyAccountPropertyEntityTransactionBuilder builder = new ModifyAccountPropertyEntityTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for mosaic account property modification transaction
    * 
    * @return the builder
    */
   public ModifyAccountPropertyMosaicTransactionBuilder accountPropMosaic() {
      ModifyAccountPropertyMosaicTransactionBuilder builder = new ModifyAccountPropertyMosaicTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }

   /**
    * create builder for metadata modification transaction
    * 
    * @return the builder
    */
   public ModifyMetadataTransactionBuilder modifyMetadata() {
      ModifyMetadataTransactionBuilder builder = new ModifyMetadataTransactionBuilder();
      initDefaults(builder);
      return builder; 
   }
}
