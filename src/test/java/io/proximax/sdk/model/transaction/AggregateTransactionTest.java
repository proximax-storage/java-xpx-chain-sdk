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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.infrastructure.TransactionMapping;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;
import io.proximax.sdk.utils.GsonUtils;

public class AggregateTransactionTest extends ResourceBasedTest {

   @Test
   void createAAggregateTransactionViaStaticConstructor() {

      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST)),
            Collections.emptyList(), PlainMessage.EMPTY);

      PublicAccount innerSigner = new PublicAccount("9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24",
            NetworkType.MIJIN_TEST);

      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new Deadline(2, ChronoUnit.HOURS))
            .networkType(NetworkType.MIJIN_TEST).build();

      assertEquals(NetworkType.MIJIN_TEST, aggregateTx.getNetworkType());
      assertTrue(2 == aggregateTx.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < aggregateTx.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), aggregateTx.getMaxFee());
      assertEquals(1, aggregateTx.getInnerTransactions().size());
   }

   @Test
   void serialization() throws IOException {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST)),
            Collections.singletonList(new Mosaic(NetworkCurrencyMosaic.ID, BigInteger.valueOf(10000000))), PlainMessage.EMPTY);

      PublicAccount innerSigner = new PublicAccount("846B4439154579A5903B1459C9CF69CB8153F6D0110A7A0ED61DE29AE4810BF2",
            NetworkType.MIJIN_TEST);
      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new FakeDeadline())
            .networkType(NetworkType.MIJIN_TEST).build();

      byte[] actual = aggregateTx.generateBytes();
//        saveBytes("aggregate_trans", actual);
      assertArrayEquals(loadBytes("aggregate_trans"), actual);
   }

   @Test
   void shouldCreateAggregateTransactionAndSignWithMultipleCosignatories() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST)),
            Arrays.asList(), new PlainMessage("test-message"));

      PublicAccount innerSigner = new PublicAccount("B694186EE4AB0558CA4AFCFDD43B42114AE71094F5A1FC4A913FE9971CACD21D",
            NetworkType.MIJIN_TEST);
      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new FakeDeadline())
            .networkType(NetworkType.MIJIN_TEST).build();

      Account cosignatoryAccount = new Account("2a2b1f5d366a5dd5dc56c3c757cf4fe6c66e2787087692cf329d7a49a594658b",
            NetworkType.MIJIN_TEST);
      Account cosignatoryAccount2 = new Account("b8afae6f4ad13a1b8aad047b488e0738a437c7389d4ff30c359ac068910c1d59",
            NetworkType.MIJIN_TEST);

      SignedTransaction signedTransaction = cosignatoryAccount.signTransactionWithCosignatories(aggregateTx,
            "7B631D803F912B00DC0CBED3014BBD17A302BA50B99D233B9C2D9533B842ABDF",
            Arrays.asList(cosignatoryAccount2));

      assertEquals("31010000", signedTransaction.getPayload().substring(0, 8));
      assertEquals("0000530000005300", signedTransaction.getPayload().substring(240, 256));
      // assertEquals("039054419050B9837EFAB4BBE8A4B9BB32D812F9885C00D8FC1650E1420D000000746573742D6D65737361676568B3FBB18729C1FDE225C57F8CE080FA828F0067E451A3FD81FA628842B0B763",
      // signedTransaction.getPayload().substring(320, 474));

   }
   
   @Test
   void checkCopyToSigner() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.MIJIN_TEST, 3,
            new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST)),
            Arrays.asList(), new PlainMessage("test-message"));

      PublicAccount innerSigner = new PublicAccount("B694186EE4AB0558CA4AFCFDD43B42114AE71094F5A1FC4A913FE9971CACD21D",
            NetworkType.MIJIN_TEST);
      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new FakeDeadline())
            .networkType(NetworkType.MIJIN_TEST).build();

      assertThrows(UnsupportedOperationException.class, () -> aggregateTx.copyForSigner(innerSigner));
   }

   @Test
   @Disabled
   void shouldFindAccountInAsASignerOfTheTransaction() {
      JsonObject aggregateTransferTransactionDTO = GsonUtils.mapToJsonObject(
            "{\"meta\":{\"hash\":\"671653C94E2254F2A23EFEDB15D67C38332AED1FBD24B063C0A8E675582B6A96\",\"height\":[18160,0],\"id\":\"5A0069D83F17CF0001777E55\",\"index\":0,\"merkleComponentHash\":\"81E5E7AE49998802DABC816EC10158D3A7879702FF29084C2C992CD1289877A7\"},\"transaction\":{\"cosignatures\":[{\"signature\":\"5780C8DF9D46BA2BCF029DCC5D3BF55FE1CB5BE7ABCF30387C4637DDEDFC2152703CA0AD95F21BB9B942F3CC52FCFC2064C7B84CF60D1A9E69195F1943156C07\",\"signer\":\"A5F82EC8EBB341427B6785C8111906CD0DF18838FB11B51CE0E18B5E79DFF630\"}],\"deadline\":[3266625578,11],\"fee\":[0,0],\"signature\":\"939673209A13FF82397578D22CC96EB8516A6760C894D9B7535E3A1E068007B9255CFA9A914C97142A7AE18533E381C846B69D2AE0D60D1DC8A55AD120E2B606\",\"signer\":\"7681ED5023141D9CDCF184E5A7B60B7D466739918ED5DA30F7E71EA7B86EFF2D\",\"transactions\":[{\"meta\":{\"aggregateHash\":\"3D28C804EDD07D5A728E5C5FFEC01AB07AFA5766AE6997B38526D36015A4D006\",\"aggregateId\":\"5A0069D83F17CF0001777E55\",\"height\":[18160,0],\"id\":\"5A0069D83F17CF0001777E56\",\"index\":0},\"transaction\":{\"message\":{\"payload\":\"746573742D6D657373616765\",\"type\":0},\"mosaics\":[{\"amount\":[3863990592,95248],\"id\":[3646934825,3576016193]}],\"recipient\":\"9050B9837EFAB4BBE8A4B9BB32D812F9885C00D8FC1650E142\",\"signer\":\"B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF\",\"type\":16724,\"version\":36867}}],\"type\":16705,\"version\":36867}}");

      AggregateTransaction aggregateTransferTransaction = (AggregateTransaction) new TransactionMapping()
            .apply(aggregateTransferTransactionDTO);

      assertTrue(aggregateTransferTransaction.isSignedByAccount(
            PublicAccount.createFromPublicKey("A5F82EC8EBB341427B6785C8111906CD0DF18838FB11B51CE0E18B5E79DFF630",
                  NetworkType.MIJIN_TEST)));
      assertTrue(aggregateTransferTransaction.isSignedByAccount(
            PublicAccount.createFromPublicKey("7681ED5023141D9CDCF184E5A7B60B7D466739918ED5DA30F7E71EA7B86EFF2D",
                  NetworkType.MIJIN_TEST)));
      assertFalse(aggregateTransferTransaction.isSignedByAccount(
            PublicAccount.createFromPublicKey("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.MIJIN_TEST)));

   }
}
