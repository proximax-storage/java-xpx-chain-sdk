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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.spongycastle.util.encoders.Hex;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.AggregateTransactionBuffer;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * The aggregate innerTransactions contain multiple innerTransactions that can be initiated by different accounts.
 *
 * @since 1.0
 */
public class AggregateTransaction extends Transaction {
   private final Schema schema = new AggregateTransactionSchema();

   private final List<Transaction> innerTransactions;
   private final List<AggregateTransactionCosignature> cosignatures;

   /**
    * @param type transaction type which has to be one of {@link EntityType#AGGREGATE_COMPLETE} or
    * {@link EntityType#AGGREGATE_BONDED}
    * @param networkType network type
    * @param version transaction version. For latest {@link EntityVersion#AGGREGATE_COMPLETE} or
    * {@link EntityVersion#AGGREGATE_BONDED}
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param innerTransactions inner transactions of this aggregate transaction
    * @param cosignatures available cosignatures if any
    */
   public AggregateTransaction(EntityType type, NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo,
         List<Transaction> innerTransactions,
         List<AggregateTransactionCosignature> cosignatures) {
      super(type, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(innerTransactions, "InnerTransactions must not be null");
      Validate.notNull(cosignatures, "Cosignatures must not be null");
      Validate.validState(type == EntityType.AGGREGATE_BONDED || type == EntityType.AGGREGATE_COMPLETE,
            "Transaction type has to be aggregate bonded or complete but was %s",
            type);
      this.innerTransactions = Collections.unmodifiableList(innerTransactions);
      this.cosignatures = Collections.unmodifiableList(cosignatures);
   }

   /**
    * Returns list of innerTransactions included in the aggregate transaction.
    *
    * @return List of innerTransactions included in the aggregate transaction.
    */
   public List<Transaction> getInnerTransactions() {
      return innerTransactions;
   }

   /**
    * Returns list of transaction cosigners signatures.
    *
    * @return List of transaction cosigners signatures.
    */
   public List<AggregateTransactionCosignature> getCosignatures() {
      return cosignatures;
   }

   /**
    * Check if account has signed transaction.
    *
    * @param publicAccount - Signer public account
    * @return true of publicAccount has signed the transaction, false otherwise
    */
   public boolean isSignedByAccount(PublicAccount publicAccount) {
      Optional<PublicAccount> signer = this.getSigner();
      if (signer.isPresent()) {
         return signer.get().equals(publicAccount)
               || this.getCosignatures().stream().anyMatch(o -> o.getSigner().equals(publicAccount));
      } else {
         return false;
      }
   }

   /**
    * Sign transaction with cosignatories creating a new SignedTransaction.
    *
    * @param initiatorAccount Initiator account
    * @param generationHash network generation hash retrieved from block 1
    * @param cosignatories The list of accounts that will cosign the transaction
    * @return {@link SignedTransaction}
    */
   public SignedTransaction signTransactionWithCosigners(Account initiatorAccount, String generationHash,
         List<Account> cosignatories) {
      // sign the transaction by the initiator account
      SignedTransaction signedTransaction = this.signWith(initiatorAccount, generationHash);

      // prepare the payload of signed transaction
      StringBuilder payload = new StringBuilder(signedTransaction.getPayload());
      // for each cosignatory append public key and signature of the hash
      for (Account cosignatory : cosignatories) {
         payload.append(cosignatory.getPublicKey());
         payload.append(CosignatureTransaction.cosignTransaction(signedTransaction.getHash(), cosignatory));
      }
      byte[] payloadBytes = Hex.decode(payload.toString());

      // prepare size as bytes in reversed order
      byte[] size = BigInteger.valueOf(payloadBytes.length).toByteArray();
      ArrayUtils.reverse(size);

      // overwrite first bytes by size?
      System.arraycopy(size, 0, payloadBytes, 0, size.length);

      // return new signed transaction
      return new SignedTransaction(Hex.toHexString(payloadBytes), signedTransaction.getHash(), getType());
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      byte[] transactionsBytes = new byte[0];
      for (Transaction innerTransaction : innerTransactions) {
         byte[] transactionBytes = innerTransaction.toAggregateTransactionBytes();
         transactionsBytes = ArrayUtils.addAll(transactionsBytes, transactionBytes);
      }

      // expected size = header + transactions bytes count + transactions bytes
      int size = getSerializedSize();

      // Create Vectors
      int signatureVector = AggregateTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = AggregateTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = AggregateTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = AggregateTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));
      int transactionsVector = AggregateTransactionBuffer.createTransactionsVector(builder, transactionsBytes);

      AggregateTransactionBuffer.startAggregateTransactionBuffer(builder);
      AggregateTransactionBuffer.addSize(builder, size);
      AggregateTransactionBuffer.addSignature(builder, signatureVector);
      AggregateTransactionBuffer.addSigner(builder, signerVector);
      AggregateTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      AggregateTransactionBuffer.addType(builder, getType().getValue());
      AggregateTransactionBuffer.addMaxFee(builder, feeVector);
      AggregateTransactionBuffer.addDeadline(builder, deadlineVector);

      AggregateTransactionBuffer.addTransactionsSize(builder, transactionsBytes.length);
      AggregateTransactionBuffer.addTransactions(builder, transactionsVector);

      int codedTransaction = AggregateTransactionBuffer.endAggregateTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate payload size excluding header
    * 
    * @param innerTransactions list of transactions inside of this aggregate transacion
    * @return the size
    */
   public static int calculatePayloadSize(List<Transaction> innerTransactions) {
      // sum sizes of inner transactions, subtract 80 as toAggregateTransactionBytes leaves out 80 bytes of header
      int innerSize = innerTransactions.stream().mapToInt(Transaction::getSerializedSize).map(size -> size - 80).sum();
      // transactions size + transactions
      return 4 + innerSize;
   }
   
   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getInnerTransactions());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      throw new UnsupportedOperationException("Can not embed aggregate transaction into aggregate transaction");
   }
}