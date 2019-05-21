/*
 * Copyright 2019 ProximaX
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

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * generic HTTP client API
 * 
 * get/post/put methods taking relative path parameter assume that client implementation is aware of the base URL
 */
public interface HttpClient {
   
   /**
    * make GET request with path relative to the base URL
    * 
    * throws {@link UnsupportedOperationException} when client implementation is not aware of base URL
    * 
    * @param path path relative to the base URL
    * @return observable response
    */
   Observable<HttpResponse> get(String path);
   
   /**
    * make POST request with path relative to the base URL
    * 
    * throws {@link UnsupportedOperationException} when client implementation is not aware of base URL
    * 
    * @param path path relative to the base URL
    * @param body request body
    * @return observable response
    */
   Observable<HttpResponse> post(String path, JsonObject body);
   
   /**
    * make PUT request with path relative to the base URL
    * 
    * throws {@link UnsupportedOperationException} when client implementation is not aware of base URL
    * 
    * @param path path relative to the base URL
    * @param body request body
    * @return observable response
    */
   Observable<HttpResponse> put(String path, JsonObject body);

   /**
    * make GET request with path relative to the base URL
    * 
    * @param absoluteUrl path relative to the base URL
    * @return observable response
    */
   Observable<HttpResponse> getAbs(String absoluteUrl);

   /**
    * make POST request with path relative to the base URL
    * 
    * @param absoluteUrl path relative to the base URL
    * @param body request body
    * @return observable response
    */
   Observable<HttpResponse> postAbs(String absoluteUrl, JsonObject body);

   /**
    * make PUT request with path relative to the base URL
    * 
    * @param absoluteUrl path relative to the base URL
    * @param body request body
    * @return observable response
    */
   Observable<HttpResponse> putAbs(String absoluteUrl, JsonObject body);

   /**
    * @param request
    * @param webSocketListener
    * @return
    */
   WebSocket newWebSocket(Request request, WebSocketListener webSocketListener);
}
