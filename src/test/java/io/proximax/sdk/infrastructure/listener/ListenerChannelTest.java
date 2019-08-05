/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * {@link ListenerChannel} tests
 */
class ListenerChannelTest {

   @Test
   void checkCode() {
      // check some random item
      assertEquals("block", ListenerChannel.BLOCK.getCode());
      // make sure that none is null
      for (ListenerChannel chnl: ListenerChannel.values()) {
         assertNotNull(chnl.getCode());
      }
   }

   @Test
   void checkRawValue() {
      for (ListenerChannel chnl: ListenerChannel.values()) {
         assertEquals(chnl, ListenerChannel.rawValueOf(chnl.getCode()));
      }
   }
}
