/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;

/**
 * {@link ModifyMetadataTransaction} tests
 */
class ModifyMetadataTransactionTest extends ResourceBasedTest {

   @Test
   void genericConstructor() {
      Deadline deadLine = new FakeDeadline();
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(TransactionType.MODIFY_ADDRESS_METADATA,
            NetworkType.MIJIN, 63, deadLine, BigInteger.valueOf(765), Optional.of("sign"),
            Optional.of(new PublicAccount("", NetworkType.MIJIN)),
            Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), MetadataType.ADDRESS,
            Optional.empty(), Optional.of(new Address("MHAH", NetworkType.MIJIN)), Arrays.asList());
      assertEquals(TransactionType.MODIFY_ADDRESS_METADATA, trans.getType());
      assertEquals(NetworkType.MIJIN, trans.getNetworkType());
      assertEquals(63, trans.getVersion());
      assertEquals(deadLine, trans.getDeadline());
      assertEquals(BigInteger.valueOf(765), trans.getMaxFee());
      assertFalse(trans.getMetadataId().isPresent());
      assertEquals(new Address("MHAH", NetworkType.MIJIN), trans.getAddress().orElseThrow(AssertionFailedError::new));
      assertEquals(MetadataType.ADDRESS, trans.getMetadataType());
      assertEquals(Arrays.asList(), trans.getModifications());
      assertEquals("sign", trans.getSignature().orElseThrow(AssertionFailedError::new));
      assertEquals(new PublicAccount("", NetworkType.MIJIN), trans.getSigner().orElseThrow(AssertionFailedError::new));
      assertEquals(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash"),
            trans.getTransactionInfo().orElseThrow(AssertionFailedError::new));
   }

   @Test
   void assertFailureWhenTargetMissing() {
      assertThrows(IllegalArgumentException.class,
            () -> new ModifyMetadataTransaction(TransactionType.MODIFY_ADDRESS_METADATA, NetworkType.MIJIN, 63,
                  new FakeDeadline(), BigInteger.valueOf(765), Optional.of("sign"),
                  Optional.of(new PublicAccount("", NetworkType.MIJIN)),
                  Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), MetadataType.ADDRESS,
                  Optional.empty(), Optional.empty(), Arrays.asList()));
   }

   @Test
   void assertFailureWhenTargetOverspecified() {
      assertThrows(IllegalArgumentException.class,
            () -> new ModifyMetadataTransaction(TransactionType.MODIFY_ADDRESS_METADATA, NetworkType.MIJIN, 63,
                  new FakeDeadline(), BigInteger.valueOf(765), Optional.of("sign"),
                  Optional.of(new PublicAccount("", NetworkType.MIJIN)),
                  Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), MetadataType.ADDRESS,
                  Optional.of(new NamespaceId("testns")), Optional.of(new Address("MHAH", NetworkType.MIJIN)),
                  Arrays.asList()));
   }

   @Test
   void serializationAddress() throws IOException {
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(TransactionType.MODIFY_ADDRESS_METADATA,
            NetworkType.MIJIN_TEST, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), MetadataType.ADDRESS, Optional.empty(),
            Optional.of(new Address("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM", NetworkType.MIJIN_TEST)),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      byte[] actual = trans.generateBytes();
      // used saveBytes to store the file data
//      saveBytes("modify_metadata_address", actual);
      assertArrayEquals(loadBytes("modify_metadata_address"), actual);
   }

   @Test
   void serializationMosaic() throws IOException {
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(TransactionType.MODIFY_MOSAIC_METADATA,
            NetworkType.MIJIN_TEST, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), MetadataType.MOSAIC, Optional.of(new MosaicId(BigInteger.ONE)),
            Optional.empty(),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      byte[] actual = trans.generateBytes();
      // used saveBytes to store the file data
//      saveBytes("modify_metadata_mosaic", actual);
      assertArrayEquals(loadBytes("modify_metadata_mosaic"), actual);
   }

   @Test
   void serializationNamespace() throws IOException {
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(TransactionType.MODIFY_NAMESPACE_METADATA,
            NetworkType.MIJIN_TEST, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), MetadataType.NAMESPACE, Optional.of(new NamespaceId("testns")),
            Optional.empty(),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      // used saveBytes to store the file data
      byte[] actual = trans.generateBytes();
//      saveBytes("modify_metadata_namespace", actual);
      assertArrayEquals(loadBytes("modify_metadata_namespace"), actual);
   }

}
