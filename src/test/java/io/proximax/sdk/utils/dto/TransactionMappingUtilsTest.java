/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.gsonfire.builders.JsonArrayBuilder;
import io.proximax.sdk.model.network.NetworkType;

/**
 * {@link TransactionMappingUtils} tests
 */
class TransactionMappingUtilsTest {

   @Test
   void serializeVersion() {
      // hard-coded constant matching the expectations and verified by e2e tests
      assertEquals(-1207959551, TransactionMappingUtils.serializeVersion(1, NetworkType.MAIN_NET.getValue()));
   }
   
   @Test
   void testExtractVersion() {
      // base data
      int version = 1;
      NetworkType nt = NetworkType.MAIN_NET;
      // serialized version
      int serializedVersion = TransactionMappingUtils.serializeVersion(version, nt.getValue());
      // convert to JSON element
      JsonElement el = new JsonPrimitive(serializedVersion);
      // now extract the version and network type
      assertEquals(version, TransactionMappingUtils.extractTransactionVersion(el));
      assertEquals(nt, TransactionMappingUtils.extractNetworkType(el));
   }

   @Test
   void testFeeExtraction() {
      JsonObject obj = new JsonObject();
      obj.add("fee", JsonArrayBuilder.start().add(1).add(5).build());
      assertEquals(BigInteger.valueOf(21474836481l), TransactionMappingUtils.extractFee(obj));
   }
   
   @Test
   void testMaxFeeExtraction() {
      JsonObject obj = new JsonObject();
      obj.add("maxFee", JsonArrayBuilder.start().add(1).add(5).build());
      assertEquals(BigInteger.valueOf(21474836481l), TransactionMappingUtils.extractFee(obj));
   }

   @Test
   void testPlainFeeExtraction() {
      JsonObject obj = new JsonObject();
      obj.add("fee", new JsonPrimitive("21474836481"));
      assertEquals(BigInteger.valueOf(21474836481l), TransactionMappingUtils.extractFee(obj));
   }
   
   @Test
   void testPlainMaxFeeExtraction() {
      JsonObject obj = new JsonObject();
      obj.add("maxFee", new JsonPrimitive("21474836481"));
      assertEquals(BigInteger.valueOf(21474836481l), TransactionMappingUtils.extractFee(obj));
   }
}
