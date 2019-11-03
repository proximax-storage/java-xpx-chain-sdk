/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * {@link ModifyAccountPropertyMosaicTransactionBuilder} tests
 */
class ModifyAccountPropertyMosaicTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private ModifyAccountPropertyMosaicTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new ModifyAccountPropertyMosaicTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   
   @Test
   void test() {
      MosaicId mosid = new MosaicId(BigInteger.ONE);
      AccountPropertyModification<UInt64Id> mod = AccountPropertyModification.add(mosid);
      ModifyAccountPropertyTransaction<UInt64Id> trans = builder.propertyType(AccountPropertyType.ALLOW_MOSAIC).modifications(Arrays.asList(mod)).build();
      
      assertEquals(AccountPropertyType.ALLOW_MOSAIC, trans.getPropertyType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(mod, trans.getPropertyModifications().get(0));
   }

   @Test
   void testAllowed() {
      MosaicId mosid = new MosaicId(BigInteger.ONE);
      AccountPropertyModification<UInt64Id> mod = AccountPropertyModification.add(mosid);
      ModifyAccountPropertyTransaction<UInt64Id> trans = builder.allowed(Arrays.asList(mod)).build();
      
      assertEquals(AccountPropertyType.ALLOW_MOSAIC, trans.getPropertyType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(mod, trans.getPropertyModifications().get(0));
   }

   @Test
   void testBlocked() {
      MosaicId mosid = new MosaicId(BigInteger.ONE);
      AccountPropertyModification<UInt64Id> mod = AccountPropertyModification.add(mosid);
      ModifyAccountPropertyTransaction<UInt64Id> trans = builder.blocked(Arrays.asList(mod)).build();
      
      assertEquals(AccountPropertyType.BLOCK_MOSAIC, trans.getPropertyType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(mod, trans.getPropertyModifications().get(0));
   }

}
