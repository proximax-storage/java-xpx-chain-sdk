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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ChainRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChainHttpTest extends BaseTest {
    private ChainRepository chainHttp;

    @BeforeAll
    void setup() throws IOException {
        chainHttp = new BlockchainApi(new URL(this.getNodeUrl()), getNetworkType()).createChainRepository();
    }

    @Test
    void getBlockchainHeight() throws ExecutionException, InterruptedException {
        BigInteger blockchainHeight = chainHttp
                .getBlockchainHeight()
                .toFuture()
                .get();

        assertTrue(blockchainHeight.intValue() > 0);
    }

    @Test
    void getBlockchainScore() throws ExecutionException, InterruptedException {
        BigInteger blockchainScore = chainHttp
                .getBlockchainScore()
                .toFuture()
                .get();

        assertTrue(blockchainScore.intValue() != 0);
    }
    
    @Test
    void checkScore() {
        chainHttp.getBlockchainScore().blockingFirst();
    }

}
