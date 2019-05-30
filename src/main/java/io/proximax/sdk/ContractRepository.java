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

package io.proximax.sdk;

import java.util.List;

import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.contract.Contract;
import io.reactivex.Observable;

/**
 * Contract interface repository.
 */
public interface ContractRepository {
    /**
     * <p>Get contract for specified address</p>
     * <p>GET '/contract/{accountId}'</p>
     * 
     * @param address the address to check
     * @return observable contract
     */
    Observable<Contract> getContract(Address address);

    /**
     * <p>Get contracts by addresses</p>
     * <p>POST '/contract'</p>
     * 
     * @param addresses the addresses to check
     * @return observable list of contracts
     */
    Observable<List<Contract>> getContracts(Address ... addresses);

    /**
     * <p>Get contract by public key</p>
     * <p>GET '/account/{publicKey}/contracts'</p>
     * 
     * @param publicKey the public key to check
     * @return observable contract
     */
    Observable<Contract> getContract(PublicKey publicKey);
    
    /**
     * <p>Get contracts by public keys</p>
     * <p>POST '/account/contracts'</p>
     * 
     * @param publicKeys the public keys to check
     * @return observable list of contracts
     */
    Observable<List<Contract>> getContracts(PublicKey ... publicKeys);
}
