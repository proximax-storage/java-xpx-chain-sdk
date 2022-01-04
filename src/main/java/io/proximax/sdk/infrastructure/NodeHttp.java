/*
 * Copyright 2022 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.infrastructure;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.NodeRepository;
import io.proximax.sdk.gen.model.NodeInfoDTO;
import io.proximax.sdk.gen.model.NodeTimeDTO;
import io.proximax.sdk.model.node.*;
import io.reactivex.Observable;

/**
 * Metadata http repository.
 */
public class NodeHttp extends Http implements NodeRepository {

    public NodeHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<NodeInfo> getNodeInfo() {
        return this.client.get("/node/info")
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, NodeInfoDTO.class))
                .map(NodeInfo::fromDto);
    }

    @Override
    public Observable<NodeTime> getNodeTime() {
        return this.client.get("/node/time")
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, NodeTimeDTO.class))
                .map(NodeTime::fromDto);
    }
}