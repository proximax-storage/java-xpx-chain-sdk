/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.mosaic.MosaicId;

/**
 * {@link MosaicMetadata} tests
 */
class MosaicMetadataTest {

   @Test
   void checkConstructor() {
      List<Field> fields = Arrays.asList(new Field("key"));
      MosaicId id = new MosaicId(BigInteger.ONE);
      MosaicMetadata meta = new MosaicMetadata(fields, id);
      
      assertEquals(OldMetadataType.MOSAIC, meta.getType());
      assertEquals(id, meta.getId());
      assertEquals(fields, meta.getFields());
   }

}
