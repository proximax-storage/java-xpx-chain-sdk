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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.nem.sdk.model.account.AccountInfo;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.MultisigAccountGraphInfo;
import io.nem.sdk.model.account.MultisigAccountInfo;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountHttpTest extends BaseTest {
	private static final String ADDRESS = "VCGXJFX2H76VX7TTLC5XVWZ34Y2LZIXHHQM7X3HC";
	private static final String PUBLIC_KEY = "A36DF1F0B64C7FF71499784317C8D63FB1DB8E1909519AB72051D2BE77A1EF45";
	
    private AccountHttp accountHttp;
    private final PublicAccount publicAccount = PublicAccount.createFromPublicKey("1026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF37755", NetworkType.MIJIN_TEST);

    @BeforeAll
    void setup() throws IOException {
        accountHttp = new AccountHttp(this.getNodeUrl());
    }

    @Test
    void getAccountInfo() throws ExecutionException, InterruptedException {
        AccountInfo accountInfo = accountHttp
                .getAccountInfo(Address.createFromRawAddress("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY"))
                .toFuture()
                .get();

        assertEquals("1026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF37755", accountInfo.getPublicKey());
    }

    @Test
    void getAccountsInfo() throws ExecutionException, InterruptedException {
        List<AccountInfo> accountInfos = accountHttp
                .getAccountsInfo(Collections.singletonList(Address.createFromRawAddress(ADDRESS)))
                .toFuture()
                .get();

        assertEquals(1, accountInfos.size());
        assertEquals(PUBLIC_KEY, accountInfos.get(0).getPublicKey());
    }

    @Test
    void getMultipleAccountsInfo() throws ExecutionException, InterruptedException {
        List<AccountInfo> accountInfos = accountHttp
                .getAccountsInfo(Collections.singletonList(Address.createFromRawAddress("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY")))
                .toFuture()
                .get();

        assertEquals(1, accountInfos.size());
        assertEquals("1026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF37755", accountInfos.get(0).getPublicKey());
    }

    @Test
    void getMultisigAccountInfo() throws ExecutionException, InterruptedException {
        MultisigAccountInfo multisigAccountInfo = accountHttp
                .getMultisigAccountInfo(Address.createFromRawAddress("SBCPGZ3S2SCC3YHBBTYDCUZV4ZZEPHM2KGCP4QXX"))
                .toFuture()
                .get();

        assertEquals("B694186EE4AB0558CA4AFCFDD43B42114AE71094F5A1FC4A913FE9971CACD21D", multisigAccountInfo.getAccount().getPublicKey());
    }

    @Test
    void getMultisigAccountGraphInfo() throws ExecutionException, InterruptedException {
        MultisigAccountGraphInfo multisigAccountGraphInfos = accountHttp
                .getMultisigAccountGraphInfo(Address.createFromRawAddress("SBCPGZ3S2SCC3YHBBTYDCUZV4ZZEPHM2KGCP4QXX"))
                .toFuture()
                .get();

        assertEquals(new HashSet<>(Arrays.asList(-2, -1, 0, 1)), multisigAccountGraphInfos.getLevelsNumber());
    }

    @Test
    void transactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .transactions(publicAccount)
                .toFuture()
                .get();

        assertEquals(10, transactions.size());
    }

    @Test
    void transactionsWithPagination() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .transactions(publicAccount)
                .toFuture()
                .get();

        assertEquals(10, transactions.size());

        List<Transaction> nextTransactions = accountHttp
                .transactions(publicAccount, new QueryParams(11, transactions.get(0).getTransactionInfo().get().getId().get()))
                .toFuture()
                .get();

        assertEquals(11, nextTransactions.size());
        assertEquals(transactions.get(1).getTransactionInfo().get().getHash(), nextTransactions.get(0).getTransactionInfo().get().getHash());
    }

    @Test
    void incomingTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .incomingTransactions(publicAccount)
                .toFuture()
                .get();

        assertEquals(3, transactions.size());
    }

    @Test
    void outgoingTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .outgoingTransactions(publicAccount)
                .toFuture()
                .get();

        assertEquals(10, transactions.size());
    }

    @Test
    void aggregateBondedTransactions() throws ExecutionException, InterruptedException {
        List<AggregateTransaction> transactions = accountHttp
                .aggregateBondedTransactions(publicAccount)
                .toFuture()
                .get();

        assertEquals(0, transactions.size());
    }

    @Test
    void unconfirmedTransactions() throws ExecutionException, InterruptedException {
        List<Transaction> transactions = accountHttp
                .unconfirmedTransactions(publicAccount)
                .toFuture()
                .get();

        assertEquals(0, transactions.size());
    }

    @Test
    void throwExceptionWhenBlockDoesNotExists() {
        TestObserver<AccountInfo> testObserver = new TestObserver<>();
        accountHttp
                .getAccountInfo(Address.createFromRawAddress("SARDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY"))
                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }
}