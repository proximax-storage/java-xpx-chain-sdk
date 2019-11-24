/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import io.proximax.sdk.BlockchainApi;

/**
 * helper class for common operations
 */
public class BlockchainHelper extends BaseHelper {

   /**
    * </p>
    * create new helper instance
    * </p>
    * <p>
    * note this might require connection to the network if network type is not known to the provided api
    * </p>
    * 
    * @param api
    */
   public BlockchainHelper(BlockchainApi api) {
      super(api);
   }

   /**
    * wait for specified number of blocks. This method blocks or throws timeout exception
    * 
    * @param count number of blocks to wait for
    * @param timeoutSeconds timeout after which the wait will be aborted
    * 
    * @return height of the last block
    */
   public BigInteger blockConfirmations(int count, long timeoutSeconds) {
      return api.createBlockchainRepository().getBlockchainHeight().timeout(timeoutSeconds, TimeUnit.SECONDS)
            .take(count).blockingLast();
   }
}
