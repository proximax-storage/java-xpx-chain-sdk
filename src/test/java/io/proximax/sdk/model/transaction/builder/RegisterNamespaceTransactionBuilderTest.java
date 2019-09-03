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
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.model.transaction.RegisterNamespaceTransaction;

/**
 * {@link RegisterNamespaceTransactionBuilder} tests
 */
class RegisterNamespaceTransactionBuilderTest {
   
   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private RegisterNamespaceTransactionBuilder builder;
   
   @BeforeEach
   void setUp() {
      builder = new RegisterNamespaceTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void rootNamespace() {
      final String ID = "test.ns";
      RegisterNamespaceTransaction trans = builder.rootNamespace(ID).build();
      
      assertEquals(NamespaceType.RootNamespace, trans.getNamespaceType());
      assertEquals(new NamespaceId(ID), trans.getNamespaceId());
      assertEquals(ID, trans.getNamespaceName());
      assertEquals(Optional.empty(), trans.getDuration());
      assertEquals(Optional.empty(), trans.getParentId());
   }

   @Test
   void childNamespace() {
      final String ID = "test.ns";
      RegisterNamespaceTransaction trans = builder.subNamespace(new NamespaceId(ID), "hello").duration(BigInteger.TEN).build();
      
      assertEquals(NamespaceType.SubNamespace, trans.getNamespaceType());
      assertEquals(new NamespaceId(ID + ".hello"), trans.getNamespaceId());
      assertEquals("hello", trans.getNamespaceName());
      assertEquals(Optional.of(BigInteger.TEN), trans.getDuration());
      assertEquals(Optional.of(new NamespaceId(ID)), trans.getParentId());
   }
}
