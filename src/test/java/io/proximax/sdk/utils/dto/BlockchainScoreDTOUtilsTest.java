/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.BlockchainScoreDTO;

/**
 * {@link BlockchainScoreDTOUtils} tests
 */
class BlockchainScoreDTOUtilsTest {

   @Test
   void test() {
      BlockchainScoreDTO dto = new BlockchainScoreDTO();
      dto.setScoreLow(UInt64Utils.dtoFromBigInt(BigInteger.ONE));
      dto.setScoreHigh(UInt64Utils.dtoFromBigInt(BigInteger.TEN));
      // test
      assertEquals(BigInteger.valueOf(42949672961L), BlockchainScoreDTOUtils.toBigInt(dto));
   }

}
