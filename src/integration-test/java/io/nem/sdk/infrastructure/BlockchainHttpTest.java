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

import io.nem.sdk.model.blockchain.BlockInfo;
import io.nem.sdk.model.blockchain.BlockchainStorageInfo;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.model.transaction.Transaction;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockchainHttpTest extends BaseTest {
    private BlockchainHttp blockchainHttp;

    @BeforeAll
    void setup() throws IOException {
        blockchainHttp = new BlockchainHttp(this.getNodeUrl());
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
        List<Transaction> transactions = blockchainHttp
                .getBlockTransactions(BigInteger.valueOf(1))
                .toFuture()
                .get();

        assertEquals(10, transactions.size());

        List<Transaction> nextTransactions = blockchainHttp
                .getBlockTransactions(BigInteger.valueOf(1), new QueryParams(15, transactions.get(0).getTransactionInfo().get().getId().get()))
                .toFuture()
                .get();

        assertEquals(15, nextTransactions.size());
        assertEquals(transactions.get(1).getTransactionInfo().get().getHash(), nextTransactions.get(0).getTransactionInfo().get().getHash());
    }

    @Test
    void getBlockchainHeight() throws ExecutionException, InterruptedException {
        BigInteger blockchainHeight = blockchainHttp
                .getBlockchainHeight()
                .toFuture()
                .get();

        assertTrue(blockchainHeight.intValue() > 0);
    }

    @Test
    void getBlockchainScore() throws ExecutionException, InterruptedException {
        BigInteger blockchainScore = blockchainHttp
                .getBlockchainScore()
                .toFuture()
                .get();

        assertTrue(blockchainScore.intValue() != 0);
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
