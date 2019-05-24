/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure.listener;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;

/**
 * TODO add proper description
 */
class ListenerMessageMappingTest extends ResourceBasedTest {

   @Test
   void allEventsHaveMetaChannelName() {
      getResources("listener", "dtos", "events").forEach(el -> {
         String code = el.getAsJsonObject().get("meta").getAsJsonObject().get("channelName").getAsString();
         ListenerChannel.rawValueOf(code);
      });
   }
   
   @Test
   void allEventsGetMapped() {
      getResources("listener", "dtos", "events").forEach(el -> {
         new ListenerMessageMapping().getMessage(el.toString(), el.getAsJsonObject());
      });
   }

}
