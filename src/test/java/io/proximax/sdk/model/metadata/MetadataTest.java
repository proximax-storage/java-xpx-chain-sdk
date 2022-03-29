/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * {@link Metadata} tests
 */
class MetadataTest {

   @Test
   void checkConstructor() {
      List<Field> fields = Arrays.asList(new Field("key"));
      Metadata meta = new Metadata(OldMetadataType.ADDRESS, fields);
      
      assertEquals(OldMetadataType.ADDRESS, meta.getType());
      assertEquals(fields, meta.getFields());
   }

}
