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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;

class TransferTransactionTest extends ResourceBasedTest {
   static Account account;

   @BeforeAll
   public static void setup() {
      account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.TEST_NET);
   }

   @Test
   void createATransferTransaction() {

      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET)),
            Arrays.asList(), PlainMessage.EMPTY);

      assertEquals(NetworkType.TEST_NET, transferTx.getNetworkType());
      assertEquals(3, transferTx.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < transferTx.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(37750), transferTx.getMaxFee());
      assertTrue(new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET)
            .equals(transferTx.getRecipient().getAddress().orElseThrow(RuntimeException::new)));
      assertEquals(0, transferTx.getMosaics().size());
      assertNotNull(transferTx.getMessage());
   }

   @Test
   @DisplayName("Serialization")
   void serialization() throws IOException {
      TransferTransaction transferTransaction = new TransferTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VDPQS6FBYDN3SD2QJPHUWRYWNHSSOQ2Q35VI7TDP", NetworkType.TEST_NET)),
            Arrays.asList(new Mosaic(new MosaicId("0de5f42a0a3b4200"), BigInteger.valueOf(100))),
            PlainMessage.EMPTY);

      byte[] actual = transferTransaction.generateBytes();
//        saveBytes("transfer_trans", actual);
      assertArrayEquals(loadBytes("transfer_trans"), actual);
   }

   @Test
   @DisplayName("To aggregate")
   void toAggregate() throws IOException {
      TransferTransaction transferTransaction = new TransferTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET)),
            Arrays.asList(new Mosaic(new MosaicId("028abda49c1ee21d"), BigInteger.valueOf(100))),
            PlainMessage.EMPTY);

      byte[] actual = transferTransaction
            .toAggregate(new PublicAccount("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
                  NetworkType.TEST_NET))
            .toAggregateTransactionBytes();
//        saveBytes("transfer_trans_toagg", actual);
      assertArrayEquals(loadBytes("transfer_trans_toagg"), actual);
   }

   @Test
   void serializeAndSignTransaction() {
      TransferTransaction transferTransaction = new TransferTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET)),
            Arrays.asList(new Mosaic(new MosaicId(new BigInteger("95442763262823")), BigInteger.valueOf(100))),
            PlainMessage.EMPTY);

      SignedTransaction signedTransaction = transferTransaction.signWith(account,
            "AC87FDA8FD94B72F3D0790A7D62F248111BD5E37B95B16E4216DA99C212530A5");

      assertEquals("A7000000E01DBDAA735C6E8657C02CCB73B08E9679480F57F9C61466EE2E4BF54C5F715790EE1EAE1C2FE5EC52950BF937ED28E06B2F686C8870CB8F626CD3047C91400B1026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF37755030000A8544100000000000000000100000000000000A8B26240287492CB5B75B32ABE3D6CCB66C93908D73CF41FD901000100672B0000CE5600006400000000000000",signedTransaction.getPayload());
      
      assertEquals("6B66E3D17B1BC7AADBAA156299E816BF54C4868B664702379E9AB55F1B1FB40A", signedTransaction.getHash());
   }
}
