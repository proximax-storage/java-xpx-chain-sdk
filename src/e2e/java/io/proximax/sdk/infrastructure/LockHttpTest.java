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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.net.URL;
import io.proximax.sdk.BaseTest;
import io.proximax.sdk.LockRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.proximax.sdk.BlockchainApi;
import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.lock.*;
import io.proximax.sdk.model.network.NetworkType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LockHttpTest extends BaseTest {
    private LockRepository lockHttp;

    @BeforeAll
    void setup() throws IOException {
        lockHttp = new BlockchainApi(new URL(getNodeUrl()), getNetworkType()).createLockRepository();
    }

    @Test
    void getAccountLockSecret() throws ExecutionException, InterruptedException {
        var publicKey = PublicAccount.createFromPublicKey(
                "42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                NetworkType.TEST_NET);
        var address = Address.createFromRawAddress("VD2FTZBEZOHCGIZGNFRLRZELQVKXRZE6KTRCZWNJ");
        List<SecretLockWithMetaInfo> accountLockSecret_PublicKey = lockHttp
                .getAccountLockSecret(PublicKey.fromHexString(publicKey.getPublicKey())).toFuture().get();

        List<SecretLockWithMetaInfo> accountLockSecret_Address = lockHttp.getAccountLockSecret(address).toFuture()
                .get();
        assertEquals(
                "42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                accountLockSecret_Address.get(0).getLock().getLockInfo().getAccount());
        assertEquals(
                "42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                accountLockSecret_PublicKey.get(0).getLock().getLockInfo().getAccount());
        assertEquals(accountLockSecret_Address.size(), accountLockSecret_PublicKey.size());
        assertEquals(accountLockSecret_Address.get(0).getLock().getCompositeHash(),
                accountLockSecret_PublicKey.get(0).getLock().getCompositeHash());
    }

    @Test
    void getSecretHash() throws ExecutionException, InterruptedException {
        var secretHash = "2B9B8AF5A7F0684325A1CF323073771DABF6369DA2E811D0F1BC8509D99D2E78";
        List<SecretLockWithMetaInfo> getSecretHash = lockHttp
                .getSecretHash(secretHash).toFuture().get();

        assertEquals(secretHash, getSecretHash.get(0).getLock().getSecret());
    }

    @Test
    void getAccountLockHash() throws ExecutionException, InterruptedException {
        var publicKey = PublicAccount.createFromPublicKey(
                "42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85",
                NetworkType.TEST_NET);
        var address = Address.createFromRawAddress("VD2FTZBEZOHCGIZGNFRLRZELQVKXRZE6KTRCZWNJ");
        List<HashLockWithMetaInfo> hashLockWithMetaInfo_PublicKey = lockHttp
                .getAccountLockHash(PublicKey.fromHexString(publicKey.getPublicKey())).toFuture().get();
        List<HashLockWithMetaInfo> hashLockWithMetaInfo_address = lockHttp.getAccountLockHash(
                address).toFuture().get();
        assertEquals(publicKey.getPublicKey(),
                hashLockWithMetaInfo_address.get(0).getLock().getLockInfo().getAccount());
        assertEquals(hashLockWithMetaInfo_PublicKey.get(0).getLock().getLockInfo()
                .getAccount(),
                hashLockWithMetaInfo_address.get(0).getLock().getLockInfo().getAccount());

    }

    @Test
    void getLockHash() throws ExecutionException, InterruptedException {
        var lockHash = "E382DFA58C39E6A822C402A014BFA2444BF39F1F7A765F61BD88D3744D01CCD6";
        HashLockWithMetaInfo getSecretHash = lockHttp.getLockHash(lockHash).toFuture().get();

        assertEquals(lockHash, getSecretHash.getLock().getHash());
    }

    @Test
    void getCompositeHash() throws ExecutionException, InterruptedException {
        var compositeHash = "AB61626DCC8C5DCB761C049F51065F978F378733F8C064B56A79698E6F774157";
        SecretLockWithMetaInfo getSecretHash = lockHttp.getCompositeHash(compositeHash).toFuture().get();

        assertEquals(compositeHash, getSecretHash.getLock().getCompositeHash());
    }

}
