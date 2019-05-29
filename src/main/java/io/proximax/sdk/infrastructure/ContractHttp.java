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

import java.util.List;

import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ContractRepository;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.contract.Contract;
import io.reactivex.Observable;

/**
 * Contract HTTP repository.
 */
public class ContractHttp extends Http implements ContractRepository {

   private static final String CONTRACT_ROUTE = "/contract/";

   public ContractHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<List<Contract>> getContracts(Address address) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<List<Contract>> getContracts(Address... addresses) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<List<Contract>> getContracts(PublicKey publicKey) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<List<Contract>> getContracts(PublicKey... publicKeys) {
      // TODO Auto-generated method stub
      return null;
   }

}
