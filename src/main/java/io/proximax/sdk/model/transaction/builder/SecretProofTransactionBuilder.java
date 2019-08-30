/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.transaction.HashType;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.SecretProofTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link SecretProofTransaction}
 */
public class SecretProofTransactionBuilder
      extends TransactionBuilder<SecretProofTransactionBuilder, SecretProofTransaction> {

   private HashType hashType;
   private String secret;
   private String proof;
   private Recipient recipient;

   public SecretProofTransactionBuilder() {
      super(TransactionType.SECRET_PROOF, TransactionVersion.SECRET_PROOF.getValue());
   }

   @Override
   protected SecretProofTransactionBuilder self() {
      return this;
   }

   @Override
   public SecretProofTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(SecretProofTransaction.calculatePayloadSize(getRecipient(), getProof())));
      // create transaction instance
      return new SecretProofTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getHashType(), getSecret(), getProof(), getRecipient());
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * type of used hashing function
    * 
    * @param hashType hash function
    * @return self
    */
   public SecretProofTransactionBuilder hashType(HashType hashType) {
      this.hashType = hashType;
      return self();
   }

   /**
    * lock secret
    * 
    * @param secret the secret
    * @return self
    */
   public SecretProofTransactionBuilder secret(String secret) {
      this.secret = secret;
      return self();
   }

   /**
    * recipient of the locked mosaic
    * 
    * @param recipient the recipient
    * @return self
    */
   public SecretProofTransactionBuilder recipient(Recipient recipient) {
      this.recipient = recipient;
      return self();
   }

   /**
    * proof of the secret
    * 
    * @param proof the proof to set
    * @return self
    */
   public SecretProofTransactionBuilder proof(String proof) {
      this.proof = proof;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the hashType
    */
   public HashType getHashType() {
      return hashType;
   }

   /**
    * @return the secret
    */
   public String getSecret() {
      return secret;
   }

   /**
    * @return the recipient
    */
   public Recipient getRecipient() {
      return recipient;
   }

   /**
    * @return the proof
    */
   public String getProof() {
      return proof;
   }

   // -------------------------------------- convenience --------------------------------------------//

}
