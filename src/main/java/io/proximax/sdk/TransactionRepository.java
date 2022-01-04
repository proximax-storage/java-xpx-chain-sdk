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

package io.proximax.sdk;

import java.util.List;

import io.proximax.sdk.infrastructure.TransactionQueryParams;
import io.proximax.sdk.model.transaction.*;
import io.reactivex.Observable;

/**
 * Transaction interface repository.
 *
 * @since 1.0
 */
public interface TransactionRepository {

    /**
     * Gets a transaction for a given hash.
     *
     * @param transactionHash String
     * @return Observable of {@link Transaction}
     */
    Observable<Transaction> getTransaction(String transactionHash);

    /**
     * Gets an list of transactions for different transaction hashes.
     *
     * @param transactionHashes List of String
     * @return Observable of {@link Transaction} list
     */
    Observable<TransactionList> getTransactions(List<String> transactionHashes);

    /**
     * Gets a transaction status for a transaction hash.
     *
     * @param transactionHash String
     * @return Observable of {@link TransactionStatus}
     */
    Observable<TransactionStatus> getTransactionStatus(String transactionHash);

    /**
     * Gets an list of transaction status for different transaction hashes.
     *
     * @param transactionHashes List of String
     * @param group             Transaction Group
     * @return Observable of {@link TransactionStatus} list
     */
    Observable<List<TransactionStatus>> getTransactionStatuses(List<String> transactionHashes);

    /**
     * Gets transaction count based on the given entity type(s)
     *
     * @param transactionType List of Entities Type
     * @param group           Transaction Group
     * @return Observable of {@link TransactionCount} list
     */
    Observable<List<TransactionCount>> getTransactionsCount(List<Integer> transactionType);

    /**
     * Send a signed transaction.
     *
     * @param signedTransaction SignedTransaction
     * @return Observable of TransactionAnnounceResponse
     */
    Observable<TransactionAnnounceResponse> announce(SignedTransaction signedTransaction);

    /**
     * Send a signed transaction with missing signatures.
     *
     * @param signedTransaction SignedTransaction
     * @return Observable of {@link TransactionAnnounceResponse}
     */
    Observable<TransactionAnnounceResponse> announceAggregateBonded(SignedTransaction signedTransaction);

    /**
     * Send a co-signature signed transaction of an already announced transaction.
     *
     * @param cosignatureSignedTransaction CosignatureSignedTransaction
     * @return Observable of {@link TransactionAnnounceResponse}
     */
    Observable<TransactionAnnounceResponse> announceAggregateBondedCosignature(
            CosignatureSignedTransaction cosignatureSignedTransaction);

    /**
     * <p>
     * Search transaction
     * </p>
     * 
     * @param TransactionGroupType transaction group type
     * @return Observable of {@link TransactionSearch}
     */
    Observable<TransactionSearch> transactionSearch(TransactionGroupType transactionGroupType);
    
    /**
     * Search transaction
     * 
     * @param TransactionGroupType   transaction group type
     * @param TransactionQueryParams transaction query params
     * @return Observable of {@link TransactionSearch}
     */
    Observable<TransactionSearch> transactionSearch(TransactionGroupType transactionGroupType,TransactionQueryParams queryParams);
}
