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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.infrastructure.AccountHttp;
import io.proximax.sdk.infrastructure.BlockchainHttp;
import io.proximax.sdk.infrastructure.Listener;
import io.proximax.sdk.infrastructure.MetadataHttp;
import io.proximax.sdk.infrastructure.MosaicHttp;
import io.proximax.sdk.infrastructure.NamespaceHttp;
import io.proximax.sdk.infrastructure.NetworkHttp;
import io.proximax.sdk.infrastructure.TransactionHttp;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.Deadline;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.SignedTransaction;
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

   protected NetworkHttp networkHttp;
   protected BlockchainHttp blockchainHttp;
   protected AccountHttp accountHttp;
   protected TransactionHttp transactionHttp;
   protected MosaicHttp mosaicHttp;
   protected NamespaceHttp namespaceHttp;
   protected MetadataHttp metadataHttp;
   
   protected Listener listener;

   protected Account seedAccount;

   protected Collection<Disposable> disposables = new LinkedList<>();
   
   @BeforeAll
   void setup() throws ExecutionException, InterruptedException, IOException {
      String nodeUrl = this.getNodeUrl();
      logger.info("Preparing tests for {} using {}", NETWORK_TYPE, nodeUrl);
      // create HTTP APIs
      networkHttp = new NetworkHttp(nodeUrl);
      transactionHttp = new TransactionHttp(nodeUrl, networkHttp);
      accountHttp = new AccountHttp(nodeUrl, networkHttp);
      blockchainHttp = new BlockchainHttp(nodeUrl, networkHttp);
      mosaicHttp = new MosaicHttp(nodeUrl, networkHttp);
      namespaceHttp = new NamespaceHttp(nodeUrl, networkHttp);
      metadataHttp = new MetadataHttp(nodeUrl, networkHttp);
      logger.info("Created HTTP interfaces");
      // prepare listener
      listener = new Listener(nodeUrl);
      listener.open().get();
      logger.info("Created listener");
      // retrieve the seed account which has for tests
      seedAccount = getSeedAccount(NETWORK_TYPE);
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
   protected Deadline getDeadline() {
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
      TransferTransaction transfer = TransferTransaction
            .create(getDeadline(), recipient, Collections.singletonList(mosaicToTransfer), PlainMessage.Empty, NETWORK_TYPE);
      SignedTransaction signedTransfer = sender.sign(transfer);
      logger.info("Sent XPX to {}: {}", recipient.pretty(), transactionHttp.announce(signedTransfer).blockingFirst());
      logger.info("request confirmed: {}", listener.confirmed(sender.getAddress()).timeout(1, TimeUnit.MINUTES).blockingFirst());
      BigInteger endAmount = accountHttp.getAccountInfo(recipient).map(acct -> acct.getMosaics().get(0).getAmount())
            .blockingFirst();
      BigInteger mosaicScale = mosaicToTransfer.getAmount().divide(bigAmount);
      assertTrue(bigAmount.multiply(mosaicScale).compareTo(endAmount) <= 0);
   }
}
