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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.infrastructure.QueryParams;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.props.AccountProperties;
import io.proximax.sdk.model.account.props.AccountProperty;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyModificationType;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.Transaction;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2EAccountTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ETransferTest.class);
   
   private final Account simpleAccount = new Account(new KeyPair(), getNetworkType());

   @BeforeAll
   void addListener() {
      logger.info("Sending transactions to {}", simpleAccount);
      signup(simpleAccount.getAddress());
      // make transfer to and from the test account to make sure it has public key announced
      sendSomeCash(seedAccount, simpleAccount.getAddress(), 1);
      sendSomeCash(simpleAccount, seedAccount.getAddress(), 1);
   }

   
   @Test
   void addBlockAccountProperty() {
      Account blocked = new Account(new KeyPair(), getNetworkType());
      signup(blocked.getAddress());
      logger.info("going to block {} by {}", blocked.getPublicAccount(), simpleAccount.getPublicAccount());
      ModifyAccountPropertyTransaction<Address> trans = ModifyAccountPropertyTransaction.createForAddress(
            getDeadline(),
            BigInteger.ZERO,
            AccountPropertyType.BLOCK_ADDRESS,
            Arrays.asList(new AccountPropertyModification<>(AccountPropertyModificationType.ADD, blocked.getAddress())),
            getNetworkType());
      // announce the transaction
      transactionHttp.announce(trans.signWith(simpleAccount)).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // now check for the block
      //23:16:55.006 INFO  going to block PublicAccount [address=Address [address=SCWH75VDZQ7IS32FUCT7ZV47KDJW4UOZUYPNFMWQ, networkType=MIJIN_TEST], publicKey=E36A049A8BA31BF6737053B7AD1D2E7299AFB5011EC5AE0726CD4225F0E5FBAE] by PublicAccount [address=Address [address=SCDNOAG4ONQFCSADAKJWGFXBLUBKWKTONSGALJO2, networkType=MIJIN_TEST], publicKey=EF84BC4E34B190B17F951E779D135BB1A48FBF8252A26C67D9E39C5F7132EEEA]
      AccountProperties aps = accountHttp.getAccountProperty(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      boolean gotMatch = false;
      for (AccountProperty ap: aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.BLOCK_ADDRESS)) {
            gotMatch = true;
         }
      }
   }
   
    @Test
    void getAccountInfo() throws ExecutionException, InterruptedException {
        AccountInfo accountInfo = accountHttp
                .getAccountInfo(simpleAccount.getAddress())
                .toFuture()
                .get();

        assertEquals(simpleAccount.getPublicKey(), accountInfo.getPublicKey());
    }

    @Test
    void getAccountsInfo() {
        List<String> accountKeys = accountHttp
                .getAccountsInfo(Arrays.asList(simpleAccount.getAddress(), seedAccount.getAddress()))
                .flatMapIterable(list -> list)
                .map(AccountInfo::getPublicKey)
                .toList()
                .blockingGet();

        assertEquals(2, accountKeys.size());
        assertTrue(accountKeys.contains(simpleAccount.getPublicKey()));
        assertTrue(accountKeys.contains(seedAccount.getPublicKey()));
    }

    @Test
    void transactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .transactions(simpleAccount.getPublicAccount())
                .toFuture()
                .get();

        assertEquals(2, transactions.size());
    }

    @Test
    void transactionsWithPagination() throws ExecutionException, InterruptedException {
       // get list of transactions
        List<Transaction> transactions = accountHttp
                .transactions(simpleAccount.getPublicAccount())
                .toFuture()
                .get();
        // there should be 2 as we did transfer to and from the account previously
        assertEquals(2, transactions.size());
        // now make another request for page size 1 and start after first transaction ID in the list
        List<Transaction> nextTransactions = accountHttp
                .transactions(simpleAccount.getPublicAccount(), new QueryParams(1, transactions.get(0).getTransactionInfo().get().getId().get()))
                .toFuture()
                .get();
        // the result should be page with one item which is the second transaction
        assertEquals(1, nextTransactions.size());
        assertEquals(transactions.get(1).getTransactionInfo().get().getHash(), nextTransactions.get(0).getTransactionInfo().get().getHash());
        // now try another request and start after second item which is last
        List<Transaction> noTransactions = accountHttp
              .transactions(simpleAccount.getPublicAccount(), new QueryParams(1, transactions.get(1).getTransactionInfo().get().getId().get()))
              .toFuture()
              .get();
      // the result should be page with no items because we skipped both transactions
      assertEquals(0, noTransactions.size());

    }

    @Test
    void incomingTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .incomingTransactions(simpleAccount.getPublicAccount())
                .toFuture()
                .get();

        assertEquals(1, transactions.size());
    }

    @Test
    void outgoingTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .outgoingTransactions(simpleAccount.getPublicAccount())
                .toFuture()
                .get();

        assertEquals(1, transactions.size());
    }

    @Test
    void unconfirmedTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .unconfirmedTransactions(simpleAccount.getPublicAccount())
                .toFuture()
                .get();

        assertEquals(0, transactions.size());
    }

    @Test
    void throwExceptionWhenBlockDoesNotExists() {
        accountHttp.getAccountInfo(Address.createFromRawAddress("SARDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY"))
//                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }
}