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
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.ModifyContractTransaction;
import io.proximax.sdk.model.transaction.MultisigCosignatoryModification;

/**
 * {@link ModifyContractTransactionBuilder} tests
 */
class ModifyContractTransactionBuilderTest {
   
   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private ModifyContractTransactionBuilder builder;
   
   @BeforeEach
   void setUp() {
      builder = new ModifyContractTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }

   @Test
   void contractWithoutMods() {
      ModifyContractTransaction trans = builder.durationDelta(BigInteger.TEN).contentHash("CAFE").build();
      
      assertTrue(trans.getExecutorsModifications().isEmpty());
      assertTrue(trans.getCustomersModifications().isEmpty());
      assertTrue(trans.getVerifiersModifications().isEmpty());
      assertEquals("CAFE", trans.getContentHash());
      assertEquals(BigInteger.TEN, trans.getDurationDelta());
   }

   @Test
   void contractWithMods() {
      PublicAccount pa1 = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      PublicAccount pa2 = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      PublicAccount pa3 = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      ModifyContractTransaction trans = builder.durationDelta(BigInteger.TEN).contentHash("CAFE")
            .customersModifications(MultisigCosignatoryModification.add(pa1))
            .verifiersModifications(MultisigCosignatoryModification.add(pa2))
            .executorsModifications(MultisigCosignatoryModification.add(pa3))
            .build();
      
      assertEquals(1, trans.getExecutorsModifications().size());
      assertEquals(pa3, trans.getExecutorsModifications().get(0).getCosignatoryPublicAccount());
      assertEquals(1, trans.getCustomersModifications().size());
      assertEquals(pa1, trans.getCustomersModifications().get(0).getCosignatoryPublicAccount());
      assertEquals(1, trans.getVerifiersModifications().size());
      assertEquals(pa2, trans.getVerifiersModifications().get(0).getCosignatoryPublicAccount());
      assertEquals("CAFE", trans.getContentHash());
      assertEquals(BigInteger.TEN, trans.getDurationDelta());
   }

   @Test
   void contractInit() {
      ModifyContractTransaction trans = builder.init("CAFE", BigInteger.TEN).build();
      
      assertTrue(trans.getExecutorsModifications().isEmpty());
      assertTrue(trans.getCustomersModifications().isEmpty());
      assertTrue(trans.getVerifiersModifications().isEmpty());
      assertEquals("CAFE", trans.getContentHash());
      assertEquals(BigInteger.TEN, trans.getDurationDelta());
   }
}
