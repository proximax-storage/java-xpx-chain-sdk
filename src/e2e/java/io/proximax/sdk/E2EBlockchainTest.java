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

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * E2E tests that demonstrate transfers
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


}
