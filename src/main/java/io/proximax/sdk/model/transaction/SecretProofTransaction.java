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
import org.spongycastle.util.encoders.Hex;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.SecretProofTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

public class SecretProofTransaction extends Transaction {
   private final Schema schema = new SecretProofTransactionSchema();

   private final HashType hashType;
   private final String secret;
   private final String proof;
   private final Recipient recipient;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#SECRET_PROOF} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param hashType hashing function used
    * @param secret hash of the secret value
    * @param proof the secret value
    * @param recipient recipient of the transfer
    */
   public SecretProofTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, HashType hashType, String secret, String proof,
         Recipient recipient) {
      super(EntityType.SECRET_PROOF, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      // validations
      Validate.notNull(secret, "Secret must not be null");
      Validate.notNull(proof, "Proof must not be null");
      Validate.notNull(recipient, "Recipient must not be null");
      if (!hashType.validate(secret)) {
         throw new IllegalArgumentException("HashType and Secret have incompatible length or not hexadecimal string");
      }
      // assignments
      this.hashType = hashType;
      this.secret = secret;
      this.proof = proof;
      this.recipient = recipient;
   }

   /**
    * Returns the hash algorithm secret is generated with.
    *
    * @return the hash algorithm secret is generated with.
    */
   public HashType getHashType() {
      return hashType;
   }

   /**
    * Returns the proof hashed.
    *
    * @return the proof hashed.
    */
   public String getSecret() {
      return secret;
   }

   /**
    * Returns proof.
    *
    * @return proof.
    */
   public String getProof() {
      return proof;
   }

   /**
    * @return the recipient
    */
   public Recipient getRecipient() {
      return recipient;
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      byte[] recipientBytes = recipient.getBytes();
      byte[] proofBytes = Hex.decode(proof);
      // Create Vectors
      int signatureVector = SecretProofTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = SecretProofTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = SecretProofTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = SecretProofTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));
      int secretVector = SecretProofTransactionBuffer.createSecretVector(builder, Hex.decode(secret));
      int recipientVector = SecretProofTransactionBuffer.createRecipientVector(builder, recipientBytes);
      int proofVector = SecretProofTransactionBuffer.createProofVector(builder, proofBytes);

      int size = getSerializedSize();

      SecretProofTransactionBuffer.startSecretProofTransactionBuffer(builder);
      SecretProofTransactionBuffer.addSize(builder, size);
      SecretProofTransactionBuffer.addSignature(builder, signatureVector);
      SecretProofTransactionBuffer.addSigner(builder, signerVector);
      SecretProofTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      SecretProofTransactionBuffer.addType(builder, getType().getValue());
      SecretProofTransactionBuffer.addMaxFee(builder, feeVector);
      SecretProofTransactionBuffer.addDeadline(builder, deadlineVector);

      SecretProofTransactionBuffer.addHashAlgorithm(builder, hashType.getValue());
      SecretProofTransactionBuffer.addSecret(builder, secretVector);
      SecretProofTransactionBuffer.addRecipient(builder, recipientVector);
      SecretProofTransactionBuffer.addProofSize(builder, proofBytes.length);
      SecretProofTransactionBuffer.addProof(builder, proofVector);

      int codedSecretProof = SecretProofTransactionBuffer.endSecretProofTransactionBuffer(builder);
      builder.finish(codedSecretProof);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate payload size excluding the header
    * 
    * @param recipient lock recipient
    * @param proof proof
    * @return the size
    */
   public static int calculatePayloadSize(Recipient recipient, String proof) {
      // 1 hash type + 32 secret + 2 proof size + recipient + proof
      return 35 + recipient.getBytes().length + Hex.decode(proof).length;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getRecipient(), getProof());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new SecretProofTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
            Optional.of(signer), getTransactionInfo(), getHashType(), getSecret(), getProof(), getRecipient());
   }
}
