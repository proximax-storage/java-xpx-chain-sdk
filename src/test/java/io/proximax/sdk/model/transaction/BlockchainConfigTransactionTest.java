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
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link BlockchainConfigTransaction} tests
 */
class BlockchainConfigTransactionTest extends ResourceBasedTest {

   @Test
   void testConstructor() throws IOException {
      String conf = getResourceAsString("config/blockchain.properties");
      String entities = getResourceAsString("config/entities.json");
      BlockchainConfigTransaction trans = BlockchainConfigTransaction.create(
            BigInteger.valueOf(3),
            conf,
            entities,
            NetworkType.MIJIN,
            new FakeDeadline());

      assertEquals(BigInteger.valueOf(3), trans.getApplyHeightDelta());
      assertEquals(conf, trans.getBlockchainConfig());
      assertEquals(entities, trans.getSupportedEntityVersions());
   }

   @Test
   void serialization() throws IOException {
      String conf = getResourceAsString("config/blockchain.properties");
      String entities = getResourceAsString("config/entities.json");
      BlockchainConfigTransaction trans = BlockchainConfigTransaction.create(
            BigInteger.valueOf(3),
            conf,
            entities,
            NetworkType.MIJIN,
            new FakeDeadline());
      
      byte[] actual = trans.generateBytes();
//      saveBytes("config_transaction", actual);
      assertArrayEquals(loadBytes("config_transaction"), actual);
   }
}
