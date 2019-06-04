/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
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
