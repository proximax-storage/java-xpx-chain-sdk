/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * {@link ListenerSubscribtionMessage} tests
 */
class ListenerSubscribtionMessageTest {

   @Test
   void checkPojo() {
      ListenerSubscribtionMessage msg = new ListenerSubscribtionMessage("uid", "hello");
      
      assertEquals("uid", msg.getUid());
      assertEquals("hello", msg.getSubscription());
   }

}
