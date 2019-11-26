/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.helpers;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionInfo;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * Base helper implementation with common initialization
 */
class BaseHelper {
   /** milliseconds in an hour 60 minutes of 60 seconds of 1000 milliseconds */
   protected static final long HOUR_MILLIS = 3_600_000;

   protected final BlockchainApi api;
   protected final TransactionBuilderFactory transact;
   protected final TransactionRepository transactionRepo;

   private ListenerRepository listener;

   /**
    * </p>
    * create new helper instance
    * </p>
    * <p>
    * note this might require connection to the network if network type is not known to the provided api
    * </p>
    * 
    * @param api
    */
   public BaseHelper(BlockchainApi api) {
      this.api = api;
      this.transact = api.transact();
      this.transactionRepo = api.createTransactionRepository();
      // initialize defaults for the transactions - 2 hour deadline and medium fees
      this.transact.setDeadlineMillis(BigInteger.valueOf(2 * HOUR_MILLIS));
      this.transact.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   /**
    * <b>BLOCKING!</b> announce transaction and wait for it to be added to confirmed transactions
    * 
    * @param transaction the transaction to announce to the network
    * @param signer account used to sign the transaction
    * @param confirmationTimeoutSeconds transaction confirmation timeout
    * @return the transaction response
    */
   public Transaction transactionConfirmed(Transaction transaction, Account signer,
         int confirmationTimeoutSeconds) {
      // sign the transaction
      SignedTransaction signedTrans = api.sign(transaction, signer);
      // announce the signed transaction
      transactionRepo.announce(signedTrans).blockingFirst();
      // wait for confirmation of the transaction
      return getListener().confirmed(signer.getAddress()).filter(trans -> equalHashes(trans, signedTrans))
            .timeout(confirmationTimeoutSeconds, TimeUnit.SECONDS).blockingFirst();
   }

   /**
    * <b>BLOCKING!</b> announce transaction and wait for it to be added to confirmed transactions
    * 
    * @param transaction the aggregate bonded transaction that is to be announced
    * @param initiatorAccount initiator of the transaction
    * @param confirmationTimeoutSeconds transaction confirmation timeout
    * @return the transaction response
    */
   public Transaction transactionBondedAdded(AggregateTransaction transaction, Account initiatorAccount,
         int confirmationTimeoutSeconds) {
      // sign the transaction
      SignedTransaction signedTrans = api.sign(transaction, initiatorAccount);
      // announce the signed transaction
      transactionRepo.announceAggregateBonded(signedTrans).blockingFirst();
      // wait for confirmation of the transaction
      return getListener().aggregateBondedAdded(initiatorAccount.getAddress())
            .filter(trans -> equalHashes(trans, signedTrans)).timeout(confirmationTimeoutSeconds, TimeUnit.SECONDS)
            .blockingFirst();
   }

   /**
    * <b>BLOCKING</b> announce transactions as aggregate bonded transaction. Make sure that inner transactions are converted to
    * aggregate via call to {@link Transaction#toAggregate(io.proximax.sdk.model.account.PublicAccount)}
    * 
    * @param initiatorAccount account announcing the transaction (will be used to lock funds)
    * @param lockBlocks number of blocks to wait for cosigners
    * @param confirmationTimeoutSeconds timeout for transaction announcements
    * @param innerTransactions transactions to include in the aggregate bonded transaction
    */
   public void announceAsAggregateBonded(Account initiatorAccount, int lockBlocks, int confirmationTimeoutSeconds,
         Transaction... innerTransactions) {
      // aggregate bonded transaction is required for cosigner opt-in so create that
      AggregateTransaction aggregateTrans = transact.aggregateBonded().innerTransactions(innerTransactions).build();
      // aggregate bonded transaction requires lock funds
      LockFundsTransaction lockTrans = transact.lockFunds()
            .forAggregate(BigInteger.valueOf(lockBlocks), api.sign(aggregateTrans, initiatorAccount)).build();
      // announce lock funds and wait for confirmation
      transactionConfirmed(lockTrans, initiatorAccount, confirmationTimeoutSeconds);
      // !!! wait a bit for server to get into consistent state !!!
      sleepForAWhile();
      // now announce the aggregate transaction
      transactionBondedAdded(aggregateTrans, initiatorAccount, confirmationTimeoutSeconds);
   }

   /**
    * blocking! create and initialize listener
    * 
    * @return listener that can immediately be used
    */
   public synchronized ListenerRepository getListener() {
      if (listener == null) {
         ListenerRepository newListener = api.createListener();
         try {
            newListener.open().get();
            listener = newListener;
         } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to open listener", e);
         }
      }
      return listener;
   }

   private static boolean equalHashes(Transaction trans, SignedTransaction signed) {
      Optional<TransactionInfo> info = trans.getTransactionInfo();
      if (info.isPresent()) {
         return info.get().getHash().equals(Optional.of(signed.getHash()));
      }
      return false;
   }

   /**
    * convenience sleep needed to work around server listener synchronization issues
    */
   public void sleepForAWhile() {
      try {
         Thread.sleep(1000l);
      } catch (InterruptedException e) {
         // do nothing
      }
   }
}
