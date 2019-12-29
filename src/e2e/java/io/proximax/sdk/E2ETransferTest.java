/*
 * Copyright 2019 ProximaX
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
package io.proximax.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.helpers.AccountHelper;
import io.proximax.sdk.helpers.TransferHelper;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.SecureMessage;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * E2E tests that demonstrate transfers
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2ETransferTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ETransferTest.class);

   private final Account simpleAccount = new Account(new KeyPair(), getNetworkType());

   @BeforeAll
   void addListener() {
      logger.info("Sending transactions to {}", simpleAccount);
      signup(simpleAccount.getAddress());
   }

   @AfterAll
   void closeDown() {
      // return the funds
      returnAllToSeed(simpleAccount);
   }

   @Test
   void sendPlaintextMessage() {
      // send transaction with plain message
      transferSimple(seedAccount,
            simpleAccount.getAddress(),
            NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1)),
            new PlainMessage("java SDK plain message test"));
   }

   @Test
   void sendPlaintextMessageAggregate() {
      // send transaction with plain message
      transferAggregate(seedAccount,
            simpleAccount.getAddress(),
            NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1)),
            new PlainMessage("java SDK plain message test"));
   }

   @Test
   void sendSecureMessage() {
      // send transaction with encrypted message
      SecureMessage secureMessage = SecureMessage.create(seedAccount.getKeyPair().getPrivateKey(),
            simpleAccount.getKeyPair().getPublicKey(),
            "java SDK secure message");
      transferSimple(seedAccount,
            simpleAccount.getAddress(),
            NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1)),
            secureMessage);
   }

   @Test
   void sendSecureMessageAggregate() {
      // send transaction with encrypted message
      SecureMessage secureMessage = SecureMessage.create(seedAccount.getKeyPair().getPrivateKey(),
            simpleAccount.getKeyPair().getPublicKey(),
            "java SDK secure message");
      transferAggregate(seedAccount,
            simpleAccount.getAddress(),
            NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1)),
            secureMessage);
   }

   @Test
   void insufficientFunds() {
      TransferTransaction transaction = transact.transfer().to(seedAccount.getAddress())
            .mosaics(NetworkCurrencyMosaic.TEN).build();
      SignedTransaction signedTransaction = api.sign(transaction, simpleAccount);
      transactionHttp.announce(signedTransaction).blockingFirst();
      // await error
      listener.status(simpleAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
   }

   @Test
   void helperSimpleTransfer() {
      TransferHelper hlp = new TransferHelper(api);
      // prepare transfer info
      Mosaic mosaic = NetworkCurrencyMosaic.createRelative(1.234);
      Account recipient = new Account(new KeyPair(), getNetworkType());
      // make twice the transfer of 1.234 network currency
      hlp.transfer(seedAccount, recipient.getAddress(), mosaic, 120);
      hlp.transfer(seedAccount, recipient.getAddress(), mosaic, PlainMessage.create("hello"), 120);
      // wait a bit
      sleepForAWhile();
      // retrieve account info
      AccountInfo recptInfo = accountHttp.getAccountInfo(recipient.getAddress()).blockingFirst();
      assertEquals(1, recptInfo.getMosaics().size());
      assertEquals(mosaic.getAmount().multiply(BigInteger.valueOf(2)), recptInfo.getMosaics().get(0).getAmount());
      // return the funds
      returnAllToSeed(recipient);
   }
   
   @Test
   void helperMultisigTransfer() {
      TransferHelper trn = new TransferHelper(api);
      AccountHelper act = new AccountHelper(api);
      // prepare data
      Account multi = signup(new Account(new KeyPair(), getNetworkType()));
      Account cosig1 = signup(new Account(new KeyPair(), getNetworkType()));
      Account cosig2 = signup(new Account(new KeyPair(), getNetworkType()));
      Account cosig3 = signup(new Account(new KeyPair(), getNetworkType()));
      // send 10 network currency to the to-be-multisig account so the lock can happen
      trn.transfer(seedAccount, multi.getAddress(), NetworkCurrencyMosaic.createRelative(10.5), 120);
      trn.transfer(seedAccount, cosig1.getAddress(), NetworkCurrencyMosaic.createRelative(10.5), 120);
      // convert account to multisig
      String transactionHash = act.accountToMultisig(multi,
            Arrays.asList(cosig1.getPublicAccount(), cosig2.getPublicAccount(), cosig3.getPublicAccount()),
            2,
            2,
            500,
            1200);
      // now cosigners need to opt-in by cosigning the multisig change
      act.cosignAggregateTransaction(multi.getPublicAccount(), transactionHash, 120, cosig1, cosig2, cosig3);
      // wait a bit just to be sure
      sleepForAWhile();
      // make transfer from the multisig
      String transferHash = trn.transferFromMultisig(multi.getPublicAccount(),
            cosig1,
            seedAccount.getAddress(),
            NetworkCurrencyMosaic.TEN,
            PlainMessage.EMPTY,
            120,
            50);
      // cosign the transfer by cosig2
      act.cosignAggregateTransaction(multi.getPublicAccount(), transferHash, 120, cosig2);
   }
   
   /**
    * return transactions as specified by arguments signed by the signer account
    * 
    * @param signerAccount account used to sign the transaction
    * @param target target address for the transfer
    * @param amount mosaic to transfer
    * @param message message for the transfer
    * @return instance of signed transaction which can be then announced to the network
    */
   private SignedTransaction signTransfer(Account signerAccount, Address target, Mosaic amount, Message message) {
      TransferTransaction transaction = transact.transfer().mosaics(amount).to(target).message(message).build();
      return api.sign(transaction, signerAccount);
   }

   /**
    * return transactions as specified by arguments signed by the signer account
    * 
    * @param signerAccount account used to sign the transaction
    * @param target target address for the transfer
    * @param amount mosaic to transfer
    * @param message message for the transfer
    * @return instance of signed transaction which can be then announced to the network
    */
   private SignedTransaction signAggregateTransfer(Account signerAccount, Address target, Mosaic amount,
         Message message) {
      TransferTransaction transfer = transact.transfer().mosaics(amount).to(target).message(message).build();
      // add the modification to the aggregate transaction. has to be bonded because we are going to test the lock
      AggregateTransaction aggregateTransaction = transact.aggregateComplete()
            .innerTransactions(transfer.toAggregate(signerAccount.getPublicAccount())).build();
      // return signed transaction
      return api.sign(aggregateTransaction, signerAccount);
   }

   /**
    * create transaction and announce it
    * 
    * @param from account used to sign the transaction
    * @param to target address for the transfer
    * @param mosaic mosaic to transfer
    * @param message message for the transfer
    * @throws InterruptedException
    * @throws ExecutionException
    */
   private void transferSimple(Account from, Address to, Mosaic mosaic, Message message) {
      SignedTransaction signedTransaction = signTransfer(from, to, mosaic, message);
      logger.info("Transfer announced. {}", transactionHttp.announce(signedTransaction).blockingFirst());
      logger.info("Transfer done. {}",
            listener.confirmed(from.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
   }

   /**
    * create transaction and announce it
    * 
    * @param from account used to sign the transaction
    * @param to target address for the transfer
    * @param mosaic mosaic to transfer
    * @param message message for the transfer
    * @throws InterruptedException
    * @throws ExecutionException
    */
   private void transferAggregate(Account from, Address to, Mosaic mosaic, Message message) {
      SignedTransaction signedAggregateTransaction = signAggregateTransfer(from, to, mosaic, message);
      logger.info("Transfer announced. {}", transactionHttp.announce(signedAggregateTransaction).blockingFirst());
      logger.info("Transfer done. {}",
            listener.confirmed(from.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst());
      sleepForAWhile();
   }

}
