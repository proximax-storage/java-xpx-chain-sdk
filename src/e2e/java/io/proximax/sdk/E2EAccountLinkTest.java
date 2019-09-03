/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.transaction.AccountLinkTransaction;
import io.proximax.sdk.model.transaction.TransactionStatusError;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class E2EAccountLinkTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ETransferTest.class);

   private final Account localAccount = new Account(new KeyPair(), getNetworkType());
   private final Account remoteAccount = new Account(new KeyPair(), getNetworkType());

   @BeforeAll
   void addListener() {
      logger.info("Linking {} to {}", localAccount, remoteAccount);
      signup(localAccount.getAddress());
      signup(remoteAccount.getAddress());
   }

   @Test
   void test01linkAccounts() {
      linkAndTestAccounts(localAccount, remoteAccount);
   }

   @Test
   void test02unlinkAccounts() {
      AccountLinkTransaction link = transact.accountLink().unlink(remoteAccount.getPublicAccount()).build();
      transactionHttp.announce(api.sign(link, localAccount)).blockingFirst();
      listener.confirmed(localAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();

      sleepForAWhile();

      // test for the result
      AccountInfo localInfo = accountHttp.getAccountInfo(localAccount.getAddress()).blockingFirst();
      AccountInfo remoteInfo = accountHttp.getAccountInfo(remoteAccount.getAddress()).blockingFirst();
      assertEquals("0000000000000000000000000000000000000000000000000000000000000000",
            remoteInfo.getLinkedAccountKey());
      assertEquals("0000000000000000000000000000000000000000000000000000000000000000", localInfo.getLinkedAccountKey());
   }

   @Test
   void test03relinkAccounts() {
      linkAndTestAccounts(localAccount, remoteAccount);
   }

   @Test
   void test04overLinkAccounts() {
      Account remoteAccount2 = new Account(new KeyPair(), getNetworkType());
      AccountLinkTransaction link = transact.accountLink().link(remoteAccount2.getPublicAccount()).build();
      transactionHttp.announce(api.sign(link, localAccount)).blockingFirst();
      TransactionStatusError error = listener.status(localAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals("Failure_AccountLink_Link_Already_Exists", error.getStatus());
   }

   private void linkAndTestAccounts(Account localAccount, Account remoteAccount) {
      AccountLinkTransaction link = transact.accountLink().link(remoteAccount.getPublicAccount()).build();
      transactionHttp.announce(api.sign(link, localAccount)).blockingFirst();
      listener.confirmed(localAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();

      sleepForAWhile();

      // test for the result
      AccountInfo localInfo = accountHttp.getAccountInfo(localAccount.getAddress()).blockingFirst();
      AccountInfo remoteInfo = accountHttp.getAccountInfo(remoteAccount.getAddress()).blockingFirst();
      assertEquals(localAccount.getPublicKey(), remoteInfo.getLinkedAccountKey());
      assertEquals(remoteAccount.getPublicKey(), localInfo.getLinkedAccountKey());
   }

}