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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.AccountRepository;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.gen.model.AccountInfoDTO;
import io.proximax.sdk.gen.model.AccountNamesDTO;
import io.proximax.sdk.gen.model.AccountPropertiesInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountGraphInfoDTO;
import io.proximax.sdk.gen.model.MultisigAccountInfoDTO;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.AccountNames;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.MultisigAccountGraphInfo;
import io.proximax.sdk.model.account.MultisigAccountInfo;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.account.props.AccountProperties;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionSearch;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

/**
 * Account http repository.
 *
 * @since 1.0
 */
public class AccountHttp extends Http implements AccountRepository {

   private static final String ROUTE = "/account";
   private static final String PROPERTIES_SUFFIX = "/properties";
   private static final String CONFIRMED_TRANSACTION = "/transactions/confirmed";
   private static final String UNCONFIRMED_TRANSACTION = "/transactions/unconfirmed";
   private static final Type TYPE_ACCOUNT_LIST = new TypeToken<List<AccountInfoDTO>>() {
   }.getType();
   private static final Type TYPE_MULTISIGACCT_LIST = new TypeToken<List<MultisigAccountGraphInfoDTO>>() {
   }.getType();
   private static final Type TYPE_ACCTPROPS_LIST = new TypeToken<List<AccountPropertiesInfoDTO>>() {
   }.getType();
   private static final Type TYPE_ACCOUNT_NAMES_LIST=new TypeToken<List<AccountNamesDTO>>(){}.getType();
   
   public AccountHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<AccountInfo> getAccountInfo(Address address) {
      return this.client.get(ROUTE + SLASH + address.plain()).map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, AccountInfoDTO.class))
            .map(AccountInfo::fromDto);
   }

   @Override
   public Observable<AccountInfo> getAccountInfo(PublicAccount publicAccount) {
      return this.client.get(ROUTE + SLASH + publicAccount.getPublicKey()).map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, AccountInfoDTO.class))
            .map(AccountInfo::fromDto);
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
   public Observable<List<AccountNames>> getAccountsNames(List<Address> addresses) {
      // prepare JSON array with addresses
      JsonArray arr = new JsonArray(addresses.size());
      addresses.stream().map(Address::plain).forEachOrdered(arr::add);

      JsonObject requestBody = new JsonObject();
      requestBody.add("addresses", arr);
      // post to the API
      return this.client.post(ROUTE + "/names", requestBody).map(Http::mapStringOrError)
            .map(this::toAccountNames)
            .flatMapIterable(item -> item)
            .map(dto -> AccountNames.fromDto(dto))
            .toList().toObservable();
   }


   @Override
   public Observable<TransactionSearch> transactions(PublicAccount publicAccount) {
      TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
            null, null, null,
            null,
            publicAccount,
            null,
            null);
      return this.transactions(publicAccount, Optional.of(queryParams));
   }

   @Override
   public Observable<TransactionSearch> transactions(PublicAccount publicAccount, TransactionQueryParams queryParams) {
      return this.transactions(publicAccount, Optional.of(queryParams));
   }

   private Observable<TransactionSearch> transactions(PublicAccount publicAccount,
         Optional<TransactionQueryParams> queryParams) {
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, CONFIRMED_TRANSACTION);
   }

   @Override
   public Observable<TransactionSearch> incomingTransactions(PublicAccount publicAccount) {
      TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
            null, null, null,
            null,
            publicAccount,
            null,
            null);
      return this.incomingTransactions(publicAccount.getPublicKey(), Optional.of(queryParams));
   }

   @Override
   public Observable<TransactionSearch> incomingTransactions(PublicAccount publicAccount,
         TransactionQueryParams queryParams) {
      return this.incomingTransactions(publicAccount.getPublicKey(), Optional.of(queryParams));
   }

   @Override
   public Observable<TransactionSearch> incomingTransactions(Address address) {
      TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
            null, null, null,
            null,
            null,
            null,
            address);
      return this.incomingTransactions(address.plain(), Optional.of(queryParams));
   }

   @Override
   public Observable<TransactionSearch> incomingTransactions(Address address, TransactionQueryParams queryParams) {
      return this.incomingTransactions(address.plain(), Optional.of(queryParams));
   }

   private Observable<TransactionSearch> incomingTransactions(String accountKey,
         Optional<TransactionQueryParams> queryParams) {
      return this.findTransactions(accountKey, queryParams, CONFIRMED_TRANSACTION);
   }

   @Override
   public Observable<TransactionSearch> outgoingTransactions(PublicAccount publicAccount) {
      TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
            null, null, null,
            null,
            publicAccount,
            null,
            null);
      return this.outgoingTransactions(publicAccount, Optional.of(queryParams));
   }

   @Override
   public Observable<TransactionSearch> outgoingTransactions(PublicAccount publicAccount,
         TransactionQueryParams queryParams) {
      return this.outgoingTransactions(publicAccount, Optional.of(queryParams));
   }

   private Observable<TransactionSearch> outgoingTransactions(PublicAccount publicAccount,
         Optional<TransactionQueryParams> queryParams) {
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, CONFIRMED_TRANSACTION);
   }

   @Override
   public Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount) {
      TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
            null, null, null,
            null,
            null,
            null,
            publicAccount.getAddress());
      return this.aggregateBondedTransactions(publicAccount, Optional.of(queryParams));
   }

   @Override
   public Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount,
         TransactionQueryParams queryParams) {
      return this.aggregateBondedTransactions(publicAccount, Optional.of(queryParams));
   }

   private Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount,
         Optional<TransactionQueryParams> queryParams) {
      return this.findAggregateTransactions(publicAccount.getPublicKey(), queryParams, "/transactions/partial")
            .flatMapIterable(item -> item)
            .map(item -> (AggregateTransaction) item).toList().toObservable();
   }

   @Override
   public Observable<TransactionSearch> unconfirmedTransactions(PublicAccount publicAccount) {
      TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
            null, null, null,
            null,
            publicAccount,
            null,
            null);
      return this.unconfirmedTransactions(publicAccount, Optional.of(queryParams));
   }

   @Override
   public Observable<TransactionSearch> unconfirmedTransactions(PublicAccount publicAccount,
         TransactionQueryParams queryParams) {
      return this.unconfirmedTransactions(publicAccount, Optional.of(queryParams));
   }

   private Observable<TransactionSearch> unconfirmedTransactions(PublicAccount publicAccount,
         Optional<TransactionQueryParams> queryParams) {
      return this.findTransactions(publicAccount.getPublicKey(), queryParams, UNCONFIRMED_TRANSACTION);
   }

   private Observable<TransactionSearch> findTransactions(String accountKey,
         Optional<TransactionQueryParams> queryParams, String path) {
      return this.client
            .get(path + queryParams.get().toUrl())
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonObject)
            .map(new TransactionSearchMapping());
   }

   private Observable<List<Transaction>> findAggregateTransactions(String accountKey,
         Optional<TransactionQueryParams> queryParams, String path) {
      return this.client
            .get(path + queryParams.get().toUrl())
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonObject)
            .map(new TransactionAggregateMapping());
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

   private List<AccountNamesDTO> toAccountNames(String json) {
      return gson.fromJson(json, TYPE_ACCOUNT_NAMES_LIST);
   }
}
