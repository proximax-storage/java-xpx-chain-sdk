/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.core.utils.StringUtils;
import io.proximax.sdk.model.transaction.BlockchainConfigTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link BlockchainConfigTransactionBuilder}
 */
public class BlockchainConfigTransactionBuilder
      extends TransactionBuilder<BlockchainConfigTransactionBuilder, BlockchainConfigTransaction> {

   private BigInteger applyHeightDelta;
   private String blockchainConfig;
   private String supportedEntityVersions;

   public BlockchainConfigTransactionBuilder() {
      super(TransactionType.BLOCKCHAIN_CONFIG, TransactionVersion.BLOCKCHAIN_CONFIG.getValue());
   }

   @Override
   protected BlockchainConfigTransactionBuilder self() {
      return this;
   }

   @Override
   public BlockchainConfigTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            BlockchainConfigTransaction.calculatePayloadSize(StringUtils.getBytes(getBlockchainConfig()).length,
                  StringUtils.getBytes(getSupportedEntityVersions()).length)));
      // create transaction instance
      return new BlockchainConfigTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getApplyHeightDelta(), getBlockchainConfig(),
            getSupportedEntityVersions());
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * number of blocks after which specified configuration will become valid
    * 
    * @param applyHeightDelta number of blocks
    * @return self
    */
   public BlockchainConfigTransactionBuilder setApplyHeightDelta(BigInteger applyHeightDelta) {
      this.applyHeightDelta = applyHeightDelta;
      return self();
   }

   /**
    * configuration in the format of property file
    * 
    * @param blockchainConfig the configuration
    * @return self
    */
   public BlockchainConfigTransactionBuilder setBlockchainConfig(String blockchainConfig) {
      this.blockchainConfig = blockchainConfig;
      return self();
   }

   /**
    * JSON string representing supported entity versions
    * 
    * @param supportedEntityVersions JSON string
    * @return self
    */
   public BlockchainConfigTransactionBuilder setSupportedEntityVersions(String supportedEntityVersions) {
      this.supportedEntityVersions = supportedEntityVersions;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the applyHeightDelta
    */
   public BigInteger getApplyHeightDelta() {
      return applyHeightDelta;
   }

   /**
    * @return the blockchainConfig
    */
   public String getBlockchainConfig() {
      return blockchainConfig;
   }

   /**
    * @return the supportedEntityVersions
    */
   public String getSupportedEntityVersions() {
      return supportedEntityVersions;
   }

   // -------------------------------------- convenience --------------------------------------------//

}
