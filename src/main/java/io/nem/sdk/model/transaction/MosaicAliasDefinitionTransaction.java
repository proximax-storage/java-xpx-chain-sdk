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

import java.math.BigInteger;
import java.util.Optional;

import org.apache.commons.lang.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.nem.sdk.infrastructure.utils.UInt64Utils;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicProperties;
import io.nem.sdk.model.namespace.NamespaceId;

/**
 * 
 */
public class MosaicAliasDefinitionTransaction extends Transaction {
    private final MosaicId mosaicId;
    private final NamespaceId namespaceId;
    private final Integer aliasAction;
    private final Schema schema = new MosaicAliasTransactionSchema();


    public MosaicAliasDefinitionTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, MosaicId mosaicId, NamespaceId namespaceId, Integer action, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, version, deadline, fee, mosaicId, namespaceId, action, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    public MosaicAliasDefinitionTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, MosaicId mosaicId, NamespaceId namespaceId, Integer action) {
        this(networkType, version, deadline, fee, mosaicId, namespaceId, action, Optional.empty(), Optional.empty(), Optional.empty());
    }

    private MosaicAliasDefinitionTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, MosaicId mosaicId, NamespaceId namespaceId, Integer action, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(TransactionType.MOSAIC_ALIAS, networkType, version, deadline, fee, signature, signer, transactionInfo);
        Validate.notNull(mosaicId, "MosaicId must not be null");
        Validate.notNull(namespaceId, "namespaceId must not be null");
        Validate.notNull(action, "action must not be null");
        this.mosaicId = mosaicId;
        this.namespaceId = namespaceId;
        this.aliasAction = action;
    }

    /**
     * Create a mosaic creation transaction object.
     *
     * @param deadline         The deadline to include the transaction.
     * @param mosaicName       The mosaic name ex: xem.
     * @param namespaceName    The namespace where mosaic will be included ex: nem.
     * @param mosaicProperties The mosaic properties.
     * @param networkType      The network type.
     * @return {@link MosaicAliasDefinitionTransaction}
     */
    public static MosaicAliasDefinitionTransaction create(MosaicId mosaicId, NamespaceId namespaceId, Integer action, Deadline deadline, MosaicProperties mosaicProperties, NetworkType networkType) {
        Validate.notNull(mosaicId, "mosaicId must not be null");
        Validate.notNull(namespaceId, "namespaceId must not be null");
        return new MosaicAliasDefinitionTransaction(networkType,
                2, // TODO is this OK?
                deadline,
                BigInteger.valueOf(0),
                mosaicId,
                namespaceId,
                action);
    }

    /**
     * Returns mosaic id generated from namespace name and mosaic name.
     *
     * @return MosaicId
     */
    public MosaicId getMosaicId() {
        return mosaicId;
    }

    byte[] generateBytes() {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        int[] fee = new int[]{0, 0};
        int version = (int) Long.parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

        // Create Vectors
        int signatureVector = MosaicAliasDefinitionTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = MosaicAliasDefinitionTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = MosaicAliasDefinitionTransactionBuffer.createDeadlineVector(builder, UInt64Utils.fromBigInteger(deadlineBigInt));
        int feeVector = MosaicAliasDefinitionTransactionBuffer.createFeeVector(builder, fee);
        int mosaicIdVector = MosaicAliasDefinitionTransactionBuffer.createMosaicIdVector(builder, UInt64Utils.fromBigInteger(mosaicId.getId()));
        int namespaceIdVector = MosaicAliasDefinitionTransactionBuffer.createNamespaceIdVector(builder, UInt64Utils.fromBigInteger(namespaceId.getId()));
        		
        MosaicAliasDefinitionTransactionBuffer.startMosaicAliasDefinitionTransactionBuffer(builder);
        MosaicAliasDefinitionTransactionBuffer.addSignature(builder, signatureVector);
        MosaicAliasDefinitionTransactionBuffer.addSigner(builder, signerVector);
        MosaicAliasDefinitionTransactionBuffer.addVersion(builder, version);
        MosaicAliasDefinitionTransactionBuffer.addType(builder, getType().getValue());
        MosaicAliasDefinitionTransactionBuffer.addFee(builder, feeVector);
        MosaicAliasDefinitionTransactionBuffer.addDeadline(builder, deadlineVector);

        MosaicAliasDefinitionTransactionBuffer.addNamespaceId(builder, namespaceIdVector);
        MosaicAliasDefinitionTransactionBuffer.addMosaicId(builder, mosaicIdVector);
        MosaicAliasDefinitionTransactionBuffer.addAliasAction(builder, aliasAction);
        
        int codedTransaction = MosaicAliasDefinitionTransactionBuffer.endMosaicAliasDefinitionTransactionBuffer(builder);
        builder.finish(codedTransaction);

        return schema.serialize(builder.sizedByteArray());
    }
}
