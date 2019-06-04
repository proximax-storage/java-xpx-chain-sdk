/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.proximax.sdk.infrastructure.TransactionMapping;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

/**
 * Message produced by the block listener channel
 */
public class TransactionChannelMessage extends ListenerMessage<Transaction> {

   /**
    * create new message from channels that are expected to use TransactionMapping to be mapped to Transaction
    * 
    * @param channel the channel that produced the message
    * @param message JSON object representing the transaction
    * @param text string representation of the message
    */
   public TransactionChannelMessage(ListenerChannel channel, JsonObject message, String text) {
      super(channel, getAddressFromMessage(message), getMessageObject(text));
   }

   /**
    * @param message
    * @return
    */
   private static Transaction getMessageObject(String message) {
      return new TransactionMapping().apply(new Gson().fromJson(message, JsonObject.class));
   }

   /**
    * prepare observable on specified message subject for transaction instances
    * 
    * @param messageSubject subject to subscribe to
    * @param channel channel which is relevant for the subscription
    * @param address address for which to observe the events
    * @return the observable of transactions
    */
   public static Observable<Transaction> subscribeTo(Subject<ListenerMessage> messageSubject, ListenerChannel channel,
         Address address) {
      return messageSubject.filter(message -> message.isForChannel(channel))
            .map(message -> (TransactionChannelMessage) message).map(ListenerMessage::getPayload)
            .filter(trans -> transactionFromAddress(trans, address));
   }

   /**
    * returns true is the transaction is relevant for given address
    * 
    * @param transaction transaction to check
    * @param address desired address
    * @return true if address is involved with the transaction, false otherwise
    */
   private static boolean transactionFromAddress(final Transaction transaction, final Address address) {
      // by default check whether address is signer or recipient of the transaction
      AtomicBoolean result = new AtomicBoolean(addressIsInvolved(transaction, address));
      // if not signer or recipient and this is aggregate transaction then run some extra checks
      if (!result.get() && transaction instanceof AggregateTransaction) {
         final AggregateTransaction aggregateTransaction = (AggregateTransaction) transaction;
         // check whether ddress is cosignatory
         aggregateTransaction.getCosignatures().forEach(cosignature -> {
            if (cosignature.getSigner().getAddress().equals(address)) {
               result.set(true);
            }
         });
         // check whether address is involved with inner transactions
         aggregateTransaction.getInnerTransactions().forEach(innerTransaction -> {
            if (addressIsInvolved(innerTransaction, address)) {
               result.set(true);
            }
         });
      }
      return result.get();
   }

   /**
    * check whether address is signer (sender) or recipient of the transaction
    * 
    * @param transaction transaction to check
    * @param address desired address
    * @return true if address has signed or is receiving the transaction
    */
   private static boolean addressIsInvolved(final Transaction transaction, final Address address) {
      // check whether address is the signer
      Optional<PublicAccount> signer = transaction.getSigner();
      if (signer.isPresent()) {
         boolean isSigner = signer.get().getAddress().equals(address);
         if (isSigner) {
            return true;
         }
      }
      // address was not signer so check whether it is recipient
      if (transaction instanceof TransferTransaction) {
         boolean isRecipient = ((TransferTransaction) transaction).getRecipient().equals(address);
         if (isRecipient) {
            return true;
         }
      }
      // it was not sender nor recipient so return false
      return false;
   }
}
