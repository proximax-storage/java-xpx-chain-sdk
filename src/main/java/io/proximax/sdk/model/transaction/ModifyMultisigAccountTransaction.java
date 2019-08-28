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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.CosignatoryModificationBuffer;
import io.proximax.sdk.gen.buffers.ModifyMultisigAccountTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Modify multisig account transactions are part of the NEM's multisig account system. A modify multisig account
 * transaction holds an array of multisig cosignatory modifications, min number of signatures to approve a transaction
 * and a min number of signatures to remove a cosignatory.
 */
public class ModifyMultisigAccountTransaction extends Transaction {
   private final Schema schema = new ModifyMultisigAccountTransactionSchema();

   private final int minApprovalDelta;
   private final int minRemovalDelta;
   private final List<MultisigCosignatoryModification> modifications;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link TransactionVersion#MODIFY_MULTISIG_ACCOUNT} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param minApprovalDelta minimum number of cosignatures for transaction approval
    * @param minRemovalDelta minimum number of cosignatures for cosignatory removal
    * @param modifications
    */
   public ModifyMultisigAccountTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, int minApprovalDelta, int minRemovalDelta,
         List<MultisigCosignatoryModification> modifications) {
      super(TransactionType.MODIFY_MULTISIG_ACCOUNT, networkType, version, deadline, maxFee, signature, signer,
            transactionInfo);
      // validations
      Validate.notNull(modifications, "Modifications must not be null");
      // assignments
      this.minApprovalDelta = minApprovalDelta;
      this.minRemovalDelta = minRemovalDelta;
      this.modifications = Collections.unmodifiableList(modifications);
   }

   /**
    * Return number of signatures needed to approve a transaction. If we are modifying and existing multi-signature
    * account this indicates the relative change of the minimum cosignatories.
    *
    * @return int
    */
   public int getMinApprovalDelta() {
      return minApprovalDelta;
   }

   /**
    * Return number of signatures needed to remove a cosignatory. If we are modifying and existing multi-signature
    * account this indicates the relative change of the minimum cosignatories.
    *
    * @return int
    */
   public int getMinRemovalDelta() {
      return minRemovalDelta;
   }

   /**
    * The List of cosigner accounts added or removed from the multi-signature account.
    *
    * @return modifications in this transaction
    */
   public List<MultisigCosignatoryModification> getModifications() {
      return modifications;
   }

   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // Create Modifications
      int[] modificationsBuffers = new int[getModifications().size()];
      for (int i = 0; i < getModifications().size(); ++i) {
         MultisigCosignatoryModification multisigCosignatoryModification = getModifications().get(i);
         byte[] byteCosignatoryPublicKey = HexEncoder
               .getBytes(multisigCosignatoryModification.getCosignatoryPublicAccount().getPublicKey());
         int cosignatoryPublicKey = CosignatoryModificationBuffer.createCosignatoryPublicKeyVector(builder,
               byteCosignatoryPublicKey);
         CosignatoryModificationBuffer.startCosignatoryModificationBuffer(builder);
         CosignatoryModificationBuffer.addType(builder, multisigCosignatoryModification.getType().getValue());
         CosignatoryModificationBuffer.addCosignatoryPublicKey(builder, cosignatoryPublicKey);
         modificationsBuffers[i] = CosignatoryModificationBuffer.endCosignatoryModificationBuffer(builder);
      }

      // Create Vectors
      int signatureVector = ModifyMultisigAccountTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = ModifyMultisigAccountTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = ModifyMultisigAccountTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = ModifyMultisigAccountTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int modificationsVector = ModifyMultisigAccountTransactionBuffer.createModificationsVector(builder,
            modificationsBuffers);

      int size = getSerializedSize();

      ModifyMultisigAccountTransactionBuffer.startModifyMultisigAccountTransactionBuffer(builder);
      ModifyMultisigAccountTransactionBuffer.addSize(builder, size);
      ModifyMultisigAccountTransactionBuffer.addSignature(builder, signatureVector);
      ModifyMultisigAccountTransactionBuffer.addSigner(builder, signerVector);
      ModifyMultisigAccountTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      ModifyMultisigAccountTransactionBuffer.addType(builder, getType().getValue());
      ModifyMultisigAccountTransactionBuffer.addMaxFee(builder, feeVector);
      ModifyMultisigAccountTransactionBuffer.addDeadline(builder, deadlineVector);

      ModifyMultisigAccountTransactionBuffer.addMinApprovalDelta(builder, (byte) getMinApprovalDelta());
      ModifyMultisigAccountTransactionBuffer.addMinRemovalDelta(builder, (byte) getMinRemovalDelta());
      ModifyMultisigAccountTransactionBuffer.addNumModifications(builder, getModifications().size());
      ModifyMultisigAccountTransactionBuffer.addModifications(builder, modificationsVector);

      int codedTransaction = ModifyMultisigAccountTransactionBuffer.endModifyMultisigAccountTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate size of payload without header
    * 
    * @param modificationCount number of multisig modifications
    * @return size
    */
   public static int calculatePayloadSize(int modificationCount) {
      // min approval, min removal, mod count, mod (type, pub key) * count
      return 1 + 1 + 1 + (1 + 32) * modificationCount;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getModifications().size());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new ModifyMultisigAccountTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
            getSignature(), Optional.of(signer), getTransactionInfo(), getMinApprovalDelta(), getMinRemovalDelta(),
            getModifications());
   }
}
