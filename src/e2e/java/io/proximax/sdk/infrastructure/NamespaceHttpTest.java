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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.events.Namespace;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import io.proximax.sdk.BaseTest;
import io.proximax.sdk.NamespaceRepository;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.namespace.NamespaceName;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.BlockchainApi;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class NamespaceHttpTest extends BaseTest {
    private NamespaceRepository namespaceHttp;
    private static final String ROOT_NAME = "test-root-namespace-"
            + new BigDecimal(Math.floor(Math.random() * 10000)).intValue();
    private static final String CHILD1_NAME = "c1";

    private static final PublicAccount simpleAccount = PublicAccount
            .createFromPublicKey("0D22E9D42F124072E14C4F804E4FC7F5431C831EAF03BEFD55D521B9A9D0B89D",
                    NetworkType.TEST_NET);
    @BeforeAll
    void setup() throws IOException {
        namespaceHttp = new BlockchainApi(new URL(this.getNodeUrl()), getNetworkType()).createNamespaceRepository();
    }

    @Test
    void getNamespaceNames() throws ExecutionException, InterruptedException {
        // check that the seed account has the namespace as expected
        List<NamespaceName> nsCount = namespaceHttp.getNamespaceNames(Arrays.asList(new NamespaceId("mnpfxpimsn"))).toFuture().get();
        assertEquals("mnpfxpimsn", nsCount.get(0).getName());

    }

    @Test
    void getNamespacesFromAccount() throws ExecutionException, InterruptedException {
        // check that the seed account has the namespace as expected
        List<NamespaceInfo> nsCount = namespaceHttp
                .getNamespacesFromAccounts(Arrays.asList(simpleAccount.getAddress())).toFuture().get();
        assertEquals("f1f7b2776f2f4df3", nsCount.get(0).getId().getIdAsHex());

    }

    @Test
    void getNamespace() throws ExecutionException, InterruptedException {
        NamespaceId namespace = new NamespaceId("mnpfxpimsn");
        // check that the seed account has the namespace as expected
        NamespaceInfo nsCount = namespaceHttp
                .getNamespace(namespace).toFuture().get();
        assertEquals("f1f7b2776f2f4df3", nsCount.getId().getIdAsHex());
    }

}
