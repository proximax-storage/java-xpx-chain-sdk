/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.CosignatoryModificationBuffer;
import io.proximax.sdk.gen.buffers.ModifyContractTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * <p>
 * Contract modification transaction can be used to create or change contract
 * </p>
 * <p>
 * Contract changes contract account to N of N multisignature account where cosignatories are the verifiers
 * </p>
 */
public class ModifyContractTransaction extends Transaction {
   private final ModifyContractTransactionSchema schema = new ModifyContractTransactionSchema();

   private final BigInteger durationDelta;
   private final String contentHash;

   private final List<MultisigCosignatoryModification> customersModifications;
   private final List<MultisigCosignatoryModification> executorsModifications;
   private final List<MultisigCosignatoryModification> verifiersModifications;

   /**
    * create new contract modification transaction
    * 
    * @param networkType network type
    * @param transactionVersion version of the transaction
    * @param deadline transaction deadline
    * @param maxFee maximum fee
    * @param signature optional transaction signature
    * @param signer optional signer of the transaction
    * @param transactionInfo optional transaction info
    * @param durationDelta change of the contract duration (+/-)
    * @param contentHash contract content hash
    * @param customersModifications changes to the list of customers
    * @param executorsModifications changes to the list of executors
    * @param verifiersModifications changes to the list of verifiers
    */
   public ModifyContractTransaction(NetworkType networkType, Integer transactionVersion, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, BigInteger durationDelta, String contentHash,
         List<MultisigCosignatoryModification> customersModifications,
         List<MultisigCosignatoryModification> executorsModifications,
         List<MultisigCosignatoryModification> verifiersModifications) {
      super(EntityType.MODIFY_CONTRACT, networkType, transactionVersion, deadline, maxFee, signature, signer,
            transactionInfo);
      // validations
      Validate.notNull(durationDelta, "durationDelta can not be null");
      Validate.notNull(contentHash, "contentHash can not be null");
      Validate.notNull(customersModifications, "customersModifications can not be null");
      Validate.notNull(executorsModifications, "executorsModifications can not be null");
      Validate.notNull(verifiersModifications, "verifiersModifications can not be null");
      // assignments
      this.durationDelta = durationDelta;
      this.contentHash = contentHash;
      this.customersModifications = Collections.unmodifiableList(customersModifications);
      this.executorsModifications = Collections.unmodifiableList(executorsModifications);
      this.verifiersModifications = Collections.unmodifiableList(verifiersModifications);
   }

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

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Modifications
      int[] customersBuffer = createCosigModificationVector(builder, getCustomersModifications());
      int[] executorsBuffer = createCosigModificationVector(builder, getExecutorsModifications());
      int[] verifiersBuffer = createCosigModificationVector(builder, getVerifiersModifications());

      // serialize content hash to byte array
      byte[] contentHashBytes = contentHashToBytes(getContentHash());

      // Create Vectors
      int signatureOffset = ModifyContractTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = ModifyContractTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = ModifyContractTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = ModifyContractTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int durationDeltaOffset = ModifyContractTransactionBuffer.createDurationDeltaVector(builder,
            UInt64Utils.fromBigInteger(getDurationDelta()));
      int hashOffset = ModifyContractTransactionBuffer.createHashVector(builder, contentHashBytes);
      int customersOffset = ModifyContractTransactionBuffer.createCustomersVector(builder, customersBuffer);
      int executorsOffset = ModifyContractTransactionBuffer.createExecutorsVector(builder, executorsBuffer);
      int verifiersOffset = ModifyContractTransactionBuffer.createVerifiersVector(builder, verifiersBuffer);

      int totalSize = getSerializedSize();

      // standard transaction information
      ModifyContractTransactionBuffer.startModifyContractTransactionBuffer(builder);
      ModifyContractTransactionBuffer.addSize(builder, totalSize);
      ModifyContractTransactionBuffer.addSignature(builder, signatureOffset);
      ModifyContractTransactionBuffer.addSigner(builder, signerOffset);
      ModifyContractTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      ModifyContractTransactionBuffer.addType(builder, getType().getValue());
      ModifyContractTransactionBuffer.addMaxFee(builder, feeOffset);
      ModifyContractTransactionBuffer.addDeadline(builder, deadlineOffset);
      // transaction-specific information
      ModifyContractTransactionBuffer.addDurationDelta(builder, durationDeltaOffset);
      ModifyContractTransactionBuffer.addHash(builder, hashOffset);
      ModifyContractTransactionBuffer.addNumCustomers(builder, customersBuffer.length);
      ModifyContractTransactionBuffer.addNumExecutors(builder, executorsBuffer.length);
      ModifyContractTransactionBuffer.addNumVerifiers(builder, verifiersBuffer.length);
      ModifyContractTransactionBuffer.addCustomers(builder, customersOffset);
      ModifyContractTransactionBuffer.addExecutors(builder, executorsOffset);
      ModifyContractTransactionBuffer.addVerifiers(builder, verifiersOffset);

      // finalize the transaction
      int codedTransaction = ModifyContractTransactionBuffer.endModifyContractTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * serialize hexadecimal string with content hash to byte array
    * 
    * @param hash hexadecimal string
    * @return byte array
    */
   private static byte[] contentHashToBytes(String hash) {
      return HexEncoder.getBytes(hash);
   }

   /**
    * create vector for cosignatory modifications
    * 
    * @param builder the flatbuffer builder
    * @param modifications list of cosignatory modifications
    * @return array of buffer offsets for modification
    */
   private static int[] createCosigModificationVector(FlatBufferBuilder builder,
         List<MultisigCosignatoryModification> modifications) {
      // create array for vector offsets
      int[] modificationsBuffers = new int[modifications.size()];
      // go over all modifications and prepare the buffers
      for (int i = 0; i < modifications.size(); ++i) {
         MultisigCosignatoryModification multisigCosignatoryModification = modifications.get(i);
         // serialize public key
         byte[] cosignatoryPublicKeyBytes = HexEncoder
               .getBytes(multisigCosignatoryModification.getCosignatoryPublicAccount().getPublicKey());
         // get vector offset for the public key
         int cosignatoryPublicKeyOffset = CosignatoryModificationBuffer.createCosignatoryPublicKeyVector(builder,
               cosignatoryPublicKeyBytes);
         // create buffer and add it to the array
         CosignatoryModificationBuffer.startCosignatoryModificationBuffer(builder);
         CosignatoryModificationBuffer.addType(builder, multisigCosignatoryModification.getType().getValue());
         CosignatoryModificationBuffer.addCosignatoryPublicKey(builder, cosignatoryPublicKeyOffset);
         modificationsBuffers[i] = CosignatoryModificationBuffer.endCosignatoryModificationBuffer(builder);
      }
      return modificationsBuffers;
   }

   public static int calculatePayloadSize(String contentHash, int customerCount, int executorsCount,
         int verifiersCount) {
      // duration delta + hash length + 3 modification lengths + 33 bytes per every modification (1 byte mod type + 32
      // bytes public key)
      return 8 + contentHashToBytes(contentHash).length + 3 + 33 * (customerCount + executorsCount + verifiersCount);
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getContentHash(),
            getCustomersModifications().size(),
            getExecutorsModifications().size(),
            getVerifiersModifications().size());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new ModifyContractTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
            Optional.of(signer), getTransactionInfo(), getDurationDelta(), getContentHash(),
            getCustomersModifications(), getExecutorsModifications(), getVerifiersModifications());
   }
}
