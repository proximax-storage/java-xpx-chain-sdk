/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.AliasTransaction;

/**
 * {@link AliasTransactionBuilder} tests
 */
class AliasTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;

   private AliasTransactionBuilder addressBuilder;
   private AliasTransactionBuilder mosaicBuilder;
   
   @BeforeEach
   void setUp() {
      addressBuilder = init(AliasTransactionBuilder.createForAddress());
      mosaicBuilder = init(AliasTransactionBuilder.createForMosaic());

   }

   private static AliasTransactionBuilder init(AliasTransactionBuilder builder) {
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
      return builder;
   }

   @Test
   void testLinkMosaic() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(123456789));
      NamespaceId nsid = new NamespaceId("test.namespace");
      
      AliasTransaction trans = mosaicBuilder.link(mosid).namespaceId(nsid).build();
      
      assertEquals(Optional.of(mosid), trans.getMosaicId());
      assertEquals(nsid, trans.getNamespaceId());
   }

   @Test
   void testLinkAddress() {
      Address addr = new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NETWORK_TYPE);
      NamespaceId nsid = new NamespaceId("test.namespace");
      
      AliasTransaction trans = addressBuilder.link(addr).namespaceId(nsid).build();
      
      assertEquals(Optional.of(addr), trans.getAddress());
      assertEquals(nsid, trans.getNamespaceId());
   }

   @Test
   void testUnlinkMosaic() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(123456789));
      NamespaceId nsid = new NamespaceId("test.namespace");
      
      AliasTransaction trans = mosaicBuilder.unlink(mosid).namespaceId(nsid).build();
      
      assertEquals(Optional.of(mosid), trans.getMosaicId());
      assertEquals(nsid, trans.getNamespaceId());
   }

   @Test
   void testUnlinkAddress() {
      Address addr = new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NETWORK_TYPE);
      NamespaceId nsid = new NamespaceId("test.namespace");
      
      AliasTransaction trans = addressBuilder.unlink(addr).namespaceId(nsid).build();
      
      assertEquals(Optional.of(addr), trans.getAddress());
      assertEquals(nsid, trans.getNamespaceId());
   }

   @Test
   void testTypeValidation() {
      MosaicId mosid = new MosaicId(BigInteger.valueOf(123456789));
      Address addr = new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NETWORK_TYPE);
      NamespaceId nsid = new NamespaceId("test.namespace");
      
      assertThrows(IllegalArgumentException.class, () -> addressBuilder.mosaicId(mosid).namespaceId(nsid).build());
      assertThrows(IllegalArgumentException.class, () -> mosaicBuilder.address(addr).namespaceId(nsid).build());
   }
}
