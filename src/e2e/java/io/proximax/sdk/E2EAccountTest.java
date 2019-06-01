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
import io.proximax.sdk.gen.model.UInt64DTO;
import io.proximax.sdk.infrastructure.QueryParams;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.props.AccountProperties;
import io.proximax.sdk.model.account.props.AccountProperty;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyModificationType;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.utils.dto.UInt64Utils;

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
      Account acct = new Account(new KeyPair(), getNetworkType());
      Account blocked = new Account(new KeyPair(), getNetworkType());
      signup(acct.getAddress());
      signup(blocked.getAddress());
      logger.info("going to block {} by {}", blocked.getPublicAccount(), acct.getPublicAccount());
      ModifyAccountPropertyTransaction<Address> trans = ModifyAccountPropertyTransaction.createForAddress(getDeadline(),
            BigInteger.ZERO,
            AccountPropertyType.BLOCK_ADDRESS,
            Arrays.asList(new AccountPropertyModification<>(AccountPropertyModificationType.ADD, blocked.getAddress())),
            getNetworkType());
      // announce the transaction
      transactionHttp.announce(trans.signWith(acct)).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(acct.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // now check for the block via GET
      AccountProperties aps = accountHttp.getAccountProperties(acct.getAddress())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      testAccountProperties(aps, blocked.getAddress());
      // check for block via POST
      List<AccountProperties> apsList = accountHttp.getAccountProperties(Arrays.asList(acct.getAddress()))
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, apsList.size());
      testAccountProperties(apsList.get(0), blocked.getAddress());
   }
   
   @Test
   void addAllowMosaicProperty() {
      Account acct = new Account(new KeyPair(), getNetworkType());
      signup(acct.getAddress());
      MosaicId allowedMosaic = NetworkCurrencyMosaic.ID;
      logger.info("going to allow {} by {}", allowedMosaic, acct.getPublicAccount());
      ModifyAccountPropertyTransaction<MosaicId> trans = ModifyAccountPropertyTransaction.createForMosaic(getDeadline(),
            BigInteger.ZERO,
            AccountPropertyType.ALLOW_MOSAIC,
            Arrays.asList(AccountPropertyModification.add(allowedMosaic)),
            getNetworkType());
      // announce the transaction
      transactionHttp.announce(trans.signWith(acct)).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(acct.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // now check for the block via GET
      AccountProperties aps = accountHttp.getAccountProperties(acct.getAddress())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      testAccountPropertiesOnSimpleAccount(aps, allowedMosaic);
      // check for block via POST
      List<AccountProperties> apsList = accountHttp.getAccountProperties(Arrays.asList(acct.getAddress()))
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, apsList.size());
      testAccountPropertiesOnSimpleAccount(apsList.get(0), allowedMosaic);
   }
   
   @Test
   void addAllowEntityTypeProperty() {
      Account acct = new Account(new KeyPair(), getNetworkType());
      signup(acct.getAddress());
      TransactionType allowedTransType = TransactionType.ACCOUNT_PROPERTIES_ENTITY_TYPE;
      logger.info("going to allow {} by {}", allowedTransType, acct.getPublicAccount());
      ModifyAccountPropertyTransaction<TransactionType> trans = ModifyAccountPropertyTransaction.createForEntityType(getDeadline(),
            BigInteger.ZERO,
            AccountPropertyType.ALLOW_TRANSACTION,
            Arrays.asList(AccountPropertyModification.add(allowedTransType)),
            getNetworkType());
      // announce the transaction
      transactionHttp.announce(trans.signWith(acct)).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(acct.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // now check for the block via GET
      AccountProperties aps = accountHttp.getAccountProperties(acct.getAddress())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      testAccountPropertiesOnSimpleAccount(aps, allowedTransType);
      // check for block via POST
      List<AccountProperties> apsList = accountHttp.getAccountProperties(Arrays.asList(acct.getAddress()))
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, apsList.size());
      testAccountPropertiesOnSimpleAccount(apsList.get(0), allowedTransType);
   }
   
   /**
    * check that address block is as expected
    * 
    * @param aps account properties
    * @param blockedAddress address that is blocked
    */
   private void testAccountProperties(AccountProperties aps, Address blockedAddress) {
      boolean gotMatch = false;
      for (AccountProperty ap: aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.BLOCK_ADDRESS)) {
            for (Object value: ap.getValues()) {
               // value should be string and should represent encoded address of the blocked account
               if (value instanceof String && blockedAddress.equals(Address.createFromEncoded((String)value))) {
                     gotMatch = true;
               }
            }
            
         }
      }
      assertTrue(gotMatch);
   }
   
   /**
    * check that simple account has block as expected
    * 
    * @param aps account properties
    * @param blockedAddress address that is blocked
    */
   private void testAccountPropertiesOnSimpleAccount(AccountProperties aps, MosaicId allowedMosaic) {
      boolean gotMatch = false;
      for (AccountProperty ap: aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.ALLOW_MOSAIC)) {
            for (Object value: ap.getValues()) {
               logger.info("{}", value);
               // value should be string and should represent encoded address of the blocked account
               if (value instanceof List) {
                     UInt64DTO dto = new UInt64DTO();
                     dto.addAll((List<Long>)value);
                     MosaicId retrievedMosaic = new MosaicId(UInt64Utils.toBigInt(dto));
                     if (retrievedMosaic.equals(allowedMosaic)) {
                        gotMatch = true;
                     }
               }
            }
            
         }
      }
      assertTrue(gotMatch);
   }

   /**
    * check that simple account has block as expected
    * 
    * @param aps account properties
    * @param blockedAddress address that is blocked
    */
   private void testAccountPropertiesOnSimpleAccount(AccountProperties aps, TransactionType allowedTransactionType) {
      boolean gotMatch = false;
      for (AccountProperty ap: aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.ALLOW_TRANSACTION)) {
            for (Object value : ap.getValues()) {
               try {
                  if (value instanceof Long && isValidTransactionTypeCode(((Long) value).intValue())) {
                     assertEquals(TransactionType.ACCOUNT_PROPERTIES_ENTITY_TYPE, TransactionType.rawValueOf(((Long) value).intValue()));
                     gotMatch = true;
                  }
               } catch (RuntimeException e) {
                  // do nothing just ignore
               }
            }
            
         }
      }
      assertTrue(gotMatch);
   }
   
   private static boolean isValidTransactionTypeCode(int code) {
      try {
         TransactionType.rawValueOf(code);
         return true;
      } catch (RuntimeException e) {
         return false;
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
       sleepForAWhile();
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