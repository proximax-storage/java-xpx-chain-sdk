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

package io.nem.sdk.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nem.sdk.model.blockchain.NetworkType;
import io.reactivex.Observable;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;

public class Http {
    protected final WebClient client;
    protected final URL url;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    private NetworkHttp networkHttp;
    private NetworkType networkType;

    Http(String host, NetworkHttp networkHttp) throws MalformedURLException {
        this.url = new URL(host);
        final Vertx vertx = Vertx.vertx();
        this.client = WebClient.create(vertx);
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        this.networkHttp = networkHttp;
    }

    Http(String host) throws MalformedURLException {
        this(host, null);
    }

    Observable<NetworkType> getNetworkTypeObservable() {
        Observable<NetworkType> networkTypeResolve;
        if (this.networkType == null) {
            networkTypeResolve = networkHttp.getNetworkType().map(networkType -> {
                this.networkType = networkType;
                return networkType;
            });
        } else {
            networkTypeResolve = Observable.just(networkType);
        }
        return networkTypeResolve;
    }
}
