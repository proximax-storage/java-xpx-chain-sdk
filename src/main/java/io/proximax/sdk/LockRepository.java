/*
 * Copyright 2022 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.sdk;

import io.reactivex.Observable;

import java.util.List;

import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.lock.*;

public interface LockRepository {

    /**
     * <p>
     * Get hash lock by address
     * </p>
     * <p>
     * GET '/account/{address}/lock'
     * </p>
     * 
     * @param  address {@link Address}
     * @return observable list of {@link HashLockWithMetaInfo}
     */
    Observable<List<HashLockWithMetaInfo>> getAccountLockHash(Address address);

    /**
     * <p>
     * Get hash lock by public key
     * </p>
     * <p>
     * GET '/account/{publicKey}/lock'
     * </p>
     * 
     * @param publicKey {@link PublicKey}
     * @return observable list of {@link HashLockWithMetaInfo}
     */
    Observable<List<HashLockWithMetaInfo>> getAccountLockHash(PublicKey publicKey);

    /**
     * <p>
     * Get lock secret by address
     * </p>
     * <p>
     * GET '/account/{address}/lock/secret'
     * </p>
     * 
     * @param address {@link Address}
     * @return observable list of {@link SecretLockWithMetaInfo}
     */
    Observable<List<SecretLockWithMetaInfo>> getAccountLockSecret(Address address);

    /**
     * <p>
     * Get lock secret by public key
     * </p>
     * <p>
     * GET '/account/{publicKey}/lock/secret'
     * </p>
     * 
     * @param publicKey {@link PublicKey}
     * @return observable list of {@link SecretLockWithMetaInfo}
     */
    Observable<List<SecretLockWithMetaInfo>> getAccountLockSecret(PublicKey publicKey);

    /**
     * <p>
     * Get lock secret by compositeHash
     * </p>
     * <p>
     * GET '/lock/compositeHash/{compositeHash}'
     * </p>
     * 
     * @param compositeHash the compositeHash to check
     * @return observable list of {@link SecretLockWithMetaInfo}
     */
    Observable<SecretLockWithMetaInfo> getCompositeHash(String compositeHash);

    /**
     * <p>
     * Get hash lock by compositeHash
     * </p>
     * <p>
     * GET '/lock/hash/{lockHash}'
     * </p>
     * 
     * @param lockHash the lockHash to check
     * @return observable of {@link HashLockWithMetaInfo}
     */
    Observable<HashLockWithMetaInfo> getLockHash(String lockHash);

    /**
     * <p>
     * Get lock secret by secretHash
     * </p>
     * <p>
     * GET '/lock/secret/{secretHash}'
     * </p>
     * 
     * @param secretHash the lockHash to check
     * @return observable link of {@link SecretLockWithMetaInfo}
     */
    Observable<List<SecretLockWithMetaInfo>> getSecretHash(String secretHash);
}
