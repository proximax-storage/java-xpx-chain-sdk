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
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.nem.sdk.BaseTest;
import io.nem.sdk.model.blockchain.NetworkType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NetworkHttpTest extends BaseTest {
    private NetworkHttp networkHttp;

    @BeforeAll
    void setup() throws IOException {
        networkHttp = new NetworkHttp(this.getNodeUrl());
    }

    @Test
    void getNetworkType() throws ExecutionException, InterruptedException {
        NetworkType networkType = networkHttp.getNetworkType().toFuture().get();

        assertEquals(NetworkType.TEST_NET.getValue(), networkType.getValue());
    }
}
