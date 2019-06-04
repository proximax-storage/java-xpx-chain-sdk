/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import java.util.Optional;

import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

/**
 * Message produced by the block listener channel
 */
public class BlockChannelMessage extends ListenerMessage<BlockInfo> {
   public static final ListenerChannel CHANNEL = ListenerChannel.BLOCK;
   
   /**
    * create new message for the block channel
    * 
    * @param message JSON object representing the block info data
    */
   public BlockChannelMessage(JsonObject message) {
      super(CHANNEL, getMessageObject(message));
   }

   /**
    * convert JSON to the block info
    * 
    * @param message JSON message
    * @return block info
    */
   private static BlockInfo getMessageObject(JsonObject message) {
      final JsonObject meta = message.getAsJsonObject("meta");
      final JsonObject block = message.getAsJsonObject("block");
      final int rawNetworkType = (int) Long.parseLong(Integer.toHexString(block.get("version").getAsInt()).substring(0, 2), 16);
      final NetworkType networkType = NetworkType.rawValueOf(rawNetworkType);
      final int version = (int) Long.parseLong(Integer.toHexString(block.get("version").getAsInt()).substring(2, 4), 16);
      return new BlockInfo(
                      meta.get("hash").getAsString(),
                      meta.get("generationHash").getAsString(),
                      Optional.empty(),
                      Optional.empty(),
                      block.get("signature").getAsString(),
                      new PublicAccount(block.get("signer").getAsString(), networkType),
                      networkType,
                      version,
                      block.get("type").getAsInt(),
                      extractBigInteger(block.getAsJsonArray("height")),
                      extractBigInteger(block.getAsJsonArray("timestamp")),
                      extractBigInteger(block.getAsJsonArray("difficulty")),
                      block.get("previousBlockHash").getAsString(),
                      block.get("blockTransactionsHash").getAsString()
      );
  
   }

   /**
    * prepare observable on specified message subject for BlockInfo instances
    * 
    * @param messageSubject subject to subscribe to
    * @return observable block info
    */
   public static Observable<BlockInfo> subscribeTo(Subject<ListenerMessage> messageSubject) {
      return messageSubject
               .filter(message -> message.isForChannel(CHANNEL))
               .map(message -> (BlockChannelMessage) message)
               .map(ListenerMessage::getPayload);
   }

}
