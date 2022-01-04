/*
 * Copyright 2022 ProximaX
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

import com.google.gson.reflect.TypeToken;

import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.LockRepository;
import io.proximax.sdk.gen.model.HashLockWithMetaDTO;
import io.proximax.sdk.gen.model.SecretLockWithMetaDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.lock.HashLockWithMetaInfo;
import io.proximax.sdk.model.lock.SecretLockWithMetaInfo;
import io.reactivex.Observable;

public class LockHttp extends Http implements LockRepository {
    private static final String account = "/account";
    private static final String lock = "/lock";
    private static final String hash = "/hash";
    private static final String secret = "/secret";

    private static final Type TYPE_HASH_LOCK_WITH_META_INFO_LIST = new TypeToken<List<HashLockWithMetaDTO>>() {
    }.getType();
    private static final Type TYPE_SECRET_LOCK_WITH_META_INFO_LIST = new TypeToken<List<SecretLockWithMetaDTO>>() {
    }.getType();

    public LockHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<List<HashLockWithMetaInfo>> getAccountLockHash(Address address) {
        return this.client.get(
                account + SLASH + address.plain() + lock + hash)
                .map(Http::mapStringOrError)
                .map(this::toHashLockMetaInfo)
                .flatMapIterable(item -> item)
                .map(dto -> HashLockWithMetaInfo.fromDto(dto))
                .toList().toObservable();
    }

    @Override
    public Observable<List<HashLockWithMetaInfo>> getAccountLockHash(PublicKey publicKey) {
        return this.client.get(
                account + SLASH + publicKey + lock + hash)
                .map(Http::mapStringOrError)
                .map(this::toHashLockMetaInfo)
                .flatMapIterable(item -> item)
                .map(dto -> HashLockWithMetaInfo.fromDto(dto))
                .toList().toObservable();
    }

    @Override
    public Observable<List<SecretLockWithMetaInfo>> getAccountLockSecret(PublicKey publicKey) {
        return this.client.get(account + SLASH + publicKey + lock + secret)
                .map(Http::mapStringOrError)
                .map(this::toSecretLockMetaInfo)
                .flatMapIterable(item -> item)
                .map(dto -> SecretLockWithMetaInfo.fromDto(dto))
                .toList().toObservable();
    }

    @Override
    public Observable<List<SecretLockWithMetaInfo>> getAccountLockSecret(Address address) {
        return this.client.get(account + SLASH + address.plain() + lock + secret)
                .map(Http::mapStringOrError)
                .map(this::toSecretLockMetaInfo)
                .flatMapIterable(item -> item)
                .map(dto -> SecretLockWithMetaInfo.fromDto(dto))
                .toList().toObservable();
    }

    @Override
    public Observable<SecretLockWithMetaInfo> getCompositeHash(String compositeHash) {
        return this.client.get(lock + SLASH + "compositeHash" + SLASH + compositeHash)
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str,
                        SecretLockWithMetaDTO.class))
                .map(SecretLockWithMetaInfo::fromDto);
    }

    @Override
    public Observable<HashLockWithMetaInfo> getLockHash(String lockHash) {
        return this.client.get(lock + hash + SLASH + lockHash)
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str,
                        HashLockWithMetaDTO.class))
                .map(HashLockWithMetaInfo::fromDto);
    }

    @Override
    public Observable<List<SecretLockWithMetaInfo>> getSecretHash(String secretHash) {
        return this.client.get(lock + secret + SLASH + secretHash)
                .map(Http::mapStringOrError)
                .map(this::toSecretLockMetaInfo)
                .flatMapIterable(item -> item)
                .map(dto -> SecretLockWithMetaInfo.fromDto(dto))
                .toList().toObservable();
    }

    private List<SecretLockWithMetaDTO> toSecretLockMetaInfo(String json) {
        return gson.fromJson(json, TYPE_SECRET_LOCK_WITH_META_INFO_LIST);
    }

    private List<HashLockWithMetaDTO> toHashLockMetaInfo(String json) {
        return gson.fromJson(json, TYPE_HASH_LOCK_WITH_META_INFO_LIST);
    }
}
