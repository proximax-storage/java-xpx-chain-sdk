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
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ListenerRepository;
import io.proximax.sdk.infrastructure.listener.ListenerChannel;
import io.proximax.sdk.infrastructure.listener.ListenerMessage;
import io.proximax.sdk.infrastructure.listener.ListenerMessageMapping;
import io.proximax.sdk.infrastructure.listener.ListenerSubscribeMessage;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionStatusError;
import io.proximax.sdk.model.transaction.TransferTransaction;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.OkHttpClient;
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
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (this.webSocket != null) {
            return CompletableFuture.completedFuture(null);
        }

        Request request = new Request.Builder()
                .url(api.getUrl().toString() + "/ws")
                .build();

        OkHttpClient client = new OkHttpClient.Builder().build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
           @Override
           public void onMessage(WebSocket webSocket, String text) {
               System.out.println("server: " + text);
               JsonObject message = new Gson().fromJson(text, JsonObject.class);
               if (message.has("uid")) {
                   Listener.this.uid = message.get("uid").getAsString();
                   future.complete(null);
               } else {
                  Listener.this.messageSubject.onNext(mapping.getMessage(text, message));
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
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.BLOCK))
                .map(rawMessage -> (BlockInfo) rawMessage.getMessage());
    }

    @Override
    public Observable<Transaction> confirmed(final Address address) {
        this.subscribeTo(ListenerChannel.CONFIRMED_ADDED, address);
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.CONFIRMED_ADDED))
                .map(rawMessage -> (Transaction) rawMessage.getMessage())
                .filter(transaction -> this.transactionFromAddress(transaction, address));
    }

    @Override
    public Observable<Transaction> unconfirmedAdded(final Address address) {
        this.subscribeTo(ListenerChannel.UNCONFIRMED_ADDED, address);
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.UNCONFIRMED_ADDED))
                .map(rawMessage -> (Transaction) rawMessage.getMessage())
                .filter(transaction -> this.transactionFromAddress(transaction, address));
    }

    @Override
    public Observable<String> unconfirmedRemoved(final Address address) {
        this.subscribeTo(ListenerChannel.UNCONFIRMED_REMOVED, address);
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.UNCONFIRMED_REMOVED))
                .map(rawMessage -> (String) rawMessage.getMessage());
    }

    @Override
    public Observable<AggregateTransaction> aggregateBondedAdded(final Address address) {
        this.subscribeTo(ListenerChannel.AGGREGATE_BONDED_ADDED, address);
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.AGGREGATE_BONDED_ADDED))
                .map(rawMessage -> (AggregateTransaction) rawMessage.getMessage())
                .filter(transaction -> this.transactionFromAddress(transaction, address));
    }

    @Override
    public Observable<String> aggregateBondedRemoved(final Address address) {
        this.subscribeTo(ListenerChannel.AGGREGATE_BONDED_REMOVED, address);
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.AGGREGATE_BONDED_REMOVED))
                .map(rawMessage -> (String) rawMessage.getMessage());
    }

    @Override
    public Observable<TransactionStatusError> status(final Address address) {
        this.subscribeTo(ListenerChannel.STATUS, address);
        return this.messageSubject
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.STATUS))
                .map(rawMessage -> (TransactionStatusError) rawMessage.getMessage());
    }

    @Override
    public Observable<CosignatureSignedTransaction> cosignatureAdded(final Address address) {
        this.subscribeTo(ListenerChannel.COSIGNATURE, address);
        return this.messageSubject
                // filter to cosignatures
                .filter(rawMessage -> rawMessage.getChannel().equals(ListenerChannel.COSIGNATURE))
                // pull the JSON out of listener message
                .map(rawMessage -> (JsonObject)rawMessage.getMessage())
                // filter for this address
                .filter(json -> Address.createFromEncoded(json.get("meta").getAsJsonObject().get("address").getAsString()).equals(address))
                // create transaction object
                .map(json -> new CosignatureSignedTransaction(
                      json.get("parentHash").getAsString(),
                      json.get("signature").getAsString(),
                      json.get("signer").getAsString()
                ));
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
    
    private void subscribeTo(String channelPath) {
      final ListenerSubscribeMessage subscribeMessage = new ListenerSubscribeMessage(this.uid, channelPath);
      String json;
      try {
         json = objectMapper.writeValueAsString(subscribeMessage);
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e.getCause());
      }
      this.webSocket.send(json);
   }

    private boolean transactionFromAddress(final Transaction transaction, final Address address) {
        AtomicBoolean transactionFromAddress = new AtomicBoolean(this.transactionHasSignerOrReceptor(transaction, address));

        if (transaction instanceof AggregateTransaction) {
            final AggregateTransaction aggregateTransaction = (AggregateTransaction) transaction;
            aggregateTransaction.getCosignatures().forEach(cosignature -> {
                if (cosignature.getSigner().getAddress().equals(address)) {
                    transactionFromAddress.set(true);
                }
            });
            aggregateTransaction.getInnerTransactions().forEach(innerTransaction -> {
                if (this.transactionHasSignerOrReceptor(innerTransaction, address)) {
                    transactionFromAddress.set(true);
                }
            });
        }
        return transactionFromAddress.get();
    }

    private boolean transactionHasSignerOrReceptor(final Transaction transaction, final Address address) {
        boolean isReceptor = false;
        if (transaction instanceof TransferTransaction) {
            isReceptor = ((TransferTransaction) transaction).getRecipient().equals(address);
        }
        return transaction.getSigner().get().getAddress().equals(address) || isReceptor;
    }
}