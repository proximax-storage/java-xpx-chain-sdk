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


import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ExchangeRepository;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.exchange.AccountExchanges;
import io.proximax.sdk.model.exchange.ExchangeMosaicsList;
import io.proximax.sdk.model.exchange.MosaicExchange;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExchangeHttpTest extends BaseTest{
    private ExchangeRepository exchangeHttp;

    @BeforeAll
    void setup() throws IOException {
        exchangeHttp = new BlockchainApi(new URL(getNodeUrl()), getNetworkType()).createExchangeRepository();
    }

    @Test
    void getOfferList() throws ExecutionException, InterruptedException {
        List<ExchangeMosaicsList> exchangeMosaics = exchangeHttp.getOfferList().toFuture().get();
        assertEquals("34B40B8AD0CEE3F3", exchangeMosaics.get(1).getMosaicId());
    }

    @Test
    void getAccountExchanges() throws ExecutionException, InterruptedException {
        String publicKey = "FCDB81E03F641B3E29015E6C972C666A008DE0DB8039D02C30D8FC0A894683C4";
        String ownerAddress = "A81F905F8BF040E4B8E46822EFE487075121F2A79624F658B0";
        var address = Address.createFromPublicKey(
                publicKey,
                NetworkType.TEST_NET);
        AccountExchanges accountExchanges = exchangeHttp.getAccountExchanges(address).toFuture().get();
        assertEquals(PublicAccount.createFromPublicKey(publicKey, NetworkType.TEST_NET), accountExchanges.getOwner());
        assertEquals(Address.createFromEncoded(ownerAddress), accountExchanges.getOwnerAddress());
        assertNotNull(accountExchanges.getBuyOffers());
        assertEquals(BigInteger.valueOf(1000), accountExchanges.getBuyOffers().get(0).getAmount().get(0));
        assertEquals(BigInteger.valueOf(394459), accountExchanges.getBuyOffers().get(0).getDeadline().get(0));
    }
    
    @Test
    void getExchangeOffers() throws ExecutionException, InterruptedException {
        String publicKey= "d9a659a3aa42fd62be88e1d96b0f10eb91f6097f8d24ec8fd7c94ec6455735ec";
        String offerType = "sell";
        MosaicId mosaicId = new MosaicId("037c5af6052a9f7d");
        
        List<MosaicExchange> mosaicExchanges = exchangeHttp.getExchangeOffers(offerType, mosaicId).toFuture().get();
        assertEquals(PublicAccount.createFromPublicKey(publicKey.toUpperCase(), NetworkType.TEST_NET),
                mosaicExchanges.get(0).getOwner());
        
    }

}
