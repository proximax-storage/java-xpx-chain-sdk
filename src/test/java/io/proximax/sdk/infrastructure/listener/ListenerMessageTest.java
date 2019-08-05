/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link ListenerMessage} tests
 */
class ListenerMessageTest {
   private static final Address ADDRESS = new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26",
         NetworkType.MIJIN_TEST);
   private static final Address ADDRESS2 = new Address("SDBLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26",
         NetworkType.MIJIN_TEST);

   @Test
   void constructor() {
      ListenerMessage<String> msg = new ListenerMessage<>(ListenerChannel.BLOCK, "hola");

      assertEquals(ListenerChannel.BLOCK, msg.getChannel());
      assertEquals("hola", msg.getPayload());
      assertFalse(msg.getAddress().isPresent());
      assertFalse(msg.isForAddress(ADDRESS));
      assertFalse(msg.isRelevant(ListenerChannel.BLOCK, ADDRESS));
      assertTrue(msg.isForChannel(ListenerChannel.BLOCK));
   }

   @Test
   void constructorWithAddress() {
      ListenerMessage<String> msg = new ListenerMessage<>(ListenerChannel.BLOCK, ADDRESS, "hola");

      assertEquals(ListenerChannel.BLOCK, msg.getChannel());
      assertEquals("hola", msg.getPayload());
      assertTrue(msg.isForChannel(ListenerChannel.BLOCK));
      assertTrue(msg.getAddress().isPresent());
      assertTrue(msg.isForAddress(ADDRESS));
      assertTrue(msg.isRelevant(ListenerChannel.BLOCK, ADDRESS));
      assertFalse(msg.isRelevant(ListenerChannel.BLOCK, ADDRESS2));
      assertFalse(msg.isRelevant(ListenerChannel.CONFIRMED_ADDED, ADDRESS2));
      assertEquals(ADDRESS, msg.getAddress().orElse(null));
   }

   @Test
   void extractBigInt() {
      JsonArray arr = new JsonArray();
      arr.add(5);
      arr.add(6);
      assertEquals(BigInteger.valueOf(25769803781l), ListenerMessage.extractBigInteger(arr));
      arr.add(7);
      assertThrows(IllegalArgumentException.class, () -> ListenerMessage.extractBigInteger(arr));
      arr.remove(0);
      arr.remove(0);
      assertThrows(IllegalArgumentException.class, () -> ListenerMessage.extractBigInteger(arr));
   }
   
   @Test
   void extractAddress2() {
      JsonObject message = new JsonObject();
      // meta missing
      assertThrows(RuntimeException.class, () -> ListenerMessage.getAddressFromMessage(message));

      JsonObject meta = new JsonObject();
      message.add(ListenerMessage.KEY_META, meta);
      // address missing in meta
      assertThrows(RuntimeException.class, () -> ListenerMessage.getAddressFromMessage(message));

      meta.addProperty(ListenerMessage.KEY_ADDRESS, Hex.toHexString(Base32Encoder.getBytes(ADDRESS.plain())));
      // all good
      assertEquals(ADDRESS, ListenerMessage.getAddressFromMessage(message));
   }
}
