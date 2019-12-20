/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicFactory;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * <p>
 * builder for {@link LockFundsTransaction}
 * </p>
 * <p>
 * Standard use: to lock funds for aggregate transaction call {@link #forAggregate(BigInteger, SignedTransaction)}
 * </p>
 */
public class LockFundsTransactionBuilder extends TransactionBuilder<LockFundsTransactionBuilder, LockFundsTransaction> {

   private Mosaic mosaic;
   private BigInteger duration;
   private SignedTransaction signedTransaction;

   public LockFundsTransactionBuilder() {
      super(EntityType.LOCK, EntityVersion.LOCK.getValue());
   }

   @Override
   protected LockFundsTransactionBuilder self() {
      return this;
   }

   @Override
   public LockFundsTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(LockFundsTransaction.calculatePayloadSize()));
      // create transaction instance
      return new LockFundsTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getMosaic(), getDuration(), getSignedTransaction());
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

   /**
    * lock 10 network currency for specified duration. this is convenience for aggregate transaction locks
    * 
    * @param duration the duration in number of blocks
    * @return self
    * @deprecated use {@link #forAggregate(MosaicFactory, BigInteger, SignedTransaction)} instead
    */
   @Deprecated
   public LockFundsTransactionBuilder aggregate(BigInteger duration) {
      return mosaic(NetworkCurrencyMosaic.TEN).duration(duration);
   }

   /**
    * <p>lock 10 network currency for specified duration and signed transaction. this is convenience for aggregate
    * transaction locks</p>
    * <p><b>NOTICE</b> this assumes that {@link NetworkCurrencyMosaic} is the network currency mosaic</p>
    * 
    * @param duration the duration in number of blocks
    * @param signedAggregateTransaction the aggregate transaction which is getting the lock created
    * @return self
    */
   public LockFundsTransactionBuilder forAggregate(BigInteger duration, SignedTransaction signedAggregateTransaction) {
      return forAggregate(NetworkCurrencyMosaic.FACTORY, duration, signedAggregateTransaction);
   }

   /**
    * lock 10 network currency for specified duration and signed transaction. this is convenience for aggregate
    * transaction locks
    * 
    * @param networkCurrencyFactory the factory for network currency. Available via call to
    * {@link BlockchainApi#networkCurrencyMosaic()}
    * @param duration the duration in number of blocks
    * @param signedAggregateTransaction the aggregate transaction which is getting the lock created
    * @return self
    */
   public LockFundsTransactionBuilder forAggregate(MosaicFactory networkCurrencyFactory, BigInteger duration,
         SignedTransaction signedAggregateTransaction) {
      Mosaic mosaicForTransfer = networkCurrencyFactory.create(BigDecimal.TEN);
      return mosaic(mosaicForTransfer).duration(duration).signedTransaction(signedAggregateTransaction);
   }
}
