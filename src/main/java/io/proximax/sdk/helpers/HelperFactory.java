/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.helpers;

import io.proximax.sdk.BlockchainApi;

/**
 * Factory class to simplify access to the helpers
 */
public class HelperFactory {
   
   private final BlockchainApi api;

   /**
    * @param api blockchain API instance used by helpers
    */
   public HelperFactory(BlockchainApi api) {
      this.api = api;
   }
   
   /**
    * @return the API used by helpers
    */
   public BlockchainApi getBlockchainApi() {
      return api;
   }
   
   /**
    * @return helper for account related operations
    */
   public AccountHelper account() {
      return new AccountHelper(api);
   }
   
   /**
    * @return helper for blockchain related operations
    */
   public BlockchainHelper blockchain() {
      return new BlockchainHelper(api);
   }
   
   /**
    * @return helper for transfers
    */
   public TransferHelper transfer() {
      return new TransferHelper(api);
   }
}
