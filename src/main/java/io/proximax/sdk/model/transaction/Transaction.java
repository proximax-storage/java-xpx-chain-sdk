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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.spongycastle.util.encoders.Hex;

import io.proximax.core.crypto.Hashes;
import io.proximax.core.crypto.Signature;
import io.proximax.core.crypto.Signer;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * An abstract transaction class that serves as the base class of all NEM transactions.
 *
 * @since 1.0
 */
public abstract class Transaction {
    private final TransactionType type;
    private final NetworkType networkType;
    private final Integer version;
    private final TransactionDeadline deadline;
    private final BigInteger fee;
    private final Optional<String> signature;
    private Optional<PublicAccount> signer;
    private final Optional<TransactionInfo> transactionInfo;

    /**
     * Constructor
     *
     * @param type            Transaction type.
     * @param networkType     Network type.
     * @param version         Transaction version.
     * @param deadline        Transaction deadline.
     * @param fee             Transaction fee.
     * @param signature       Transaction signature.
     * @param signer          Transaction signer.
     * @param transactionInfo Transaction meta data info.
     */
    public Transaction(TransactionType type, NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger fee, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        Validate.notNull(type, "Type must not be null");
        Validate.notNull(networkType, "NetworkType must not be null");
        Validate.notNull(version, "Version must not be null");
        Validate.notNull(deadline, "Deadline must not be null");
        Validate.notNull(fee, "Fee must not be null");
        this.type = type;
        this.networkType = networkType;
        this.version = version;
        this.deadline = deadline;
        this.fee = fee;

        this.signature = signature;
        this.signer = signer;
        this.transactionInfo = transactionInfo;
    }

    /**
     * Generates hash for a serialized transaction payload.
     *
     * @param payloadBytes Transaction payload as byte array
     * @param generationHashBytes the network generation hash
     * @return generated transaction hash.
     */
    public static String createTransactionHash(byte[] payloadBytes, byte[] generationHashBytes) {
        // expected size is payload - 4 bytes
        byte[] signingBytes = new byte[payloadBytes.length - 4];
        // 32 bytes = skip 4 bytes and take half of the signature
        System.arraycopy(payloadBytes, 4, signingBytes, 0, 32);
        // 32 bytes = skip second half of signature and take signer
        System.arraycopy(payloadBytes, 68, signingBytes, 32, 32);
        // 32 bytes = generation hash
        System.arraycopy(generationHashBytes, 0, signingBytes, 64, 32);
        // remainder
        System.arraycopy(payloadBytes, 100, signingBytes, 96, payloadBytes.length - 100);
        
        // hash and encode as upper-case hexadecimal string
        byte[] result = Hashes.sha3_256(signingBytes);
        return Hex.toHexString(result).toUpperCase();
    }

    /**
     * Returns the transaction type.
     *
     * @return transaction type
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Returns the network type.
     *
     * @return the network type
     */
    public NetworkType getNetworkType() {
        return networkType;
    }

    /**
     * Returns the transaction version.
     *
     * @return transaction version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Returns the deadline to include the transaction.
     *
     * @return deadline to include transaction into a block.
     */
    public TransactionDeadline getDeadline() {
        return deadline;
    }

    /**
     * Returns the fee for the transaction. The higher the fee, the higher the priority of the transaction.
     * Transactions with high priority get included in a block before transactions with lower priority.
     *
     * @return fee amount
     */
    public BigInteger getFee() {
        return fee;
    }

    /**
     * Returns the transaction signature (missing if part of an aggregate transaction).
     *
     * @return transaction signature
     */
    public Optional<String> getSignature() { return signature; }

    /**
     * Returns the transaction creator public account.
     *
     * @return signer public account
     */
    public Optional<PublicAccount> getSigner() { return signer; }

    /**
     * Returns meta data object contains additional information about the transaction.
     *
     * @return transaction meta data info.
     */
    public Optional<TransactionInfo> getTransactionInfo() { return transactionInfo; }

    abstract byte[] generateBytes();

    /**
     * Serialize and sign transaction creating a new SignedTransaction.
     *
     * @param account The account to sign the transaction.
     * @param generationHash network generation hash retrieved from block 1
     * @return {@link SignedTransaction}
     */
    public SignedTransaction signWith(Account account, String generationHash) {
       // 32 bytes of the generation hash
        byte[] generationHashBytes = Hex.decode(generationHash);
        byte[] bytes = this.generateBytes();
        // ignore first 4 + 64 + 32 bytes from the serialized form and concat with 32 bytes of generation hash
        byte[] signingBytes = new byte[bytes.length - 100 + 32];
        System.arraycopy(generationHashBytes, 0, signingBytes, 0, 32);
        System.arraycopy(bytes, 100, signingBytes, 32, bytes.length - 100);
        
        // sign the byte array with generation hash and serialized transaction
        Signature transSignature = new Signer(account.getKeyPair()).sign(signingBytes);

        // create payload
        byte[] payload = new byte[bytes.length];
        // 4 bytes = size
        System.arraycopy(bytes, 0, payload, 0, 4);
        // 64 bytes = signature
        byte[] transSignatureBytes = transSignature.getBytes();
        System.arraycopy(transSignatureBytes, 0, payload, 4, transSignatureBytes.length);
        // 32 bytes = signer
        byte[] rawSignerPublicKey = account.getKeyPair().getPublicKey().getRaw();
        System.arraycopy(rawSignerPublicKey, 0, payload, 64 + 4, rawSignerPublicKey.length);
        // append remainder of the transaction bytes
        System.arraycopy(bytes, 100, payload, 100, bytes.length - 100);
        
        // compute transaction hash
        String hash = Transaction.createTransactionHash(payload, generationHashBytes);
        
        // return signed transaction
        return new SignedTransaction(Hex.toHexString(payload).toUpperCase(), hash, type);
    }

    /**
     * Takes a transaction and formats bytes to be included in an aggregate transaction.
     *
     * @return transaction with signer serialized to be part of an aggregate transaction
     */
    byte[] toAggregateTransactionBytes() {
        byte[] signerBytes = Hex.decode(this.signer.orElseThrow(() -> new IllegalStateException("missing signer")).getPublicKey());
        byte[] bytes = this.generateBytes();
        byte[] resultBytes = new byte[bytes.length - 64 - 16];

        System.arraycopy(signerBytes, 0, resultBytes, 4, 32); // Copy signer
        System.arraycopy(bytes, 100, resultBytes, 32 + 4, 4); // Copy type and version
        System.arraycopy(bytes, 100 + 2 + 2 + 16, resultBytes, 32 + 4 + 4, bytes.length - 120); // Copy following data

        byte[] size = BigInteger.valueOf(bytes.length - 64l - 16).toByteArray();
        ArrayUtils.reverse(size);

        System.arraycopy(size, 0, resultBytes, 0, size.length);

        return resultBytes;
    }

    /**
     * Convert an aggregate transaction to an inner transaction including transaction signer.
     *
     * @param signer Transaction signer.
     * @return instance of Transaction with signer
     */
    public Transaction toAggregate(PublicAccount signer) {
        this.signer = Optional.of(signer);
        return this;
    }

    /**
     * Returns if a transaction is pending to be included in a block.
     *
     * @return if a transaction is pending to be included in a block
     */
    public boolean isUnconfirmed() {
        return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().equals(BigInteger.valueOf(0)) && this.transactionInfo.get().getHash().equals(this.transactionInfo.get().getMerkleComponentHash());
    }

    /**
     * Return if a transaction is included in a block.
     *
     * @return if a transaction is included in a block
     */
    public boolean isConfirmed() {
        return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().intValue() > 0;
    }

    /**
     * Returns if a transaction has missing signatures.
     *
     * @return if a transaction has missing signatures
     */
    public boolean hasMissingSignatures() {
        return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().equals(BigInteger.valueOf(0)) && !this.transactionInfo.get().getHash().equals(this.transactionInfo.get().getMerkleComponentHash());
    }

    /**
     * Returns if a transaction is not known by the network.
     *
     * @return if a transaction is not known by the network
     */
    public boolean isUnannounced() {
        return !this.transactionInfo.isPresent();
    }

   @Override
   public String toString() {
      return "Transaction [type=" + type + ", networkType=" + networkType + ", version=" + version + ", deadline="
            + deadline + ", fee=" + fee + ", signature=" + signature + ", signer=" + signer + ", transactionInfo="
            + transactionInfo + "]";
   }
    
    
}
