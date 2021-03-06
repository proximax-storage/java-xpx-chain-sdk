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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.AccountRepository;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.gen.model.AccountInfoDTO;
import io.proximax.sdk.gen.model.AccountPropertiesInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountGraphInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountInfoDTO;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.MultisigAccountGraphInfo;
import io.proximax.sdk.model.account.MultisigAccountInfo;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.account.props.AccountProperties;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.reactivex.Observable;

/**
 * Account http repository.
 *
 * @since 1.0
 */
public class AccountHttp extends Http implements AccountRepository {

   private static final String ROUTE = "/account";
   private static final String PROPERTIES_SUFFIX = "/properties";

   private static final Type TYPE_ACCOUNT_LIST = new TypeToken<List<AccountInfoDTO>>(){}.getType();
   private static final Type TYPE_MULTISIGACCT_LIST = new TypeToken<List<MultisigAccountGraphInfoDTO>>(){}.getType();
   private static final Type TYPE_ACCTPROPS_LIST = new TypeToken<List<AccountPropertiesInfoDTO>>(){}.getType();
   
   public AccountHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<AccountInfo> getAccountInfo(Address address) {
      return this.client.get(ROUTE + SLASH + address.plain()).map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, AccountInfoDTO.class))
            .map(AccountInfo::fromDto);
   }

   private List<AccountInfoDTO> toAccountInfo(String json) {
      return gson.fromJson(json, TYPE_ACCOUNT_LIST);
   }
   
   private List<MultisigAccountGraphInfoDTO> toMultisigAccountInfo(String json) {
      return gson.fromJson(json, TYPE_MULTISIGACCT_LIST);
   }
   
   private List<AccountPropertiesInfoDTO> toAccountProperties(String json) {
      return gson.fromJson(json, TYPE_ACCTPROPS_LIST);
   }
   
   @Override
   public Observable<List<AccountInfo>> getAccountsInfo(List<Address> addresses) {
      // prepare JSON array with addresses
      JsonArray arr = new JsonArray(addresses.size());
      addresses.stream().map(Address::plain).forEachOrdered(arr::add);

      JsonObject requestBody = new JsonObject();
      requestBody.add("addresses", arr);
      return this.client.post(ROUTE, requestBody)
            .map(Http::mapStringOrError)
            .map(this::toAccountInfo)
            .flatMapIterable(item -> item)
            .map(AccountInfo::fromDto)
            .toList().toObservable();
   }

   @Override
   public Observable<MultisigAccountInfo> getMultisigAccountInfo(Address address) {
      return this.client.get(ROUTE + SLASH + address.plain() + "/multisig").map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, MultisigAccountInfoDTO.class))
            .map(dto -> MultisigAccountInfo.fromDto(dto, api.getNetworkType()));
   }

   @Override
   public Observable<MultisigAccountGraphInfo> getMultisigAccountGraphInfo(Address address) {
      return this.client.get(ROUTE + SLASH + address.plain() + "/multisig/graph")
            .map(Http::mapStringOrError)
            .map(this::toMultisigAccountInfo)
            .map(dto -> MultisigAccountGraphInfo.fromDto(dto, api.getNetworkType()));
   }

   @Override
   public Observable<AccountProperties> getAccountProperties(Address address) {
      return this.client.get(ROUTE + SLASH + address.plain() + PROPERTIES_SUFFIX).map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, AccountPropertiesInfoDTO.class))
            .map(AccountPropertiesInfoDTO::getAccountProperties).map(AccountProperties::fromDto);
   }

   @Override
   public Observable<List<AccountProperties>> getAccountProperties(List<Address> addresses) {
      // prepare JSON array with addresses
      JsonArray arr = new JsonArray(addresses.size());
      addresses.stream().map(Address::plain).forEachOrdered(arr::add);

      JsonObject requestBody = new JsonObject();
      requestBody.add("addresses", arr);
      // post to the API
      return this.client.post(ROUTE + PROPERTIES_SUFFIX, requestBody).map(Http::mapStringOrError)
            .map(this::toAccountProperties)
            .flatMapIterable(item -> item).map(AccountPropertiesInfoDTO::getAccountProperties)
            .map(AccountProperties::fromDto).toList().toObservable();

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
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, "/transactions");
   }

   @Override
   public Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount) {
      return this.incomingTransactions(publicAccount.getPublicKey(), Optional.empty());
   }

   @Override
   public Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount, QueryParams queryParams) {
      return this.incomingTransactions(publicAccount.getPublicKey(), Optional.of(queryParams));
   }

   @Override
   public Observable<List<Transaction>> incomingTransactions(Address address) {
      return this.incomingTransactions(address.plain(), Optional.empty());
   }

   @Override
   public Observable<List<Transaction>> incomingTransactions(Address address, QueryParams queryParams) {
      return this.incomingTransactions(address.plain(), Optional.of(queryParams));
   }
   
   private Observable<List<Transaction>> incomingTransactions(String accountKey,
         Optional<QueryParams> queryParams) {
      return this.findTransactions(accountKey, queryParams, "/transactions/incoming");
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
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, "/transactions/outgoing");
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
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, "/transactions/partial").flatMapIterable(item -> item)
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
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, "/transactions/unconfirmed");
   }

   private Observable<List<Transaction>> findTransactions(String accountKey,
         Optional<QueryParams> queryParams, String path) {
      return this.client
            .get(ROUTE + SLASH + accountKey + path + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
            .map(Http::mapStringOrError)
            .map(str -> stream(new Gson().fromJson(str, JsonArray.class)).map(s -> (JsonObject) s)
                  .collect(Collectors.toList()))
            .flatMapIterable(item -> item).map(new TransactionMapping()).toList().toObservable();
   }
}
