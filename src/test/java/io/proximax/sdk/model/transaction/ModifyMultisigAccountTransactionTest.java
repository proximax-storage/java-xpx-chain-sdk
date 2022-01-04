/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

class ModifyMultisigAccountTransactionTest extends ResourceBasedTest {

   @Test
   void createAMultisigModificationTransactionViaConstructor() {
      ModifyMultisigAccountTransaction modifyMultisigAccountTransaction = new ModifyMultisigAccountTransaction(
            NetworkType.TEST_NET, 3, new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(),
            Optional.empty(), Optional.empty(), 2, 1,
            Collections.singletonList(new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD,
                  PublicAccount.createFromPublicKey("68b3fbb18729c1fde225c57f8ce080fa828f0067e451a3fd81fa628842b0b763",
                        NetworkType.TEST_NET))));

      assertEquals(NetworkType.TEST_NET, modifyMultisigAccountTransaction.getNetworkType());
      assertTrue(3 == modifyMultisigAccountTransaction.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < modifyMultisigAccountTransaction.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), modifyMultisigAccountTransaction.getMaxFee());
      assertEquals(2, modifyMultisigAccountTransaction.getMinApprovalDelta());
      assertEquals(1, modifyMultisigAccountTransaction.getMinRemovalDelta());
      assertEquals("68b3fbb18729c1fde225c57f8ce080fa828f0067e451a3fd81fa628842b0b763",
            modifyMultisigAccountTransaction.getModifications().get(0).getCosignatoryPublicAccount().getPublicKey());
      assertEquals(MultisigCosignatoryModificationType.ADD,
            modifyMultisigAccountTransaction.getModifications().get(0).getType());
   }

   @Test
   @DisplayName("Serialization")
   void serialization() throws IOException {
      ModifyMultisigAccountTransaction modifyMultisigAccountTransaction = new ModifyMultisigAccountTransaction(
            NetworkType.TEST_NET, 3, new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
            Optional.empty(), 2, 1,
            Arrays.asList(new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD,
                  PublicAccount.createFromPublicKey("68b3fbb18729c1fde225c57f8ce080fa828f0067e451a3fd81fa628842b0b763",
                        NetworkType.TEST_NET)),
                  new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD,
                        PublicAccount.createFromPublicKey(
                              "cf893ffcc47c33e7f68ab1db56365c156b0736824a0c1e273f9e00b8df8f01eb",
                              NetworkType.TEST_NET))

            ));

      byte[] actual = modifyMultisigAccountTransaction.generateBytes();
//        saveBytes("modify_multisig_trans", actual);
      assertArrayEquals(loadBytes("modify_multisig_trans"), actual);
   }
}
