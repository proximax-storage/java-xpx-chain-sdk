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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlocksLimit;
import io.proximax.sdk.model.blockchain.MerklePath;
import io.proximax.sdk.model.blockchain.Receipts;

/**
 * E2E tests that demonstrate blockchain repository
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EBlockchainTest extends E2EBaseTest {

   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EBlockchainTest.class);

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
      assertTrue(blockchainHttp.getNodeInfo().blockingFirst().getPublicKey()!=null);
   }
   
   @Test
   void retrieveNodeTime() {
      assertTrue(blockchainHttp.getNodeTime().blockingFirst().getReceiveTimestamp()!=null);
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
      MerklePath p = blockchainHttp.getReceiptMerklePath(BigInteger.valueOf(2l), "4B92740450C7FB9027AFF3E38625274211A166A820CF41E796F6808C52968F4C").blockingFirst();
      // block 2 has something
      assertEquals(0, p.getItems().size());
   }
   
   @Test
   void block1TransactionMerklePath() {
      MerklePath p = blockchainHttp.getTransactionMerklePath(BigInteger.valueOf(1l), "31537B377050C8CDF6E85B6D88A3B6BD94501B0A3C9387493B3973A4EDAD490E").blockingFirst();
      // block 2 has something
      assertEquals(5, p.getItems().size());
   }
   
   @Test
   void blocksWithLimit() {
      BlocksLimit limit = BlocksLimit.LIMIT_25;
      List<BlockInfo> blocks = blockchainHttp.getBlocksByHeightWithLimit(BigInteger.ONE, limit).blockingFirst();
      assertTrue(blocks.size() <= limit.getLimit());
   }
   
   @Test
   void recentBlocksWithLimit() {
      BigInteger height = blockchainHttp.getBlockchainHeight().blockingFirst();
      BlocksLimit limit = BlocksLimit.LIMIT_25;
      List<BlockInfo> blocks = blockchainHttp.getBlocksByHeightWithLimit(height, limit).blockingFirst();
      // there can not be 25 blocks if we started from last one
      assertTrue(blocks.size() < limit.getLimit(), blocks.size() + " is expected to be less than " + limit.getLimit());
   }
}
