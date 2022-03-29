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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionList;
import io.proximax.sdk.model.transaction.TransactionCount;
import io.proximax.sdk.model.transaction.TransactionGroupType;
import io.proximax.sdk.model.transaction.TransactionSearch;
import io.proximax.sdk.model.transaction.TransactionStatus;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @Disabled("partially replaced by other tests")
public class TransactionHttpTest extends BaseTest {
    private TransactionRepository transactionHttp;
    private static final String TRANSACTION_HASH = "FDDD64488CA70664203A053D2A7729E8B85B2830598BB46060F1B337E7F14C42";
    private static final String TRANSACTION_HASH2 = "DB0E01ACD12693C6DD41F4F10BD183DA05EE8E3AF2D23F99E9F7FA61E4DEEC34";

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
    public void getTransactionsCount() throws ExecutionException, InterruptedException {

        var entitytest1 = 16724;
        List<Integer> entityValue = new ArrayList<Integer>();
        entityValue.add(entitytest1);
        List<TransactionCount> transactionCount = transactionHttp
                .getTransactionsCount(entityValue)
                .toFuture()
                .get();

        assertEquals(EntityType.rawValueOf(entitytest1).getValue(), transactionCount.get(0).getEntityType());
        assertEquals(6775, transactionCount.get(0).getCount());
    }

    @Test
    public void getTransactions() throws ExecutionException, InterruptedException {
        List<String> hashes = new ArrayList<String>();
        hashes.add(TRANSACTION_HASH);
        hashes.add(TRANSACTION_HASH2);
        var publicKey = "F06FE22FBA1E116B8F0E673BA4EE424B16BD6EA7548ED259F3DCEBF8D74C49B9";
        TransactionList transaction = transactionHttp
                .getTransactions(hashes)
                .toFuture()
                .get();
        assertEquals(EntityType.AGGREGATE_COMPLETE.getValue(),
                transaction.getTransactions().get(0).getType().getValue());
        assertEquals(Optional.of(PublicAccount.createFromPublicKey(publicKey, NetworkType.TEST_NET)),
                transaction.getTransactions().get(0).getSigner());

        assertEquals(2,
                transaction.getTransactions().size());
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
        List<String> hashes = new ArrayList<String>();
        hashes.add(TRANSACTION_HASH);
        hashes.add(TRANSACTION_HASH2);
        List<TransactionStatus> transactionStatuses = transactionHttp
                .getTransactionStatuses(hashes)
                .toFuture()
                .get();

        assertEquals(TRANSACTION_HASH, transactionStatuses.get(0).getHash());
        assertEquals(TRANSACTION_HASH2, transactionStatuses.get(1).getHash());
    }

    @Test
    public void transactionSearch() throws ExecutionException, InterruptedException {

        TransactionSearch transactionSearch = transactionHttp
                .transactionSearch(TransactionGroupType.CONFIRMED)
                .toFuture()
                .get();

        assertEquals(EntityType.TRANSFER, transactionSearch.getTransactions().get(0).getType());

    }

    @Test
    public void transactionSearchwithParam() throws ExecutionException, InterruptedException {
        String signature = "AE7E198F6B70CB7FFD841C309CEB7619C45A06F2E5B477D9E94BC4C623B4DD46534BD028AD7826B87C3B6C33A0A57618E410C62AFB2F0A7DCAC46E0824AE1002";
        TransactionSearch transactionSearch = transactionHttp
                .transactionSearch(TransactionGroupType.CONFIRMED, new TransactionQueryParams(null, null, null, null,
                        null, null, null, null, null, null, null,
                        Address.createFromRawAddress("VA36Q6FO2BID7TDC7KDLP4F3P7E7SNUWUATD6LBV")))
                .toFuture()
                .get();

        assertEquals(Optional.of(signature), transactionSearch.getTransactions().get(0).getSignature());

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