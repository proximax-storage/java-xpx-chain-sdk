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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.AccountRepository;
import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.model.account.AccountInfo;
import io.proximax.sdk.model.account.AccountNames;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.TransactionSearch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class AccountHttpTest extends BaseTest {
    private AccountRepository accountHttp;

    private static final PublicAccount simpleAccount = PublicAccount
            .createFromPublicKey("0D22E9D42F124072E14C4F804E4FC7F5431C831EAF03BEFD55D521B9A9D0B89D", NetworkType.TEST_NET);
    
    @BeforeAll
    public void setup() throws IOException {
        accountHttp = new BlockchainApi(new URL(getNodeUrl()), getNetworkType()).createAccountRepository();
    }

    @Test
    void getAccountInfo() throws ExecutionException, InterruptedException {
        AccountInfo accountInfo = accountHttp.getAccountInfo(simpleAccount.getAddress()).toFuture().get();

        assertEquals(simpleAccount.getPublicKey(), accountInfo.getPublicKey());
    }

    @Test
    void getAccountsInfo() {
        List<String> accountKeys = accountHttp
                .getAccountsInfo(Arrays.asList(simpleAccount.getAddress(), simpleAccount.getAddress()))
                .flatMapIterable(list -> list).map(AccountInfo::getPublicKey).toList().blockingGet();

        assertEquals(1, accountKeys.size());
        assertTrue(accountKeys.contains(simpleAccount.getPublicKey()));
        assertTrue(accountKeys.contains(simpleAccount.getPublicKey()));
    }

    @Test
    void transactions() throws ExecutionException, InterruptedException {
        TransactionSearch transactions = accountHttp.transactions(simpleAccount).toFuture().get();

        assertEquals(20, transactions.getTransactions().size());
    }

    @Test
    void transactionsWithPagination() throws ExecutionException, InterruptedException {
        // get list of transactions
        TransactionSearch transactions = accountHttp.transactions(simpleAccount).toFuture().get();
        assertEquals(20, transactions.getTransactions().size());
       
        TransactionSearch nextTransactions = accountHttp.transactions(simpleAccount).toFuture()
                .get();
        assertEquals(20, nextTransactions.getTransactions().size());
        assertEquals(transactions.getTransactions()
                .get(0).getTransactionInfo().get().getHash(),
                nextTransactions.getTransactions().get(0).getTransactionInfo().get().getHash());
        TransactionSearch noTransactions = accountHttp.transactions(simpleAccount).toFuture().get();
        assertEquals(20, noTransactions.getTransactions().size());

    }

    @Test
    void incomingTransactions() throws ExecutionException, InterruptedException {
        TransactionSearch transactions = accountHttp.incomingTransactions(simpleAccount).toFuture()
                .get();
        TransactionSearch transactionsByAddr = accountHttp
                .incomingTransactions(simpleAccount.getAddress()).toFuture().get();

        assertEquals(20, transactions.getTransactions().size());
        assertEquals(20, transactionsByAddr.getTransactions().size());
        assertEquals(transactions.getTransactions().get(0).getSigner(),
                transactionsByAddr.getTransactions().get(0).getSigner());
    }

    @Test
    void outgoingTransactions() throws ExecutionException, InterruptedException {
        TransactionSearch transactions = accountHttp.outgoingTransactions(simpleAccount).toFuture()
                .get();

        assertEquals(20, transactions.getTransactions().size());
    }

    @Test
    void AggregateTransactions() throws ExecutionException, InterruptedException {

         PublicAccount test = PublicAccount
                .createFromPublicKey("7884a596d618fc72bcec642f022b78404d4517b4e0bd164f1017ffef2cbc2bcc",
                        NetworkType.TEST_NET);
        List<AggregateTransaction> transactions = accountHttp.aggregateBondedTransactions(
                test).toFuture()
                .get();

        assertEquals(1, transactions.size());
    }

    @Test
    void unconfirmedTransactions() throws ExecutionException, InterruptedException {
        TransactionSearch transactions = accountHttp.unconfirmedTransactions(simpleAccount)
                .toFuture()
                .get();

        assertEquals(0, transactions.getTransactions().size());
    }

    @Test
    void getAccountsNames() throws ExecutionException, InterruptedException {
            var address = Address.createFromRawAddress("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z");
            List<AccountNames> accountsNames = accountHttp.getAccountsNames(Arrays.asList(
                            address))
                            .toFuture()
                            .get();
            assertEquals(address, accountsNames.get(0).getAddress());
    }
    
    
}
