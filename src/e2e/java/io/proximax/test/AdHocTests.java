/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.NamespaceRepository;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceName;

/**
 * ad-hoc tests used for debugging or test instrumentation
 */
class AdHocTests extends BaseTest {
   private static final String NAMESPACE_PRX_XPX = "prx.xpx";
   
   private BlockchainApi api;
   
   @BeforeEach
   void prepare() throws MalformedURLException {
      api = new BlockchainApi(new URL(getNodeUrl()));
   }
   
   @AfterEach
   void cleanup() {
      api = null;
   }
   
   @Test
   void checkForPrxXpxNamespace() {
      NamespaceRepository nameSpaces = api.createNamespaceRepository();
      List<NamespaceName> names = nameSpaces.getNamespaceNames(Arrays.asList(new NamespaceId(NAMESPACE_PRX_XPX))).blockingFirst();
      assertFalse(names.isEmpty());
   }

   @Test
   @Disabled
   void getTransactions() {
      /*
       * create debugging listener in the E2EBaseTest and add line as follows
       * BlockchainHttp.getBlockTransactions(BigInteger height, Optional<QueryParams> queryParams)
       *         .map(Http::mapStringOrError)
       *         .map(listener::writeTransaction)
       */
//      BlockchainHttp.listener = new DebuggingListener(api);
      long startBlock = 213771;
      long endBlock = 214321;
      BlockchainRepository repo = api.createBlockchainRepository();
      for (long block = startBlock; block < endBlock; block++) {
         repo.getBlockTransactions(BigInteger.valueOf(block)).blockingFirst();
      }
   }
}
