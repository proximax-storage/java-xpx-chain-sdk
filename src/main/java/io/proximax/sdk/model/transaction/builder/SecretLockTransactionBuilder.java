/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.transaction.HashType;
import io.proximax.sdk.model.transaction.SecretLockTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link SecretLockTransaction}
 */
public class SecretLockTransactionBuilder
      extends TransactionBuilder<SecretLockTransactionBuilder, SecretLockTransaction> {

   private Mosaic mosaic;
   private BigInteger duration;
   private HashType hashType;
   private String secret;
   private Address recipient;

   public SecretLockTransactionBuilder() {
      super(TransactionType.SECRET_LOCK, TransactionVersion.SECRET_LOCK.getValue());
   }

   @Override
   protected SecretLockTransactionBuilder self() {
      return this;
   }

   @Override
   public SecretLockTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(SecretLockTransaction.calculatePayloadSize()));
      // create transaction instance
      return new SecretLockTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getMosaic(), getDuration(), getHashType(), getSecret(), getRecipient());
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * mosaic amount to lock
    * 
    * @param mosaic locked mosaic
    * @return self
    */
   public SecretLockTransactionBuilder mosaic(Mosaic mosaic) {
      this.mosaic = mosaic;
      return self();
   }

   /**
    * duration of the lock in number of blocks
    * 
    * @param duration number of blocks
    * @return self
    */
   public SecretLockTransactionBuilder duration(BigInteger duration) {
      this.duration = duration;
      return self();
   }

   /**
    * type of used hashing function
    * 
    * @param hashType hash function
    * @return self
    */
   public SecretLockTransactionBuilder hashType(HashType hashType) {
      this.hashType = hashType;
      return self();
   }

   /**
    * lock secret
    * 
    * @param secret the secret
    * @return self
    */
   public SecretLockTransactionBuilder secret(String secret) {
      this.secret = secret;
      return self();
   }

   /**
    * recipient of the locked mosaic
    * 
    * @param recipient the recipient
    * @return self
    */
   public SecretLockTransactionBuilder recipient(Address recipient) {
      this.recipient = recipient;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the mosaic
    */
   public Mosaic getMosaic() {
      return mosaic;
   }

   /**
    * @return the duration
    */
   public BigInteger getDuration() {
      return duration;
   }

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
   public Address getRecipient() {
      return recipient;
   }

   // -------------------------------------- convenience --------------------------------------------//

}
