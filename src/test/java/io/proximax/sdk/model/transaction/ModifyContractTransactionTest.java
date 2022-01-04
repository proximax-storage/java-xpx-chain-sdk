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
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link ModifyContractTransaction} tests
 */
class ModifyContractTransactionTest extends ResourceBasedTest {
   private static final String PUBKEYHASH = "9d6dec37eae62bf2cdfa2c292c9e346027ca2e412706536be1b5cbf22043398e";

   @Test
   void constructor() throws IOException {
      ModifyContractTransaction trans = new ModifyContractTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(), BigInteger.valueOf(324), 
            Optional.empty(), Optional.empty(), Optional.empty(), BigInteger.valueOf(5), "CAFE", Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))), Arrays.asList());
      assertEquals("CAFE", trans.getContentHash());
      assertEquals(Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            trans.getCustomersModifications());
      assertEquals(
            Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            trans.getExecutorsModifications());
      assertTrue(trans.getVerifiersModifications().isEmpty());
      assertEquals(BigInteger.valueOf(5), trans.getDurationDelta());
   }

   @Test
   void serialization() throws IOException {
      ModifyContractTransaction trans = new ModifyContractTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.valueOf(324), Optional.empty(), Optional.empty(), Optional.empty(), BigInteger.valueOf(5),
            "CAFE",
            Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            Arrays.asList());
      // used saveBytes to store the file data
      byte[] actual = trans.generateBytes();
//      saveBytes("modify_contract", actual);
      assertArrayEquals(loadBytes("modify_contract"), actual);
   }

   @Test
   void checkCopyToSigner() throws IOException {
      PublicAccount remoteAccount = new Account(new KeyPair(), NetworkType.TEST_NET).getPublicAccount();
      
      ModifyContractTransaction trans = new ModifyContractTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.valueOf(324), Optional.empty(), Optional.empty(), Optional.empty(), BigInteger.valueOf(5),
            "CAFE",
            Arrays.asList(MultisigCosignatoryModification.add(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            Arrays.asList(MultisigCosignatoryModification.remove(new PublicAccount(PUBKEYHASH, NetworkType.TEST_NET))),
            Arrays.asList());

      Transaction t = trans.copyForSigner(remoteAccount);
      assertEquals(Optional.of(remoteAccount), t.getSigner());
   }
}
