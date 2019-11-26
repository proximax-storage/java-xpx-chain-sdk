/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.helpers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkHarvestMosaic;
import io.proximax.sdk.model.transaction.AccountLinkTransaction;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * Helper for account-related tasks
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
   public Stream<Account> randomAccount() {
      final NetworkType network = api.getNetworkType();
      return Stream.generate(() -> Account.generateNewAccount(network));
   }

   /**
    * generate specified number of account instances
    * 
    * @param count the count of items in the returned list of accounts
    * @return list of accounts
    */
   public List<Account> randomAccount(int count) {
      // generate specified number of random accounts
      return randomAccount().limit(count).collect(Collectors.toList());
   }

   /**
    * <b>BLOCKING</b> convert account to multisig account
    * 
    * @param account the account that will be converted to multisignature account
    * @param cosigners account cosigners
    * @param minApprovals minimum approvals for transactions
    * @param minRemovals minimum approvals for removal of cosigner
    * @param optinBlocks number of blocks to wait for cosigner opt-in
    * @param confirmationTimeoutSeconds timeout before transaction expires
    */
   public void accountToMultisig(Account account, List<PublicAccount> cosigners, int minApprovals, int minRemovals,
         int optinBlocks, int confirmationTimeoutSeconds) {
      // first prepare the actual transaction to change account to multisig account
      ModifyMultisigAccountTransaction multisigChangeTrans = transact.multisigModification()
            .changeToMultisig(cosigners, minApprovals, minRemovals).build();
      // aggregate bonded transaction is required for cosigner opt-in so use that
      announceAsAggregateBonded(account,
            optinBlocks,
            confirmationTimeoutSeconds,
            multisigChangeTrans.toAggregate(account.getPublicAccount()));
   }

   /**
    * <b>BLOCKING</b> NOT SUPPORTED YET! activate delegated harvesting for an account
    * 
    * @param account the account that will get delegated harvesting activated
    * @param nodePublicKey node public key (use {@link BlockchainRepository#getNodeInfo()} or /node/info)
    * @param confirmationTimeoutSeconds transaction timeout in seconds
    * @return the remote account which is used for delegated harvesting
    * @deprecated not supported yet
    */
   @Deprecated
   public Account activateDelegatedHarvesting(Account account, String nodePublicKey, int confirmationTimeoutSeconds) {
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
      transactionConfirmed(transaction, account, confirmationTimeoutSeconds);
      // upon successful execution return the remote account
      return remoteAccount;
   }
}
