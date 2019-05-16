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

public interface HttpClient {
   Observable<HttpResponse> get(String path);
   Observable<HttpResponse> getAbs(String absoluteUrl);
   Observable<HttpResponse> postAbs(String absoluteUrl, JsonObject jsonObject);
   Observable<HttpResponse> putAbs(String absoluteUrl, JsonObject jsonObject);
   Observable<HttpResponse> post(String path, JsonObject jsonObject);
   Observable<HttpResponse> put(String path, JsonObject jsonObject);
}
