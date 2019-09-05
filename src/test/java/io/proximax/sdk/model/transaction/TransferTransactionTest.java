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
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;

class TransferTransactionTest extends ResourceBasedTest {
   static Account account;

   @BeforeAll
   public static void setup() {
      account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.MIJIN_TEST);
   }

   @Test
   void createATransferTransaction() {

      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.valueOf(37750), Optional.empty(), Optional.empty(),
            Optional.empty(),
            Recipient.from(new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST)),
            Arrays.asList(), PlainMessage.EMPTY);

      assertEquals(NetworkType.MIJIN_TEST, transferTx.getNetworkType());
      assertEquals(3, transferTx.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < transferTx.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(37750), transferTx.getMaxFee());
      assertTrue(new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST)
            .equals(transferTx.getRecipient().getAddress().orElseThrow(RuntimeException::new)));
      assertEquals(0, transferTx.getMosaics().size());
      assertNotNull(transferTx.getMessage());
   }

   @Test
   @DisplayName("Serialization")
   void serialization() throws IOException {
      TransferTransaction transferTransaction = new TransferTransaction(NetworkType.MIJIN_TEST, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM", NetworkType.MIJIN_TEST)),
            Arrays.asList(new Mosaic(new MosaicId(new BigInteger("95442763262823")), BigInteger.valueOf(100))),
            PlainMessage.EMPTY);

      byte[] actual = transferTransaction.generateBytes();
//        saveBytes("transfer_trans", actual);
      assertArrayEquals(loadBytes("transfer_trans"), actual);
   }

   @Test
   @DisplayName("To aggregate")
   void toAggregate() throws IOException {
      TransferTransaction transferTransaction = new TransferTransaction(NetworkType.MIJIN_TEST, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM", NetworkType.MIJIN_TEST)),
            Arrays.asList(new Mosaic(new MosaicId(new BigInteger("95442763262823")), BigInteger.valueOf(100))),
            PlainMessage.EMPTY);

      byte[] actual = transferTransaction
            .toAggregate(new PublicAccount("9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24",
                  NetworkType.MIJIN_TEST))
            .toAggregateTransactionBytes();
//        saveBytes("transfer_trans_toagg", actual);
      assertArrayEquals(loadBytes("transfer_trans_toagg"), actual);
   }

   @Test
   void serializeAndSignTransaction() {
      TransferTransaction transferTransaction = new TransferTransaction(NetworkType.MIJIN_TEST, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM", NetworkType.MIJIN_TEST)),
            Arrays.asList(new Mosaic(new MosaicId(new BigInteger("95442763262823")), BigInteger.valueOf(100))),
            PlainMessage.EMPTY);

      SignedTransaction signedTransaction = transferTransaction.signWith(account,
            "7B631D803F912B00DC0CBED3014BBD17A302BA50B99D233B9C2D9533B842ABDF");

      assertEquals(
            "A700000012714D1BA6A1169BBEC67ADE7B77D107BA81D22CB90AF46AC398549FEE693F21228F94FBEDE0122318408F8247FEEBD753D6D6EF9F243E0E0507B38922B222081026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF377550300009054410000000000000000010000000000000090E8FEBD671DD41BEE94EC3BA5831CB608A312C2F203BA84AC01000100672B0000CE5600006400000000000000",
            signedTransaction.getPayload());
      assertEquals("00E440438762F09A150235494F9C11F074F310D102CB708751C712B4A84F810A", signedTransaction.getHash());
   }
}
