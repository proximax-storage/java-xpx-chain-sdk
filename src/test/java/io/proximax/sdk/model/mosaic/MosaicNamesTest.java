/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * {@link MosaicNames} tests
 */
class MosaicNamesTest {

   @Test
   void checkConstructor() {
      MosaicId mosaicId = new MosaicId(BigInteger.ONE);
      List<String> names = Arrays.asList("mike", "frank");
      MosaicNames mnames = new MosaicNames(mosaicId, names);
      
      assertEquals(mosaicId, mnames.getMosaicId());
      assertEquals(names, mnames.getNames());
   }

   @Test
   void checkToString() {
      MosaicId mosaicId = new MosaicId(BigInteger.ONE);
      List<String> names = Arrays.asList("mike", "frank");
      MosaicNames mnames = new MosaicNames(mosaicId, names);
      
      assertTrue(mnames.toString().startsWith("MosaicNames "));
   }

}
