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

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * test main API
 */
class BlockchainApiTest {
   private static final NetworkType NETWORK = NetworkType.MAIN_NET;
   
   @Test
   void testApiConstructor() throws MalformedURLException {
      BlockchainApi api = new BlockchainApi(new URL("http://localhost:3000"), NETWORK);
      assertEquals(new URL("http://localhost:3000"), api.getUrl());
      assertEquals(NETWORK, api.getNetworkType());
   }

   @Test
   void testRetrieveTransactionFactory() throws MalformedURLException {
      BlockchainApi api = new BlockchainApi(new URL("http://localhost:3000"), NETWORK);

      TransactionBuilderFactory fac = api.transact();
      assertEquals(NETWORK, fac.getNetworkType());
   }
}
