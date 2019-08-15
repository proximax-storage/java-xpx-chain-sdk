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
import java.util.ArrayList;
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
   private final List<Transaction> innerTransactions;
   private final List<AggregateTransactionCosignature> cosignatures;
   private final Schema schema = new AggregateTransactionSchema();

   public AggregateTransaction(NetworkType networkType, TransactionType transactionType, Integer version,
         TransactionDeadline deadline, BigInteger fee, List<Transaction> innerTransactions,
         List<AggregateTransactionCosignature> cosignatures, String signature, PublicAccount signer,
         TransactionInfo transactionInfo) {
      this(networkType, transactionType, version, deadline, fee, innerTransactions, cosignatures,
            Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
   }

   public AggregateTransaction(NetworkType networkType, TransactionType transactionType, Integer version,
         TransactionDeadline deadline, BigInteger fee, List<Transaction> innerTransactions,
         List<AggregateTransactionCosignature> cosignatures) {
      this(networkType, transactionType, version, deadline, fee, innerTransactions, cosignatures, Optional.empty(),
            Optional.empty(), Optional.empty());
   }

   private AggregateTransaction(NetworkType networkType, TransactionType transactionType, Integer version,
         TransactionDeadline deadline, BigInteger fee, List<Transaction> innerTransactions,
         List<AggregateTransactionCosignature> cosignatures, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo) {
      super(transactionType, networkType, version, deadline, fee, signature, signer, transactionInfo);
      Validate.notNull(innerTransactions, "InnerTransactions must not be null");
      Validate.notNull(cosignatures, "Cosignatures must not be null");
      this.innerTransactions = innerTransactions;
      this.cosignatures = cosignatures;
   }

   /**
    * Create an aggregate complete transaction object
    *
    * @param deadline The deadline to include the transaction.
    * @param innerTransactions The list of inner innerTransactions.
    * @param networkType The network type.
    * @return {@link AggregateTransaction}
    */
   public static AggregateTransaction createComplete(TransactionDeadline deadline, List<Transaction> innerTransactions,
         NetworkType networkType) {
      return new AggregateTransaction(networkType, TransactionType.AGGREGATE_COMPLETE, 2, deadline,
            BigInteger.valueOf(0), innerTransactions, new ArrayList<>());
   }

   /**
    * Create an aggregate bonded transaction object
    *
    * @param deadline The deadline to include the transaction.
    * @param innerTransactions The list of inner innerTransactions.
    * @param networkType The network type.
    * @return {@link AggregateTransaction}
    */
   public static AggregateTransaction createBonded(TransactionDeadline deadline, List<Transaction> innerTransactions,
         NetworkType networkType) {
      return new AggregateTransaction(networkType, TransactionType.AGGREGATE_BONDED, 2, deadline, BigInteger.valueOf(0),
            innerTransactions, new ArrayList<>());
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
    * Returns list of transaction cosigners signatures.
    *
    * @return List of transaction cosigners signatures.
    */
   public List<AggregateTransactionCosignature> getCosignatures() {
      return cosignatures;
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
   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      byte[] transactionsBytes = new byte[0];
      for (Transaction innerTransaction : innerTransactions) {
         byte[] transactionBytes = innerTransaction.toAggregateTransactionBytes();
         transactionsBytes = ArrayUtils.addAll(transactionsBytes, transactionBytes);
      }

      // expected size = header + transactions bytes count + transactions bytes
      int size = HEADER_SIZE + 4 + transactionsBytes.length;

      // Create Vectors
      int signatureVector = AggregateTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = AggregateTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = AggregateTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = AggregateTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
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
}