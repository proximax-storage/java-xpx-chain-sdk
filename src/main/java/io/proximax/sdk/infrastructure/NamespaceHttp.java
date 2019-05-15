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

import static io.proximax.sdk.infrastructure.utils.UInt64Utils.toBigInt;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.proximax.sdk.gen.model.NamespaceInfoDTO;
import io.proximax.sdk.gen.model.NamespaceNameDTO;
import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.namespace.NamespaceName;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/**
 * Namespace http repository.
 *
 * @since 1.0
 */
public class NamespaceHttp extends Http implements NamespaceRepository {

    public NamespaceHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
    }

    public NamespaceHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<NamespaceInfo> getNamespace(NamespaceId namespaceId) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/namespace/" + namespaceId.getIdAsHex())
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), NamespaceInfoDTO.class))
                        .map(namespaceInfoDTO -> new NamespaceInfo(namespaceInfoDTO.getMeta().isActive(),
                                namespaceInfoDTO.getMeta().getIndex(),
                                namespaceInfoDTO.getMeta().getId(),
                                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType()),
                                namespaceInfoDTO.getNamespace().getDepth(),
                                extractLevels(namespaceInfoDTO),
                                new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getParentId())),
                                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), networkType),
                                toBigInt(namespaceInfoDTO.getNamespace().getStartHeight()),
                                toBigInt(namespaceInfoDTO.getNamespace().getEndHeight())
                        )));
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
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/account/" + address.plain() + "/namespaces" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .as(BodyCodec.jsonArray())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<NamespaceInfoDTO>>readValue(json.toString(), new TypeReference<List<NamespaceInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> new NamespaceInfo(namespaceInfoDTO.getMeta().isActive(),
                                namespaceInfoDTO.getMeta().getIndex(),
                                namespaceInfoDTO.getMeta().getId(),
                                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType()),
                                namespaceInfoDTO.getNamespace().getDepth(),
                                extractLevels(namespaceInfoDTO),
                                new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getParentId())),
                                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), networkType),
                                toBigInt(namespaceInfoDTO.getNamespace().getStartHeight()),
                                toBigInt(namespaceInfoDTO.getNamespace().getEndHeight())
                        ))
                        .toList()
                        .toObservable());
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
        requestBody.put("addresses", addresses.stream().map(Address::plain).collect(Collectors.toList()));
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .postAbs(this.url + "/account/namespaces" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .as(BodyCodec.jsonArray())
                        .rxSendJson(requestBody)
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<NamespaceInfoDTO>>readValue(json.toString(), new TypeReference<List<NamespaceInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> new NamespaceInfo(namespaceInfoDTO.getMeta().isActive(),
                                namespaceInfoDTO.getMeta().getIndex(),
                                namespaceInfoDTO.getMeta().getId(),
                                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType()),
                                namespaceInfoDTO.getNamespace().getDepth(),
                                extractLevels(namespaceInfoDTO),
                                new NamespaceId(toBigInt(namespaceInfoDTO.getNamespace().getParentId())),
                                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), networkType),
                                toBigInt(namespaceInfoDTO.getNamespace().getStartHeight()),
                                toBigInt(namespaceInfoDTO.getNamespace().getEndHeight())
                        ))
                        .toList()
                        .toObservable());
    }

    @Override
    public Observable<List<NamespaceName>> getNamespaceNames(List<NamespaceId> namespaceIds) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("namespaceIds", namespaceIds.stream().map(id -> UInt64Utils.bigIntegerToHex(id.getId())).collect(Collectors.toList()));
        return this.client
                .postAbs(this.url + "/namespace/names")
                .as(BodyCodec.jsonArray())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> objectMapper.<List<NamespaceNameDTO>>readValue(json.toString(), new TypeReference<List<NamespaceNameDTO>>() {
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
