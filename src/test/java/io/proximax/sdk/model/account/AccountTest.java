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

package io.proximax.sdk.model.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.ed25519.Ed25519CryptoEngine;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.FakeDeadline;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

class AccountTest {

   @Test
   void shouldCreateAccountViaConstructor() {
      Account account = new Account("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923",
            NetworkType.TEST_NET);
      assertEquals("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", account.getAddress().plain());
      assertEquals("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9", account.getPublicKey());
      assertEquals("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923", account.getPrivateKey());
   }

   @Test
   void shouldCreateAccountViaStaticConstructor() {
      Account account = Account.createFromPrivateKey("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923",
            NetworkType.TEST_NET);
      assertEquals("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923", account.getPrivateKey());
      assertEquals("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", account.getAddress().plain());
      assertEquals("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9", account.getPublicKey());
   }

   @Test
   void shouldCreateAccountViaStaticConstructor2() {
      Account account = Account.createFromPrivateKey("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923",
            NetworkType.TEST_NET);
      assertEquals("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923", account.getPrivateKey());
      assertEquals("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9", account.getPublicKey());
      assertEquals("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", account.getAddress().pretty());
   }

   @Test
   void shouldCreateAccountViaStaticConstructor3() {
      Account account = Account.createFromPrivateKey("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923",
            NetworkType.TEST_NET);
      assertEquals("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923", account.getPrivateKey());
      assertEquals("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9", account.getPublicKey());
      assertEquals("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", account.getAddress().plain());
   }

   @Test
   void generateNewAccountTest() {
      Account account = Account.generateNewAccount(NetworkType.TEST_NET);
      assertNotEquals(account.getPrivateKey(), null);
      assertNotEquals(account.getPublicKey(), null);
      assertEquals(64, account.getPrivateKey().length());
   }

   @Test
   void shouldSignTransaction() {
      Account account = new Account("D54AC0CB0FF50FB44233782B3A6B5FDE2F1C83B9AE2F1352119F93713F3AB923",
            NetworkType.TEST_NET);
      TransferTransaction transferTransaction = new TransactionBuilderFactory().transfer()
            .mosaics(new Mosaic(new MosaicId("0189718c21876314"), BigInteger.valueOf(100)))
            .to(new Address("VDPQS6FBYDN3SD2QJPHUWRYWNHSSOQ2Q35VI7TDP", NetworkType.TEST_NET))
            .networkType(NetworkType.TEST_NET).deadline(new FakeDeadline()).build();

      SignedTransaction signedTransaction = account.sign(transferTransaction,
            "AC87FDA8FD94B72F3D0790A7D62F248111BD5E37B95B16E4216DA99C212530A5");
      assertEquals(
            "A7000000ACC35E1FB8328C40260F5B143191C809F2FDD21FEB19BEABEA435B12C9E99A367DAB8F7D5B81F6E58608245CB50CE3B1E7432DF9F9F4669A7685C111628AD80CF06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9030000A8544100000000000000000100000000000000A8DF0978A1C0DBB90F504BCF4B471669E5274350DF6A8FCC6F01000100146387218C7189016400000000000000",
            signedTransaction.getPayload());
      assertEquals("7B01BAE9B39300B8155360827806F9AAFDF82A95BFA3DE0D3A1CDDFB56DE6F30", signedTransaction.getHash());
   }

   @Test
   void shouldAcceptKeyPairAsConstructor() {
      KeyPair random = KeyPair.random(new Ed25519CryptoEngine());
      Account account = new Account(random, NetworkType.TEST_NET);
      assertEquals(random.getPrivateKey().toString().toUpperCase(), account.getPrivateKey());
      assertEquals(account.getAddress().getNetworkType(), NetworkType.TEST_NET);
   }

   @Test
   void checkToString() {
      Account account = new Account("7d6256ee3d38eae579dc05657e7442cec0dd4c50d0684544e1ed1e64a9d4b148",
            NetworkType.TEST_NET);
      assertTrue(account.toString().startsWith("Account"));
   }
}
