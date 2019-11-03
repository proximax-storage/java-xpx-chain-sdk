/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.transaction.AccountLinkAction;
import io.proximax.sdk.model.transaction.AccountLinkTransaction;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;

/**
 * <p>
 * builder for {@link AccountLinkTransaction}
 * </p>
 * <p>
 * Standard use: call {@link #link(PublicAccount)} or {@link #unlink(PublicAccount)} methods to specify the action that
 * will be performed
 * </p>
 */
public class AccountLinkTransactionBuilder
      extends TransactionBuilder<AccountLinkTransactionBuilder, AccountLinkTransaction> {

   private PublicAccount remoteAccount;
   private AccountLinkAction action;

   public AccountLinkTransactionBuilder() {
      super(EntityType.ACCOUNT_LINK, EntityVersion.ACCOUNT_LINK.getValue());
   }

   @Override
   protected AccountLinkTransactionBuilder self() {
      return this;
   }

   @Override
   public AccountLinkTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(AccountLinkTransaction.calculatePayloadSize()));
      // create transaction instance
      return new AccountLinkTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getRemoteAccount(), getAction());
   }

   // ------------------------------------- setters ---------------------------------------------//
   /**
    * define the remote account to link/unlink to/from
    * 
    * @param remoteAccount linked account
    * @return self
    */
   public AccountLinkTransactionBuilder remoteAccount(PublicAccount remoteAccount) {
      this.remoteAccount = remoteAccount;
      return self();
   }

   /**
    * the link/unlink action to perform
    * 
    * @param action the action
    * @return self
    */
   public AccountLinkTransactionBuilder action(AccountLinkAction action) {
      this.action = action;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the remoteAccount
    */
   public PublicAccount getRemoteAccount() {
      return remoteAccount;
   }

   /**
    * @return the action
    */
   public AccountLinkAction getAction() {
      return action;
   }

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * link remote account
    * 
    * @param remoteAccount the account
    * @return self
    */
   public AccountLinkTransactionBuilder link(PublicAccount remoteAccount) {
      return action(AccountLinkAction.LINK).remoteAccount(remoteAccount);
   }

   /**
    * unlink remote account
    * 
    * @param remoteAccount the account
    * @return self
    */
   public AccountLinkTransactionBuilder unlink(PublicAccount remoteAccount) {
      return action(AccountLinkAction.UNLINK).remoteAccount(remoteAccount);
   }
}
