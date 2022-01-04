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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ChainUpgradeRepository;
import io.proximax.sdk.model.ChainUpgrade.BlockchainUpgrade;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class ChainUpgradeHttpTest extends BaseTest {

    private ChainUpgradeRepository chainUpgradeHttp;

    @BeforeAll
    void setup() throws IOException {
        chainUpgradeHttp = new BlockchainApi(new URL(this.getNodeUrl()), getNetworkType()).createChainUpgradeRepository();
    }
    @Test
    void checkBlockchainUpgrade() {
        BlockchainUpgrade upgrade = chainUpgradeHttp.getBlockchainUpgrade(BigInteger.ONE).blockingFirst();
        assertEquals(BigInteger.ONE, upgrade.getHeight());
        assertNotNull(upgrade.getVersion());
    }

   
}
