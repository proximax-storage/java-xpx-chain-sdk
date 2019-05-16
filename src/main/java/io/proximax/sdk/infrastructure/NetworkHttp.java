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


import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.NetworkRepository;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

/**
 * Network http repository.
 *
 * @since 1.0
 */
public class NetworkHttp extends Http implements NetworkRepository {
    public NetworkHttp(BlockchainApi api) {
        super(api);
    }

    public Observable<NetworkType> getNetworkType() {
        return this.client
                .get("/network")
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(obj -> obj.get("name").getAsString())
                .map(name -> {
                    if (name.equalsIgnoreCase("mijinTest"))
                        return NetworkType.MIJIN_TEST;
                    else if (name.equalsIgnoreCase("mijin"))
                        return NetworkType.MIJIN;
                    else if (name.equalsIgnoreCase("publicTest"))
                        return NetworkType.TEST_NET;
                    else if (name.equalsIgnoreCase("public"))
                        return NetworkType.MAIN_NET;
                    else if (name.equalsIgnoreCase("privateTest"))
                        return NetworkType.PRIVATE_TEST;
                    else if (name.equalsIgnoreCase("private"))
                        return NetworkType.PRIVATE;
                    else {
                        throw new IllegalArgumentException("Network " + name + " is not supported by the sdk");
                    }
                });
    }
}
