/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * TODO add proper description
 */
class BlockchainHelperTest {
   private BlockchainApi api;
   private BlockchainHelper helper;
   
   @BeforeEach
   void init() throws MalformedURLException {
      // specify both URL and network type so the network does not need to be accessed unless really needed
      api = new BlockchainApi(new URL("http://localhost:3000"), NetworkType.MAIN_NET);
      helper = new BlockchainHelper(api);
   }
   
   @AfterEach
   void cleanup() {
      this.api = null;
      this.helper = null;
   }
   
}
