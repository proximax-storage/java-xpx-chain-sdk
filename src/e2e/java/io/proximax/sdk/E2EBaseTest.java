/*
 * Copyright 2019 ProximaX
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.infrastructure.Listener;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.Deadline;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransactionDeadline;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Base class for new set of tests that proof and demo the functionality
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EBaseTest extends BaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EBaseTest.class);

   protected NetworkRepository networkHttp;
   protected BlockchainRepository blockchainHttp;
   protected AccountRepository accountHttp;
   protected TransactionRepository transactionHttp;
   protected MosaicRepository mosaicHttp;
   protected NamespaceRepository namespaceHttp;
   protected MetadataRepository metadataHttp;
   
   protected Listener listener;

   protected Account seedAccount;

   protected Collection<Disposable> disposables = new LinkedList<>();
   
   @BeforeAll
   void setup() throws ExecutionException, InterruptedException, IOException {
      String nodeUrl = this.getNodeUrl();
      logger.info("Preparing tests for {} using {}", getNetworkType(), nodeUrl);
      // create HTTP APIs
      BlockchainApi api = new BlockchainApi(new URL(nodeUrl), getNetworkType());
      // make sure all is OK
      assertTrue(api.isNetworkTypeValid());
      // create services
      blockchainHttp = api.createBlockchainRepository();
      networkHttp = api.createNetworkRepository();
      accountHttp = api.createAccountRepository();
      transactionHttp = api.createTransactionRepository();
      mosaicHttp = api.createMosaicRepository();
      namespaceHttp = api.createNamespaceRepository();
      metadataHttp = api.createMetadataRepository();
      logger.info("Created HTTP interfaces");
      // prepare listener
      listener = api.createListener();
      listener.open().get();
      logger.info("Created listener");
      // retrieve the seed account which has for tests
      seedAccount = getSeedAccount();
      logger.info("Seed account: {}", seedAccount);
      // add listener to see account
      disposables.add(listener.status(seedAccount.getAddress())
            .subscribe(err -> logger.error("Operation failed: {}", err), t -> logger.error("exception thrown", t)));
      logger.info("Base initialized");
   }

   @AfterAll
   void cleanup() {
      logger.info("Cleaning up");
      listener.close();
      disposables.stream().forEach(Disposable::dispose);
      disposables.clear();
   }

   /**
    * create deadline of 5 minutes
    * 
    * @return deadline
    */
   protected TransactionDeadline getDeadline() {
      return new Deadline(5, ChronoUnit.MINUTES);
   }
   

   /**
    * subscribe to all channels for the address
    * 
    * @param addr
    */
   protected void signup(Address addr) {
      // output nothing by default
      Consumer<? super Object> logme = (obj) -> logger.trace("listener fired: {}", obj);
      disposables.add(listener.status(addr).subscribe(logme, logme));
      disposables.add(listener.unconfirmedAdded(addr).subscribe(logme, logme));
      disposables.add(listener.unconfirmedRemoved(addr).subscribe(logme, logme));
      disposables.add(listener.aggregateBondedAdded(addr).subscribe(logme, logme));
      disposables.add(listener.aggregateBondedRemoved(addr).subscribe(logme, logme));
      disposables.add(listener.cosignatureAdded(addr).subscribe(logme, logme));
      disposables.add(listener.confirmed(addr).subscribe(logme, logme));
   }
   /**
    * send XPX from account to recipient
    * 
    * @param recipient address who gets the funds
    * @param amount amount of XPX taking the divisibility into account
    */
   protected void sendSomeCash(Account sender, Address recipient, long amount) {
      BigInteger bigAmount = BigInteger.valueOf(amount);
      Mosaic mosaicToTransfer = NetworkCurrencyMosaic.createRelative(bigAmount);
      sendMosaic(sender, recipient, mosaicToTransfer);
      BigInteger endAmount = accountHttp.getAccountInfo(recipient).map(acct -> acct.getMosaics().get(0).getAmount())
            .blockingFirst();
      BigInteger mosaicScale = mosaicToTransfer.getAmount().divide(bigAmount);
      assertTrue(bigAmount.multiply(mosaicScale).compareTo(endAmount) <= 0);
   }
   
   /**
    * send XPX from account to recipient
    * 
    * @param recipient address who gets the funds
    * @param amount amount of XPX taking the divisibility into account
    */
   protected void sendMosaic(Account sender, Address recipient, Mosaic mosaicToTransfer) {
      TransferTransaction transfer = TransferTransaction
            .create(getDeadline(), recipient, Collections.singletonList(mosaicToTransfer), PlainMessage.Empty, getNetworkType());
      SignedTransaction signedTransfer = sender.sign(transfer);
      logger.info("Sent XPX to {}: {}", recipient.pretty(), transactionHttp.announce(signedTransfer).blockingFirst());
      logger.info("request confirmed: {}", listener.confirmed(sender.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
   }
   
   /**
    * return all mosaics to seed account
    * 
    * @param from account to be cleaned
    */
   protected void returnAllToSeed(Account from) {
      logger.info("removing mosaics from {}", from.getPublicAccount());
      try {
         List<Mosaic> mosaics = accountHttp.getAccountInfo(from.getAddress()).blockingFirst().getMosaics();
         mosaics.forEach(mosaic -> {
            sendMosaic(from, seedAccount.getAddress(), mosaic);
         });
      } catch (RuntimeException e) {
         if (!"Not Found".equals(e.getMessage())) {
            fail(e);
         }
      }
   }
}
