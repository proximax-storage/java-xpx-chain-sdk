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

package io.nem.sdk.model.transaction;

import com.google.flatbuffers.FlatBufferBuilder;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceType;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Accounts can rent a namespace for an amount of blocks and after a this renew the contract.
 * This is done via a RegisterNamespaceTransaction.
 *
 * @since 1.0
 */
public class RegisterNamespaceTransaction extends Transaction {
    private final String namespaceName;
    private final NamespaceId namespaceId;
    private final Optional<BigInteger> duration;
    private final Optional<NamespaceId> parentId;
    private final NamespaceType namespaceType;
    private final Schema schema = new RegisterNamespaceTransactionSchema();

    public RegisterNamespaceTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, String namespaceName, NamespaceId namespaceId, NamespaceType namespaceType, Optional<BigInteger> duration, Optional<NamespaceId> parentId, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, version, deadline, fee, namespaceName, namespaceId, namespaceType, duration, parentId, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    public RegisterNamespaceTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, String namespaceName, NamespaceId namespaceId, NamespaceType namespaceType, Optional<BigInteger> duration, Optional<NamespaceId> parentId) {
        this(networkType, version, deadline, fee, namespaceName, namespaceId, namespaceType, duration, parentId, Optional.empty(), Optional.empty(), Optional.empty());
    }

    private RegisterNamespaceTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, String namespaceName, NamespaceId namespaceId, NamespaceType namespaceType, Optional<BigInteger> duration, Optional<NamespaceId> parentId, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(TransactionType.REGISTER_NAMESPACE, networkType, version, deadline, fee, signature, signer, transactionInfo);
        Validate.notNull(namespaceName, "NamespaceName must not be null");
        Validate.notNull(namespaceType, "NamespaceType must not be null");
        Validate.notNull(namespaceId, "NamespaceId must not be null");
        if (namespaceType == NamespaceType.RootNamespace) {
            Validate.notNull(duration, "Duration must not be null");
        } else {
            Validate.notNull(parentId, "ParentId must not be null");
        }
        this.namespaceName = namespaceName;
        this.namespaceType = namespaceType;
        this.namespaceId = namespaceId;
        this.duration = duration;
        this.parentId = parentId;
    }

    /**
     * Create a root namespace object.
     *
     * @param deadline      The deadline to include the transaction.
     * @param namespaceName The namespace name.
     * @param duration      The duration of the namespace.
     * @param networkType   The network type.
     * @return instance of RegisterNamespaceTransaction
     */
    public static RegisterNamespaceTransaction createRootNamespace(Deadline deadline, String namespaceName, BigInteger duration, NetworkType networkType) {
        Validate.notNull(namespaceName, "NamespaceName must not be null");
        return new RegisterNamespaceTransaction(networkType, 2, deadline, BigInteger.valueOf(0), namespaceName, new NamespaceId(IdGenerator.generateNamespaceId(namespaceName)), NamespaceType.RootNamespace, Optional.of(duration), Optional.empty());
    }

    /**
     * Create a sub namespace object.
     *
     * @param deadline      - The deadline to include the transaction.
     * @param namespaceName - The namespace name.
     * @param parentId      - The parent id name.
     * @param networkType   - The network type.
     * @return instance of RegisterNamespaceTransaction
     */
    public static RegisterNamespaceTransaction createSubNamespace(Deadline deadline, String namespaceName, NamespaceId parentId, NetworkType networkType) {
        Validate.notNull(namespaceName, "NamespaceName must not be null");
        Validate.notNull(parentId, "ParentId must not be null");
        return new RegisterNamespaceTransaction(networkType, 2, deadline, BigInteger.valueOf(0), namespaceName, new NamespaceId(IdGenerator.generateSubNamespaceIdFromParentId(parentId.getId(), namespaceName)), NamespaceType.SubNamespace, Optional.empty(), Optional.of(parentId));
    }


    /**
     * Returns namespace name.
     *
     * @return namespace name
     */
    public String getNamespaceName() {
        return namespaceName;
    }

    /**
     * Returns id of the namespace derived from namespaceName.
     * When creating a sub namespace the namespaceId is derived from namespaceName and parentId.
     *
     * @return namespace id
     */
    public NamespaceId getNamespaceId() {
        return namespaceId;
    }

    /**
     * Returns number of blocks a namespace is active.
     *
     * @return namespace renting duration
     */
    public Optional<BigInteger> getDuration() { return duration; }

    /**
     * The id of the parent sub namespace.
     *
     * @return sub namespace
     */
    public Optional<NamespaceId> getParentId() { return parentId; }

    /**
     * Returns namespace type either RootNamespace or SubNamespace.
     *
     * @return namespace type
     */
    public NamespaceType getNamespaceType() {
        return namespaceType;
    }

    byte[] generateBytes() {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        int[] fee = new int[]{0, 0};
        int version = (int) Long.parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

        // Create Vectors
        int signatureVector = ProvisionNamespaceTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = ProvisionNamespaceTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = ProvisionNamespaceTransactionBuffer.createDeadlineVector(builder, UInt64.fromBigInteger(deadlineBigInt));
        int feeVector = ProvisionNamespaceTransactionBuffer.createFeeVector(builder, fee);
        int namespaceIdVector = ProvisionNamespaceTransactionBuffer.createNamespaceIdVector(builder, UInt64.fromBigInteger(namespaceId.getId()));
        int durationParentIdVector = ProvisionNamespaceTransactionBuffer.createDurationParentIdVector(builder, getNamespaceType() == NamespaceType.RootNamespace ? UInt64.fromBigInteger(duration.get()) : UInt64.fromBigInteger(parentId.get().getId()));

        int fixSize = 138; // replace by the all numbers sum or add a comment explaining this

        int name = builder.createString(namespaceName);

        ProvisionNamespaceTransactionBuffer.startProvisionNamespaceTransactionBuffer(builder);
        ProvisionNamespaceTransactionBuffer.addSize(builder, fixSize + namespaceName.length());
        ProvisionNamespaceTransactionBuffer.addSignature(builder, signatureVector);
        ProvisionNamespaceTransactionBuffer.addSigner(builder, signerVector);
        ProvisionNamespaceTransactionBuffer.addVersion(builder, version);
        ProvisionNamespaceTransactionBuffer.addType(builder, getType().getValue());
        ProvisionNamespaceTransactionBuffer.addFee(builder, feeVector);
        ProvisionNamespaceTransactionBuffer.addDeadline(builder, deadlineVector);
        ProvisionNamespaceTransactionBuffer.addNamespaceType(builder, getNamespaceType().getValue());
        ProvisionNamespaceTransactionBuffer.addDurationParentId(builder, durationParentIdVector);
        ProvisionNamespaceTransactionBuffer.addNamespaceId(builder, namespaceIdVector);
        ProvisionNamespaceTransactionBuffer.addNamespaceNameSize(builder, namespaceName.length());
        ProvisionNamespaceTransactionBuffer.addNamespaceName(builder, name);

        int codedTransaction = ProvisionNamespaceTransactionBuffer.endProvisionNamespaceTransactionBuffer(builder);
        builder.finish(codedTransaction);

        return schema.serialize(builder.sizedByteArray());
    }
}
