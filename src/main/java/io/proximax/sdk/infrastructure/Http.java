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

package io.proximax.sdk.infrastructure;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

import com.google.gson.Gson;

import io.proximax.sdk.BlockchainApi;

/**
 * base HTTP repository implementation, keeping track of the API, HTTP client and mapper
 */
public class Http {
   protected final BlockchainApi api;
   protected final HttpClient client;
   protected final Gson gson;

   /**
    * create and initialize new instance for specified API
    * 
    * @param api the main API
    */
   Http(BlockchainApi api) {
      Validate.notNull(api, "api has to be provided");
      this.api = api;
      this.client = new OkHttpHttpClient(api);
      // gson instance
      this.gson = new Gson();
   }

   /**
    * @return the api
    */
   public BlockchainApi getApi() {
      return api;
   }

   /**
    * @return the client
    */
   public HttpClient getClient() {
      return client;
   }

   /**
    * @return the gson
    */
   public Gson getGson() {
      return gson;
   }

   /**
    * throw RuntimeException on error or return body of the response
    * 
    * @param response response to examine
    * @return body of the response as string
    */
   static String mapStringOrError(final HttpResponse response) {
      if (response.getCode() < 200 || response.getCode() > 299) {
         throw new RuntimeException(response.getStatusMessage());
      }
      try {
         String body = response.getBodyString();
         System.out.println("haa: " + body);
         return body;
      } catch (IOException e) {
         throw new RuntimeException(e.getMessage());
      }
   }
}
