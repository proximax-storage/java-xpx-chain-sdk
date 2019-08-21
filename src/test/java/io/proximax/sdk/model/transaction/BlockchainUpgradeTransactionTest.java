/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link BlockchainUpgradeTransaction} tests
 */
class BlockchainUpgradeTransactionTest extends ResourceBasedTest {

   @Test
   void testConstructor() {
      BlockchainUpgradeTransaction trans = BlockchainUpgradeTransaction
            .create(BigInteger.ONE, new BlockchainVersion(1, 2, 3, 4), new FakeDeadline(), NetworkType.MIJIN);

      assertEquals(BigInteger.ONE, trans.getUpgradePeriod());
      assertEquals(new BlockchainVersion(1, 2, 3, 4), trans.getNewVersion());
   }

   @Test
   void serialization() throws IOException {
      BlockchainUpgradeTransaction trans = BlockchainUpgradeTransaction
            .create(BigInteger.ONE, new BlockchainVersion(1, 2, 3, 4), new FakeDeadline(), NetworkType.MIJIN);

      byte[] actual = trans.generateBytes();
//      saveBytes("blockchain_upgrade", actual);
      assertArrayEquals(actual, loadBytes("blockchain_upgrade"));
   }

}
