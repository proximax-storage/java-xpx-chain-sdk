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

import java.math.BigInteger;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * generic message received by listener from the server
 */
public class ListenerMessage<T> {
   public static final String KEY_META = "meta";
   public static final String KEY_CHANNELNAME = "channelName";
   public static final String KEY_ADDRESS = "address";

   private final ListenerChannel channel;
   private final Optional<Address> address;
   private final T payload;

   public ListenerMessage(ListenerChannel channel, T payload) {
      this(channel, Optional.empty(), payload);
   }

   public ListenerMessage(ListenerChannel channel, Address address, T payload) {
      this(channel, Optional.of(address), payload);
   }

   public ListenerMessage(ListenerChannel channel, Optional<Address> address, T payload) {
      Validate.notNull(channel, "channel can not be null");
      Validate.notNull(address, "address can not be null");
      Validate.notNull(payload, "payload can not be null");
      this.channel = channel;
      this.address = address;
      this.payload = payload;
   }

   /**
    * @return the channel which produced this message
    */
   public ListenerChannel getChannel() {
      return channel;
   }

   /**
    * @return the payload captured by this listener message
    */
   public T getPayload() {
      return payload;
   }

   /**
    * @return the address for which this message was fired
    */
   public Optional<Address> getAddress() {
      return address;
   }
   /**
    * check whether this message was captured on provided channel
    * 
    * @param channel channel to check against
    * @return true if message channel matches the specified channel
    */
   public boolean isForChannel(ListenerChannel channel) {
      return getChannel().equals(channel);
   }
   
   /**
    * check whether this message was captured for specified address
    * 
    * @param address address to check against
    * @return true or false indicating whether specified address matches
    */
   public boolean isForAddress(Address address) {
      Validate.notNull(address, "address for comparison is mandatory");
      return address.equals(getAddress().orElse(null));
   }
   
   /**
    * check whether message is relevant for given channel and address
    * 
    * @param channel channel to check against
    * @param address address to check against
    * @return try if both address and channel match the message
    */
   public boolean isRelevant(ListenerChannel channel, Address address) {
      return isForChannel(channel) && isForAddress(address);
   }
   
   /**
    * extract BigInteger from JSON array
    * 
    * @param input JSON array with 2 uints
    * @return the BigInteger represented the array
    */
   protected static BigInteger extractBigInteger(JsonArray input) {
      Validate.isTrue(input.size() == 2);
      return UInt64Utils.fromLongArray(new Gson().fromJson(input.toString(), long[].class));
   }
   
   protected static Address getAddressFromMessage(JsonObject message) {
      if (message.has(KEY_META)) {
         JsonObject meta = message.get(KEY_META).getAsJsonObject();
         if (meta.has(KEY_ADDRESS)) {
            return Address.createFromEncoded(meta.get(KEY_ADDRESS).getAsString());
         }
      }
      throw new RuntimeException("Missing address filed. " + message.toString());
   }
}
