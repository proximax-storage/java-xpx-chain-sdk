/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionInfo;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * helper class for common operations
 */
public class BlockchainHelper {
   /** milliseconds in an hour 60 minutes of 60 seconds of 1000 milliseconds */
   private static final long HOUR_MILLIS = 3_600_000;

   private final BlockchainApi api;
   private final TransactionBuilderFactory transact;

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
   public BlockchainHelper(BlockchainApi api) {
      this.api = api;
      this.transact = api.transact();
      // initialize defaults for the transactions - 2 hour deadline and medium fees
      this.transact.setDeadlineMillis(BigInteger.valueOf(2 * HOUR_MILLIS));
      this.transact.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   /**
    * get infinite source of new, random accounts
    * 
    * @return stream of random accounts
    */
   public Stream<Account> accountRandom() {
      final NetworkType network = api.getNetworkType();
      return Stream.generate(() -> Account.generateNewAccount(network));
   }

   /**
    * generate specified number of account instances
    * 
    * @param count the count of items in the returned list of accounts
    * @return list of accounts
    */
   public List<Account> accountRandom(int count) {
      // generate specified number of random accounts
      return accountRandom().limit(count).collect(Collectors.toList());
   }

   public void accountToMultisig(Account account, List<PublicAccount> cosigners, int minApprovals, int minRemovals,
         int optinBlocks, int confirmationTimeoutSeconds) {
      // prepare transaction repo
      TransactionRepository transactions = api.createTransactionRepository();
      // prepare listener
      ListenerRepository listener = createListener();
      // first prepare the actual transaction to change account to multisig account
      ModifyMultisigAccountTransaction multisigChangeTrans = transact.multisigModification()
            .changeToMultisig(cosigners, minApprovals, minRemovals).build();
      // aggregate bonded transaction is required for cosigner opt-in so create that
      AggregateTransaction aggregateTrans = transact.aggregateBonded()
            .innerTransactions(multisigChangeTrans.toAggregate(account.getPublicAccount())).build();
      // aggregate bonded transaction requires lock funds
      LockFundsTransaction lockTrans = transact.lockFunds()
            .forAggregate(BigInteger.valueOf(optinBlocks), api.sign(aggregateTrans, account)).build();
      // announce lock funds and wait for confirmation
      transactionConfirmed(transactions, listener, lockTrans, account, confirmationTimeoutSeconds);
      // !!! wait a bit for server to get into consistent state !!!
      sleepForAWhile();
      // now announce the aggregate transaction
      transactionBondedAdded(transactions, listener, aggregateTrans, account, confirmationTimeoutSeconds);
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

   /**
    * wait for specified number of blocks. This method blocks or throws timeout exception
    * 
    * @param count number of blocks to wait for
    * @param timeoutSeconds timeout after which the wait will be aborted
    * 
    * @return height of the last block
    */
   public BigInteger blockConfirmations(int count, long timeoutSeconds) {
      return api.createBlockchainRepository().getBlockchainHeight().timeout(timeoutSeconds, TimeUnit.SECONDS)
            .take(count).blockingLast();
   }

   private static boolean equalHashes(Transaction trans, SignedTransaction signed) {
      System.out.println(trans);
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
