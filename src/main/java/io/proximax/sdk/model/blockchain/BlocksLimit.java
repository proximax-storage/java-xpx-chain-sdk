/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

/**
 * maximum number of blocks that are retrieved
 */
public enum BlocksLimit {
   LIMIT_25(25), LIMIT_50(50), LIMIT_75(75), LIMIT_100(100);
   
   private final int limit;

   /**
    * @param limit
    */
   private BlocksLimit(int limit) {
      this.limit = limit;
   }

   /**
    * @return the limit
    */
   public int getLimit() {
      return limit;
   }
   
}
