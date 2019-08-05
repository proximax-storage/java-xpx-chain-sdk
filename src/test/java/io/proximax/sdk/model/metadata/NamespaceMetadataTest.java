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

import io.proximax.sdk.model.namespace.NamespaceId;

/**
 * {@link NamespaceMetadata} tests
 */
class NamespaceMetadataTest {

   @Test
   void checkConstructor() {
      List<Field> fields = Arrays.asList(new Field("key"));
      NamespaceId id = new NamespaceId(BigInteger.ONE);
      NamespaceMetadata meta = new NamespaceMetadata(fields, id);
      
      assertEquals(MetadataType.NAMESPACE, meta.getType());
      assertEquals(id, meta.getId());
      assertEquals(fields, meta.getFields());
   }

}
