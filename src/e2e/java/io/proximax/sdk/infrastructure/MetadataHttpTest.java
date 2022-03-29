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
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.MetadataRepository;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.metadata.MetadataEntry;
import io.proximax.sdk.model.metadata.MetadataSearch;
import io.proximax.sdk.model.network.NetworkType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MetadataHttpTest extends BaseTest {
    private static final String compositeHash = "5DB111FAFD1CD1AB10747B1BDDF895D6469965A1D11D73E8B74F0D44A16BBE8E";
    private MetadataRepository metadataHttp;

    @BeforeAll
    void setup() throws IOException {
        metadataHttp = new BlockchainApi(new URL(getNodeUrl()), getNetworkType()).createMetadataRepository();
    }

    @Test
    void getMetadata() throws ExecutionException, InterruptedException {

        MetadataEntry metadata = metadataHttp.getMetadata(compositeHash).toFuture().get();
        assertEquals(compositeHash, metadata.getCompositeHash());
    }

    @Test
    void getMetadatas() throws ExecutionException, InterruptedException {
        List<MetadataEntry> metadatas = metadataHttp.getMetadatas(Collections.singletonList(
                compositeHash)).toFuture().get();

        assertEquals(compositeHash, metadatas.get(0).getCompositeHash());
    }

    @Test
    void metadataSearch() throws ExecutionException, InterruptedException {
        String compositeHash = "EE388CCB5A0F5A35B58E37323332B59C94F1667BDE433307381419C612392C8C";
       String publicKey = "F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9";
        MetadataSearch metadata = metadataHttp.MetadataEntrySearch(new MetadataQueryParams(null, null, null, null,
                                        PublicAccount.createFromPublicKey(publicKey, NetworkType.TEST_NET), null, null,
                                        null)).toFuture().get();
        assertEquals(compositeHash, metadata.getMetadataEntries().get(0).getCompositeHash());
        assertEquals(9, metadata.getMetadataEntries().size());

        assertEquals(9, metadata.getPagination().getTotalEntries());
        assertEquals(20, metadata.getPagination().getPageSize());

    }
}
