/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.ModifyMetadataTransaction;
import io.proximax.sdk.model.transaction.EntityType;

/**
 * {@link ModifyMetadataTransactionBuilder} tests
 */
class ModifyMetadataTransactionBuilderTest {
   
   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private ModifyMetadataTransactionBuilder builder;
   
   @BeforeEach
   void setUp() {
      builder = new ModifyMetadataTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void testForAddress() {
      Address addr = new Address("SADD", NETWORK_TYPE);
      MetadataModification mod = MetadataModification.add("key", "value");

      ModifyMetadataTransaction trans = builder.forAddress(addr).modifications(mod).build();
      
      assertEquals(EntityType.MODIFY_ADDRESS_METADATA, trans.getType());
      assertEquals(Optional.of(addr), trans.getAddress());
      assertEquals(1, trans.getModifications().size());
      assertEquals(mod, trans.getModifications().get(0));
      assertEquals(MetadataType.ADDRESS, trans.getMetadataType());
   }
   
   @Test
   void testForMosaic() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(123456789));
      NamespaceId nsid = new NamespaceId("test.namespace");
      MetadataModification mod = MetadataModification.add("key", "value");

      ModifyMetadataTransaction trans = builder.forMosaic(mosid).modifications(mod).build();
      
      assertEquals(EntityType.MODIFY_MOSAIC_METADATA, trans.getType());
      assertEquals(Optional.of(mosid), trans.getMetadataId());
      assertEquals(1, trans.getModifications().size());
      assertEquals(mod, trans.getModifications().get(0));
      assertEquals(MetadataType.MOSAIC, trans.getMetadataType());
   }
   
   @Test
   void testForNamespace() {
      NamespaceId nsid = new NamespaceId("test.namespace");
      MetadataModification mod = MetadataModification.add("key", "value");

      ModifyMetadataTransaction trans = builder.forNamespace(nsid).modifications(mod).build();
      
      assertEquals(EntityType.MODIFY_NAMESPACE_METADATA, trans.getType());
      assertEquals(Optional.of(nsid), trans.getMetadataId());
      assertEquals(1, trans.getModifications().size());
      assertEquals(mod, trans.getModifications().get(0));
      assertEquals(MetadataType.NAMESPACE, trans.getMetadataType());
   }
}
