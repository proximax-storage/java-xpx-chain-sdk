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

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.infrastructure.TransactionMapping;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;
import io.proximax.sdk.utils.GsonUtils;

public class AggregateTransactionTest extends ResourceBasedTest {

   @Test
   void createAAggregateTransactionViaStaticConstructor() {

      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET)),
            Collections.emptyList(), PlainMessage.EMPTY);

      PublicAccount innerSigner = new PublicAccount("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
            NetworkType.TEST_NET);

      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new Deadline(2, ChronoUnit.HOURS))
            .networkType(NetworkType.TEST_NET).build();

      assertEquals(NetworkType.TEST_NET, aggregateTx.getNetworkType());
      assertEquals(3, aggregateTx.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < aggregateTx.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), aggregateTx.getMaxFee());
      assertEquals(1, aggregateTx.getInnerTransactions().size());
   }

   @Test
   void serialization() throws IOException {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET)),
            Collections.singletonList(new Mosaic(NetworkCurrencyMosaic.ID, BigInteger.valueOf(10000000))),
            PlainMessage.EMPTY);

      PublicAccount innerSigner = new PublicAccount("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
            NetworkType.TEST_NET);
      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new FakeDeadline())
            .networkType(NetworkType.TEST_NET).build();

      byte[] actual = aggregateTx.generateBytes();
//      saveBytes("aggregate_trans", actual);
      assertArrayEquals(loadBytes("aggregate_trans"), actual);
   }

   @Test
   void shouldCreateAggregateTransactionAndSignWithMultipleCosignatories() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VB6K3ABE44ZQ4UU2SQEJ2MONQKANV4ZXW66Q5HT3", NetworkType.TEST_NET)),
            Arrays.asList(), new PlainMessage("test-message"));

      PublicAccount innerSigner = new PublicAccount("F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
            NetworkType.TEST_NET);
      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new FakeDeadline())
            .networkType(NetworkType.TEST_NET).build();

      Account cosignatoryAccount = new Account("1253749245020f997e1a5e43ad3edaf45a8fc36a1103ac894c48bdfabcd2ea40",
            NetworkType.TEST_NET);
      Account cosignatoryAccount2 = new Account("b972e62b4f49de3b1ce68443ad1f33824889c254d50bee9edb1c05791396d635",
            NetworkType.TEST_NET);

      SignedTransaction signedTransaction = cosignatoryAccount.signTransactionWithCosignatories(aggregateTx,
            "AC87FDA8FD94B72F3D0790A7D62F248111BD5E37B95B16E4216DA99C212530A5",
            Arrays.asList(cosignatoryAccount2));

      assertEquals("31010000", signedTransaction.getPayload().substring(0, 8));
      assertEquals("0000530000005300", signedTransaction.getPayload().substring(240, 256));
      // assertEquals("039054419050B9837EFAB4BBE8A4B9BB32D812F9885C00D8FC1650E1420D000000746573742D6D65737361676568B3FBB18729C1FDE225C57F8CE080FA828F0067E451A3FD81FA628842B0B763",
      // signedTransaction.getPayload().substring(320, 474));

   }

   @Test
   void serializeAggregateWithCosigners() throws IOException {
      NetworkType netType = NetworkType.TEST_NET;
      TransactionBuilderFactory fac = new TransactionBuilderFactory();
      fac.setFeeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
      fac.setNetworkType(netType);

      Account alice = Account.createFromPrivateKey("4b43e9897409e8b010806985fe8c00b2f5b3148c62aa1efe703d6061fa0761bf",
            netType);
      Account bob = Account.createFromPrivateKey("1253749245020f997e1a5e43ad3edaf45a8fc36a1103ac894c48bdfabcd2ea40",
            netType);
      Mosaic amount = new Mosaic(new MosaicId("0dc67fbe1cad29e3"), BigInteger.valueOf(1000));
      String gen_hash = "AC87FDA8FD94B72F3D0790A7D62F248111BD5E37B95B16E4216DA99C212530A5";

      AggregateTransaction aggregateTx = fac.aggregateComplete().innerTransactions(
            fac.transfer().mosaics(amount).to(alice).signer(bob).deadline(new FakeDeadline()).build(),
            fac.transfer().mosaics(amount).to(bob).signer(alice).deadline(new FakeDeadline()).build())
            .deadline(new FakeDeadline()).build();

      SignedTransaction signedTransaction = alice
            .signTransactionWithCosignatories(aggregateTx, gen_hash, Arrays.asList(bob));

//      saveBytes("aggregate_trans_cosigs", aggregateTx.generateBytes());
//      saveBytes("aggregate_trans_cosigs_signed", signedTransaction.getPayload().getBytes());

      assertArrayEquals(loadBytes("aggregate_trans_cosigs"), aggregateTx.generateBytes());
      assertArrayEquals(loadBytes("aggregate_trans_cosigs_signed"), signedTransaction.getPayload().getBytes());

   }

   @Test
   void checkCopyToSigner() {
      TransferTransaction transferTx = new TransferTransaction(NetworkType.TEST_NET, 3, new FakeDeadline(),
            BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            Recipient.from(new Address("VAUFFX5Q4YXDBHGRJQPUK7QXGNSNFJETVBHNASLR", NetworkType.TEST_NET)),
            Arrays.asList(), new PlainMessage("test-message"));

      PublicAccount innerSigner = new PublicAccount("39468C1395B9A315A7306AFB3066A3BF1BA593D1C349E83BC7F0B7DF521B5BA5",
            NetworkType.TEST_NET);
      AggregateTransaction aggregateTx = new TransactionBuilderFactory().aggregateComplete()
            .innerTransactions(transferTx.toAggregate(innerSigner)).deadline(new FakeDeadline())
            .networkType(NetworkType.TEST_NET).build();

      assertThrows(UnsupportedOperationException.class, () -> aggregateTx.copyForSigner(innerSigner));
   }

   @Test
   @Disabled
   void shouldFindAccountInAsASignerOfTheTransaction() {
      JsonObject aggregateTransferTransactionDTO = GsonUtils.mapToJsonObject(
                  "{\"meta\":{\"height\":[130996,0],\"hash\":\"1E9BD8D5D13D85466555D799298A397CF1399AA6389363027BEBA97604212B2A\",\"merkleComponentHash\":\"1E9BD8D5D13D85466555D799298A397CF1399AA6389363027BEBA97604212B2A\",\"index\":0,\"id\":\"6191E454A4E1A152670C2653\"},\"transaction\":{\"signature\":\"2EEF3260F5202074867629EE761295217F5FC3FB1AC3E201F3E2F7FFD54F0B2D2BB02A41B2E6BDD03E4F05F011E7ECE30C1D3D4CBC0B7BD83E11E55FAC97D906\",\"signer\":\"42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85\",\"version\":2818572290,\"type\":16705,\"maxFee\":[0,0],\"deadline\":[1392261397,41],\"cosignatures\":[],\"transactions\":[{\"meta\":{\"height\":[130996,0],\"aggregateHash\":\"1E9BD8D5D13D85466555D799298A397CF1399AA6389363027BEBA97604212B2A\",\"uniqueAggregateHash\":\"86C47C9D2CF804EF8B910B7AEF5E81F12F8503BF84CD11243941E4913409216F\",\"aggregateId\":\"6191E454A4E1A152670C2653\",\"index\":0,\"id\":\"6191E454A4E1A152670C2654\"},\"transaction\":{\"signer\":\"42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85\",\"version\":2818572291,\"type\":16724,\"recipient\":\"A8EAB19AB426757D083D0656EA9EBEA2B8378A073D8E2AFFB3\",\"message\":{\"type\":0,\"payload\":\"746573742D6D657373616765\"},\"mosaics\":[{\"id\":[2434186742,3220914849],\"amount\":[1000000000,0]}]}}]}}");

      AggregateTransaction aggregateTransferTransaction = (AggregateTransaction) new TransactionMapping()
            .apply(aggregateTransferTransactionDTO);

      assertTrue(aggregateTransferTransaction.isSignedByAccount(
            PublicAccount.createFromPublicKey("42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                  NetworkType.TEST_NET)));
      assertTrue(aggregateTransferTransaction.isSignedByAccount(
            PublicAccount.createFromPublicKey("42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                  NetworkType.TEST_NET)));
      assertFalse(aggregateTransferTransaction.isSignedByAccount(
            PublicAccount.createFromPublicKey("42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                  NetworkType.TEST_NET)));

   }
}
