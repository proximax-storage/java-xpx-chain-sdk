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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * E2E tests that demonstrate transfers
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EMosaicTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EMosaicTest.class);

   @BeforeAll
   void initStuff() {
   }

   @AfterAll
   void closeDown() {

   }

   @Test
   void test01CreateMosaic() throws InterruptedException {
      Thread.sleep(2000l);
      logger.info("Going to create mosaic");
      // create mosaic
      MosaicProperties mosaicProperties = new MosaicProperties(false, false, false, 1000000, BigInteger.valueOf(100));
      MosaicDefinitionTransaction mosaicDefTrans = MosaicDefinitionTransaction.create(1, seedAccount.getPublicKey(), getDeadline(), mosaicProperties, NETWORK_TYPE);
      SignedTransaction mosaicDefTransSigned = seedAccount.sign(mosaicDefTrans);
      transactionHttp.announce(mosaicDefTransSigned).blockingFirst();
      logger.info("Defined mosaic. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(30, TimeUnit.SECONDS).blockingFirst());
      // check the mosaic
      
   }

}
