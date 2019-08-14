/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.infrastructure.Listener;
import okhttp3.WebSocket;

/**
 * Listener that outputs data to json file
 */
public class DebuggingListener extends Listener {

   private static final FileWriter listenerResponseWriter;
   private static final FileWriter dtoTransactionWriter;
   
   static {
      try {
         listenerResponseWriter = new FileWriter(new File("./src/test/resources/dtos/listener.json"), true);
         dtoTransactionWriter = new FileWriter(new File("./src/test/resources/transactions/all.json"), true);
      } catch (FileNotFoundException e) {
         throw new RuntimeException("failed to init listener", e);
      } catch (IOException e) {
         throw new RuntimeException("failed to init listener", e);
      }
   }
   
   /**
    * @param api
    */
   public DebuggingListener(BlockchainApi api) {
      super(api);

   }

   @Override
   protected void onNewEventReceived(WebSocket webSocket, String text) {
      super.onNewEventReceived(webSocket, text);
      
      if (text.contains("\"uid\"")) return;
      try {
         listenerResponseWriter.append(text).append(",\n");
         listenerResponseWriter.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public String writeTransaction(String text) {
      try {
         String str = text.substring(1, text.length()-1);
         if (str.isEmpty()) {
            return text;
         }
         dtoTransactionWriter.append(str).append(",\n");
         dtoTransactionWriter.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return text;
   }
   
}
