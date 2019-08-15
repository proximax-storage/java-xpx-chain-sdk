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

import io.proximax.sdk.gen.buffers.RegisterNamespaceTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.utils.dto.UInt64Utils;

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

    public RegisterNamespaceTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee, String namespaceName, NamespaceId namespaceId, NamespaceType namespaceType, Optional<BigInteger> duration, Optional<NamespaceId> parentId, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, version, deadline, fee, namespaceName, namespaceId, namespaceType, duration, parentId, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    public RegisterNamespaceTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee, String namespaceName, NamespaceId namespaceId, NamespaceType namespaceType, Optional<BigInteger> duration, Optional<NamespaceId> parentId) {
        this(networkType, version, deadline, fee, namespaceName, namespaceId, namespaceType, duration, parentId, Optional.empty(), Optional.empty(), Optional.empty());
    }

    private RegisterNamespaceTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee, String namespaceName, NamespaceId namespaceId, NamespaceType namespaceType, Optional<BigInteger> duration, Optional<NamespaceId> parentId, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
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
    public static RegisterNamespaceTransaction createRootNamespace(TransactionDeadline deadline, String namespaceName, BigInteger duration, NetworkType networkType) {
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
    public static RegisterNamespaceTransaction createSubNamespace(TransactionDeadline deadline, String namespaceName, NamespaceId parentId, NetworkType networkType) {
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

        // Create Vectors
        int signatureVector = RegisterNamespaceTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = RegisterNamespaceTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = RegisterNamespaceTransactionBuffer.createDeadlineVector(builder, UInt64Utils.fromBigInteger(deadlineBigInt));
        int feeVector = RegisterNamespaceTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
        int namespaceIdVector = RegisterNamespaceTransactionBuffer.createNamespaceIdVector(builder, UInt64Utils.fromBigInteger(namespaceId.getId()));
        int durationParentIdVector = RegisterNamespaceTransactionBuffer.createDurationParentIdVector(builder, getNamespaceType() == NamespaceType.RootNamespace ? UInt64Utils.fromBigInteger(duration.get()) : UInt64Utils.fromBigInteger(parentId.get().getId()));
        int name = builder.createString(namespaceName);

        // header, ns type, duration, ns id, name size, name
        int size = HEADER_SIZE + 1 + 8 + 8 + 1 + namespaceName.length();


        RegisterNamespaceTransactionBuffer.startRegisterNamespaceTransactionBuffer(builder);
        RegisterNamespaceTransactionBuffer.addSize(builder, size);
        RegisterNamespaceTransactionBuffer.addSignature(builder, signatureVector);
        RegisterNamespaceTransactionBuffer.addSigner(builder, signerVector);
        RegisterNamespaceTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
        RegisterNamespaceTransactionBuffer.addType(builder, getType().getValue());
        RegisterNamespaceTransactionBuffer.addMaxFee(builder, feeVector);
        RegisterNamespaceTransactionBuffer.addDeadline(builder, deadlineVector);
        
        RegisterNamespaceTransactionBuffer.addNamespaceType(builder, getNamespaceType().getValue());
        RegisterNamespaceTransactionBuffer.addDurationParentId(builder, durationParentIdVector);
        RegisterNamespaceTransactionBuffer.addNamespaceId(builder, namespaceIdVector);
        RegisterNamespaceTransactionBuffer.addNamespaceNameSize(builder, namespaceName.length());
        RegisterNamespaceTransactionBuffer.addNamespaceName(builder, name);

        int codedTransaction = RegisterNamespaceTransactionBuffer.endRegisterNamespaceTransactionBuffer(builder);
        builder.finish(codedTransaction);

        // validate size
        byte[] output = schema.serialize(builder.sizedByteArray());
        Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
        return output;
    }
}
