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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicFactory;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.mosaic.NetworkHarvestMosaic;
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
   
   @Test
   void testRetrieveRepositories() throws MalformedURLException {
      BlockchainApi api = new BlockchainApi(new URL("http://localhost:3000"), NETWORK);

      assertNotNull(api.createAccountRepository());
      assertNotNull(api.createBlockchainRepository());
      assertNotNull(api.createContractRepository());
      assertNotNull(api.createListener());
      assertNotNull(api.createMetadataRepository());
      assertNotNull(api.createMosaicRepository());
      assertNotNull(api.createNamespaceRepository());
      assertNotNull(api.createTransactionRepository());
   }
   
   @Test
   void testNetworkMosaicDefault() throws MalformedURLException {
      BlockchainApi api = new BlockchainApi(new URL("http://localhost:3000"), NETWORK);
      assertEquals(NetworkCurrencyMosaic.FACTORY, api.networkCurrencyMosaic());
      assertEquals(NetworkHarvestMosaic.FACTORY, api.networkHarvestMosaic());
   }
   
   @Test
   void testNetworkMosaicExplicit() throws MalformedURLException {
      MosaicFactory crr = new MosaicFactory(NetworkCurrencyMosaic.ID, BigInteger.ONE, false, false, 2, Optional.empty());
      MosaicFactory hrv = new MosaicFactory(NetworkHarvestMosaic.ID, BigInteger.ZERO, true, true, 5, Optional.empty());
      BlockchainApi api = new BlockchainApi(new URL("http://localhost:3000"), NETWORK, crr, hrv);
      // check currency
      assertEquals(crr, api.networkCurrencyMosaic());
      // check harvest
      assertEquals(hrv, api.networkHarvestMosaic());
   }
   
   @Test
   void testNetworkTypeDefined() throws MalformedURLException {
      BlockchainApi api = Mockito.spy(new BlockchainApi(new URL("http://bogus:1234"), NETWORK));
      Mockito.doReturn(NETWORK).when(api).queryForNetworkType();

      assertEquals(NETWORK, api.getNetworkType());
      assertTrue(api.isNetworkTypeValid());
      // is valid = 1
      Mockito.verify(api, Mockito.times(1)).queryForNetworkType();
   }
   
   @Test
   void testNetworkTypeInvalid() throws MalformedURLException {
      BlockchainApi api = Mockito.spy(new BlockchainApi(new URL("http://bogus:1234"), NetworkType.MAIN_NET));
      Mockito.doReturn(NetworkType.TEST_NET).when(api).queryForNetworkType();

      assertFalse(api.isNetworkTypeValid());
      // is valid = 1
      Mockito.verify(api, Mockito.times(1)).queryForNetworkType();
   }
   
   @Test
   void testNetworkTypeQueried() throws MalformedURLException {
      BlockchainApi api = Mockito.spy(new BlockchainApi(new URL("http://bogus:1234")));
      Mockito.doReturn(NETWORK).when(api).queryForNetworkType();
      
      assertEquals(NETWORK, api.getNetworkType());
      assertTrue(api.isNetworkTypeValid());
      // get network + is valid = 2
      Mockito.verify(api, Mockito.times(2)).queryForNetworkType();
   }
}
