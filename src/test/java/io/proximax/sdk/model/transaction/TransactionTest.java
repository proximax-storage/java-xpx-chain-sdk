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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

public class TransactionTest {
   private static final String GENERATION_HASH = "AC87FDA8FD94B72F3D0790A7D62F248111BD5E37B95B16E4216DA99C212530A5";
   private final PublicAccount signer = PublicAccount.createFromPublicKey(
         "F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
         NetworkType.TEST_NET);

   @Test
   void generateHashFromTransferTransactionPayload() {
      String hash = Transaction.createTransactionHash(
            Hex.decode("C7000000D0B190DFEEAB0378F943F79CDB7BC44453491890FAA70F5AA95B909E67487408407956BDE32AC977D035FBBA575C11AA034B23402066C16FD6126893F3661B099A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24039054410000000000000000A76541BE0C00000090E8FEBD671DD41BEE94EC3BA5831CB608A312C2F203BA84AC03000300303064000000000000006400000000000000002F00FA0DEDD9086400000000000000443F6D806C05543A6400000000000000"),
            Hex.decode(GENERATION_HASH));
      assertEquals("2232457ECED2CD76E1A08CCD1F2BC3338F55AF6AEF12C931BB3AF5D17019E657", hash);
   }

   @Test
   void generateHashFromAggregateTransactionPayload() {
      String hash = Transaction.createTransactionHash(
            Hex.decode("E9000000A37C8B0456474FB5E3E910E84B5929293C114E0AF97FEF0D940D3A2A2C337BAFA0C59538E5988229B65A3065B4E9BD57B1AFAEC64DFBE2211B8AF6E742801E08C2F93346E27CE6AD1A9F8F5E3066F8326593A406BDF357ACB041E2F9AB402EFE0390414100000000000000008EEAC2C80C0000006D0000006D000000C2F93346E27CE6AD1A9F8F5E3066F8326593A406BDF357ACB041E2F9AB402EFE0390554101020200B0F93CBEE49EEB9953C6F3985B15A4F238E205584D8F924C621CBE4D7AC6EC2400B1B5581FC81A6970DEE418D2C2978F2724228B7B36C5C6DF71B0162BB04778B4"),
            Hex.decode(GENERATION_HASH));
      assertEquals("E9FE0C729595F5D4D0731F448C27FB1821688E8E62C17D195DE53FBD6E4560F8", hash);
   }

   @Test
   void shouldReturnTransactionIsUnannouncedWhenThereIsNoTransactionInfo() {
      FakeTransferTransaction fakeTransaction = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0));

      assertTrue(fakeTransaction.isUnannounced());
   }

   @Test
   void shouldReturnTransactionIsUnconfirmedWhenHeightIs0() {
      FakeTransferTransaction fakeTransaction = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0), "signature", signer,
            TransactionInfo.create(BigInteger.valueOf(0), 1, "id_hash", "hash", "hash"));

      assertTrue(fakeTransaction.isUnconfirmed());
   }

   @Test
   void shouldReturnTransactionIsNotUnconfirmedWhenHeightIsNot0() {
      FakeTransferTransaction fakeTransaction = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0), "signature", signer,
            TransactionInfo.create(BigInteger.valueOf(100), 1, "id_hash", "hash", "hash"));

      assertFalse(fakeTransaction.isUnconfirmed());
   }

   @Test
   void shouldReturnTransactionIsConfirmedWhenHeightIsNot0() {
      FakeTransferTransaction fakeTransaction = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0), "signature", signer,
            TransactionInfo.create(BigInteger.valueOf(100), 1, "id_hash", "hash", "hash"));

      assertTrue(fakeTransaction.isConfirmed());
   }

   @Test
   void shouldReturnTransactionIsAggregateBondedWhenHeightIs0AndHashAndMerkHashAreDifferent() {
      FakeTransferTransaction fakeTransaction = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0), "signature", signer,
            TransactionInfo.create(BigInteger.valueOf(0), 1, "id_hash", "hash", "hash_2"));

      assertTrue(fakeTransaction.hasMissingSignatures());
   }

   @Test
   void testToStringImplemented() {
      FakeTransferTransaction fakeTransaction = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0), "signature", signer,
            TransactionInfo.create(BigInteger.valueOf(0), 1, "id_hash", "hash", "hash_2"));
      assertTrue(fakeTransaction.toString().startsWith("Transaction "));
   }
   
   @Test
   void testSign() {
      System.out.println(Hex.decode("42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85").length);
   }
   
   @Test
   void testSerializationVersion() {
      FakeTransferTransaction trans = new FakeTransferTransaction(NetworkType.TEST_NET, 1,
            new FakeDeadline(), BigInteger.valueOf(0), "signature", signer,
            TransactionInfo.create(BigInteger.valueOf(0), 1, "id_hash", "hash", "hash_2"));
      // now mijin test is 0x90 and trans version is set to 1
      // (144 << 24) + 1
      assertEquals((144 << 24) + 1, trans.getTxVersionforSerialization());
   }
}
