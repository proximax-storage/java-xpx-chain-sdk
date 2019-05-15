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
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.CosignatoryModificationBuffer;
import io.proximax.sdk.gen.buffers.TransferTransactionBuffer;
import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * Modify multisig account transactions are part of the NEM's multisig account system.
 * A modify multisig account transaction holds an array of multisig cosignatory modifications, min number of signatures to approve a transaction and a min number of signatures to remove a cosignatory.
 *
 * @since 1.0
 */
public class ModifyMultisigAccountTransaction extends Transaction {
    private final int minApprovalDelta;
    private final int minRemovalDelta;
    private final List<MultisigCosignatoryModification> modifications;
    private final Schema schema = new ModifyMultisigAccountTransactionSchema();

    public ModifyMultisigAccountTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, int minApprovalDelta, int minRemovalDelta, List<MultisigCosignatoryModification> modifications, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, version, deadline, fee, minApprovalDelta, minRemovalDelta, modifications, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    public ModifyMultisigAccountTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, int minApprovalDelta, int minRemovalDelta, List<MultisigCosignatoryModification> modifications) {
        this(networkType, version, deadline, fee, minApprovalDelta, minRemovalDelta, modifications, Optional.empty(), Optional.empty(), Optional.empty());
    }

    private ModifyMultisigAccountTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, int minApprovalDelta, int minRemovalDelta, List<MultisigCosignatoryModification> modifications, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(TransactionType.MODIFY_MULTISIG_ACCOUNT, networkType, version, deadline, fee, signature, signer, transactionInfo);
        Validate.notNull(modifications, "Modifications must not be null");
        this.minApprovalDelta = minApprovalDelta;
        this.minRemovalDelta = minRemovalDelta;
        this.modifications = modifications;
    }

    /**
     * Create a modify multisig account transaction object.
     *
     * @param deadline         The deadline to include the transaction.
     * @param minApprovalDelta The min approval relative change.
     * @param minRemovalDelta  The min removal relative change.
     * @param modifications    The list of modifications.
     * @param networkType      The network type.
     * @return {@link ModifyMultisigAccountTransaction}
     */

    public static ModifyMultisigAccountTransaction create(Deadline deadline, int minApprovalDelta, int minRemovalDelta, List<MultisigCosignatoryModification> modifications, NetworkType networkType) {
        return new ModifyMultisigAccountTransaction(networkType, 3, deadline, BigInteger.valueOf(0), minApprovalDelta, minRemovalDelta, modifications);
    }

    /**
     * Return number of signatures needed to approve a transaction.
     * If we are modifying and existing multi-signature account this indicates the relative change of the minimum cosignatories.
     *
     * @return int
     */
    public int getMinApprovalDelta() {
        return minApprovalDelta;
    }

    /**
     * Return number of signatures needed to remove a cosignatory.
     * If we are modifying and existing multi-signature account this indicates the relative change of the minimum cosignatories.
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

    byte[] generateBytes() {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        int[] fee = new int[]{0, 0};
        int version = (int) Long.parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

        // Create Modifications
        int[] modificationsBuffers = new int[modifications.size()];
        for (int i = 0; i < modifications.size(); ++i) {
            MultisigCosignatoryModification multisigCosignatoryModification = modifications.get(i);
            byte[] byteCosignatoryPublicKey = HexEncoder.getBytes(multisigCosignatoryModification.getCosignatoryPublicAccount().getPublicKey());
            int cosignatoryPublicKey = CosignatoryModificationBuffer.createCosignatoryPublicKeyVector(builder, byteCosignatoryPublicKey);
            CosignatoryModificationBuffer.startCosignatoryModificationBuffer(builder);
            CosignatoryModificationBuffer.addType(builder, multisigCosignatoryModification.getType().getValue());
            CosignatoryModificationBuffer.addCosignatoryPublicKey(builder, cosignatoryPublicKey);
            modificationsBuffers[i] = CosignatoryModificationBuffer.endCosignatoryModificationBuffer(builder);
        }

        // Create Vectors
        int signatureVector = MultisigAggregateModificationTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = MultisigAggregateModificationTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = MultisigAggregateModificationTransactionBuffer.createDeadlineVector(builder, UInt64Utils.fromBigInteger(deadlineBigInt));
        int feeVector = MultisigAggregateModificationTransactionBuffer.createFeeVector(builder, fee);
        int modificationsVector = TransferTransactionBuffer.createMosaicsVector(builder, modificationsBuffers);

        int fixSize = 123; // replace by the all numbers sum or add a comment explaining this

        MultisigAggregateModificationTransactionBuffer.startMultisigAggregateModificationTransactionBuffer(builder);
        MultisigAggregateModificationTransactionBuffer.addSize(builder, fixSize + (33 * modifications.size()));
        MultisigAggregateModificationTransactionBuffer.addSignature(builder, signatureVector);
        MultisigAggregateModificationTransactionBuffer.addSigner(builder, signerVector);
        MultisigAggregateModificationTransactionBuffer.addVersion(builder, version);
        MultisigAggregateModificationTransactionBuffer.addType(builder, getType().getValue());
        MultisigAggregateModificationTransactionBuffer.addFee(builder, feeVector);
        MultisigAggregateModificationTransactionBuffer.addDeadline(builder, deadlineVector);
        MultisigAggregateModificationTransactionBuffer.addMinApprovalDelta(builder, minApprovalDelta);
        MultisigAggregateModificationTransactionBuffer.addMinRemovalDelta(builder, minRemovalDelta);
        MultisigAggregateModificationTransactionBuffer.addNumModifications(builder, modifications.size());
        MultisigAggregateModificationTransactionBuffer.addModifications(builder, modificationsVector);

        int codedTransaction = MultisigAggregateModificationTransactionBuffer.endMultisigAggregateModificationTransactionBuffer(builder);
        builder.finish(codedTransaction);

        return schema.serialize(builder.sizedByteArray());
    }
}
