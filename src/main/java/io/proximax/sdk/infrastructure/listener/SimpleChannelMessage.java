/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

/**
 * Message produced by the removal listener channels
 */
public class SimpleChannelMessage extends ListenerMessage<String> {

   /**
    * create new message for any channel that is mapped only to the meta/hash field
    * 
    * @param channel the channel that produced this message
    * @param message JSON object expected to have meta/hash field
    */
   public SimpleChannelMessage(ListenerChannel channel, JsonObject message) {
      super(channel, getAddressFromMessage(message), getMessageObject(message));
   }

   /**
    * map JSON to String
    * 
    * @param message the JSO message
    * @return simple string with hash form the event
    */
   private static String getMessageObject(JsonObject message) {
      return message.get("meta").getAsJsonObject().get("hash").getAsString();
   }

   /**
    * prepare observable on specified message subject for BlockInfo instances
    * 
    * @param messageSubject subject to subscribe to
    * @param channel channel which is relevant for the subscription
    * @param address address for which to observe the events
    * @return the observable of Strings
    */
   public static Observable<String> subscribeTo(Subject<ListenerMessage> messageSubject, ListenerChannel channel, Address address) {
      return messageSubject
               .filter(message -> message.isRelevant(channel, address))
               .map(message -> (SimpleChannelMessage) message)
               .map(ListenerMessage::getPayload);
   }
}
