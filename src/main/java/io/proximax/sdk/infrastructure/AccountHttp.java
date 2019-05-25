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

import static io.proximax.sdk.utils.GsonUtils.stream;
import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.AccountRepository;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.gen.model.AccountInfoDTO;
import io.proximax.sdk.gen.model.AccountPropertiesInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountGraphInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountInfoDTO;
import io.proximax.sdk.gen.model.MultisigDTO;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.MultisigAccountGraphInfo;
import io.proximax.sdk.model.account.MultisigAccountInfo;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.account.props.AccountProperties;
import io.proximax.sdk.model.account.props.AccountProperty;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.utils.dto.AccountDTOUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Account http repository.
 *
 * @since 1.0
 */
public class AccountHttp extends Http implements AccountRepository {

   private static final String ROUTE = "/account/";
   private static final String PROPERTIES_SUFFIX = "/properties";
   
   public AccountHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<AccountInfo> getAccountInfo(Address address) {
      return this.client.get(ROUTE + address.plain())
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, AccountInfoDTO.class))
            .map(AccountInfoDTO::getAccount)
            .map(accountDTO -> new AccountInfo(
                  Address.createFromRawAddress(AccountDTOUtils.getAddressEncoded(accountDTO)),
                  toBigInt(accountDTO.getAddressHeight()), accountDTO.getPublicKey(),
                  toBigInt(accountDTO.getPublicKeyHeight()),
                  accountDTO.getMosaics().stream()
                        .map(mosaicDTO -> new Mosaic(new MosaicId(toBigInt(mosaicDTO.getId())),
                              toBigInt(mosaicDTO.getAmount())))
                        .collect(Collectors.toList())));
   }

   @Override
   public Observable<List<AccountInfo>> getAccountsInfo(List<Address> addresses) {
      // prepare JSON array with addresses
      JsonArray arr = new JsonArray(addresses.size());
      addresses.stream().map(Address::plain).forEachOrdered(addr -> arr.add(addr));

      JsonObject requestBody = new JsonObject();
      requestBody.add("addresses", arr);
      return this.client.post(ROUTE, requestBody).map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<AccountInfoDTO>>readValue(str, new TypeReference<List<AccountInfoDTO>>() {
            })).flatMapIterable(item -> item).map(AccountInfoDTO::getAccount).map(
                  accountDTO -> new AccountInfo(
                        Address.createFromRawAddress(AccountDTOUtils.getAddressEncoded(accountDTO)),
                        toBigInt(accountDTO.getAddressHeight()), accountDTO.getPublicKey(),
                        toBigInt(accountDTO.getPublicKeyHeight()),
                        accountDTO.getMosaics().stream()
                              .map(mosaicDTO -> new Mosaic(new MosaicId(toBigInt(mosaicDTO.getId())),
                                    toBigInt(mosaicDTO.getAmount())))
                              .collect(Collectors.toList())))
            .toList().toObservable();
   }

   @Override
   public Observable<MultisigAccountInfo> getMultisigAccountInfo(Address address) {
      return this.client.get(ROUTE + address.plain() + "/multisig").map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, MultisigAccountInfoDTO.class))
            .map(MultisigAccountInfoDTO::getMultisig).map(transfromMultisigAccountInfoDTO(api.getNetworkType()));
   }

   @Override
   public Observable<MultisigAccountGraphInfo> getMultisigAccountGraphInfo(Address address) {
      final NetworkType networkType = api.getNetworkType();
      return this.client.get(ROUTE + address.plain() + "/multisig/graph").map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<MultisigAccountGraphInfoDTO>>readValue(str,
                  new TypeReference<List<MultisigAccountGraphInfoDTO>>() {
                  }))
            .map(multisigAccountGraphInfoDTOList -> {
               Map<Integer, List<MultisigAccountInfo>> multisigAccountInfoMap = new HashMap<>();
               multisigAccountGraphInfoDTOList.forEach(item -> multisigAccountInfoMap.put(item.getLevel(),
                     item.getMultisigEntries().stream().map(MultisigAccountInfoDTO::getMultisig)
                           .map(item2 -> new MultisigAccountInfo(new PublicAccount(item2.getAccount(), networkType),
                                 item2.getMinApproval(), item2.getMinRemoval(),
                                 item2.getCosignatories().stream()
                                       .map(cosigner -> new PublicAccount(cosigner, networkType))
                                       .collect(Collectors.toList()),
                                 item2.getMultisigAccounts().stream()
                                       .map(multisigAccount -> new PublicAccount(multisigAccount, networkType))
                                       .collect(Collectors.toList())))
                           .collect(Collectors.toList())));
               return new MultisigAccountGraphInfo(multisigAccountInfoMap);
            });
   }

   @Override
   public Observable<AccountProperties> getAccountProperty(Address address) {
      return this.client.get(ROUTE + address.plain() + PROPERTIES_SUFFIX)
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, AccountPropertiesInfoDTO.class))
            .map(dto -> new AccountProperties(
                  Address.createFromEncoded(dto.getAccountProperties().getAddress()),
                  dto.getAccountProperties().getProperties().stream()
                     .map(propDto -> new AccountProperty(
                           AccountPropertyType.getByCode(propDto.getPropertyType()), 
                           propDto.getValues()
                      )).collect(Collectors.toList())
                  ));
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

   private Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount,
         Optional<QueryParams> queryParams) {
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

   private Observable<List<Transaction>> outgoingTransactions(PublicAccount publicAccount,
         Optional<QueryParams> queryParams) {
      return this.findTransactions(publicAccount, queryParams, "/transactions/outgoing");
   }

   @Override
   public Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount) {
      return this.aggregateBondedTransactions(publicAccount, Optional.empty());
   }

   @Override
   public Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount,
         QueryParams queryParams) {
      return this.aggregateBondedTransactions(publicAccount, Optional.of(queryParams));
   }

   private Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount,
         Optional<QueryParams> queryParams) {
      return this.findTransactions(publicAccount, queryParams, "/transactions/partial").flatMapIterable(item -> item)
            .map(item -> (AggregateTransaction) item).toList().toObservable();
   }

   @Override
   public Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount) {
      return this.unconfirmedTransactions(publicAccount, Optional.empty());
   }

   @Override
   public Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount, QueryParams queryParams) {
      return this.unconfirmedTransactions(publicAccount, Optional.of(queryParams));
   }

   private Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount,
         Optional<QueryParams> queryParams) {
      return this.findTransactions(publicAccount, queryParams, "/transactions/unconfirmed");
   }

   private Observable<List<Transaction>> findTransactions(PublicAccount publicAccount,
         Optional<QueryParams> queryParams, String path) {
      return this.client
            .get(ROUTE
                  + publicAccount.getPublicKey() + path + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
            .map(Http::mapStringOrError)
            .map(str -> stream(new Gson().fromJson(str, JsonArray.class)).map(s -> (JsonObject) s)
                  .collect(Collectors.toList()))
            .flatMapIterable(item -> item).map(new TransactionMapping()).toList().toObservable();
   }

   /**
    * return function that transforms MultisigDTO to MultisigAccountInfo
    * 
    * @param networkType
    * @return
    */
   private Function<MultisigDTO, MultisigAccountInfo> transfromMultisigAccountInfoDTO(NetworkType networkType) {
      return multisig -> new MultisigAccountInfo(new PublicAccount(multisig.getAccount(), networkType),
            multisig.getMinApproval(), multisig.getMinRemoval(),
            multisig.getCosignatories().stream().map(cosigner -> new PublicAccount(cosigner, networkType))
                  .collect(Collectors.toList()),
            multisig.getMultisigAccounts().stream()
                  .map(multisigAccount -> new PublicAccount(multisigAccount, networkType))
                  .collect(Collectors.toList()));
   }
}
