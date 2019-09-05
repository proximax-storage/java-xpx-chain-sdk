/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.CatapultUpgradeTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Transaction requesting upgrade of blockchain version. Transaction defines version of nodes which will be able to
 * participate on generation of next block. Transaction becomes valid after upgradePeriod blocks get generated.
 * 
 * e.g. if upgrade transaction to version 1.2.3.4 with upgrade period 1000 blocks is accepted in block 50, then starting
 * block 1050 all nodes that want to participate on generation of new block need to have version 1.2.3.4
 * 
 * By default the upgrade period is required to be at least 360
 */
public class BlockchainUpgradeTransaction extends Transaction {
   private final Schema schema = new BlockchainUpgradeTransactionSchema();

   private final BigInteger upgradePeriod;
   private final BlockchainVersion newVersion;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#BLOCKCHAIN_UPGRADE} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param upgradePeriod period after version is enforced. Default minimum is 360
    * @param newVersion new node version which will be enforced after upgrade period elapses
    */
   public BlockchainUpgradeTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, BigInteger upgradePeriod, BlockchainVersion newVersion) {
      super(EntityType.BLOCKCHAIN_UPGRADE, networkType, version, deadline, maxFee, signature, signer,
            transactionInfo);
      // validations
      Validate.notNull(upgradePeriod, "upgrade period is mandatory");
      Validate.notNull(newVersion, "new version is required");
      // assignments
      this.upgradePeriod = upgradePeriod;
      this.newVersion = newVersion;
   }

   /**
    * @return the upgradePeriod after which the version becomes required for block generation
    */
   public BigInteger getUpgradePeriod() {
      return upgradePeriod;
   }

   /**
    * @return the newVersion new version to which the upgrade is requested
    */
   public BlockchainVersion getNewVersion() {
      return newVersion;
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Vectors
      int signatureOffset = CatapultUpgradeTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = CatapultUpgradeTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = CatapultUpgradeTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = CatapultUpgradeTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int upgradePeriodOffset = CatapultUpgradeTransactionBuffer.createUpgradePeriodVector(builder,
            UInt64Utils.fromBigInteger(getUpgradePeriod()));
      int newCatapultVersionOffset = CatapultUpgradeTransactionBuffer.createNewCatapultVersionVector(builder,
            UInt64Utils.fromBigInteger(getNewVersion().getVersionValue()));

      int size = getSerializedSize();

      CatapultUpgradeTransactionBuffer.startCatapultUpgradeTransactionBuffer(builder);
      CatapultUpgradeTransactionBuffer.addDeadline(builder, deadlineOffset);
      CatapultUpgradeTransactionBuffer.addMaxFee(builder, feeOffset);
      CatapultUpgradeTransactionBuffer.addSigner(builder, signerOffset);
      CatapultUpgradeTransactionBuffer.addSignature(builder, signatureOffset);
      CatapultUpgradeTransactionBuffer.addSize(builder, size);
      CatapultUpgradeTransactionBuffer.addType(builder, getType().getValue());
      CatapultUpgradeTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      CatapultUpgradeTransactionBuffer.addUpgradePeriod(builder, upgradePeriodOffset);
      CatapultUpgradeTransactionBuffer.addNewCatapultVersion(builder, newCatapultVersionOffset);

      int codedTransaction = CatapultUpgradeTransactionBuffer.endCatapultUpgradeTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate payload size excluding header
    * 
    * @return size
    */
   public static int calculatePayloadSize() {
      // period offset + version
      return 8 + 8;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize();
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new BlockchainUpgradeTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getUpgradePeriod(), getNewVersion());
   }
}
