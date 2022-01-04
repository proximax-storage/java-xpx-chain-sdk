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

package io.proximax.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.infrastructure.QueryParams.Order;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;
import io.proximax.sdk.model.blockchain.Receipts;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.TransactionSearch;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockchainHttpTest extends BaseTest {
    private BlockchainRepository blockchainHttp;

    @BeforeAll
    void setup() throws IOException {
        blockchainHttp = new BlockchainApi(new URL(this.getNodeUrl()), getNetworkType()).createBlockchainRepository();
    }

    @Test
    void getBlockByHeight() throws ExecutionException, InterruptedException {
        BlockInfo blockInfo = blockchainHttp
                .getBlockByHeight(BigInteger.valueOf(1))
                .toFuture()
                .get();

        assertEquals(1, blockInfo.getHeight().intValue());
        assertEquals(0, blockInfo.getTimestamp().intValue());

    }

    @Test
    void getBlockTransactions() throws ExecutionException, InterruptedException {
        TransactionSearch transactions = blockchainHttp
                .getBlockTransactions(BigInteger.valueOf(1))
                .toFuture()
                .get();

        assertEquals(1, transactions.getPaginations().getPageNumber());
        assertEquals(35, transactions.getPaginations().getTotalEntries());
        assertEquals(20, transactions.getTransactions().size());
        assertEquals(EntityType.rawValueOf(16724), transactions.getTransactions().get(0).getType());

        TransactionSearch nextTransactions = blockchainHttp
                .getBlockTransactions(BigInteger.valueOf(1), new TransactionQueryParams(0, 0, Order.ASC, null, null,
                        TransactionSortingField.BLOCK, null, null, null,
                        null,
                        null,
                        null))
                .toFuture()
                .get();

        assertTrue(nextTransactions.getPaginations()!=null);
        assertEquals(EntityType.rawValueOf(16973), nextTransactions.getTransactions().get(19).getType());
    }

  

    @Test
    void getBlockchainStorage() throws ExecutionException, InterruptedException {
        BlockchainStorageInfo blockchainStorageInfo = blockchainHttp
                .getBlockchainStorage()
                .toFuture()
                .get();

        assertTrue(blockchainStorageInfo.getNumAccounts() > 0);
        assertTrue(blockchainStorageInfo.getNumTransactions() > 0);
        assertTrue(blockchainStorageInfo.getNumBlocks() > 0);
    }

    @Test
    void getBlockReceipts() throws ExecutionException, InterruptedException {
        Receipts blockReceiptsInfo = blockchainHttp
                .getBlockReceipts(BigInteger.TWO)
                .toFuture()
                .get();

        assertTrue(blockReceiptsInfo.getAddressResolutionStatements().size() == 0);
        assertTrue(blockReceiptsInfo.getAddressResolutionStatements().size() == 0);
        assertTrue(blockReceiptsInfo.getTransactionStatements().size() > 0);
    }

    @Test
    void throwExceptionWhenBlockDoesNotExists() {
        TestObserver<BlockInfo> testObserver = new TestObserver<>();
        blockchainHttp
                .getBlockByHeight(BigInteger.valueOf(1000000000))
                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }
}
