/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link ModifyContractTransaction} tests
 */
class ModifyContractTransactionTest extends ResourceBasedTest {
   private static final String PUBKEYHASH = "9d6dec37eae62bf2cdfa2c292c9e346027ca2e412706536be1b5cbf22043398e";
   
   @Test
   void constructor() throws IOException {
      ModifyContractTransaction trans = ModifyContractTransaction.create(new FakeDeadline(),
            BigInteger.valueOf(324),
            BigInteger.valueOf(5),
            "CAFE",
            Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.MIJIN))),
            Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.MIJIN))),
            Arrays.asList(),
            NetworkType.MIJIN);
      assertEquals("CAFE", trans.getContentHash());
      assertEquals(Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.MIJIN))), trans.getCustomersModifications());
      assertEquals(Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.MIJIN))), trans.getExecutorsModifications());
      assertTrue(trans.getVerifiersModifications().isEmpty());
      assertEquals(BigInteger.valueOf(5), trans.getDurationDelta());
   }
   
   @Test
   void serialization() throws IOException {
      ModifyContractTransaction trans = ModifyContractTransaction.create(new FakeDeadline(),
            BigInteger.valueOf(324),
            BigInteger.valueOf(5),
            "CAFE",
            Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.MIJIN))),
            Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.MIJIN))),
            Arrays.asList(),
            NetworkType.MIJIN);
      // used saveBytes to store the file data
      assertArrayEquals(loadBytes("modify_contract"), trans.generateBytes());
   }

}
