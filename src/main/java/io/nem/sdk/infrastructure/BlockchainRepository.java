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
import io.nem.sdk.model.transaction.Transaction;
import io.reactivex.Observable;

import java.math.BigInteger;
import java.util.List;

/**
 * Blockchain interface repository
 *
 * @since 1.0
 */
public interface BlockchainRepository {

    /**
     * Gets a BlockInfo for a given block height.
     *
     * @param height BigInteger
     * @return Observable of {@link BlockInfo}
     */
    Observable<BlockInfo> getBlockByHeight(BigInteger height);

    /**
     * Gets list of transactions included in a block for a block height
     *
     * @param height BigInteger
     * @return Observable of List of {@link Transaction}
     */
    Observable<List<Transaction>> getBlockTransactions(BigInteger height);

    /**
     * Gets list of transactions included in a block for a block height
     * With pagination.
     *
     * @param height      BigInteger
     * @param queryParams QueryParams
     * @return Observable of List of {@link Transaction}
     */
    Observable<List<Transaction>> getBlockTransactions(BigInteger height, QueryParams queryParams);

    /**
     * Gets current blockchain height.
     *
     * @return Observable of BigInteger
     */
    Observable<BigInteger> getBlockchainHeight();

    /**
     * Gets current blockchain score.
     *
     * @return Observable of BigInteger
     */
    Observable<BigInteger> getBlockchainScore();

    /**
     * Gets blockchain storage info.
     *
     * @return Observable of {@link BlockchainStorageInfo}
     */
    Observable<BlockchainStorageInfo> getBlockchainStorage();
}
