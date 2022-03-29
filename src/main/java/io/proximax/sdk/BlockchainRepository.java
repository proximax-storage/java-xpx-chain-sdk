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

import java.math.BigInteger;
import java.util.List;

import io.proximax.sdk.infrastructure.TransactionQueryParams;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;
import io.proximax.sdk.model.blockchain.BlocksLimit;
import io.proximax.sdk.model.blockchain.MerklePath;
import io.proximax.sdk.model.blockchain.Receipts;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionSearch;
import io.reactivex.Observable;

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
     * @return Observable of {@link Transaction} list
     */
    Observable<TransactionSearch> getBlockTransactions(BigInteger height);

    /**
     * Gets list of transactions included in a block for a block height
     * With pagination.
     *
     * @param height                 BigInteger
     * @param queryParams Transaction QueryParams
     * @return Observable of {@link Transaction} list
     */
    Observable<TransactionSearch> getBlockTransactions(BigInteger height, TransactionQueryParams queryParams);

    /**
     * Gets blockchain storage info.
     *
     * @return Observable of {@link BlockchainStorageInfo}
     */
    Observable<BlockchainStorageInfo> getBlockchainStorage();
    
    /**
     * Get block receipts
     * 
     * @param height height of the block to retrieve receipts
     * @return observable receipts
     */
    Observable<Receipts> getBlockReceipts(BigInteger height);
    
    /**
     * get the merkle path of receipt specified by the block height and hash
     * 
     * @param height height of the block to check
     * @param receiptHash hash of the receipt
     * @return the merkle path
     */
    Observable<MerklePath> getReceiptMerklePath(BigInteger height, String receiptHash);
    
    /**
     * get the merkle path of transaction specified by the block height and hash
     * 
     * @param height height of the block to check
     * @param transactionHash hash of the transaction
     * @return the merkle path
     */
    Observable<MerklePath> getTransactionMerklePath(BigInteger height, String transactionHash);
    
    /**
     * Gets up to limit number of blocks starting by given block height (i.e. inclusive of the specified height)
     * 
     * @param height block height to start from
     * @param limit limit on number of blocks that get retrieved
     * @return list of block info instances
     */
    Observable<List<BlockInfo>> getBlocksByHeightWithLimit(BigInteger height, BlocksLimit limit);
    
    
 

}
