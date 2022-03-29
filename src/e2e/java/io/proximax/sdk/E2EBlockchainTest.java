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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.infrastructure.QueryParams.Order;
import io.proximax.sdk.infrastructure.TransactionQueryParams;
import io.proximax.sdk.infrastructure.TransactionSortingField;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.BlocksLimit;
import io.proximax.sdk.model.blockchain.MerklePath;
import io.proximax.sdk.model.blockchain.Receipts;
import io.proximax.sdk.model.transaction.BlockchainUpgradeTransaction;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.TransactionSearch;

/**
 * E2E tests that demonstrate blockchain repository
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EBlockchainTest extends E2EBaseTest {

   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EBlockchainTest.class);

   private static final String NEMESIS_PRIVATE_KEY = "put nemesis private key here";

   @Test
   void blockByHeight() {
      BlockInfo blockInfo = blockchainHttp.getBlockByHeight(BigInteger.ONE).blockingFirst();
      assertEquals(BigInteger.ONE, blockInfo.getHeight());
   }

   @Test
   void listTransactions() {
      TransactionQueryParams queryParams = new TransactionQueryParams(0, 0, Order.ASC, null, null,
                        TransactionSortingField.BLOCK, null, null, null,
                        null,
                        null,
            null);
      TransactionSearch transactions = blockchainHttp.getBlockTransactions(BigInteger.ONE, queryParams).blockingFirst();

      assertEquals(9932, transactions.getPaginations().getTotalEntries());
      assertEquals(20, transactions.getTransactions().size());
      assertEquals(EntityType.rawValueOf(16973), transactions.getTransactions().get(19).getType());

   }

   @Test
   void checkStorage() {
      blockchainHttp.getBlockchainStorage().blockingFirst();
   }


   @Test
   void block1Receipts() {
      Receipts r = blockchainHttp.getBlockReceipts(BigInteger.ONE).blockingFirst();
      // block 1 is expected to have no receipts
      assertEquals(0, r.getTransactionStatements().size());
      assertEquals(0, r.getAddressResolutionStatements().size());
      assertEquals(0, r.getMosaicResolutionStatements().size());
   }

   @Test
   void block2Receipts() {
      Receipts r = blockchainHttp.getBlockReceipts(BigInteger.valueOf(2l)).blockingFirst();
      // block 2 has something
      assertEquals(1, r.getTransactionStatements().size());
      assertEquals(0, r.getAddressResolutionStatements().size());
      assertEquals(0, r.getMosaicResolutionStatements().size());
   }

   @Test
   void block2MerklePath() {
      // block 2 has receipts but no merkle path items
      checkBlockReceiptMerklePath(2l, 0);
   }

   private void checkBlockReceiptMerklePath(long blockHeight, int expectedItems) {
      BigInteger height = BigInteger.valueOf(blockHeight);
      BlockInfo block = blockchainHttp.getBlockByHeight(height).blockingFirst();
      MerklePath p = blockchainHttp
            .getReceiptMerklePath(height,
                  block.getBlockReceiptsHash().orElseThrow(() -> new RuntimeException("expected recepts hash")))
            .blockingFirst();
      // check number of items
      assertEquals(expectedItems, p.getItems().size());
   }

   @Test
   void blocksWithLimit() {
      BlocksLimit limit = BlocksLimit.LIMIT_25;
      List<BlockInfo> blocks = blockchainHttp.getBlocksByHeightWithLimit(BigInteger.ONE, limit).blockingFirst();
      assertTrue(blocks.size() <= limit.getLimit());
   }


  

  

   
   void loadTransactions(long block) {
      // try to load first 100 transactions and see if we can parse them
      TransactionSearch transactions = blockchainHttp.getBlockTransactions(BigInteger.valueOf(block), 
            new TransactionQueryParams(0, 0, Order.ASC, null, null,
                  TransactionSortingField.BLOCK, null, null, null,
                  null,
                  null,
                  null)).blockingFirst();
      assertTrue(transactions.getPaginations().getPageSize() <= 100);
   }
   
   /**
    * this test requires upgrade of nodes to version 1.2.3.4 after 1_000_000_000 blocks get generated => will break the
    * blockchain!!!!
    * 
    * To run it you need first to enter private key for nemesis account to constant NEMESIS_PRIVATE_KEY
    */
   @Test
   @Disabled("This test breaks blockchain after 1_000_000_000 blocks")
   void upgradeBlockchainVersion() {
      BlockchainVersion version = new BlockchainVersion(1, 2, 3, 4);
      Account nemesis = Account.createFromPrivateKey(NEMESIS_PRIVATE_KEY, getNetworkType());
      BigInteger upgradePeriod = BigInteger.valueOf(1_000_000_000l);
      BlockchainUpgradeTransaction trans = transact.blockchainUpgrade().upgradePeriod(upgradePeriod).newVersion(version)
            .build();
      transactionHttp.announce(api.sign(trans, nemesis)).blockingFirst();
      BlockchainUpgradeTransaction conFirmedTrans = (BlockchainUpgradeTransaction) listener
            .confirmed(nemesis.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(version, conFirmedTrans.getNewVersion());
      assertEquals(upgradePeriod, conFirmedTrans.getUpgradePeriod());
   }

}
