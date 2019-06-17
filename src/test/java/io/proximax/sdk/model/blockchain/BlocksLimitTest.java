/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * {@link BlocksLimit} tests
 */
class BlocksLimitTest {

   @Test
   void test() {
      assertEquals(25, BlocksLimit.LIMIT_25.getLimit());
      assertEquals(50, BlocksLimit.LIMIT_50.getLimit());
      assertEquals(75, BlocksLimit.LIMIT_75.getLimit());
      assertEquals(100, BlocksLimit.LIMIT_100.getLimit());
   }

}
