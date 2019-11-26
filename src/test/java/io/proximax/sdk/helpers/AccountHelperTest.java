/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.helpers.AccountHelper;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * TODO add proper description
 */
class AccountHelperTest {
   private BlockchainApi api;
   private AccountHelper helper;
   
   @BeforeEach
   void init() throws MalformedURLException {
      // specify both URL and network type so the network does not need to be accessed unless really needed
      api = new BlockchainApi(new URL("http://localhost:3000"), NetworkType.MAIN_NET);
      helper = new AccountHelper(api);
   }
   
   @AfterEach
   void cleanup() {
      this.api = null;
      this.helper = null;
   }

   @Test
   void testAccounts() {
      // make sure that when asking for 5 account 5 accounts are returned
      assertEquals(5, helper.randomAccount(5).size());
   }

   @Test
   @Disabled("not proper test - but cool anyway!")
   void findAddress() {
      // note this expects MAIN_NET as addresses there start by X
      // find account with address starting by XCHG (1M attempts should not take too long)
      Optional<Account> account = helper.randomAccount().parallel().limit(1_000_000).filter(acc -> acc.getAddress().plain().startsWith("XCHG")).findFirst();
      assertTrue(account.isPresent(), "Account was not found :(");
      System.out.println(account.orElseThrow(() -> new IllegalStateException("account not found :(")));
   }
}
