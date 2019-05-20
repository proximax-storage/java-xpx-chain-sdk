/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import java.math.BigInteger;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.infrastructure.TransactionMapping;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.DeadlineBP;
import io.proximax.sdk.model.transaction.TransactionStatusError;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Mappings for messages received by listener from server
 */
public class ListenerMessageMapping {

   /**
    * 
    */
   public ListenerMessageMapping() {
   }
   
   public ListenerMessage getMessage(String text, JsonObject message) {
      if (message.has("transaction")) {
          return new ListenerMessage(
                  ListenerChannel.rawValueOf(message.getAsJsonObject("meta").get("channelName").getAsString()),
                  new TransactionMapping().apply(new Gson().fromJson(text, com.google.gson.JsonObject.class))
          );
      } else if (message.has("block")) {
          final JsonObject meta = message.getAsJsonObject("meta");
          final JsonObject block = message.getAsJsonObject("block");
          final int rawNetworkType = (int) Long.parseLong(Integer.toHexString(block.get("version").getAsInt()).substring(0, 2), 16);
          final NetworkType networkType = NetworkType.rawValueOf(rawNetworkType);
          final int version = (int) Long.parseLong(Integer.toHexString(block.get("version").getAsInt()).substring(2, 4), 16);
          return new ListenerMessage(
                  ListenerChannel.BLOCK,
                  new BlockInfo(
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
                  )
          );
      } else if (message.has("status")) {
          return new ListenerMessage(
                  ListenerChannel.STATUS,
                  new TransactionStatusError(
                          message.get("hash").getAsString(),
                          message.get("status").getAsString(),
                          new DeadlineBP(extractBigInteger(message.getAsJsonArray("deadline")))
                  )
          );
      } else if (message.has("meta") && message.get("meta").getAsJsonObject().has("hash")) {
          return new ListenerMessage(
                  ListenerChannel.rawValueOf(message.getAsJsonObject("meta").get("channelName").getAsString()),
                  message.get("meta").getAsJsonObject().get("hash").getAsString()
          );
      } else if (message.has("parentHash")) {
          return new ListenerMessage(
                  ListenerChannel.COSIGNATURE,
                  message
          );
      } else {
         throw new IllegalArgumentException("Unsupported server notification " + text);
      }
   }

   private BigInteger extractBigInteger(JsonArray input) {
      return UInt64Utils.fromLongArray(new Gson().fromJson(input.toString(), long[].class));
  }


}
