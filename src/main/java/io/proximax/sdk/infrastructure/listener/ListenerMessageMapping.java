/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import static io.proximax.sdk.infrastructure.listener.ListenerMessage.KEY_CHANNELNAME;
import static io.proximax.sdk.infrastructure.listener.ListenerMessage.KEY_META;

import com.google.gson.JsonObject;

/**
 * Mappings for messages received by listener from server
 */
public class ListenerMessageMapping {

   /**
    * 
    */
   public ListenerMessageMapping() {
      // nothing to do as of now
   }

   public ListenerMessage getMessage(String text, JsonObject message) {
      ListenerChannel channel = getChannel(message);
      switch (channel) {
      case BLOCK:
         return new BlockChannelMessage(message);
      case STATUS:
         return new StatusChannelMessage(message);
      case COSIGNATURE:
         return new CosignatureChannelMessage(message);
      case UNCONFIRMED_ADDED: case CONFIRMED_ADDED: case AGGREGATE_BONDED_ADDED:
         return new TransactionChannelMessage(channel, message, text);
      case UNCONFIRMED_REMOVED: case AGGREGATE_BONDED_REMOVED:
         return new SimpleChannelMessage(channel, message);
      }
      throw new IllegalArgumentException("Unsupported server notification " + text);
   }

   /**
    * use event to determine the listener channel
    * 
    * @param event event received from the server
    * @return the listener channel
    */
   private ListenerChannel getChannel(JsonObject event) {
      if (event.has(KEY_META)) {
         JsonObject meta = event.get(KEY_META).getAsJsonObject();
         if (meta.has(KEY_CHANNELNAME)) {
            return ListenerChannel.rawValueOf(meta.get(KEY_CHANNELNAME).getAsString());
         }
      }
      throw new IllegalStateException("Unsupported event channel for " + event.toString());
   }
}
