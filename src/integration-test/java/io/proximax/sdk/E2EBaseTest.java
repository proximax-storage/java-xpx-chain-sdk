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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.infrastructure.AccountHttp;
import io.proximax.sdk.infrastructure.BlockchainHttp;
import io.proximax.sdk.infrastructure.Listener;
import io.proximax.sdk.infrastructure.MosaicHttp;
import io.proximax.sdk.infrastructure.TransactionHttp;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.XPX;
import io.proximax.sdk.model.transaction.Deadline;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.reactivex.disposables.Disposable;

/**
 * Base class for new set of tests that proof and demo the functionality
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EBaseTest extends BaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EBaseTest.class);

   protected BlockchainHttp blockchainHttp;
   protected AccountHttp accountHttp;
   protected TransactionHttp transactionHttp;
   protected MosaicHttp mosaicHttp;

   protected Listener listener;

   protected Account seedAccount;
   protected MosaicInfo mosaic;

   protected Collection<Disposable> disposables = new LinkedList<>();
   
   @BeforeAll
   void setup() throws ExecutionException, InterruptedException, IOException {
      String nodeUrl = this.getNodeUrl();
      logger.info("Preparing tests for {} using {}", NETWORK_TYPE, nodeUrl);
      // create HTTP APIs
      transactionHttp = new TransactionHttp(nodeUrl);
      accountHttp = new AccountHttp(nodeUrl);
      blockchainHttp = new BlockchainHttp(nodeUrl);
      mosaicHttp = new MosaicHttp(nodeUrl);
      // prepare listener
      listener = new Listener(nodeUrl);
      listener.open().get();
      // retrieve the seed account which has for tests
      seedAccount = getSeedAccount(NETWORK_TYPE);
      // get the mosaic that will be used here
      mosaic = getMosaic();
      // add listener to see account
      disposables.add(listener.status(seedAccount.getAddress())
            .subscribe(err -> logger.error("Operation failed: {}", err), t -> logger.error("exception thrown", t)));
   }

   @AfterAll
   void cleanup() {
      logger.info("Cleaning up");
      listener.close();
      disposables.stream().forEach(Disposable::dispose);
      disposables.clear();
   }

   /**
    * retrieve mosaic from seed account that will be used to transfer funds
    * 
    * @return
    * @throws InterruptedException
    * @throws ExecutionException
    */
   private MosaicInfo getMosaic() throws InterruptedException, ExecutionException {
      // check that account actually has some funds
      AccountInfo seedAccountInfo = accountHttp.getAccountInfo(seedAccount.getAddress()).toFuture().get();
      List<MosaicId> seedMosaicIDs = seedAccountInfo.getMosaics().stream().map(Mosaic::getId)
            .collect(Collectors.toList());
      List<MosaicInfo> mosaics = mosaicHttp.getMosaics(seedMosaicIDs).toFuture().get();
      logger.info("Seed account has following mosaics: {}", mosaics);
      // TODO pick the mosaic to be used here
      return mosaics.get(0);
   }

   /**
    * wait for next block and return the block info
    * 
    * @return block info
    * @throws InterruptedException when wait for block is interrupted
    * @throws ExecutionException when retrieval of block info fails
    */
   protected BlockInfo waitForBlock(int numberOfBlocks) throws InterruptedException, ExecutionException {
      logger.info("Waiting for {} blocks", numberOfBlocks);
      // wait for new block so we know all is on the blockchain
      return listener.newBlock().take(numberOfBlocks)
            .doOnNext((block) -> logger.info("Block created with height {}", block.getHeight())).blockingLast();
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
    * send XPX from account to recipient
    * 
    * @param recipient address who gets the funds
    * @param amount amount of XPX taking the divisibility into account
    */
   protected void sendSomeCash(Account sender, Address recipient, long amount) {
      BigInteger bigAmount = BigInteger.valueOf(amount);
      Mosaic mosaicToTransfer = XPX.createRelative(bigAmount);
      TransferTransaction transfer = TransferTransaction
            .create(getDeadline(), recipient, Collections.singletonList(mosaicToTransfer), PlainMessage.Empty, NETWORK_TYPE);
      SignedTransaction signedTransfer = sender.sign(transfer);
      logger.info("Sent XPX to {}: {}", recipient.pretty(), transactionHttp.announce(signedTransfer).blockingFirst());
      logger.info("request confirmed: {}", listener.confirmed(sender.getAddress()).blockingFirst());
      BigInteger endAmount = accountHttp.getAccountInfo(recipient).map(acct -> acct.getMosaics().get(0).getAmount())
            .blockingFirst();
      BigInteger mosaicScale = mosaicToTransfer.getAmount().divide(bigAmount);
      assertTrue(bigAmount.multiply(mosaicScale).compareTo(endAmount) <= 0);
   }
}
