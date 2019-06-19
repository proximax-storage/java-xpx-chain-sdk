/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * {@link Receipts} tests
 */
class ReceiptsTest {

   @Test
   void testConstructor() {
      Receipts r = new Receipts(new JsonArray(), new JsonArray(), new JsonArray());
      assertEquals(0, r.getAddressResolutionStatements().size());
      assertEquals(0, r.getMosaicResolutionStatements().size());
      assertEquals(0, r.getTransactionStatements().size());
   }

   @Test
   void testFromDto() {
      JsonObject json = new JsonObject();
      json.add("transactionStatements", new JsonArray());
      json.add("addressResolutionStatements", new JsonArray());
      json.add("mosaicResolutionStatements", new JsonArray());
      Receipts r = Receipts.fromJson(json);
      assertEquals(0, r.getAddressResolutionStatements().size());
      assertEquals(0, r.getMosaicResolutionStatements().size());
      assertEquals(0, r.getTransactionStatements().size());
   }
}
