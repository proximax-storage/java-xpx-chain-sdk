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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.infrastructure.listener.BlockChannelMessage;
import io.proximax.sdk.infrastructure.listener.CosignatureChannelMessage;
import io.proximax.sdk.infrastructure.listener.ListenerChannel;
import io.proximax.sdk.infrastructure.listener.ListenerMessage;
import io.proximax.sdk.infrastructure.listener.ListenerMessageMapping;
import io.proximax.sdk.infrastructure.listener.ListenerSubscribtionMessage;
import io.proximax.sdk.infrastructure.listener.SimpleChannelMessage;
import io.proximax.sdk.infrastructure.listener.StatusChannelMessage;
import io.proximax.sdk.infrastructure.listener.TransactionChannelMessage;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionStatusError;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Listener repository implementation
 */
public class Listener extends Http implements ListenerRepository {
   private final Subject<ListenerMessage> messageSubject;
   private String uid;
   private WebSocket webSocket;
   private final ListenerMessageMapping mapping = new ListenerMessageMapping();

   /**
    * create new listener for specified API
    * 
    * @param api blockchain API
    */
   public Listener(BlockchainApi api) {
      super(api);
      this.messageSubject = PublishSubject.create();
   }

   @Override
   public CompletableFuture<Void> open() {
      // return immediately if websocket is already opened
      if (this.webSocket != null) {
         return CompletableFuture.completedFuture(null);
      }

      // prepare the future that will indicate that socket was opened
      CompletableFuture<Void> future = new CompletableFuture<>();

      // prepare the request for the websocket registration
      Request request = new Request.Builder().url(api.getUrl().toString() + "/ws").build();

      // create the websocket connection
      webSocket = client.newWebSocket(request, new WebSocketListener() {
         @Override
         public void onMessage(WebSocket webSocket, String text) {
            onNewEventReceived(webSocket, text);
            // parse the event text
            JsonObject message = new Gson().fromJson(text, JsonObject.class);
            // for for UID complete the socket initialization
            if (message.has("uid")) {
               Listener.this.uid = message.get("uid").getAsString();
               future.complete(null);
            } else {
               // non-UID events need to be mapped to listener events
               try {
                  Listener.this.messageSubject.onNext(mapping.getMessage(text, message));
               } catch (RuntimeException e) {
                  Listener.this.messageSubject.onError(e);
               }
            }
         }
      });

      return future;
   }

   @Override
   public String getUID() {
      return uid;
   }

   @Override
   public void close() {
      this.webSocket.close(1000, "Closed.");
   }

   @Override
   public Observable<BlockInfo> newBlock() {
      this.subscribeTo(ListenerChannel.BLOCK);
      return BlockChannelMessage.subscribeTo(messageSubject);
   }

   @Override
   public Observable<Transaction> confirmed(final Address address) {
      ListenerChannel channel = ListenerChannel.CONFIRMED_ADDED;
      this.subscribeTo(channel, address);
      return TransactionChannelMessage.subscribeTo(messageSubject, channel, address);
   }

   @Override
   public Observable<Transaction> unconfirmedAdded(final Address address) {
      ListenerChannel channel = ListenerChannel.UNCONFIRMED_ADDED;
      this.subscribeTo(channel, address);
      return TransactionChannelMessage.subscribeTo(messageSubject, channel, address);
   }

   @Override
   public Observable<String> unconfirmedRemoved(final Address address) {
      ListenerChannel channel = ListenerChannel.UNCONFIRMED_REMOVED;
      this.subscribeTo(channel, address);
      return SimpleChannelMessage.subscribeTo(messageSubject, channel, address);
   }

   @Override
   public Observable<AggregateTransaction> aggregateBondedAdded(final Address address) {
      ListenerChannel channel = ListenerChannel.AGGREGATE_BONDED_ADDED;
      this.subscribeTo(channel, address);
      return TransactionChannelMessage.subscribeTo(messageSubject, channel, address)
            .map(transaction -> (AggregateTransaction)transaction);
   }

   @Override
   public Observable<String> aggregateBondedRemoved(final Address address) {
      ListenerChannel channel = ListenerChannel.AGGREGATE_BONDED_REMOVED;
      this.subscribeTo(channel, address);
      return SimpleChannelMessage.subscribeTo(messageSubject, channel, address);
   }

   @Override
   public Observable<TransactionStatusError> status(final Address address) {
      this.subscribeTo(ListenerChannel.STATUS, address);
      return StatusChannelMessage.subscribeTo(messageSubject, address);
   }

   @Override
   public Observable<CosignatureSignedTransaction> cosignatureAdded(final Address address) {
      this.subscribeTo(ListenerChannel.COSIGNATURE, address);
      return CosignatureChannelMessage.subscribeTo(messageSubject, address);
   }

   /**
    * override this method to get notification about every event received from the server
    * 
    * @param webSocket socket which produced the event
    * @param text event text
    */
   protected void onNewEventReceived(WebSocket webSocket, String text) {
      // do nothing in default implementation
   }
   
   private void subscribeTo(ListenerChannel channel) {
      subscribeTo(channel, Optional.empty());
   }

   private void subscribeTo(ListenerChannel channel, Address address) {
      subscribeTo(channel, Optional.of(address));
   }

   private void subscribeTo(ListenerChannel channel, Optional<Address> address) {
      String channelPath;
      if (address.isPresent()) {
         channelPath = channel.getCode() + '/' + address.get().plain();
      } else {
         channelPath = channel.getCode();
      }
      subscribeTo(channelPath);
   }

   /**
    * submit subscription for specified path to the server
    * 
    * @param channelPath path to subscribe to (typically channel/address)
    */
   private void subscribeTo(String channelPath) {
      final ListenerSubscribtionMessage subscribeMessage = new ListenerSubscribtionMessage(this.uid, channelPath);
      String json = gson.toJson(subscribeMessage);
      this.webSocket.send(json);
   }
}