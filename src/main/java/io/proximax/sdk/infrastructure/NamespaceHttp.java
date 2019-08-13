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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.NamespaceRepository;
import io.proximax.sdk.gen.model.NamespaceInfoDTO;
import io.proximax.sdk.gen.model.NamespaceNameDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.namespace.NamespaceName;
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
   
   private static final Type NAMESPACE_INFO_LIST_TYPE = new TypeToken<List<NamespaceInfoDTO>>(){}.getType();
   private static final Type NAMESPACE_NAME_LIST_TYPE = new TypeToken<List<NamespaceNameDTO>>(){}.getType();

    public NamespaceHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<NamespaceInfo> getNamespace(NamespaceId namespaceId) {
        return this.client
                        .get(NS_ROUTE + namespaceId.getIdAsHex())
                        .map(Http::mapStringOrError)
                        .map(str -> gson.fromJson(str, NamespaceInfoDTO.class))
                        .map(namespaceInfoDTO -> NamespaceInfo.fromDto(namespaceInfoDTO, api.getNetworkType()));
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
                        .map(this::toNamespaceInfoList)
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> NamespaceInfo.fromDto(namespaceInfoDTO, api.getNetworkType()))
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
                        .map(this::toNamespaceInfoList)
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> NamespaceInfo.fromDto(namespaceInfoDTO, api.getNetworkType()))
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
                .map(this::toNamespaceNameList)
                .flatMapIterable(item -> item)
                .map(NamespaceName::fromDto)
                .toList()
                .toObservable();
    }
    
    private List<NamespaceInfoDTO> toNamespaceInfoList(String json) {
       return gson.fromJson(json, NAMESPACE_INFO_LIST_TYPE);
    }
    
    private List<NamespaceNameDTO> toNamespaceNameList(String json) {
       return gson.fromJson(json, NAMESPACE_NAME_LIST_TYPE);
    }
}
