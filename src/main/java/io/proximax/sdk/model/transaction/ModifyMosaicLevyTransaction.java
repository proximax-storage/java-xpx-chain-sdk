/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Optional;

import com.google.flatbuffers.FlatBufferBuilder;

import org.apache.commons.lang3.Validate;

import io.proximax.sdk.gen.buffers.ModifyMosaicLevyTransactionBuffer;
import io.proximax.sdk.gen.buffers.MosaicLevyBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.utils.dto.UInt64Utils;
import io.proximax.sdk.model.mosaic.MosaicLevy;
import io.proximax.sdk.model.network.NetworkType;

/**
 * Accounts can collect some taxes for transfers those mosaics. This can be done
 * via ModifyMosaicLevyTransaction.
 */
public class ModifyMosaicLevyTransaction extends Transaction {
        private final ModifyMosaicLevyTransactionSchema schema = new ModifyMosaicLevyTransactionSchema();
        private final MosaicLevy mosaicLevy;
        private final MosaicId mosaicId;

        /**
         * @param networkType     network type
         * @param version         transaction version. Use
         *                        {@link EntityVersion#MODIFY_MOSAIC_LEVY} for
         *                        current version
         * @param deadline        transaction deadline
         * @param maxFee          transaction fee
         * @param signature       optional signature
         * @param signer          optional signer
         * @param transactionInfo optional transaction info
         * @param mosaicLevy      mosaic levy
         * @param mosaicId        mosaic id
         */
        public ModifyMosaicLevyTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
                        BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
                        Optional<TransactionInfo> transactionInfo, MosaicLevy mosaicLevy, MosaicId mosaicId) {
                super(EntityType.MODIFY_MOSAIC_LEVY, networkType, version, deadline, maxFee, signature, signer,
                                transactionInfo);
                // validations
                Validate.notNull(mosaicLevy, "mosaic levy can not be null");
                Validate.notNull(mosaicId, "mosaic Id can not be null");
                // assignments
                this.mosaicLevy = mosaicLevy;
                this.mosaicId = mosaicId;
        }

        /**
         * Returns mosaic id.
         *
         * @return BigInteger
         */
        public MosaicId getMosaicId() {
                return mosaicId;
        }

        /**
         * Returns mosaic levy.
         *
         * @return {@link MosaicLevy}
         */
        public MosaicLevy getMosaicLevy() {
                return mosaicLevy;
        }

        @Override
        protected byte[] generateBytes() {
                FlatBufferBuilder builder = new FlatBufferBuilder();

                // prepare data
                BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
                byte[] recipientBytes = mosaicLevy.getRecipient().getBytes();

                int levyRecipientVector = MosaicLevyBuffer.createRecipientVector(builder, recipientBytes);
                int levyMosaicIdVector = MosaicLevyBuffer.createMosaicIdVector(builder,
                                UInt64Utils.fromBigInteger(mosaicLevy.getMosaicId().getId()));
                int levyFeeVector = MosaicLevyBuffer.createFeeVector(builder,
                                UInt64Utils.fromBigInteger(mosaicLevy.getFee()));

                MosaicLevyBuffer.startMosaicLevyBuffer(builder);
                MosaicLevyBuffer.addRecipient(builder, levyRecipientVector);
                MosaicLevyBuffer.addMosaicId(builder, levyMosaicIdVector);
                MosaicLevyBuffer.addType(builder, mosaicLevy.getType().getValue());
                MosaicLevyBuffer.addFee(builder, levyFeeVector);
                int levy = MosaicLevyBuffer.endMosaicLevyBuffer(builder);

                // Create Vectors
                int signatureVector = ModifyMosaicLevyTransactionBuffer.createSignatureVector(builder, new byte[64]);
                int signerVector = ModifyMosaicLevyTransactionBuffer.createSignerVector(builder, new byte[32]);
                int deadlineVector = ModifyMosaicLevyTransactionBuffer.createDeadlineVector(builder,
                                UInt64Utils.fromBigInteger(deadlineBigInt));
                int feeVector = ModifyMosaicLevyTransactionBuffer.createMaxFeeVector(builder,
                                UInt64Utils.fromBigInteger(getMaxFee()));
                int mosaicIdVector = ModifyMosaicLevyTransactionBuffer.createMosaicIdVector(builder,
                                UInt64Utils.fromBigInteger(mosaicId.getId()));

                int size = getSerializedSize();

                // flatbuffer serialization
                ModifyMosaicLevyTransactionBuffer.startModifyMosaicLevyTransactionBuffer(builder);
                ModifyMosaicLevyTransactionBuffer.addSize(builder, size);
                ModifyMosaicLevyTransactionBuffer.addSignature(builder, signatureVector);
                ModifyMosaicLevyTransactionBuffer.addSigner(builder, signerVector);
                ModifyMosaicLevyTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
                ModifyMosaicLevyTransactionBuffer.addType(builder, getType().getValue());
                ModifyMosaicLevyTransactionBuffer.addMaxFee(builder, feeVector);
                ModifyMosaicLevyTransactionBuffer.addDeadline(builder, deadlineVector);
                ModifyMosaicLevyTransactionBuffer.addLevy(builder, levy);
                ModifyMosaicLevyTransactionBuffer.addMosaicId(builder, mosaicIdVector);

                int codedTransaction = ModifyMosaicLevyTransactionBuffer.endModifyMosaicLevyTransactionBuffer(builder);
                builder.finish(codedTransaction);

                // validate size
                byte[] output = schema.serialize(builder.sizedByteArray());
                Validate.isTrue(output.length == size,
                                "Serialized transaction has incorrect length: " + output + this.getClass());

                return output;
        }

        public static int calculatePayloadSize() {
                // mosaicid + levy type + recipent(levy) + mosaid id(levy) + fee (levy)
                return 8 + 1 + 25 + 8 + 8;
        }

        @Override
        protected int getPayloadSerializedSize() {
                return calculatePayloadSize();
        }

        @Override
        protected Transaction copyForSigner(PublicAccount signer) {
                return new ModifyMosaicLevyTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(),
                                getSignature(), Optional.of(signer), getTransactionInfo(), getMosaicLevy(),
                                getMosaicId());
        }
}
