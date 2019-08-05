/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.BlockchainApi;

/**
 * {@link Http} tests
 */
class HttpTest {
   
   @Test
   void constructor() throws MalformedURLException {
      BlockchainApi api = new BlockchainApi(new URL("http://localhost:1234"));
      Http http = new Http(api);

      assertEquals(api, http.getApi());
      assertNotNull(http.getGson());
      assertNotNull(http.getClient());
   }

   @Test
   void parsingResponses() throws IOException {
      // 200 - 299 is OK, the rest is exception
      assertOKResponse(200, "hello1");
      assertOKResponse(220, "hello2");
      assertOKResponse(270, "hello3");
      assertOKResponse(299, "hello4");
      assertErrorResponse(0, "not used1");
      assertErrorResponse(50, "not used2");
      assertErrorResponse(199, "not used3");
      assertErrorResponse(300, "not used4");
      assertErrorResponse(350, "not used5");
      assertErrorResponse(876, "not used6");
   }
   
   @Test
   void invalidRespones() throws IOException {
      HttpResponse response = mock(HttpResponse.class);
      when(response.getCode()).thenReturn(250);
      when(response.getBodyString()).thenThrow(new IOException("bad luck"));
      assertThrows(RuntimeException.class, () -> Http.mapStringOrError(response));

   }
   
   private static void assertOKResponse(int code, String body) throws IOException {
      HttpResponse response = mock(HttpResponse.class);
      when(response.getCode()).thenReturn(code);
      when(response.getBodyString()).thenReturn(body);
      assertEquals(body, Http.mapStringOrError(response));
   }
   
   private static void assertErrorResponse(int code, String body) throws IOException {
      HttpResponse response = mock(HttpResponse.class);
      when(response.getCode()).thenReturn(code);
      when(response.getBodyString()).thenReturn(body);
      assertThrows(RuntimeException.class, () -> Http.mapStringOrError(response));
   }
}
