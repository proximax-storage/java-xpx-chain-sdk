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

import io.proximax.core.utils.StringUtils;
import io.proximax.sdk.gen.buffers.CatapultConfigTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Transaction defining configuration of the network. Specified configuration will become activated after
 * applyHeightDelta blocks get created after transaciton is announced
 */
public class BlockchainConfigTransaction extends Transaction {
   private final Schema schema = new BlockchainConfigTransactionSchema();

   private final BigInteger applyHeightDelta;
   private final String blockchainConfig;
   private final String supportedEntityVersions;

   /**
    * @param networkType
    * @param version
    * @param deadline
    * @param fee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param applyHeightDelta
    * @param blockchainConfig
    * @param supportedEntityVersions
    */
   public BlockchainConfigTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger fee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, BigInteger applyHeightDelta, String blockchainConfig,
         String supportedEntityVersions) {
      super(TransactionType.BLOCKCHAIN_CONFIG, networkType, version, deadline, fee, signature, signer, transactionInfo);
      // basic input validations
      Validate.notNull(applyHeightDelta);
      Validate.notNull(blockchainConfig);
      Validate.notNull(supportedEntityVersions);
      // make the assignments
      this.applyHeightDelta = applyHeightDelta;
      this.blockchainConfig = blockchainConfig;
      this.supportedEntityVersions = supportedEntityVersions;
   }

   /**
    * @param applyHeightDelta number of blocks after which the configuration becomes valid
    * @param blockchainConfig string matching the content of the configuration file
    * @param supportedEntityVersions JSON string with supported entity versions
    * @param networkType type of the network
    * @param deadline transaction deadline
    * @return the transaction instance
    */
   public static BlockchainConfigTransaction create(BigInteger applyHeightDelta, String blockchainConfig,
         String supportedEntityVersions, NetworkType networkType, TransactionDeadline deadline) {
      return new BlockchainConfigTransaction(networkType, TransactionVersion.BLOCKCHAIN_CONFIG.getValue(), deadline,
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), applyHeightDelta, blockchainConfig,
            supportedEntityVersions);
   }

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

   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      byte[] configBytes = StringUtils.getBytes(getBlockchainConfig());
      byte[] entityBytes = StringUtils.getBytes(getSupportedEntityVersions());

      // Create Vectors
      int signatureOffset = CatapultConfigTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = CatapultConfigTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = CatapultConfigTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = CatapultConfigTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      // specific fields
      int applyHeightOffset = CatapultConfigTransactionBuffer.createApplyHeightDeltaVector(builder,
            UInt64Utils.fromBigInteger(getApplyHeightDelta()));
      int confgOffset = CatapultConfigTransactionBuffer.createBlockChainConfigVector(builder, configBytes);
      int entityOffset = CatapultConfigTransactionBuffer.createSupportedEntityVersionsVector(builder, entityBytes);

      // header, 2 uint64 and int
      int size = HEADER_SIZE + 8 + 2 + 2 + configBytes.length + entityBytes.length;

      CatapultConfigTransactionBuffer.startCatapultConfigTransactionBuffer(builder);
      CatapultConfigTransactionBuffer.addDeadline(builder, deadlineOffset);
      CatapultConfigTransactionBuffer.addMaxFee(builder, feeOffset);
      CatapultConfigTransactionBuffer.addSigner(builder, signerOffset);
      CatapultConfigTransactionBuffer.addSignature(builder, signatureOffset);
      CatapultConfigTransactionBuffer.addSize(builder, size);
      CatapultConfigTransactionBuffer.addType(builder, getType().getValue());
      CatapultConfigTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      CatapultConfigTransactionBuffer.addApplyHeightDelta(builder, applyHeightOffset);
      CatapultConfigTransactionBuffer.addBlockChainConfigSize(builder, configBytes.length);
      CatapultConfigTransactionBuffer.addBlockChainConfig(builder, confgOffset);
      CatapultConfigTransactionBuffer.addSupportedEntityVersionsSize(builder, entityBytes.length);
      CatapultConfigTransactionBuffer.addSupportedEntityVersions(builder, entityOffset);

      int codedTransaction = CatapultConfigTransactionBuffer.endCatapultConfigTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }
}
