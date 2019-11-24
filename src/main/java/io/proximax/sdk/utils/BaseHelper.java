/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

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
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionInfo;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * TODO add proper description
 */
class BaseHelper {
   
   /** milliseconds in an hour 60 minutes of 60 seconds of 1000 milliseconds */
   protected static final long HOUR_MILLIS = 3_600_000;

   protected final BlockchainApi api;
   protected final TransactionBuilderFactory transact;

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
      // initialize defaults for the transactions - 2 hour deadline and medium fees
      this.transact.setDeadlineMillis(BigInteger.valueOf(2 * HOUR_MILLIS));
      this.transact.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   /**
    * announce transaction and wait for it to be added to confirmed transactions
    * 
    * @param transRepo
    * @param listener
    * @param transaction
    * @param initiatorAccount
    * @param confirmationTimeoutSeconds
    * @return
    */
   public Transaction transactionConfirmed(TransactionRepository transRepo, ListenerRepository listener,
         Transaction transaction, Account initiatorAccount, int confirmationTimeoutSeconds) {
      // sign the transaction
      SignedTransaction signedTrans = api.sign(transaction, initiatorAccount);
      // announce the signed transaction
      transRepo.announce(signedTrans).blockingFirst();
      // wait for confirmation of the transaction
      return listener.confirmed(initiatorAccount.getAddress()).filter(trans -> equalHashes(trans, signedTrans))
            .timeout(confirmationTimeoutSeconds, TimeUnit.SECONDS).blockingFirst();
   }

   /**
    * announce transaction and wait for it to be added to confirmed transactions
    * 
    * @param transRepo
    * @param listener
    * @param transaction
    * @param initiatorAccount
    * @param confirmationTimeoutSeconds
    * @return
    */
   public Transaction transactionBondedAdded(TransactionRepository transRepo, ListenerRepository listener,
         AggregateTransaction transaction, Account initiatorAccount, int confirmationTimeoutSeconds) {
      // sign the transaction
      SignedTransaction signedTrans = api.sign(transaction, initiatorAccount);
      // announce the signed transaction
      transRepo.announceAggregateBonded(signedTrans).blockingFirst();
      // wait for confirmation of the transaction
      return listener.aggregateBondedAdded(initiatorAccount.getAddress())
            .filter(trans -> equalHashes(trans, signedTrans)).timeout(confirmationTimeoutSeconds, TimeUnit.SECONDS)
            .blockingFirst();
   }

   /**
    * blocking! create and initialize listener
    * 
    * @return listener that can immediately be used
    */
   public ListenerRepository createListener() {
      ListenerRepository listener = api.createListener();
      try {
         listener.open().get();
         return listener;
      } catch (InterruptedException | ExecutionException e) {
         throw new RuntimeException("Failed to open listener", e);
      }
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
