/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * {@link HelperFactory} tests
 */
class HelperFactoryTest {

   @Test
   void testConstructor() {
      BlockchainApi api = Mockito.mock(BlockchainApi.class);
      assertEquals(api, new HelperFactory(api).getBlockchainApi());
   }

   @Test
   void testFacMethods() {
      BlockchainApi api = Mockito.mock(BlockchainApi.class);
      TransactionBuilderFactory transact = Mockito.mock(TransactionBuilderFactory.class);
      
      Mockito.when(api.transact()).thenReturn(transact);
      
      HelperFactory fac = new HelperFactory(api);
      // make sure that API is passed to the helpers
      assertEquals(api, fac.account().getBlockchainApi());
      assertEquals(api, fac.blockchain().getBlockchainApi());
      assertEquals(api, fac.transfer().getBlockchainApi());
   }
}
