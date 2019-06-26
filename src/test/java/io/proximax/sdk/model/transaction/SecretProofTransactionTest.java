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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

public class SecretProofTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
      String secretSeed = "9a493664";
      SecretProofTransaction tx = new SecretProofTransaction(NetworkType.MIJIN, 23, new FakeDeadline(), BigInteger.ONE,
            HashType.SHA3_256, secret, secretSeed, "signaturestring",
            new PublicAccount(new KeyPair().getPublicKey().getHexString(), NetworkType.MIJIN),
            TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash"));
      assertEquals(secret, tx.getSecret());
      assertEquals(secretSeed, tx.getProof());
      assertEquals(HashType.SHA3_256, tx.getHashType());
   }

   @Test
   void serialization() throws IOException {
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
      String secretSeed = "9a493664";
      SecretProofTransaction secretProoftx = SecretProofTransaction
            .create(new FakeDeadline(), HashType.SHA3_256, secret, secretSeed, NetworkType.MIJIN_TEST);
      byte[] actual = secretProoftx.generateBytes();
      assertArrayEquals(loadBytes("secret_proof"), actual);
   }

   @Test
   void shouldThrowErrorWhenSecretIsNotValid() {
      String proof = "B778A39A3663719DFC5E48C9D78431B1E45C2AF9DF538782BF199C189DABEAC7680ADA57DCEC8EEE91"
            + "C4E3BF3BFA9AF6FFDE90CD1D249D1C6121D7B759A001B1";
      assertThrows(IllegalArgumentException.class, () -> {
         SecretProofTransaction secretProoftx = SecretProofTransaction
               .create(new FakeDeadline(), HashType.SHA3_256, "non valid hash", proof, NetworkType.MIJIN_TEST);
      }, "not a valid secret");
   }
}
