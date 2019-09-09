/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.steps;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;
import io.proximax.sdk.model.transaction.builder.TransferTransactionBuilder;

/**
 * TODO add proper description
 */
public class TransferUtils {

   public static TransferTransactionBuilder transfer(TransactionBuilderFactory transact, Recipient recipient, Optional<List<Mosaic>> mosaics, Optional<Message> message) {
      // mandatory fields
      TransferTransactionBuilder builder = transact.transfer().to(recipient);
      // optional mosaics
      if (mosaics.isPresent()) {
         builder.mosaics(mosaics.get());
      }
      // optional message
      if (message.isPresent()) {
         builder.message(message.get());
      }
      // return the builder
      return builder;
   }
   
   public static Transaction announce(BlockchainApi api, Account signer, Transaction tx, long timeout) throws InterruptedException, ExecutionException {
      // sign the transaction
      SignedTransaction signedTx = api.sign(tx, signer);
      // announce the transaction
      api.createTransactionRepository().announce(signedTx).blockingFirst();
      // create and init listener
      ListenerRepository listener = api.createListener();
      listener.open().get();
      // wait for confirmation of signed transaction
      return listener.confirmed(signer.getAddress())
            .filter(trans -> trans.getTransactionInfo().get().getHash().get().equals(signedTx.getHash()))
            .timeout(timeout, TimeUnit.SECONDS).blockingFirst();
   }
}
