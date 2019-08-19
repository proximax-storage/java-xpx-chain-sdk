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
 * Transaction requesting upgrade of blockchain version
 */
public class BlockchainUpgradeTransaction extends Transaction {

   private final BigInteger upgradePeriod;
   private final BlockchainVersion newVersion;
   
   private final Schema schema = new BlockchainUpgradeTransactionSchema();

   /**
    * @param networkType
    * @param version
    * @param deadline
    * @param fee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param upgradePeriod
    * @param newVersion
    */
   public BlockchainUpgradeTransaction(NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger fee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, BigInteger upgradePeriod, BlockchainVersion newVersion) {
      super(TransactionType.BLOCKCHAIN_UPGRADE, networkType, version, deadline, fee, signature, signer, transactionInfo);
      // basic validations
      Validate.notNull(upgradePeriod);
      Validate.notNull(newVersion);
      // make assignments
      this.upgradePeriod = upgradePeriod;
      this.newVersion = newVersion;
   }

   public static BlockchainUpgradeTransaction create(BigInteger upgradePeriod, BlockchainVersion newVersion, TransactionDeadline deadline, NetworkType networkType) {
      return new BlockchainUpgradeTransaction(networkType, TransactionVersion.BLOCKCHAIN_UPGRADE.getValue(), deadline, BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), upgradePeriod, newVersion);
   }
   
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

   @Override
   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      
      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Vectors
      int signatureOffset = CatapultUpgradeTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = CatapultUpgradeTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = CatapultUpgradeTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = CatapultUpgradeTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      int upgradePeriodOffset = CatapultUpgradeTransactionBuffer.createUpgradePeriodVector(builder, UInt64Utils.fromBigInteger(getUpgradePeriod()));
      int newCatapultVersionOffset = CatapultUpgradeTransactionBuffer.createNewCatapultVersionVector(builder, UInt64Utils.fromBigInteger(getNewVersion().getVersionValue()));
      
      // header, 2 uint64
      int size = HEADER_SIZE + 8 + 8;

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
}
