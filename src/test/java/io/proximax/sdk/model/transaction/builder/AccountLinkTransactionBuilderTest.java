/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.AccountLinkAction;
import io.proximax.sdk.model.transaction.AccountLinkTransaction;

/**
 * {@link AccountLinkTransactionBuilder} tests
 */
class AccountLinkTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private AccountLinkTransactionBuilder builder;
   
   @BeforeEach
   void setUp() {
      builder = new AccountLinkTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void testLink() {
      PublicAccount pa = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      AccountLinkTransaction trans = builder.link(pa).build();
      
      assertEquals(AccountLinkAction.LINK, trans.getAction());
      assertEquals(pa, trans.getRemoteAccount());
   }

   @Test
   void testUnlink() {
      PublicAccount pa = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      AccountLinkTransaction trans = builder.unlink(pa).build();
      
      assertEquals(AccountLinkAction.UNLINK, trans.getAction());
      assertEquals(pa, trans.getRemoteAccount());
   }

}
