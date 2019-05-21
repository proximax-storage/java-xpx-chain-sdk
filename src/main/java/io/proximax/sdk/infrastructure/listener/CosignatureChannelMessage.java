/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

/**
 * Message produced by the cosignature listener channel
 */
public class CosignatureChannelMessage extends ListenerMessage<CosignatureSignedTransaction> {
   private static final ListenerChannel CHANNEL = ListenerChannel.COSIGNATURE;
   /**
    * @param message
    */
   public CosignatureChannelMessage(JsonObject message) {
      super(CHANNEL, getAddressFromMessage(message), getPayload(message));
   }

   private static CosignatureSignedTransaction getPayload(JsonObject message) {
      return new CosignatureSignedTransaction(message.get("parentHash").getAsString(),
            message.get("signature").getAsString(), message.get("signer").getAsString());
   }
   
   /**
    * prepare observable on specified message subject for BlockInfo instances
    * 
    * @param messageSubject subject to subscribe to
    * @param address address for which to observe the events
    * @return observable of cosignature transaction
    */
   public static Observable<CosignatureSignedTransaction> subscribeTo(Subject<ListenerMessage> messageSubject, Address address) {
      return messageSubject
               .filter(message -> message.isRelevant(CHANNEL, address))
               .map(message -> (CosignatureChannelMessage) message)
               .map(ListenerMessage::getPayload);
   }
}
