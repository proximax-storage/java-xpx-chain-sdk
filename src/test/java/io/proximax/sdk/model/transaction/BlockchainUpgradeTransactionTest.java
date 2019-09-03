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
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link BlockchainUpgradeTransaction} tests
 */
class BlockchainUpgradeTransactionTest extends ResourceBasedTest {

   @Test
   void testConstructor() {
      BlockchainUpgradeTransaction trans = new BlockchainUpgradeTransaction(NetworkType.MIJIN, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), BigInteger.ONE,
            new BlockchainVersion(1, 2, 3, 4));

      assertEquals(BigInteger.ONE, trans.getUpgradePeriod());
      assertEquals(new BlockchainVersion(1, 2, 3, 4), trans.getNewVersion());
   }

   @Test
   void serialization() throws IOException {
      BlockchainUpgradeTransaction trans = new BlockchainUpgradeTransaction(NetworkType.MIJIN, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), BigInteger.ONE,
            new BlockchainVersion(1, 2, 3, 4));

      byte[] actual = trans.generateBytes();
//      saveBytes("blockchain_upgrade", actual);
      assertArrayEquals(actual, loadBytes("blockchain_upgrade"));
   }

   @Test
   void checkCopyToSigner() throws IOException {
      PublicAccount remoteAccount = new Account(new KeyPair(), NetworkType.MIJIN).getPublicAccount();
      
      BlockchainUpgradeTransaction trans = new BlockchainUpgradeTransaction(NetworkType.MIJIN, 1, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), BigInteger.ONE,
            new BlockchainVersion(1, 2, 3, 4));

      Transaction t = trans.copyForSigner(remoteAccount);
      assertEquals(Optional.of(remoteAccount), t.getSigner());
   }
}
