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

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;

/**
 * {@link ModifyAccountPropertyAddressTransactionBuilder} tests
 */
class ModifyAccountPropertyAddressTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private ModifyAccountPropertyAddressTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new ModifyAccountPropertyAddressTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   
   @Test
   void test() {
      Address addr = new Account(new KeyPair(), NETWORK_TYPE).getAddress();
      AccountPropertyModification<Address> mod = AccountPropertyModification.add(addr);
      ModifyAccountPropertyTransaction<Address> trans = builder.propertyType(AccountPropertyType.ALLOW_ADDRESS).modifications(Arrays.asList(mod)).build();
      
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, trans.getPropertyType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(mod, trans.getPropertyModifications().get(0));
   }

   @Test
   void testAllowed() {
      Address addr = new Account(new KeyPair(), NETWORK_TYPE).getAddress();
      AccountPropertyModification<Address> mod = AccountPropertyModification.add(addr);
      ModifyAccountPropertyTransaction<Address> trans = builder.allowed(Arrays.asList(mod)).build();
      
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, trans.getPropertyType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(mod, trans.getPropertyModifications().get(0));
   }

   @Test
   void testBlocked() {
      Address addr = new Account(new KeyPair(), NETWORK_TYPE).getAddress();
      AccountPropertyModification<Address> mod = AccountPropertyModification.add(addr);
      ModifyAccountPropertyTransaction<Address> trans = builder.blocked(Arrays.asList(mod)).build();
      
      assertEquals(AccountPropertyType.BLOCK_ADDRESS, trans.getPropertyType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(mod, trans.getPropertyModifications().get(0));
   }

}
