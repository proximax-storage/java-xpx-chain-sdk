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

import io.nem.sdk.model.account.*;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.reactivex.Observable;

import java.util.List;

/**
 * Account interface repository.
 *
 * @since 1.0
 */
public interface AccountRepository {
    /**
     * Gets an AccountInfo for an account.
     *
     * @param address Address
     * @return Observable of {@link AccountInfo}
     */
    Observable<AccountInfo> getAccountInfo(Address address);

    /**
     * Gets AccountsInfo for different accounts.
     *
     * @param addresses List of Address
     * @return Observable of List<{@link AccountInfo}>
     */
    Observable<List<AccountInfo>> getAccountsInfo(List<Address> addresses);

    /**
     * Gets a MultisigAccountInfo for an account.
     *
     * @param address Address
     * @return Observable of {@link MultisigAccountInfo}
     */
    Observable<MultisigAccountInfo> getMultisigAccountInfo(Address address);

    /**
     * Gets a MultisigAccountGraphInfo for an account.
     *
     * @param address Address
     * @return Observable of {@link MultisigAccountGraphInfo}
     */
    Observable<MultisigAccountGraphInfo> getMultisigAccountGraphInfo(Address address);

    /**
     * Gets an list of confirmed transactions for which an account is signer or receiver.
     *
     * @param publicAccount PublicAccount
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> transactions(PublicAccount publicAccount);

    /**
     * Gets an list of confirmed transactions for which an account is signer or receiver.
     * With pagination.
     *
     * @param publicAccount PublicAccount
     * @param queryParams   QueryParams
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> transactions(PublicAccount publicAccount, QueryParams queryParams);

    /**
     * Gets an list of transactions for which an account is the recipient of a transaction.
     * A transaction is said to be incoming with respect to an account if the account is the recipient of a transaction.
     *
     * @param publicAccount PublicAccount
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount);

    /**
     * Gets an list of transactions for which an account is the recipient of a transaction.
     * A transaction is said to be incoming with respect to an account if the account is the recipient of a transaction.
     * With pagination.
     *
     * @param publicAccount PublicAccount
     * @param queryParams   QueryParams
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> incomingTransactions(PublicAccount publicAccount, QueryParams queryParams);

    /**
     * Gets an list of transactions for which an account is the sender a transaction.
     * A transaction is said to be outgoing with respect to an account if the account is the sender of a transaction.
     *
     * @param publicAccount PublicAccount
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> outgoingTransactions(PublicAccount publicAccount);

    /**
     * Gets an list of transactions for which an account is the sender a transaction.
     * A transaction is said to be outgoing with respect to an account if the account is the sender of a transaction.
     * With pagination.
     *
     * @param publicAccount PublicAccount
     * @param queryParams   QueryParams
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> outgoingTransactions(PublicAccount publicAccount, QueryParams queryParams);

    /**
     * Gets an list of transactions for which an account is the sender or has sign the transaction.
     * A transaction is said to be aggregate bonded with respect to an account if there are missing signatures.
     *
     * @param publicAccount PublicAccount
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount);

    /**
     * Gets an list of transactions for which an account is the sender or has sign the transaction.
     * A transaction is said to be aggregate bonded with respect to an account if there are missing signatures.
     * With pagination.
     *
     * @param publicAccount PublicAccount
     * @param queryParams   QueryParams
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<AggregateTransaction>> aggregateBondedTransactions(PublicAccount publicAccount, QueryParams queryParams);

    /**
     * Gets the list of transactions for which an account is the sender or receiver and which have not yet been included in a block.
     * Unconfirmed transactions are those transactions that have not yet been included in a block.
     * Unconfirmed transactions are not guaranteed to be included in any block.
     *
     * @param publicAccount PublicAccount
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount);

    /**
     * Gets the list of transactions for which an account is the sender or receiver and which have not yet been included in a block.
     * Unconfirmed transactions are those transactions that have not yet been included in a block.
     * Unconfirmed transactions are not guaranteed to be included in any block.
     * With pagination.
     *
     * @param publicAccount PublicAccount
     * @param queryParams   QueryParams
     * @return Observable of List<{@link Transaction}>
     */
    Observable<List<Transaction>> unconfirmedTransactions(PublicAccount publicAccount, QueryParams queryParams);
}
