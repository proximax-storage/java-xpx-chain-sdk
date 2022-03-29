/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.transaction;

import java.util.List;

public class TransactionSearch {
    private final List<Transaction> transactions;
    private final Pagination paginations;

    public TransactionSearch(List<Transaction> transactions, Pagination paginations) {
        this.transactions = transactions;
        this.paginations = paginations;
    }

    /**
     * Returns list of transaction
     * 
     * @return List of {@link Pagination}
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Returns paginations
     *
     * @return {@link Pagination}
     */
    public Pagination getPaginations() {
        return paginations;
    }

    

}
