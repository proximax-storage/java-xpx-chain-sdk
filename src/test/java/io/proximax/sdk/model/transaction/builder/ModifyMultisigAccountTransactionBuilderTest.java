/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;

/**
 * {@link ModifyMultisigAccountTransactionBuilder} tests
 */
class ModifyMultisigAccountTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private ModifyMultisigAccountTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new ModifyMultisigAccountTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void testEmpty() {
      ModifyMultisigAccountTransaction trans = builder.build();
      
      assertEquals(0, trans.getMinApprovalDelta());
      assertEquals(0, trans.getMinRemovalDelta());
      assertTrue(trans.getModifications().isEmpty());
   }

   @Test
   void testMinAppRem() {
      ModifyMultisigAccountTransaction trans = builder.minApprovalDelta(5).minRemovalDelta(8).build();
      
      assertEquals(5, trans.getMinApprovalDelta());
      assertEquals(8, trans.getMinRemovalDelta());
      assertTrue(trans.getModifications().isEmpty());
   }
   
   @Test
   void testMods() {
      MultisigCosignatoryModification mod = MultisigCosignatoryModification.add(new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount());
      ModifyMultisigAccountTransaction trans = builder.minApprovalDelta(5).minRemovalDelta(8).modifications(mod).build();
      
      assertEquals(5, trans.getMinApprovalDelta());
      assertEquals(8, trans.getMinRemovalDelta());
      assertEquals(1, trans.getModifications().size());
      assertEquals(mod, trans.getModifications().get(0));
   }
}
