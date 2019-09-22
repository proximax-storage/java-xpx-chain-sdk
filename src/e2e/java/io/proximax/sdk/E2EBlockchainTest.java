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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.proximax.sdk.infrastructure.QueryParams;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainConfig;
import io.proximax.sdk.model.blockchain.BlockchainUpgrade;
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.BlocksLimit;
import io.proximax.sdk.model.blockchain.MerklePath;
import io.proximax.sdk.model.blockchain.Receipts;
import io.proximax.sdk.model.transaction.BlockchainConfigTransaction;
import io.proximax.sdk.model.transaction.BlockchainUpgradeTransaction;
import io.proximax.sdk.model.transaction.Transaction;

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
      QueryParams queryParams = new QueryParams(15);
      List<Transaction> transactions = blockchainHttp.getBlockTransactions(BigInteger.ONE, queryParams).blockingFirst();
      assertTrue(!transactions.isEmpty());
      assertTrue(transactions.size() <= 15);
   }

   @Test
   void blocksShouldBeAdded() {
      BigInteger height1 = blockchainHttp.getBlockchainHeight().blockingFirst();
      logger.info("Waiting for next block");
      listener.newBlock().timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      BigInteger height2 = blockchainHttp.getBlockchainHeight().blockingFirst();
      // it should go up
      assertTrue(height1.compareTo(height2) < 0);
   }

   @Test
   void checkScore() {
      blockchainHttp.getBlockchainScore().blockingFirst();
   }

   @Test
   void checkStorage() {
      blockchainHttp.getBlockchainStorage().blockingFirst();
   }

   @Test
   void nemesisNotEmpty() {
      assertTrue(!blockchainHttp.getBlockTransactions(BigInteger.ONE).blockingFirst().isEmpty());
   }

   @Test
   void retrieveNodeInfo() {
      assertTrue(blockchainHttp.getNodeInfo().blockingFirst().getPublicKey() != null);
   }

   @Test
   void retrieveNodeTime() {
      assertTrue(blockchainHttp.getNodeTime().blockingFirst().getReceiveTimestamp() != null);
   }

   @Test
   void networkTypeIsAsDeclared() {
      assertEquals(getNetworkType(), blockchainHttp.getNetworkType().blockingFirst());
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

   @Test
   @Disabled("something weird happening there")
   void recentBlocksWithLimit() {
      BigInteger height = blockchainHttp.getBlockchainHeight().blockingFirst();
      BlocksLimit limit = BlocksLimit.LIMIT_25;
      List<BlockInfo> blocks = blockchainHttp.getBlocksByHeightWithLimit(height, limit).blockingFirst();
      // there can not be 25 blocks if we started from last one
      assertTrue(blocks.size() < limit.getLimit(), blocks.size() + " is expected to be less than " + limit.getLimit());
   }

   @Test
   void checkBlockchainConfiguration() throws IOException {
      BlockchainConfig config = blockchainHttp.getBlockchainConfiguration(BigInteger.ONE).blockingFirst();
      Properties props = new Properties();
      props.load(new ByteArrayInputStream(config.getConfig().getBytes(StandardCharsets.UTF_8)));
      JsonObject entities = new Gson().fromJson(config.getSupportedEntityVersions(), JsonObject.class);
      // make some assertions but fact that we got here is good sign
      assertEquals(BigInteger.ONE, config.getHeight());
      assertNotNull(props.getProperty("namespaceRentalFeeSinkPublicKey"));
      assertNotNull(entities.getAsJsonArray("entities").get(0).getAsJsonObject().get("name"));
   }

   @Test
   void checkBlockchainUpgrade() {
      BlockchainUpgrade upgrade = blockchainHttp.getBlockchainUpgrade(BigInteger.ONE).blockingFirst();
      assertEquals(BigInteger.ONE, upgrade.getHeight());
      assertNotNull(upgrade.getVersion());
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

   @Test
   @Disabled("Requires nemesis private key to be defined")
   void configTransaction() {
      Account nemesis = Account.createFromPrivateKey(NEMESIS_PRIVATE_KEY, getNetworkType());
      BigInteger height = blockchainHttp.getBlockchainHeight().blockingFirst();
      BlockchainConfig configuration = blockchainHttp.getBlockchainConfiguration(height).blockingFirst();
      String conf = configuration.getConfig();
      String entities = configuration.getSupportedEntityVersions();
      // prepare transaction
      BlockchainConfigTransaction trans = transact.blockchainConfig().applyHeightDelta(BigInteger.valueOf(3))
            .blockchainConfig(conf).supportedEntityVersions(entities).build();
      transactionHttp.announce(api.sign(trans, nemesis)).blockingFirst();
      listener.confirmed(nemesis.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
   }
}
