/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.CosignatoryModificationBuffer;
import io.proximax.sdk.gen.buffers.ModifyContractTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Contract modification Transaction
 */
public class ModifyContractTransaction extends Transaction {
   
   private final ModifyContractTransactionSchema schema = new ModifyContractTransactionSchema();
   
   private final BigInteger durationDelta;
   private final String contentHash;
   
   private final List<MultisigCosignatoryModification> customersModifications;
   private final List<MultisigCosignatoryModification> executorsModifications;
   private final List<MultisigCosignatoryModification> verifiersModifications;
   
   /**
    * @param networkType
    * @param deadline
    * @param maxFee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param durationDelta
    * @param contentHash
    * @param customersModifications
    * @param executorsModifications
    * @param verifiersModifications
    */
   public ModifyContractTransaction(NetworkType networkType,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, BigInteger durationDelta, String contentHash,
         List<MultisigCosignatoryModification> customersModifications,
         List<MultisigCosignatoryModification> executorsModifications,
         List<MultisigCosignatoryModification> verifiersModifications) {
      super(TransactionType.MODIFY_CONTRACT, networkType, TransactionVersion.MODIFY_CONTRACT.getValue(), deadline, maxFee, signature, signer, transactionInfo);
      this.durationDelta = durationDelta;
      this.contentHash = contentHash;
      this.customersModifications = customersModifications;
      this.executorsModifications = executorsModifications;
      this.verifiersModifications = verifiersModifications;
   }

   public static ModifyContractTransaction create(TransactionDeadline deadline, BigInteger maxFee, BigInteger durationDelta, String contentHash,
         List<MultisigCosignatoryModification> customersModifications,
         List<MultisigCosignatoryModification> executorsModifications,
         List<MultisigCosignatoryModification> verifiersModifications, NetworkType networkType) {
      return new ModifyContractTransaction(networkType, deadline, maxFee, Optional.empty(), Optional.empty(),
            Optional.empty(), durationDelta, contentHash, customersModifications, executorsModifications,
            verifiersModifications);
   }
   
   @Override
   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      int version = (int) Long.parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

      // Create Modifications
      int[] customersBuffer = createCosigModificationVector(builder, customersModifications);
      int[] executorsBuffer = createCosigModificationVector(builder, executorsModifications);
      int[] verifiersBuffer = createCosigModificationVector(builder, verifiersModifications);

      // serialize content hash to byte array
      byte[] contentHashBytes = contentHash.getBytes(StandardCharsets.UTF_8);
      
      // Create Vectors
      int signatureOffset = ModifyContractTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = ModifyContractTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = ModifyContractTransactionBuffer.createDeadlineVector(builder, UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = ModifyContractTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      int durationDeltaOffset = ModifyContractTransactionBuffer.createDurationDeltaVector(builder, UInt64Utils.fromBigInteger(durationDelta));
      int hashOffset = ModifyContractTransactionBuffer.createHashVector(builder, contentHashBytes);
      int customersOffset = ModifyContractTransactionBuffer.createCustomersVector(builder, customersBuffer);
      int executorsOffset = ModifyContractTransactionBuffer.createExecutorsVector(builder, executorsBuffer);
      int verifiersOffset = ModifyContractTransactionBuffer.createVerifiersVector(builder, verifiersBuffer);

      // standard fields + duration delta + hash length + 3 modification lengths + 33 bytes per every modification
      int totalSize = 120 + 8 + contentHashBytes.length + 3 + 33 * (customersBuffer.length + executorsBuffer.length + verifiersBuffer.length);

      // standard transaction information
      ModifyContractTransactionBuffer.startModifyContractTransactionBuffer(builder);
      ModifyContractTransactionBuffer.addSize(builder, totalSize);
      ModifyContractTransactionBuffer.addSignature(builder, signatureOffset);
      ModifyContractTransactionBuffer.addSigner(builder, signerOffset);
      ModifyContractTransactionBuffer.addVersion(builder, version);
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

      // serialize the transaction
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized form has incorrect length. Was %d indicated %d", output.length, totalSize);
      return output;
   }
   
   /**
    * create vector for cosignatory modifications
    * 
    * @param builder the flatbuffer builder
    * @param modifications list of cosignatory modifications
    * @return array of buffer offsets for modification
    */
   private static int[] createCosigModificationVector(FlatBufferBuilder builder, List<MultisigCosignatoryModification> modifications) {
      // create array for vector offsets
      int[] modificationsBuffers = new int[modifications.size()];
      // go over all modifications and prepare the buffers
      for (int i = 0; i < modifications.size(); ++i) {
          MultisigCosignatoryModification multisigCosignatoryModification = modifications.get(i);
          // serialize public key
          byte[] cosignatoryPublicKeyBytes = HexEncoder.getBytes(multisigCosignatoryModification.getCosignatoryPublicAccount().getPublicKey());
          // get vector offset for the public key
          int cosignatoryPublicKeyOffset = CosignatoryModificationBuffer.createCosignatoryPublicKeyVector(builder, cosignatoryPublicKeyBytes);
          // create buffer and add it to the array
          CosignatoryModificationBuffer.startCosignatoryModificationBuffer(builder);
          CosignatoryModificationBuffer.addType(builder, multisigCosignatoryModification.getType().getValue());
          CosignatoryModificationBuffer.addCosignatoryPublicKey(builder, cosignatoryPublicKeyOffset);
          modificationsBuffers[i] = CosignatoryModificationBuffer.endCosignatoryModificationBuffer(builder);
      }
      return modificationsBuffers;
   }
}
