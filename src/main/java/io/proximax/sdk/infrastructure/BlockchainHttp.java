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

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.gen.model.BlockInfoDTO;
import io.proximax.sdk.gen.model.BlockchainScoreDTO;
import io.proximax.sdk.gen.model.BlockchainStorageInfoDTO;
import io.proximax.sdk.gen.model.HeightDTO;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.utils.dto.BlockchainScoreDTOUtils;
import io.reactivex.Observable;

/**
 * Blockchain http repository.
 *
 * @since 1.0
 */
public class BlockchainHttp extends Http implements BlockchainRepository {
    public BlockchainHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<BlockInfo> getBlockByHeight(BigInteger height) {
        return this.client
                        .get("/block/" + height.toString())
                        .map(Http::mapStringOrError)
                        .map(str -> objectMapper.readValue(str, BlockInfoDTO.class))
                        .map(blockInfoDTO -> new BlockInfo(blockInfoDTO.getMeta().getHash(),
                                blockInfoDTO.getMeta().getGenerationHash(),
                                Optional.of(toBigInt(blockInfoDTO.getMeta().getTotalFee())),
                                Optional.of(blockInfoDTO.getMeta().getNumTransactions().intValue()),
                                blockInfoDTO.getBlock().getSignature(),
                                new PublicAccount(blockInfoDTO.getBlock().getSigner(), api.getNetworkType()),
                                api.getNetworkType(),
                                (int) Long.parseLong(Integer.toHexString(blockInfoDTO.getBlock().getVersion().intValue()).substring(2, 4), 16),
                                blockInfoDTO.getBlock().getType().intValue(),
                                toBigInt(blockInfoDTO.getBlock().getHeight()),
                                toBigInt(blockInfoDTO.getBlock().getTimestamp()),
                                toBigInt(blockInfoDTO.getBlock().getDifficulty()),
                                blockInfoDTO.getBlock().getPreviousBlockHash(),
                                blockInfoDTO.getBlock().getBlockTransactionsHash()));
    }

    @Override
    public Observable<List<Transaction>> getBlockTransactions(BigInteger height, QueryParams queryParams) {
        return this.getBlockTransactions(height, Optional.of(queryParams));
    }

    @Override
    public Observable<List<Transaction>> getBlockTransactions(BigInteger height) {
        return this.getBlockTransactions(height, Optional.empty());
    }

    private Observable<List<Transaction>> getBlockTransactions(BigInteger height, Optional<QueryParams> queryParams) {
        return this.client
                .get("/block/" + height + "/transactions" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                .map(Http::mapStringOrError)
                .map(str -> StreamSupport.stream(new Gson().fromJson(str, JsonArray.class).spliterator(), false).map(s -> (JsonObject) s).collect(Collectors.toList()))
                .flatMapIterable(item -> item)
                .map(new TransactionMapping())
                .toList()
                .toObservable();
    }

    @Override
    public Observable<BigInteger> getBlockchainHeight() {
        return this.client
                .get("/chain/height")
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, HeightDTO.class))
                .map(blockchainHeight -> toBigInt(blockchainHeight.getHeight()));
    }

    public Observable<BigInteger> getBlockchainScore() {
        return this.client
                .get("/chain/score")
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, BlockchainScoreDTO.class))
                .map(BlockchainScoreDTOUtils::toBigInt);
    }

    @Override
    public Observable<BlockchainStorageInfo> getBlockchainStorage() {
        return this.client
                .get("/diagnostic/storage")
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, BlockchainStorageInfoDTO.class))
                .map(blockchainStorageInfoDTO -> new BlockchainStorageInfo(blockchainStorageInfoDTO.getNumAccounts(),
                        blockchainStorageInfoDTO.getNumBlocks(),
                        blockchainStorageInfoDTO.getNumBlocks()));
    }
}
