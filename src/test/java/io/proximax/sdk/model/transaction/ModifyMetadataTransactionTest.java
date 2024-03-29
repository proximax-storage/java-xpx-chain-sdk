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

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.OldMetadataType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link ModifyMetadataTransaction} tests
 */
class ModifyMetadataTransactionTest extends ResourceBasedTest {

   @Test
   void genericConstructor() {
      Deadline deadLine = new FakeDeadline();
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(EntityType.MODIFY_ADDRESS_METADATA,
            NetworkType.TEST_NET, 63, deadLine, BigInteger.valueOf(765), Optional.of("sign"),
            Optional.of(new PublicAccount("", NetworkType.TEST_NET)),
            Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), OldMetadataType.ADDRESS,
            Optional.empty(), Optional.of(new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET)), Arrays.asList());
      assertEquals(EntityType.MODIFY_ADDRESS_METADATA, trans.getType());
      assertEquals(NetworkType.TEST_NET, trans.getNetworkType());
      assertEquals(63, trans.getVersion());
      assertEquals(deadLine, trans.getDeadline());
      assertEquals(BigInteger.valueOf(765), trans.getMaxFee());
      assertFalse(trans.getMetadataId().isPresent());
      assertEquals(new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET), trans.getAddress().orElseThrow(AssertionFailedError::new));
      assertEquals(OldMetadataType.ADDRESS, trans.getMetadataType());
      assertEquals(Arrays.asList(), trans.getModifications());
      assertEquals("sign", trans.getSignature().orElseThrow(AssertionFailedError::new));
      assertEquals(new PublicAccount("", NetworkType.TEST_NET), trans.getSigner().orElseThrow(AssertionFailedError::new));
      assertEquals(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash"),
            trans.getTransactionInfo().orElseThrow(AssertionFailedError::new));
   }

   @Test
   void assertFailureWhenTargetMissing() {
      assertThrows(IllegalArgumentException.class,
            () -> new ModifyMetadataTransaction(EntityType.MODIFY_ADDRESS_METADATA, NetworkType.TEST_NET, 63,
                  new FakeDeadline(), BigInteger.valueOf(765), Optional.of("sign"),
                  Optional.of(new PublicAccount("", NetworkType.TEST_NET)),
                  Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), OldMetadataType.ADDRESS,
                  Optional.empty(), Optional.empty(), Arrays.asList()));
   }

   @Test
   void assertFailureWhenTargetOverspecified() {
      assertThrows(IllegalArgumentException.class,
            () -> new ModifyMetadataTransaction(EntityType.MODIFY_ADDRESS_METADATA, NetworkType.TEST_NET, 63,
                  new FakeDeadline(), BigInteger.valueOf(765), Optional.of("sign"),
                  Optional.of(new PublicAccount("", NetworkType.TEST_NET)),
                  Optional.of(TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash")), OldMetadataType.ADDRESS,
                  Optional.of(new NamespaceId("testns")), Optional.of(new Address("MHAH", NetworkType.TEST_NET)),
                  Arrays.asList()));
   }

   @Test
   void serializationAddress() throws IOException {
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(EntityType.MODIFY_ADDRESS_METADATA,
            NetworkType.TEST_NET, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), OldMetadataType.ADDRESS, Optional.empty(),
            Optional.of(new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET)),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      byte[] actual = trans.generateBytes();
      // used saveBytes to store the file data
//      saveBytes("modify_metadata_address", actual);
      assertArrayEquals(loadBytes("modify_metadata_address"), actual);
   }

   @Test
   void serializationMosaic() throws IOException {
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(EntityType.MODIFY_MOSAIC_METADATA,
            NetworkType.TEST_NET, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), OldMetadataType.MOSAIC, Optional.of(new MosaicId(BigInteger.ONE)),
            Optional.empty(),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      byte[] actual = trans.generateBytes();
      // used saveBytes to store the file data
//      saveBytes("modify_metadata_mosaic", actual);
      assertArrayEquals(loadBytes("modify_metadata_mosaic"), actual);
   }

   @Test
   void serializationNamespace() throws IOException {
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(EntityType.MODIFY_NAMESPACE_METADATA,
            NetworkType.TEST_NET, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), OldMetadataType.NAMESPACE, Optional.of(new NamespaceId("testns")),
            Optional.empty(),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      // used saveBytes to store the file data
      byte[] actual = trans.generateBytes();
//      saveBytes("modify_metadata_namespace", actual);
      assertArrayEquals(loadBytes("modify_metadata_namespace"), actual);
   }

   @Test
   void checkCopyToSigner() throws IOException {
      PublicAccount remoteAccount = new Account(new KeyPair(), NetworkType.TEST_NET).getPublicAccount();
      
      ModifyMetadataTransaction trans = new ModifyMetadataTransaction(EntityType.MODIFY_NAMESPACE_METADATA,
            NetworkType.TEST_NET, 1, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), OldMetadataType.NAMESPACE, Optional.of(new NamespaceId("testns")),
            Optional.empty(),
            Arrays.asList(MetadataModification.remove("keytoremove"), MetadataModification.add("addedkey", "value")));

      Transaction t = trans.copyForSigner(remoteAccount);
      assertEquals(Optional.of(remoteAccount), t.getSigner());
   }
}
