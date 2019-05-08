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

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * E2E tests that demonstrate transfers
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EMosaicTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EMosaicTest.class);

   @BeforeAll
   void addListener() {
   }

   @AfterAll
   void closeDown() {
   }

   @Test
   void createMosaic() {
      MosaicNonce nonce = MosaicNonce.createRandom();
      MosaicDefinitionTransaction mdt = MosaicDefinitionTransaction.create(nonce, new MosaicId(nonce, seedAccount.getPublicKey()),
            getDeadline(),
            new MosaicProperties(false, true, false, 6, BigInteger.valueOf(20)),
            NETWORK_TYPE);
      SignedTransaction signedMosaic = seedAccount.sign(mdt);
      transactionHttp.announce(signedMosaic).blockingFirst();
      logger.info("Mosaic created. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
   }

}
