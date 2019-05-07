/*
 * Copyright 2019 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.infrastructure.MetadataHttp;
import io.proximax.sdk.model.metadata.AddressMetadata;
import io.proximax.sdk.model.metadata.Metadata;
import io.proximax.sdk.model.metadata.MetadataId;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.metadata.MosaicMetadata;
import io.proximax.sdk.model.metadata.NamespaceMetadata;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;

/**
 * E2E tests to proof work with metadata
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EMetadataTest extends E2EBaseTest {

   @Test
   void testHardcodedMetaAddress() throws MalformedURLException {
      MetadataHttp http = new MetadataHttp("http://bcstage1.xpxsirius.io:3000");
      Metadata meta = http.getMetadata("VCMY23PRJYEVEZWLNY3GCPYDOYLMOLZCJWUVYK7U").blockingFirst();
      assertEquals(MetadataType.ADDRESS, meta.getType());
      assertEquals(1, meta.getFields().size());
      assertEquals("jora229", meta.getFields().get(0).getKey());
      assertEquals("I Love you", meta.getFields().get(0).getValue());
      assertEquals(AddressMetadata.class, meta.getClass());
      // request the same thing from account
      AddressMetadata addrMeta = http.getMetadataFromAddress("VCMY23PRJYEVEZWLNY3GCPYDOYLMOLZCJWUVYK7U")
            .blockingFirst();
      assertEquals(meta.getFields().get(0).getKey(), addrMeta.getFields().get(0).getKey());
   }

   @Test
   void testHardcodedMetaNamespace() throws MalformedURLException {
      MetadataHttp http = new MetadataHttp("http://bcstage1.xpxsirius.io:3000");
      Metadata meta = http.getMetadata(new MetadataId("C9363100BC38D88F")).blockingFirst();
      assertEquals(MetadataType.NAMESPACE, meta.getType());
      assertEquals(1, meta.getFields().size());
      assertEquals("hello", meta.getFields().get(0).getKey());
      assertEquals("world", meta.getFields().get(0).getValue());
      assertEquals(NamespaceMetadata.class, meta.getClass());
      // request the same thing from namespace
      NamespaceMetadata addrMeta = http.getMetadataFromNamespace(new NamespaceId(new BigInteger("C9363100BC38D88F", 16))).blockingFirst();
      assertEquals(meta.getFields().get(0).getKey(), addrMeta.getFields().get(0).getKey());
   }

   @Test
   void testHardcodedMetaMosaic() throws MalformedURLException {
      MetadataHttp http = new MetadataHttp("http://bcstage1.xpxsirius.io:3000");
      Metadata meta = http.getMetadata(new MetadataId("04E8F1965203B09C")).blockingFirst();
      assertEquals(MetadataType.MOSAIC, meta.getType());
      assertEquals(1, meta.getFields().size());
      assertEquals("hello", meta.getFields().get(0).getKey());
      assertEquals("hell", meta.getFields().get(0).getValue());
      assertEquals(MosaicMetadata.class, meta.getClass());
      // request the same thing from account
      MosaicMetadata addrMeta = http.getMetadataFromMosaic(new MosaicId("04E8F1965203B09C")).blockingFirst();
      assertEquals(meta.getFields().get(0).getKey(), addrMeta.getFields().get(0).getKey());
   }

   @Test
   void testHardcodedMetaAll() throws MalformedURLException {
      MetadataHttp http = new MetadataHttp("http://bcstage1.xpxsirius.io:3000");
      long count = http
            .getMetadata(
                  Arrays.asList("04E8F1965203B09C", "VCMY23PRJYEVEZWLNY3GCPYDOYLMOLZCJWUVYK7U", "C9363100BC38D88F"))
            .count().blockingGet();
      assertEquals(3, count);
   }

}
