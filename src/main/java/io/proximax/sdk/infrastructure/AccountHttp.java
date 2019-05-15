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

import static io.proximax.sdk.infrastructure.utils.AccountDTOUtils.getAddressEncoded;
import static io.proximax.sdk.infrastructure.utils.UInt64Utils.toBigInt;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.proximax.sdk.gen.model.AccountInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountGraphInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountInfoDTO;
import io.proximax.sdk.gen.model.MultisigDTO;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.MultisigAccountGraphInfo;
import io.proximax.sdk.model.account.MultisigAccountInfo;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/**
 * Account http repository.
 *
 * @since 1.0
 */
public class AccountHttp extends Http implements AccountRepository {

    public AccountHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
    }

    public AccountHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host + "/account/", networkHttp);
    }

    @Override
    public Observable<AccountInfo> getAccountInfo(Address address) {
        return this.client
                .getAbs(this.url + address.plain())
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> objectMapper.readValue(json.toString(), AccountInfoDTO.class))
                .map(AccountInfoDTO::getAccount)
                .map(accountDTO -> new AccountInfo(Address.createFromRawAddress(getAddressEncoded(accountDTO)),
                		toBigInt(accountDTO.getAddressHeight()),
                        accountDTO.getPublicKey(),
                        toBigInt(accountDTO.getPublicKeyHeight()),
                        accountDTO.getMosaics().stream().map(mosaicDTO -> new Mosaic(
                                new MosaicId(toBigInt(mosaicDTO.getId())),
                                toBigInt(mosaicDTO.getAmount())
                        )).collect(Collectors.toList())));
    }

    @Override
    public Observable<List<AccountInfo>> getAccountsInfo(List<Address> addresses) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("addresses", addresses.stream().map(Address::plain).collect(Collectors.toList()));
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .postAbs(this.url.toString())
                        .as(BodyCodec.jsonArray())
                        .rxSendJson(requestBody)
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<AccountInfoDTO>>readValue(json.toString(), new TypeReference<List<AccountInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(AccountInfoDTO::getAccount)
                        .map(accountDTO -> new AccountInfo(Address.createFromRawAddress(getAddressEncoded(accountDTO)),
                        		toBigInt(accountDTO.getAddressHeight()),
                                accountDTO.getPublicKey(),
                                toBigInt(accountDTO.getPublicKeyHeight()),
                                accountDTO.getMosaics().stream().map(mosaicDTO -> new Mosaic(
                                        new MosaicId(toBigInt(mosaicDTO.getId())),
                                        toBigInt(mosaicDTO.getAmount())
                                )).collect(Collectors.toList())))
                        .toList()
                        .toObservable());
    }

    @Override
    public Observable<MultisigAccountInfo> getMultisigAccountInfo(Address address) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + address.plain() + "/multisig")
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), MultisigAccountInfoDTO.class))
                        .map(MultisigAccountInfoDTO::getMultisig)
                        .map(transfromMultisigAccountInfoDTO(networkType)));
    }

    @Override
    public Observable<MultisigAccountGraphInfo> getMultisigAccountGraphInfo(Address address) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + address.plain() + "/multisig/graph")
                        .as(BodyCodec.jsonArray())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<MultisigAccountGraphInfoDTO>>readValue(json.toString(), new TypeReference<List<MultisigAccountGraphInfoDTO>>() {
                        }))
                        .map(multisigAccountGraphInfoDTOList -> {
                            Map<Integer, List<MultisigAccountInfo>> multisigAccountInfoMap = new HashMap<>();
                            multisigAccountGraphInfoDTOList.forEach(item -> multisigAccountInfoMap.put(item.getLevel(), item
                                    .getMultisigEntries()
                                    .stream()
                                    .map(MultisigAccountInfoDTO::getMultisig)
                                    .map(item2 -> new MultisigAccountInfo(new PublicAccount(item2.getAccount(), networkType),
                                            item2.getMinApproval(),
                                            item2.getMinRemoval(),
                                            item2.getCosignatories().stream().map(cosigner -> new PublicAccount(cosigner, networkType)).collect(Collectors.toList()),
                                            item2.getMultisigAccounts().stream().map(multisigAccount -> new PublicAccount(multisigAccount, networkType)).collect(Collectors.toList())))
                                    .collect(Collectors.toList())));
                            return new MultisigAccountGraphInfo(multisigAccountInfoMap);
                        }));
    }

    @Override
    public Observable<List<Transaction>> transactions(PublicAccount publicAccount) {
        return this.transactions(publicAccount, Optional.empty());
    }

    @Override
    public Observable<List<Transaction>> transactions(PublicAccount publicAccount, QueryParams queryParams) {
        return this.transactions(publicAccount, Optional.of(queryParams));
    }

    private Observable<List<Transaction>> transactions(PublicAccount publicAccount, Optional<QueryParams> queryParams) {
        return this.findTransactions(publicAccount, queryParams, "/transactions");
    }

    @Override
    public Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount) {
        return this.incomingTransactions(publicAccount, Optional.empty());
    }

    @Override
    public Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount, QueryParams queryParams) {
        return this.incomingTransactions(publicAccount, Optional.of(queryParams));
    }

    private Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount, Optional<QueryParams> queryParams) {
        return this.findTransactions(publicAccount, queryParams, "/transactions/incoming");
    }

    @Override
    public Observable<List<Transaction>> outgoingTransactions(PublicAccount publicAccount) {
        return this.outgoingTransactions(publicAccount, Optional.empty());
    }

    @Override
    public Observable<List<Transaction>> outgoingTransactions(PublicAccount publicAccount, QueryParams queryParams) {
        return this.outgoingTransactions(publicAccount, Optional.of(queryParams));
    }

    private Observable<List<Transaction>> outgoingTransactions(PublicAccount publicAccount, Optional<QueryParams> queryParams) {
        return this.findTransactions(publicAccount, queryParams, "/transactions/outgoing");
    }

    @Override
    public Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount) {
        return this.aggregateBondedTransactions(publicAccount, Optional.empty());
    }

    @Override
    public Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount, QueryParams queryParams) {
        return this.aggregateBondedTransactions(publicAccount, Optional.of(queryParams));
    }

    private Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount, Optional<QueryParams> queryParams) {
        return this.findTransactions(publicAccount, queryParams, "/transactions/partial")
                .flatMapIterable(item -> item)
                .map(item -> (AggregateTransaction) item)
                .toList()
                .toObservable();
    }

    @Override
    public Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount) {
        return this.unconfirmedTransactions(publicAccount, Optional.empty());
    }

    @Override
    public Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount, QueryParams queryParams) {
        return this.unconfirmedTransactions(publicAccount, Optional.of(queryParams));
    }

    private Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount, Optional<QueryParams> queryParams) {
        return this.findTransactions(publicAccount, queryParams, "/transactions/unconfirmed");
    }

    private Observable<List<Transaction>> findTransactions(PublicAccount publicAccount, Optional<QueryParams> queryParams, String path) {
        return this.client
                .getAbs(this.url + publicAccount.getPublicKey() + path + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                .as(BodyCodec.jsonArray())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> new JsonArray(json.toString()).stream().map(s -> (JsonObject) s).collect(Collectors.toList()))
                .flatMapIterable(item -> item)
                .map(new TransactionMapping())
                .toList()
                .toObservable();
    }

    /**
     * return function that transforms MultisigDTO to MultisigAccountInfo
     * 
     * @param networkType
     * @return
     */
    private Function<MultisigDTO, MultisigAccountInfo> transfromMultisigAccountInfoDTO(NetworkType networkType) {
        return multisig -> new MultisigAccountInfo(
                new PublicAccount(multisig.getAccount(), networkType),
                multisig.getMinApproval(),
                multisig.getMinRemoval(),
                multisig.getCosignatories().stream().map(cosigner -> new PublicAccount(cosigner, networkType)).collect(Collectors.toList()),
                multisig.getMultisigAccounts().stream().map(multisigAccount -> new PublicAccount(multisigAccount, networkType)).collect(Collectors.toList())
        );
    }
}
