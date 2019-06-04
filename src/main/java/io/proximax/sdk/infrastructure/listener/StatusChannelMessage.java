/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.transaction.DeadlineBP;
import io.proximax.sdk.model.transaction.TransactionStatusError;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

/**
 * Message produced by the status listener channel
 */
public class StatusChannelMessage extends ListenerMessage<TransactionStatusError> {
   private static final ListenerChannel CHANNEL = ListenerChannel.STATUS;
   
   /**
    * create new message from the status channel
    * 
    * @param message JSON object representing data of the transaction status errors
    */
   public StatusChannelMessage(JsonObject message) {
      super(CHANNEL, getAddressFromMessage(message), getMessageObject(message));
   }

   /**
    * map JSON to TransactionStatusError
    * 
    * @param message JSON from server
    * @return status error object
    */
   private static TransactionStatusError getMessageObject(JsonObject message) {
      return new TransactionStatusError(
            message.get("hash").getAsString(), 
            message.get("status").getAsString(),
            new DeadlineBP(extractBigInteger(message.getAsJsonArray("deadline"))));

   }

   /**
    * prepare observable on specified message subject for status error instances
    * 
    * @param messageSubject subject to subscribe to
    * @param address address for which to observe the events
    * @return the observable of status errors
    */
   public static Observable<TransactionStatusError> subscribeTo(Subject<ListenerMessage> messageSubject, Address address) {
      return messageSubject
               .filter(message -> message.isRelevant(CHANNEL, address))
               .map(message -> (StatusChannelMessage) message)
               .map(ListenerMessage::getPayload);
   }
}
