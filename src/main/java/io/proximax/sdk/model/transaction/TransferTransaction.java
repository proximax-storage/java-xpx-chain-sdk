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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;

/**
 * The transfer transactions object contain data about transfers of mosaics and message to another account.
 *
 * @since 1.0
 */
public class TransferTransaction extends Transaction {
    private final Address recipient;
    private final List<Mosaic> mosaics;
    private final Message message;
    private final Schema schema = new TransferTransactionSchema();

    public TransferTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, Address recipient, List<Mosaic> mosaics, Message message, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, version, deadline, fee, recipient, mosaics, message, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    public TransferTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, Address recipient, List<Mosaic> mosaics, Message message) {
        this(networkType, version, deadline, fee, recipient, mosaics, message, Optional.empty(), Optional.empty(), Optional.empty());
    }

    private TransferTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, Address recipient, List<Mosaic> mosaics, Message message, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(TransactionType.TRANSFER, networkType, version, deadline, fee, signature, signer, transactionInfo);
        Validate.notNull(recipient, "Recipient must not be null");
        Validate.notNull(mosaics, "Mosaics must not be null");
        Validate.notNull(message, "Message must not be null");
        this.recipient = recipient;
        this.mosaics = mosaics;
        this.message = message;
    }

    /**
     * Create a transfer transaction object.
     *
     * @param deadline    - The deadline to include the transaction.
     * @param recipient   - The recipient of the transaction.
     * @param mosaics     - The array of mosaics.
     * @param message     - The transaction message.
     * @param networkType - The network type.
     * @return a TransferTransaction instance
     */
    public static TransferTransaction create(Deadline deadline, Address recipient, List<Mosaic> mosaics, Message message, NetworkType networkType) {
        return new TransferTransaction(networkType, 3, deadline, BigInteger.valueOf(0), recipient, mosaics, message);
    }

    /**
     * Returns address of the recipient.
     *
     * @return recipient address
     */
    public Address getRecipient() {
        return recipient;
    }

    /**
     * Returns list of mosaic objects.
     *
     * @return list of mosaics in the transfer
     */
    public List<Mosaic> getMosaics() {
        return mosaics;
    }

    /**
     * Returns transaction message.
     *
     * @return Message
     */
    public Message getMessage() {
        return message;
    }

    byte[] generateBytes() {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        int[] fee = new int[]{0, 0};
        int version = (int) Long.parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

        // Create Message
        byte[] bytePayload = message.getEncodedPayload();
        int payload = MessageBuffer.createPayloadVector(builder, bytePayload);
        MessageBuffer.startMessageBuffer(builder);
        MessageBuffer.addType(builder, message.getTypeCode());
        MessageBuffer.addPayload(builder, payload);
        int message = MessageBuffer.endMessageBuffer(builder);

        // Create Mosaics
        int[] mosaicBuffers = new int[mosaics.size()];
        for (int i = 0; i < mosaics.size(); ++i) {
            Mosaic mosaic = mosaics.get(i);
            int id = MosaicBuffer.createIdVector(builder, UInt64Utils.fromBigInteger(mosaic.getId().getId()));
            int amount = MosaicBuffer.createAmountVector(builder, UInt64Utils.fromBigInteger(mosaic.getAmount()));
            MosaicBuffer.startMosaicBuffer(builder);
            MosaicBuffer.addId(builder, id);
            MosaicBuffer.addAmount(builder, amount);
            mosaicBuffers[i] = MosaicBuffer.endMosaicBuffer(builder);
        }

        byte[] address = new Base32().decode(getRecipient().plain().getBytes(StandardCharsets.UTF_8));
        // Create Vectors
        int signatureVector = TransferTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = TransferTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = TransferTransactionBuffer.createDeadlineVector(builder, UInt64Utils.fromBigInteger(deadlineBigInt));
        int feeVector = TransferTransactionBuffer.createFeeVector(builder, fee);
        int recipientVector = TransferTransactionBuffer.createRecipientVector(builder, address);
        int mosaicsVector = TransferTransactionBuffer.createMosaicsVector(builder, mosaicBuffers);

        int size = 120 + 29 + (16 * mosaics.size()) + bytePayload.length;

        TransferTransactionBuffer.startTransferTransactionBuffer(builder);
        TransferTransactionBuffer.addSize(builder, size);
        TransferTransactionBuffer.addSignature(builder, signatureVector);
        TransferTransactionBuffer.addSigner(builder, signerVector);
        TransferTransactionBuffer.addVersion(builder, version);
        TransferTransactionBuffer.addType(builder, getType().getValue());
        TransferTransactionBuffer.addFee(builder, feeVector);
        TransferTransactionBuffer.addDeadline(builder, deadlineVector);
        TransferTransactionBuffer.addRecipient(builder, recipientVector);
        TransferTransactionBuffer.addNumMosaics(builder, mosaics.size());
        TransferTransactionBuffer.addMessageSize(builder, bytePayload.length + 1);
        TransferTransactionBuffer.addMessage(builder, message);
        TransferTransactionBuffer.addMosaics(builder, mosaicsVector);

        int codedTransfer = TransferTransactionBuffer.endTransferTransactionBuffer(builder);
        builder.finish(codedTransfer);

        byte[] output = schema.serialize(builder.sizedByteArray());
        Validate.isTrue(output.length == size, "Serialized form has incorrect length");
        return output;
    }
}
