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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ConfigRepository;
import io.proximax.sdk.model.config.BlockchainConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigHttpTest extends BaseTest {
    private ConfigRepository configHttp;

    @BeforeAll
    void setup() throws IOException {
        configHttp = new BlockchainApi(new URL(this.getNodeUrl()), getNetworkType()).createConfigRepository();
    }
    
    @Test
    void checkBlockchainConfiguration() throws IOException {
        BlockchainConfig config = configHttp.getBlockchainConfiguration(BigInteger.ONE).blockingFirst();
        Properties props = new Properties();
        props.load(new ByteArrayInputStream(config.getConfig().getBytes(StandardCharsets.UTF_8)));
        JsonObject entities = new Gson().fromJson(config.getSupportedEntityVersions(), JsonObject.class);
        // make some assertions but fact that we got here is good sign
        assertEquals(BigInteger.ONE, config.getHeight());
        assertNotNull(props.getProperty("namespaceRentalFeeSinkPublicKey"));
        assertNotNull(entities.getAsJsonArray("entities").get(0).getAsJsonObject().get("name"));
    }
}
