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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.DeadlineRaw;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransactionDeadline;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;
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
  
   protected static final BigInteger DEFAULT_DEADLINE_DURATION = BigInteger.valueOf(60*60*1000);

   protected BlockchainApi api;
   protected BlockchainRepository blockchainHttp;
   protected AccountRepository accountHttp;
   protected TransactionRepository transactionHttp;
   protected MosaicRepository mosaicHttp;
   protected NamespaceRepository namespaceHttp;
   protected NetworkRepository networkHttp;
   protected NodeRepository nodeHttp;
   protected MetadataRepository metadataHttp;

   protected TransactionBuilderFactory transact;

   protected ListenerRepository listener;

   protected Account seedAccount;

   protected Collection<Disposable> disposables = new LinkedList<>();
   
   @BeforeAll
   void setup() throws ExecutionException, InterruptedException, IOException {
      String nodeUrl = this.getNodeUrl();
      logger.info("Preparing tests for {} using {}", getNetworkType(), nodeUrl);
      // create HTTP APIs
      api = new BlockchainApi(new URL(nodeUrl), getNetworkType());
      // make sure all is OK
      assertTrue(api.isNetworkTypeValid());
      // create services
      blockchainHttp = api.createBlockchainRepository();
      accountHttp = api.createAccountRepository();
      transactionHttp = api.createTransactionRepository();
      mosaicHttp = api.createMosaicRepository();
      namespaceHttp = api.createNamespaceRepository();
      networkHttp = api.createNetworkRepository();
      nodeHttp = api.createNodeRepository();
      metadataHttp = api.createMetadataRepository();
      logger.info("Created HTTP interfaces");
      // create and initialize transaction factory
      transact = api.transact();
      transact.setDeadlineMillis(DEFAULT_DEADLINE_DURATION);
      transact.setFeeCalculationStrategy(FeeCalculationStrategy.ZERO);
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
      return DeadlineRaw.startNow(DEFAULT_DEADLINE_DURATION);
   }
   

   /**
    * subscribe to all channels for the address
    * 
    * @param addr
    */
   protected void signup(Address addr) {
      // output nothing by default
      Consumer<? super Object> logAll = (obj) -> logger.debug("listener fired: {}", obj);
      Consumer<? super Object> logStatus = (obj) -> logger.warn("status fired: {}", obj);
      disposables.add(listener.status(addr).subscribe(logStatus, logStatus));
      disposables.add(listener.unconfirmedAdded(addr).subscribe(logAll, logAll));
      disposables.add(listener.unconfirmedRemoved(addr).subscribe(logAll, logAll));
      disposables.add(listener.aggregateBondedAdded(addr).subscribe(logAll, logAll));
      disposables.add(listener.aggregateBondedRemoved(addr).subscribe(logAll, logAll));
      disposables.add(listener.cosignatureAdded(addr).subscribe(logAll, logAll));
      disposables.add(listener.confirmed(addr).subscribe(logAll, logAll));
   }
   /**
    * send XPX from account to recipient
    * 
    * @param recipient address who gets the funds
    * @param amount amount of XPX taking the divisibility into account
    */
   protected void sendSomeCash(Account sender, Address recipient, long amount) {
      BigInteger bigAmount = BigInteger.valueOf(amount);
      Mosaic mosaicToTransfer = NetworkCurrencyMosaic.createRelative(BigDecimal.valueOf(amount));
      sendMosaic(sender, recipient, mosaicToTransfer);
      BigInteger endAmount = accountHttp.getAccountInfo(recipient).map(acct -> acct.getMosaics().get(0).getAmount()).blockingFirst();
      BigInteger mosaicScale = mosaicToTransfer.getAmount().divide(bigAmount);
      assertTrue(bigAmount.multiply(mosaicScale).compareTo(endAmount) <= 0);
   }
   
   /**
    * send XPX from account to recipient
    * 
    * @param recipient address who gets the funds
    * @param mosaicToTransfer amount of XPX taking the divisibility into account
    */
   protected void sendMosaic(Account sender, Address recipient, Mosaic mosaicToTransfer) {
      TransferTransaction transfer = api.transact().transfer().mosaics(mosaicToTransfer).to(recipient)
            .maxFee(BigInteger.ZERO).deadline(getDeadline()).build();
      SignedTransaction signedTransfer = sender.sign(transfer, api.getNetworkGenerationHash());
      logger.info("Sent XPX to {}: {}", recipient.pretty(), transactionHttp.announce(signedTransfer).blockingFirst());
      logger.info("request confirmed: {}",
            listener.confirmed(sender.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
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
         if (!"404 Not Found".equals(e.getMessage())) {
            fail(e);
         }
      }
   }
   
   /**
    * convenience sleep needed to work around server listener synchronization issues
    */
   protected void sleepForAWhile() {
      try {
         Thread.sleep(3000l);
      } catch (InterruptedException e) {
         // do nothing
      }
   }
}
