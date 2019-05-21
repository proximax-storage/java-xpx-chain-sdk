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

package io.proximax.sdk.infrastructure.listener;

/**
 * Channels recognized by the listener
 */
public enum ListenerChannel {
   BLOCK("block"), 
   CONFIRMED_ADDED("confirmedAdded"), 
   UNCONFIRMED_ADDED("unconfirmedAdded"),
   UNCONFIRMED_REMOVED("unconfirmedRemoved"), 
   AGGREGATE_BONDED_ADDED("partialAdded"),
   AGGREGATE_BONDED_REMOVED("partialRemoved"), 
   COSIGNATURE("cosignature"), 
   STATUS("status");

   private final String code;

   ListenerChannel(final String code) {
      this.code = code;
   }

   /**
    * retrieve channel by the channel code
    * 
    * @param code code of the channel as recognize dby the server
    * @return item from this enum
    */
   public static ListenerChannel rawValueOf(String code) {
      for (ListenerChannel channel : values()) {
         if (channel.code.equals(code)) {
            return channel;
         }
      }
      throw new IllegalArgumentException(code + " is not valid listener channel");
   }
   
   /**
    * get the code of this channel
    * 
    * @return the code of channel recognized by the server
    */
   public String getCode() {
      return this.code;
   }
}
