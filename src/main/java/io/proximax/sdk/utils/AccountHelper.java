/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkHarvestMosaic;
import io.proximax.sdk.model.transaction.AccountLinkTransaction;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * TODO add proper description
 */
public class AccountHelper extends BaseHelper {

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
   public AccountHelper(BlockchainApi api) {
      super(api);
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

   /**
    * convert account to multisig account
    * 
    * @param account
    * @param cosigners
    * @param minApprovals
    * @param minRemovals
    * @param optinBlocks
    * @param confirmationTimeoutSeconds
    */
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
    * activate delegated harvesting for an account
    * 
    * @param account the account that will get delegated harvesting activated
    * @param nodePublicKey node public key (use {@link BlockchainRepository#getNodeInfo()} or /node/info)
    * @param confirmationTimeoutSeconds transaction timeout in seconds
    * @return the remote account which is used for delegated harvesting
    * @deprecated not supported yet
    */
   @Deprecated
   public Account activateDelegatedHarvesting(Account account, String nodePublicKey, int confirmationTimeoutSeconds) {
      // prepare transaction repo
      TransactionRepository transactions = api.createTransactionRepository();
      // prepare listener
      ListenerRepository listener = createListener();
      // prepare new account that will be sent to the node
      Account remoteAccount = Account.generateNewAccount(api.getNetworkType());
      // link accounts
      AccountLinkTransaction link = transact.accountLink().link(remoteAccount.getPublicAccount()).build();
      // send 500 harvest currency to node public key
      PublicAccount node = PublicAccount.createFromPublicKey(nodePublicKey, api.getNetworkType());
      // TODO figure out the message!!!
      Message message = null;
      TransferTransaction transfer = transact.transfer()
            .mosaics(NetworkHarvestMosaic.createRelative(BigDecimal.valueOf(500))).to(node.getAddress())
            .message(message).build();
      // wrap both transactions to aggregate complete transaction
      AggregateTransaction transaction = transact.aggregateComplete()
            .innerTransactions(link.toAggregate(account.getPublicAccount()),
                  transfer.toAggregate(account.getPublicAccount()))
            .build();
      // wait for confirmation
      transactionConfirmed(transactions, listener, transaction, account, confirmationTimeoutSeconds);
      // upon successful execution return the remote account
      return remoteAccount;
   }
}
