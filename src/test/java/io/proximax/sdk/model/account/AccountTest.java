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
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.FakeDeadline;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

class AccountTest {

   @Test
   void shouldCreateAccountViaConstructor() {
      Account account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d",
            NetworkType.MIJIN_TEST);
      assertEquals("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", account.getAddress().plain());
      assertEquals("1026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF37755", account.getPublicKey());
      assertEquals("787225AAFF3D2C71F4FFA32D4F19EC4922F3CD869747F267378F81F8E3FCB12D", account.getPrivateKey());
   }

   @Test
   void shouldCreateAccountViaStaticConstructor() {
      Account account = Account.createFromPrivateKey("787225AAFF3D2C71F4FFA32D4F19EC4922F3CD869747F267378F81F8E3FCB12D",
            NetworkType.MIJIN_TEST);
      assertEquals("787225AAFF3D2C71F4FFA32D4F19EC4922F3CD869747F267378F81F8E3FCB12D", account.getPrivateKey());
      assertEquals("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", account.getAddress().plain());
      assertEquals("1026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF37755", account.getPublicKey());
   }

   @Test
   void shouldCreateAccountViaStaticConstructor2() {
      Account account = Account.createFromPrivateKey("5098D500390934F81EA416D9A2F50F276DE446E28488E1801212931E3470DA31",
            NetworkType.MIJIN_TEST);
      assertEquals("5098D500390934F81EA416D9A2F50F276DE446E28488E1801212931E3470DA31", account.getPrivateKey());
      assertEquals("9B800145F7228CE0014FC6FB44AD899BFCAD7B0CDF48DB63A7CC7299E373D734", account.getPublicKey());
      assertEquals("SAQC5A-K6X2K6-YYAI4L-2TQI2T-4ZRWAO-URYDYT-UO77", account.getAddress().pretty());
   }

   @Test
   void shouldCreateAccountViaStaticConstructor3() {
      Account account = Account.createFromPrivateKey("B8AFAE6F4AD13A1B8AAD047B488E0738A437C7389D4FF30C359AC068910C1D59",
            NetworkType.MIJIN_TEST);
      assertEquals("B8AFAE6F4AD13A1B8AAD047B488E0738A437C7389D4FF30C359AC068910C1D59", account.getPrivateKey());
      assertEquals("68B3FBB18729C1FDE225C57F8CE080FA828F0067E451A3FD81FA628842B0B763", account.getPublicKey());
      assertEquals("SBE6CS7LZKJXLDVTNAC3VZ3AUVZDTF3PACNFIXFN", account.getAddress().plain());
   }

   @Test
   void generateNewAccountTest() {
      Account account = Account.generateNewAccount(NetworkType.MIJIN_TEST);
      assertNotEquals(account.getPrivateKey(), null);
      assertNotEquals(account.getPublicKey(), null);
      assertEquals(64, account.getPrivateKey().length());
   }

   @Test
   void shouldSignTransaction() {
      Account account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d",
            NetworkType.MIJIN_TEST);
      TransferTransaction transferTransaction = new TransactionBuilderFactory().transfer()
            .mosaic(new Mosaic(new MosaicId(new BigInteger("95442763262823")), BigInteger.valueOf(100)))
            .to(new Address("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM", NetworkType.MIJIN_TEST))
            .networkType(NetworkType.MIJIN_TEST).deadline(new FakeDeadline()).build();

      SignedTransaction signedTransaction = account.sign(transferTransaction,
            "7B631D803F912B00DC0CBED3014BBD17A302BA50B99D233B9C2D9533B842ABDF");
      assertEquals(
            "A7000000AEE2F8829E185D14922282DCB55114B9E97E854FCF3351E7CAEB40E2FB442F4923632C8D214AAB9A472EB3C6F8E2BBFE0DC66F03FF0D8DFA9296D8894150AD081026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF377550300009054417A00000000000000010000000000000090E8FEBD671DD41BEE94EC3BA5831CB608A312C2F203BA84AC01000100672B0000CE5600006400000000000000",
            signedTransaction.getPayload());
      assertEquals("B042D22F9B2EDD2C2818415145B911562A2369A219E67D34BF2B58992A9CC458", signedTransaction.getHash());
   }

   @Test
   void shouldAcceptKeyPairAsConstructor() {
      KeyPair random = KeyPair.random(new Ed25519CryptoEngine());
      Account account = new Account(random, NetworkType.MIJIN_TEST);
      assertEquals(random.getPrivateKey().toString().toUpperCase(), account.getPrivateKey());
      assertEquals(account.getAddress().getNetworkType(), NetworkType.MIJIN_TEST);
   }

   @Test
   void checkToString() {
      Account account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d",
            NetworkType.MIJIN_TEST);
      assertTrue(account.toString().startsWith("Account"));
   }
}
