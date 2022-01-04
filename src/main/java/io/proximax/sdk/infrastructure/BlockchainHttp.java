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

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.gen.model.BlockInfoDTO;
import io.proximax.sdk.gen.model.MerkleProofInfo;
import io.proximax.sdk.gen.model.MerkleProofInfoDTO;
import io.proximax.sdk.gen.model.NetworkConfigDTO;
import io.proximax.sdk.gen.model.StorageInfoDTO;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainConfig;
import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;
import io.proximax.sdk.model.blockchain.BlocksLimit;
import io.proximax.sdk.model.blockchain.MerklePath;
import io.proximax.sdk.model.blockchain.Receipts;
import io.proximax.sdk.model.transaction.TransactionSearch;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

/**
 * Blockchain http repository.
 *
 * @since 1.0
 */
public class BlockchainHttp extends Http implements BlockchainRepository {
    private static final String BLOCK = "/block/";
    private static final String CONFIG = "/config";

    private static final Type BLOCK_INFO_LIST_TYPE = new TypeToken<List<BlockInfoDTO>>() {
    }.getType();

    public BlockchainHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<BlockInfo> getBlockByHeight(BigInteger height) {
        return this.client
                .get(BLOCK + height.toString())
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, BlockInfoDTO.class))
                .map(blockInfoDTO -> BlockInfo.fromDto(blockInfoDTO, api.getNetworkType()));
    }

    @Override
    public Observable<TransactionSearch> getBlockTransactions(BigInteger height, TransactionQueryParams queryParams) {
        
        return this.getBlockTransactions(height, Optional.of(queryParams));
    }

    @Override
    public Observable<TransactionSearch> getBlockTransactions(BigInteger height) {
        TransactionQueryParams queryParams = new TransactionQueryParams(null, null, null, null, null,
                null, null, null,
                height.intValue(),
                null,
                null,
                null);
        return this.getBlockTransactions(height, Optional.of(queryParams));
    }

    private Observable<TransactionSearch> getBlockTransactions(BigInteger height,
            Optional<TransactionQueryParams> queryParams) {

        return this.client
                .get("/transactions/confirmed" + queryParams.get().toUrl())
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(new TransactionSearchMapping());
            }


    @Override
    public Observable<BlockchainStorageInfo> getBlockchainStorage() {
        return this.client
                .get("/diagnostic/storage")
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, StorageInfoDTO.class))
                .map(storageInfo -> new BlockchainStorageInfo(storageInfo.getNumAccounts(),
                        storageInfo.getNumBlocks(),
                        storageInfo.getNumBlocks()));
    }

    @Override
    public Observable<Receipts> getBlockReceipts(BigInteger height) {
        return this.client
                .get(BLOCK + height.toString() + "/receipts")
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(Receipts::fromJson);
    }

    @Override
    public Observable<MerklePath> getReceiptMerklePath(BigInteger height, String receiptHash) {
        return this.client.get(BLOCK + height.toString() + "/receipt/" + receiptHash + "/merkle")
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, MerkleProofInfo.class))
                .map(MerklePath::fromDto);
    }

    @Override
    public Observable<MerklePath> getTransactionMerklePath(BigInteger height, String trsansactionHash) {
        return this.client.get(BLOCK + height.toString() + "/transaction/" + trsansactionHash + "/merkle")
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, MerkleProofInfoDTO.class))
                .map(MerkleProofInfoDTO::getPayload)
                .map(MerklePath::fromDto);
    }

    @Override
    public Observable<List<BlockInfo>> getBlocksByHeightWithLimit(BigInteger height, BlocksLimit limit) {
        return this.client.get("/blocks/" + height.toString() + "/limit/" + limit.getLimit())
                .map(Http::mapStringOrError)
                .map(this::toBlockInfoList)
                .flatMapIterable(item -> item)
                .map(blockInfoDTO -> BlockInfo.fromDto(blockInfoDTO, api.getNetworkType()))
                .toList().toObservable();
    }

    @Override
    public Observable<BlockchainConfig> getBlockchainConfiguration(BigInteger height) {
        return this.client.get(CONFIG + SLASH + height.toString())
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, NetworkConfigDTO.class))
                .map(NetworkConfigDTO::getNetworkConfig)
                .map(BlockchainConfig::fromDto);
    }

  

    /**
     * allow use of gson list deserialization in stream
     * 
     * @param json json string representing list
     * @return list of block info DTOs
     */
    private List<BlockInfoDTO> toBlockInfoList(String json) {
        return gson.fromJson(json, BLOCK_INFO_LIST_TYPE);
    }
    
}
