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

import org.junit.jupiter.api.Test;

import io.proximax.sdk.infrastructure.QueryParams.Order;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

public class MetadataQueryParamTest {
    private static final String publicKey = "F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9";
    private static final NetworkType networkType = NetworkType.TEST_NET;

    @Test
    void shouldCreateMetadataQueryParamTestViaCostructor() {
        MetadataQueryParams queryParams = new MetadataQueryParams(0, 0, Order.ASC, MetadataSortingField.VALUE_SIZE,
                PublicAccount.createFromPublicKey(publicKey, networkType), "D21218B252C39BAE", "01000",
                Address.createFromPublicKey(publicKey, networkType));
        assertEquals(20, queryParams.getPageSize());
        assertEquals(1, queryParams.getPageNumber());
        assertEquals(Order.ASC, queryParams.getOrder());
        assertEquals(MetadataSortingField.VALUE_SIZE, queryParams.getSortField());
        assertEquals(PublicAccount.createFromPublicKey(publicKey, networkType), queryParams.getTargetKey());
        assertEquals("D21218B252C39BAE", queryParams.getTargetId());
        assertEquals("01000", queryParams.getScopedMetadataKey());
        assertEquals(Address.createFromPublicKey(publicKey, networkType), queryParams.getSourceAddress());
    }

    @Test
    void urlIsOK() {
        assertEquals(
                "?pageSize=20&pageNumber=1&order=asc&sortField=metadataEntry.valueSize&targetKey=F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9&targetId=D21218B252C39BAE&scopedMetadataKey=01000&sourceAddress=VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z",
                new MetadataQueryParams(0, 0, Order.ASC, MetadataSortingField.VALUE_SIZE,
                        PublicAccount.createFromPublicKey(publicKey, networkType), "D21218B252C39BAE", "01000",
                        Address.createFromPublicKey(publicKey, networkType)).toUrl());
        assertEquals(
                "?order=asc&sortField=metadataEntry.valueSize&targetKey=F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9&targetId=D21218B252C39BAE&scopedMetadataKey=01000&sourceAddress=VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z",
                new MetadataQueryParams(null, null, Order.ASC, MetadataSortingField.VALUE_SIZE,
                        PublicAccount.createFromPublicKey(publicKey, networkType), "D21218B252C39BAE", "01000",
                                        Address.createFromPublicKey(publicKey, networkType)).toUrl());
                        
        assertEquals(
                        "?targetKey=F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9",
                        new MetadataQueryParams(null, null, null, null,
                                        PublicAccount.createFromPublicKey(publicKey, networkType), null, null,
                                        null).toUrl());

                                        
    }
}
