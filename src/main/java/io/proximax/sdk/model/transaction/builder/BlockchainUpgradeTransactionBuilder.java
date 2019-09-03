/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.transaction.BlockchainUpgradeTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link BlockchainUpgradeTransaction}
 */
public class BlockchainUpgradeTransactionBuilder
      extends TransactionBuilder<BlockchainUpgradeTransactionBuilder, BlockchainUpgradeTransaction> {

   private BigInteger upgradePeriod;
   private BlockchainVersion newVersion;

   public BlockchainUpgradeTransactionBuilder() {
      super(TransactionType.BLOCKCHAIN_UPGRADE, TransactionVersion.BLOCKCHAIN_UPGRADE.getValue());
      // defaults
   }

   @Override
   protected BlockchainUpgradeTransactionBuilder self() {
      return this;
   }

   @Override
   public BlockchainUpgradeTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(BlockchainUpgradeTransaction.calculatePayloadSize()));
      // create transaction instance
      return new BlockchainUpgradeTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getUpgradePeriod(), getNewVersion());
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * number of blocks after which the new version will be required if node wants to generate new block
    * 
    * @param upgradePeriod number of blocks
    * @return self
    */
   public BlockchainUpgradeTransactionBuilder upgradePeriod(BigInteger upgradePeriod) {
      this.upgradePeriod = upgradePeriod;
      return self();
   }

   /**
    * new blockchain version which will eventually need to be matched by node versions
    * 
    * @param newVersion new blockchain version
    * @return self
    */
   public BlockchainUpgradeTransactionBuilder newVersion(BlockchainVersion newVersion) {
      this.newVersion = newVersion;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the upgradePeriod
    */
   public BigInteger getUpgradePeriod() {
      return upgradePeriod;
   }

   /**
    * @return the newVersion
    */
   public BlockchainVersion getNewVersion() {
      return newVersion;
   }

   // -------------------------------------- convenience --------------------------------------------//

}
