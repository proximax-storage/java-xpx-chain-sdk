/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.AccountRepository;
import io.proximax.sdk.MosaicRepository;
import io.proximax.sdk.NamespaceRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * test steps involving transfer transaction
 */
public class TransferSteps extends BaseSteps {

   private static final Object NETWORK_CURRENCY = "network currency";

   private final Map<String, Account> accounts;

   public TransferSteps() throws MalformedURLException {
      super();
      accounts = new HashMap<>();
   }

   @Given("{string} is granted {bigdecimal} of {string}")
   public void grantFundsToAccount(String recipientName, BigDecimal amount, String mosaicName)
         throws InterruptedException, ExecutionException {
      Mosaic mosaic = getMosaic(mosaicName, amount);
      // create transaction
      TransferTransaction tx = TransferUtils.transfer(transact,
            Recipient.from(getAccount(recipientName).getAddress()),
            Optional.of(Arrays.asList(mosaic)),
            Optional.empty()).build();
      // sign, announce and wait for confirmation
      TransferUtils.announce(api, getSeedAccount(), tx, getTimeoutSeconds());
   }

   @When("{string} sends {bigdecimal} of {string} to {string} with plaintext message {string} and max fee {bigdecimal}")
   public void sendMosaicWithPlaintextMessage(String sender, BigDecimal amount, String mosaicName, String recipientName,
         String message, BigDecimal fee) throws InterruptedException, ExecutionException {
      Mosaic mosaic = getMosaic(mosaicName, amount);
      // calculate max fee based on specified fee and network currency mosaic divisibility
      BigInteger maxFee = fee.scaleByPowerOfTen(NetworkCurrencyMosaic.DIVISIBILITY).toBigInteger();
      // create transaction
      TransferTransaction tx = TransferUtils.transfer(transact,
            Recipient.from(getAccount(recipientName).getAddress()),
            Optional.of(Arrays.asList(mosaic)),
            Optional.of(PlainMessage.create(message))).maxFee(maxFee).build();
      // sign, announce and wait for confirmation
      TransferUtils.announce(api, getAccount(sender), tx, getTimeoutSeconds());

   }

   @Then("{string} has {bigdecimal} of {string}")
   public void verifyFunds(String accountName, BigDecimal mosaicAmount, String mosaicName) {
      // inputs
      Account acc = getAccount(accountName);
      Mosaic mosaic = getMosaic(mosaicName, mosaicAmount);
      // repos
      AccountRepository accRepo = api.createAccountRepository();
      MosaicRepository mosaicRepo = api.createMosaicRepository();
      NamespaceRepository nsRepo = api.createNamespaceRepository();
      // get the mosaicId
      MosaicId mosaicId;
      // get the id of the mosaic which might be hidden by the alias namespace id
      if (mosaic.getId() instanceof NamespaceId) {
         NamespaceInfo ns = nsRepo.getNamespace((NamespaceId) mosaic.getId()).blockingFirst();
         mosaicId = ns.getMosaicAlias().get();
      } else {
         mosaicId = (MosaicId) mosaic.getId();
      }
      final MosaicId desiredMosaicId = mosaicId;
      // determine divisibility
      int divisibility = mosaicRepo.getMosaic(desiredMosaicId).blockingFirst().getDivisibility();
      // find owned mosaics
      AccountInfo accInfo = accRepo.getAccountInfo(acc.getAddress()).blockingFirst();
      // check that owned mosaics contain the required mosaic
      Optional<Mosaic> ownedMosaic = accInfo.getMosaics().stream().filter(mos -> mos.getId().equals(desiredMosaicId))
            .findFirst();
      // definitely need to have the mosaic in the list
      assertTrue(ownedMosaic.isPresent());
      // check balance
      assertTrue(
            mosaicAmount.scaleByPowerOfTen(divisibility).toBigInteger().compareTo(ownedMosaic.get().getAmount()) <= 0);
   }

   @After
   public void returnMosaicsToSeed(Scenario scenario) throws InterruptedException, ExecutionException {
      for (Account acct : accounts.values()) {
         List<Mosaic> mosaics = api.createAccountRepository().getAccountInfo(acct.getAddress()).blockingFirst()
               .getMosaics();
         // return funds from accounts to seed
         TransferTransaction tx = TransferUtils.transfer(transact,
               Recipient.from(getSeedAccount().getAddress()),
               Optional.of(mosaics),
               Optional.empty()).build();
         // sign, announce and wait for confirmation
         TransferUtils.announce(api, acct, tx, getTimeoutSeconds());
      }
   }

   /**
    * get existing account by name or create a new one
    * 
    * @param name name of the account
    * @return existing or new account for the specified account name
    */
   private synchronized Account getAccount(final String name) {
      final String accountKey = name.toLowerCase();
      Account acc = accounts.get(accountKey);
      if (acc == null) {
         acc = new Account(new KeyPair(), getNetworkType());
         accounts.put(accountKey, acc);
      }
      return acc;
   }

   private static Mosaic getMosaic(String name, BigDecimal amount) {
      if (name.toLowerCase().equals(NETWORK_CURRENCY)) {
         return NetworkCurrencyMosaic.createRelative(amount);
      } else {
         throw new cucumber.api.PendingException();
      }
   }
}
