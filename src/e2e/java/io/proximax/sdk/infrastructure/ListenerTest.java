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

package io.proximax.sdk.infrastructure;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.AccountRepository;
import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.proximax.sdk.model.transaction.CosignatureTransaction;
import io.proximax.sdk.model.transaction.Deadline;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionStatusError;
import io.proximax.sdk.model.transaction.TransferTransaction;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("listeners are used by other tests")
class ListenerTest extends BaseTest {
   private BlockchainApi api;
   private TransactionRepository transactionHttp;
   private AccountRepository accountHttp;
   private Account account;
   private Account multisigAccount;
   private Account cosignatoryAccount;
   private Account cosignatoryAccount2;

   @BeforeAll
   void setup() throws IOException {
      api = new BlockchainApi(new URL(getNodeUrl()), getNetworkType());
      transactionHttp = api.createTransactionRepository();
      accountHttp = api.createAccountRepository();
      account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.MIJIN_TEST);
      multisigAccount = new Account("5edebfdbeb32e9146d05ffd232c8af2cf9f396caf9954289daa0362d097fff3b",
            NetworkType.MIJIN_TEST);
      cosignatoryAccount = new Account("2a2b1f5d366a5dd5dc56c3c757cf4fe6c66e2787087692cf329d7a49a594658b",
            NetworkType.MIJIN_TEST);
      cosignatoryAccount2 = new Account("b8afae6f4ad13a1b8aad047b488e0738a437c7389d4ff30c359ac068910c1d59",
            NetworkType.MIJIN);
   }

   @Test
   void shouldConnectToWebSocket() throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      CompletableFuture<Void> connected = listener.open();
      connected.get();
      assertTrue(connected.isDone());
      assertNotNull(listener.getUID());
   }

   @Test
   void shouldReturnNewBlockViaListener() throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      this.announceStandaloneTransferTransaction();

      BlockInfo blockInfo = listener.newBlock().take(1).toFuture().get();

      assertTrue(blockInfo.getHeight().intValue() > 0);
   }

   @Test
   void shouldReturnConfirmedTransactionAddressSignerViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceStandaloneTransferTransaction();

      Transaction transaction = listener.confirmed(this.account.getAddress()).take(1).toFuture().get();
      assertEquals(signedTransaction.getHash(), transaction.getTransactionInfo().get().getHash());
   }

   @Test
   void shouldReturnConfirmedTransactionAddressRecipientViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceStandaloneTransferTransaction();

      Transaction transaction = listener
            .confirmed(Address.createFromRawAddress("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC")).take(1).toFuture()
            .get();
      assertEquals(signedTransaction.getHash(), transaction.getTransactionInfo().get().getHash());

   }

   @Test
   void shouldReturnUnconfirmedAddedTransactionViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceStandaloneTransferTransaction();

      Transaction transaction = listener.unconfirmedAdded(this.account.getAddress()).take(1).toFuture().get();
      assertEquals(signedTransaction.getHash(), transaction.getTransactionInfo().get().getHash());
   }

   @Test
   void shouldReturnUnconfirmedRemovedTransactionViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceStandaloneTransferTransaction();

      String transactionHash = listener.unconfirmedRemoved(this.account.getAddress()).take(1).toFuture().get();
      assertEquals(signedTransaction.getHash(), transactionHash);
   }

   @Disabled
   @Test
   void shouldReturnAggregateBondedAddedTransactionViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceAggregateBondedTransaction();

      AggregateTransaction aggregateTransaction = listener.aggregateBondedAdded(this.account.getAddress()).take(1)
            .toFuture().get();
      assertEquals(signedTransaction.getHash(), aggregateTransaction.getTransactionInfo().get().getHash());
   }

   @Disabled
   @Test
   void shouldReturnAggregateBondedRemovedTransactionViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceAggregateBondedTransaction();

      String transactionHash = listener.aggregateBondedRemoved(this.account.getAddress()).take(1).toFuture().get();
      assertEquals(signedTransaction.getHash(), transactionHash);
   }

   @Disabled
   @Test
   void shouldReturnCosignatureAddedViaListener() throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceAggregateBondedTransaction();

      AggregateTransaction announcedTransaction = listener.aggregateBondedAdded(this.cosignatoryAccount.getAddress())
            .take(1).toFuture().get();

      assertEquals(signedTransaction.getHash(), announcedTransaction.getTransactionInfo().get().getHash());

      List<AggregateTransaction> transactions = accountHttp
            .aggregateBondedTransactions(this.cosignatoryAccount.getPublicAccount()).toFuture().get();

      AggregateTransaction transactionToCosign = transactions.get(0);

      this.announceCosignatureTransaction(transactionToCosign);

      CosignatureSignedTransaction cosignatureSignedTransaction = listener
            .cosignatureAdded(this.cosignatoryAccount.getAddress()).take(1).toFuture().get();

      assertEquals(cosignatureSignedTransaction.getSigner(), this.cosignatoryAccount2.getPublicKey());
   }

   @Test
   void shouldReturnTransactionStatusGivenAddedViaListener()
         throws ExecutionException, InterruptedException, IOException {
      ListenerRepository listener = api.createListener();
      listener.open().get();

      SignedTransaction signedTransaction = this.announceStandaloneTransferTransactionWithInsufficientBalance();

      TransactionStatusError transactionHash = listener.status(this.account.getAddress()).take(1).toFuture().get();
      assertEquals(signedTransaction.getHash(), transactionHash.getHash());
   }

   private SignedTransaction announceStandaloneTransferTransaction() throws ExecutionException, InterruptedException {
      TransferTransaction transferTransaction = TransferTransaction.create(new Deadline(2, HOURS),
            new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST),
            Arrays.asList(),
            PlainMessage.create("test-message"),
            NetworkType.MIJIN_TEST);

      SignedTransaction signedTransaction = this.account.sign(transferTransaction, api.getNetworkGenerationHash());
      transactionHttp.announce(signedTransaction).toFuture().get();
      return signedTransaction;
   }

   private SignedTransaction announceStandaloneTransferTransactionWithInsufficientBalance()
         throws ExecutionException, InterruptedException {
      TransferTransaction transferTransaction = TransferTransaction.create(new Deadline(2, HOURS),
            new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST),
            Arrays.asList(NetworkCurrencyMosaic.createRelative(new BigDecimal("100000000000"))),
            PlainMessage.create("test-message"),
            NetworkType.MIJIN_TEST);

      SignedTransaction signedTransaction = this.account.sign(transferTransaction, api.getNetworkGenerationHash());
      transactionHttp.announce(signedTransaction).toFuture().get();
      return signedTransaction;
   }

   private SignedTransaction announceAggregateBondedTransaction() throws ExecutionException, InterruptedException {
      TransferTransaction transferTransaction = TransferTransaction.create(new Deadline(2, HOURS),
            new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST),
            Arrays.asList(),
            PlainMessage.create("test-message"),
            NetworkType.MIJIN_TEST);

      AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(new Deadline(2, HOURS),
            Collections.singletonList(transferTransaction.toAggregate(this.multisigAccount.getPublicAccount())),
            NetworkType.MIJIN_TEST);

      SignedTransaction signedTransaction = this.cosignatoryAccount.sign(aggregateTransaction, api.getNetworkGenerationHash());

      transactionHttp.announceAggregateBonded(signedTransaction).toFuture().get();

      return signedTransaction;
   }

   private CosignatureSignedTransaction announceCosignatureTransaction(AggregateTransaction transactionToCosign)
         throws ExecutionException, InterruptedException {
      CosignatureTransaction cosignatureTransaction = new CosignatureTransaction(transactionToCosign);

      CosignatureSignedTransaction cosignatureSignedTransaction = this.cosignatoryAccount2
            .signCosignatureTransaction(cosignatureTransaction);

      transactionHttp.announceAggregateBondedCosignature(cosignatureSignedTransaction).toFuture().get();

      return cosignatureSignedTransaction;
   }
}