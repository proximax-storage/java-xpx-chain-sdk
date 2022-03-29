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
import io.proximax.sdk.model.network.NetworkType;
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
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#BLOCKCHAIN_CONFIG} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param applyHeightDelta number of blocks after which the configuration becomes valid
    * @param blockchainConfig string matching the content of the configuration file
    * @param supportedEntityVersions JSON string with supported entity versions
    */
   public BlockchainConfigTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, BigInteger applyHeightDelta, String blockchainConfig,
         String supportedEntityVersions) {
      super(EntityType.BLOCKCHAIN_CONFIG, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
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

   protected byte[] generateBytes() {
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
      int feeOffset = CatapultConfigTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      // specific fields
      int applyHeightOffset = CatapultConfigTransactionBuffer.createApplyHeightDeltaVector(builder,
            UInt64Utils.fromBigInteger(getApplyHeightDelta()));
      int confgOffset = CatapultConfigTransactionBuffer.createBlockChainConfigVector(builder, configBytes);
      int entityOffset = CatapultConfigTransactionBuffer.createSupportedEntityVersionsVector(builder, entityBytes);

      int size = getSerializedSize();

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

   /**
    * calculate the payload size excluding the header
    * 
    * @param configBytesLength number of bytes of configuration
    * @param entityBytesLength number of bytes of supported entities
    * @return the size
    */
   public static int calculatePayloadSize(int configBytesLength, int entityBytesLength) {
      // height offset + size of config + config + size of entities + entities
      return 8 + 2 + configBytesLength + 2 + entityBytesLength;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(StringUtils.getBytes(getBlockchainConfig()).length,
            StringUtils.getBytes(getSupportedEntityVersions()).length);
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new BlockchainConfigTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
            Optional.of(signer), getTransactionInfo(), getApplyHeightDelta(), getBlockchainConfig(),
            getSupportedEntityVersions());
   }
}
