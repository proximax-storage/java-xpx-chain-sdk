/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.transaction.AccountLinkTransaction;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link AccountLinkTransaction}
 */
public class LockFundsTransactionBuilder
      extends TransactionBuilder<LockFundsTransactionBuilder, LockFundsTransaction> {

   private Mosaic mosaic;
   private BigInteger duration;
   private SignedTransaction signedTransaction;

   public LockFundsTransactionBuilder() {
      super(TransactionType.LOCK, TransactionVersion.LOCK.getValue());
   }

   @Override
   protected LockFundsTransactionBuilder self() {
      return this;
   }

   @Override
   public LockFundsTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(AccountLinkTransaction.calculatePayloadSize()));
      // create transaction instance
      return new LockFundsTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(), getTransactionInfo(), getMosaic(), getDuration(), getSignedTransaction());
   }


   // ------------------------------------- setters ---------------------------------------------//

   /**
    * set mosaic defining what to lock
    * 
    * @param mosaic mosaic to lock
    * @return self
    */
   public LockFundsTransactionBuilder mosaic(Mosaic mosaic) {
      this.mosaic = mosaic;
      return self();
   }

   /**
    * set duration of the lock
    * 
    * @param duration the duration in number of blocks
    * @return self
    */
   public LockFundsTransactionBuilder duration(BigInteger duration) {
      this.duration = duration;
      return self();
   }

   /**
    * set signed transaction which is the lock for
    * 
    * @param signedTransaction the transaction
    * @return self
    */
   public LockFundsTransactionBuilder signedTransaction(SignedTransaction signedTransaction) {
      this.signedTransaction = signedTransaction;
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
    * @return the signedTransaction
    */
   public SignedTransaction getSignedTransaction() {
      return signedTransaction;
   }
   
   // -------------------------------------- convenience --------------------------------------------//

}
