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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionStatus;
import io.proximax.sdk.model.transaction.EntityType;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("partially replaced by other tests")
public class TransactionHttpTest extends BaseTest {
    private TransactionRepository transactionHttp;
    private static final String TRANSACTION_HASH = "CFF075FD2A496D474775ACCDE4877901023ECE8466590A7B48C5D3E8F098F106";
    private static final String TRANSACTION_HASH_NOT_EXISTING = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @BeforeAll
    public void setup() throws IOException {
        transactionHttp = new BlockchainApi(new URL(getNodeUrl()), getNetworkType()).createTransactionRepository();
    }

    @Test
    public void getTransaction() throws ExecutionException, InterruptedException {
        Transaction transaction = transactionHttp
                .getTransaction(TRANSACTION_HASH)
                .toFuture()
                .get();

        assertEquals(EntityType.TRANSFER.getValue(), transaction.getType().getValue());
        assertEquals(TRANSACTION_HASH, transaction.getTransactionInfo().get().getHash().get());
    }

    @Test
    public void getTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transaction = transactionHttp
                .getTransactions(Collections.singletonList(TRANSACTION_HASH))
                .toFuture()
                .get();

        assertEquals(EntityType.TRANSFER.getValue(), transaction.get(0).getType().getValue());
        assertEquals(TRANSACTION_HASH, transaction.get(0).getTransactionInfo().get().getHash().get());
    }

    @Test
    public void getTransactionStatus() throws ExecutionException, InterruptedException {
        TransactionStatus transactionStatus = transactionHttp
                .getTransactionStatus(TRANSACTION_HASH)
                .toFuture()
                .get();

        assertEquals(TRANSACTION_HASH, transactionStatus.getHash());
    }

    @Test
    public void getTransactionsStatuses() throws ExecutionException, InterruptedException {
        List<TransactionStatus> transactionStatuses = transactionHttp
                .getTransactionStatuses(Collections.singletonList(TRANSACTION_HASH))
                .toFuture()
                .get();

        assertEquals(TRANSACTION_HASH, transactionStatuses.get(0).getHash());
    }


    @Test
    public void throwExceptionWhenTransactionStatusOfATransactionDoesNotExists() {
        TestObserver<TransactionStatus> testObserver = new TestObserver<>();
        transactionHttp
                .getTransactionStatus(TRANSACTION_HASH_NOT_EXISTING)
                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }


    @Test
    public void throwExceptionWhenTransactionDoesNotExists() {
        TestObserver<Transaction> testObserver = new TestObserver<>();
        transactionHttp
                .getTransaction(TRANSACTION_HASH_NOT_EXISTING)
                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }
}