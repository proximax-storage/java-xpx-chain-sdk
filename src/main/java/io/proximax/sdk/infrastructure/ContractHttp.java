/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.infrastructure;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ContractRepository;
import io.proximax.sdk.gen.model.ContractInfoDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.contract.Contract;
import io.reactivex.Observable;

/**
 * Contract HTTP repository.
 */
public class ContractHttp extends Http implements ContractRepository {

   private static final String CONTRACT_ROUTE = "/contract/";
   private static final String CONTRACS_SUFFIX = "/contracts";
   private static final String ACCOUNT_ROUTE = "/account/";
   private static final String ACCOUNT_CONTRACTS_ROUTE = "/account/contracts";
   
   public ContractHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<Contract> getContract(Address address) {
      return this.client.get(CONTRACT_ROUTE + address.plain())
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, ContractInfoDTO.class))
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto);
   }
   
   @Override
   public Observable<List<Contract>> getContracts(Address... addresses) {
      // prepare JSON array with addresses
      JsonArray arr = new JsonArray(addresses.length);
      Arrays.stream(addresses).map(Address::plain).forEachOrdered(addr -> arr.add(addr));

      JsonObject requestBody = new JsonObject();
      requestBody.add("addresses", arr);
      return this.client.post(CONTRACT_ROUTE, requestBody)
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<ContractInfoDTO>>readValue(str, new TypeReference<List<ContractInfoDTO>>() {}))
            .flatMapIterable(item -> item)
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto)
            .toList().toObservable();
   }

   @Override
   public Observable<Contract> getContract(PublicKey publicKey) {
      return this.client.get(ACCOUNT_ROUTE + publicKey.getHexString() + CONTRACS_SUFFIX)
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<ContractInfoDTO>>readValue(str, new TypeReference<List<ContractInfoDTO>>() {}))
            .flatMapIterable(item -> item)
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto);
   }

   @Override
   public Observable<List<Contract>> getContracts(PublicKey... publicKeys) {
      // prepare JSON array with public keys
      JsonArray arr = new JsonArray(publicKeys.length);
      Arrays.stream(publicKeys).map(PublicKey::getHexString).forEachOrdered(pubKey -> arr.add(pubKey));

      JsonObject requestBody = new JsonObject();
      requestBody.add("publicKeys", arr);
      return this.client.post(ACCOUNT_CONTRACTS_ROUTE, requestBody)
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<ContractInfoDTO>>readValue(str, new TypeReference<List<ContractInfoDTO>>() {}))
            .flatMapIterable(item -> item)
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto)
            .toList().toObservable();
   
   }

}