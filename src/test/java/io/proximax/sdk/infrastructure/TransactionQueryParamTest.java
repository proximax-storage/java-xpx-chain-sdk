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
import io.proximax.sdk.model.transaction.EntityType;

public class TransactionQueryParamTest {
    private static final String publicKey = "F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9";
    private static final NetworkType networkType = NetworkType.TEST_NET;

    @Test
    void shouldCreateTransactionQueryParamTestViaCostructor() {
        TransactionQueryParams queryParams = new TransactionQueryParams(0, 0, Order.ASC, EntityType.ACCOUNT_LINK, "true",
                TransactionSortingField.BLOCK, 1, 10, 2, PublicAccount.createFromPublicKey(publicKey, networkType),
                Address.createFromPublicKey(publicKey, networkType),
                Address.createFromPublicKey(publicKey, networkType));
        assertEquals(20, queryParams.getPageSize());
        assertEquals(1, queryParams.getPageNumber());
        assertEquals(Order.ASC, queryParams.getOrder());
        assertEquals(EntityType.ACCOUNT_LINK.getValue(), queryParams.getType().getValue());
        assertEquals("true", queryParams.getEmbedded());
        assertEquals(TransactionSortingField.BLOCK, queryParams.getSortField());
        assertEquals(1, queryParams.getToHeight());
        assertEquals(10, queryParams.getFromHeight());
        assertEquals(2, queryParams.getHeight());
        assertEquals(PublicAccount.createFromPublicKey(publicKey, networkType), queryParams.getSignerPublicKey());

        assertEquals(Address.createFromPublicKey(publicKey, networkType), queryParams.getRecipientAddress());
        assertEquals(Address.createFromPublicKey(publicKey, networkType), queryParams.getAddress());
    }

    @Test
    void urlIsOK() {
        assertEquals(
                "?pageSize=20&pageNumber=1&order=asc&sortField=meta.height&type[]=16724&embedded=true&toHeight=1&fromHeight=10&height=2&signerPublicKey=F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9&recipientAddress=VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z&address=VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z",
                new TransactionQueryParams(0, 0, Order.ASC, EntityType.TRANSFER, "true",
                        TransactionSortingField.BLOCK, 1, 10, 2,
                        PublicAccount.createFromPublicKey(publicKey, networkType),
                        Address.createFromPublicKey(publicKey, networkType),
                        Address.createFromPublicKey(publicKey, networkType)).toUrl());
        assertEquals(
                "?pageSize=20&pageNumber=1&order=asc&sortField=meta.height&signerPublicKey=F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9&recipientAddress=VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z&address=VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z",
                new TransactionQueryParams(0, 0, Order.ASC, null, null,
                        TransactionSortingField.BLOCK, null, null, null,
                        PublicAccount.createFromPublicKey(publicKey, networkType),
                        Address.createFromPublicKey(publicKey, networkType),
                        Address.createFromPublicKey(publicKey, networkType)).toUrl());
    }
}
