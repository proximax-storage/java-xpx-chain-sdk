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

package io.nem.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceInfo;
import io.nem.sdk.model.namespace.NamespaceName;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NamespaceHttpTest extends BaseTest {
    private NamespaceHttp namespaceHttp;

    @BeforeAll
    void setup() throws IOException {
        namespaceHttp = new NamespaceHttp(this.getNodeUrl());
    }

    @Test
    void getNamespace() throws ExecutionException, InterruptedException {
        NamespaceInfo namespaceInfo = namespaceHttp
                .getNamespace(PROXIMA_NAMESPACE)
                .toFuture()
                .get();

        assertEquals(new BigInteger("1"), namespaceInfo.getStartHeight());
        assertEquals(new BigInteger("-1"), namespaceInfo.getEndHeight());
        assertEquals(PROXIMA_NAMESPACE, namespaceInfo.getLevels().get(0));
    }

    @Test
    void getNamespacesFromAccount() throws ExecutionException, InterruptedException {
        List<NamespaceInfo> namespacesInfo = namespaceHttp
                .getNamespacesFromAccount(Address.createFromRawAddress("SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP"))
                .toFuture()
                .get();

        assertEquals(1, namespacesInfo.size());
        assertEquals(new BigInteger("1"), namespacesInfo.get(0).getStartHeight());
        assertEquals(new BigInteger("-1"), namespacesInfo.get(0).getEndHeight());
        assertEquals(PROXIMA_NAMESPACE, namespacesInfo.get(0).getLevels().get(0));
    }

    @Test
    void getNamespacesFromAccounts() throws ExecutionException, InterruptedException {
        List<NamespaceInfo> namespacesInfo = namespaceHttp
                .getNamespacesFromAccounts(Collections.singletonList(Address.createFromRawAddress("SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP")))
                .toFuture()
                .get();

        assertEquals(1, namespacesInfo.size());
        assertEquals(new BigInteger("1"), namespacesInfo.get(0).getStartHeight());
        assertEquals(new BigInteger("-1"), namespacesInfo.get(0).getEndHeight());
        assertEquals(PROXIMA_NAMESPACE, namespacesInfo.get(0).getLevels().get(0));
    }

    @Test
    void getNamespaceNames() throws ExecutionException, InterruptedException {
        List<NamespaceName> namespaceNames = namespaceHttp
                .getNamespaceNames(Collections.singletonList(PROXIMA_NAMESPACE))
                .toFuture()
                .get();


        assertEquals(1, namespaceNames.size());
        assertEquals("nem", namespaceNames.get(0).getName());
        assertEquals(PROXIMA_NAMESPACE, namespaceNames.get(0).getNamespaceId());
    }

    @Test
    void throwExceptionWhenNamespaceDoesNotExists() {
        TestObserver<NamespaceInfo> testObserver = new TestObserver<>();
        namespaceHttp
                .getNamespace(new NamespaceId("nonregisterednamespace"))
                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }
}
