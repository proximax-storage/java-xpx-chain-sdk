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

import static io.proximax.sdk.utils.GsonUtils.getJsonArray;
import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.NamespaceRepository;
import io.proximax.sdk.gen.model.NamespaceInfoDTO;
import io.proximax.sdk.gen.model.NamespaceNameDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.namespace.NamespaceName;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.utils.dto.UInt64Utils;
import io.reactivex.Observable;

/**
 * Namespace http repository.
 *
 * @since 1.0
 */
public class NamespaceHttp extends Http implements NamespaceRepository {
   private static final String NS_ROUTE = "/namespace/";
   private static final String ACC_ROUTE = "/account/";
   
    public NamespaceHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<NamespaceInfo> getNamespace(NamespaceId namespaceId) {
        return this.client
                        .get(NS_ROUTE + namespaceId.getIdAsHex())
                        .map(Http::mapStringOrError)
                        .map(str -> objectMapper.readValue(str, NamespaceInfoDTO.class))
                        .map(namespaceInfoDTO -> new NamespaceInfo(namespaceInfoDTO.getMeta().getActive(),
                                namespaceInfoDTO.getMeta().getIndex(),
                                namespaceInfoDTO.getMeta().getId(),
                                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType().getValue()),
                                namespaceInfoDTO.getNamespace().getDepth(),
                                extractLevels(namespaceInfoDTO),
                                new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getParentId())),
                                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), api.getNetworkType()),
                                toBigInt(namespaceInfoDTO.getNamespace().getStartHeight()),
                                toBigInt(namespaceInfoDTO.getNamespace().getEndHeight())
                        ));
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address, QueryParams queryParams) {
        return this.getNamespacesFromAccount(address, Optional.of(queryParams));
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address) {
        return this.getNamespacesFromAccount(address, Optional.empty());
    }

    private Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address, Optional<QueryParams> queryParams) {
        return this.client
                        .get(ACC_ROUTE + address.plain() + "/namespaces" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .map(Http::mapStringOrError)
                        .map(json -> objectMapper.<List<NamespaceInfoDTO>>readValue(json.toString(), new TypeReference<List<NamespaceInfoDTO>>() { }))
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> new NamespaceInfo(namespaceInfoDTO.getMeta().getActive(),
                                namespaceInfoDTO.getMeta().getIndex(),
                                namespaceInfoDTO.getMeta().getId(),
                                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType().getValue()),
                                namespaceInfoDTO.getNamespace().getDepth(),
                                extractLevels(namespaceInfoDTO),
                                new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getParentId())),
                                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), api.getNetworkType()),
                                toBigInt(namespaceInfoDTO.getNamespace().getStartHeight()),
                                toBigInt(namespaceInfoDTO.getNamespace().getEndHeight())
                        ))
                        .toList()
                        .toObservable();
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses, QueryParams queryParams) {
        return this.getNamespacesFromAccounts(addresses, Optional.of(queryParams));
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses) {
        return this.getNamespacesFromAccounts(addresses, Optional.empty());
    }

    private Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses, Optional<QueryParams> queryParams) {
        JsonObject requestBody = new JsonObject();
        requestBody.add("addresses", getJsonArray(addresses, Address::plain));
        return this.client
                        .post("/account/namespaces" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""), requestBody)
                        .map(Http::mapStringOrError)
                        .map(str -> objectMapper.<List<NamespaceInfoDTO>>readValue(str, new TypeReference<List<NamespaceInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> new NamespaceInfo(namespaceInfoDTO.getMeta().getActive(),
                                namespaceInfoDTO.getMeta().getIndex(),
                                namespaceInfoDTO.getMeta().getId(),
                                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType().getValue()),
                                namespaceInfoDTO.getNamespace().getDepth(),
                                extractLevels(namespaceInfoDTO),
                                new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getParentId())),
                                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), api.getNetworkType()),
                                toBigInt(namespaceInfoDTO.getNamespace().getStartHeight()),
                                toBigInt(namespaceInfoDTO.getNamespace().getEndHeight())
                        ))
                        .toList()
                        .toObservable();
    }

    @Override
    public Observable<List<NamespaceName>> getNamespaceNames(List<NamespaceId> namespaceIds) {
        JsonObject requestBody = new JsonObject();
        requestBody.add("namespaceIds", getJsonArray(namespaceIds, id -> UInt64Utils.bigIntegerToHex(id.getId())));
        return this.client
                .post("/namespace/names", requestBody)
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.<List<NamespaceNameDTO>>readValue(str, new TypeReference<List<NamespaceNameDTO>>() {
                }))
                .flatMapIterable(item -> item)
                .map(namespaceNameDTO -> {
                    if (namespaceNameDTO.getParentId() != null) {
                        return new NamespaceName(
                                new NamespaceId(toBigInt(namespaceNameDTO.getNamespaceId())),
                                namespaceNameDTO.getName(),
                                new NamespaceId(toBigInt(namespaceNameDTO.getParentId())));
                    } else {
                        return new NamespaceName(
                                new NamespaceId(toBigInt(namespaceNameDTO.getNamespaceId())),
                                namespaceNameDTO.getName());
                    }
                })
                .toList()
                .toObservable();
    }

    private List<NamespaceId> extractLevels(NamespaceInfoDTO namespaceInfoDTO) {
        List<NamespaceId> levels = new ArrayList<>();
        if (namespaceInfoDTO.getNamespace().getLevel0() != null) {
            levels.add(new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getLevel0())));
        }

        if (namespaceInfoDTO.getNamespace().getLevel1() != null) {
            levels.add(new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getLevel1())));
        }

        if (namespaceInfoDTO.getNamespace().getLevel2() != null) {
            levels.add(new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getLevel2())));
        }

        return levels;
    }

}
