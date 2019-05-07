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

import static io.proximax.sdk.infrastructure.utils.UInt64Utils.toBigInt;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.proximax.sdk.infrastructure.model.BlockInfoDTO;
import io.proximax.sdk.infrastructure.model.BlockchainScoreDTO;
import io.proximax.sdk.infrastructure.model.BlockchainStorageInfoDTO;
import io.proximax.sdk.infrastructure.model.HeightDTO;
import io.proximax.sdk.infrastructure.utils.BlockchainScoreDTOUtils;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.Transaction;
import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/**
 * Blockchain http repository.
 *
 * @since 1.0
 */
public class BlockchainHttp extends Http implements BlockchainRepository {

    public BlockchainHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
    }

    public BlockchainHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<BlockInfo> getBlockByHeight(BigInteger height) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/block/" + height.toString())
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), BlockInfoDTO.class))
                        .map(blockInfoDTO -> new BlockInfo(blockInfoDTO.getMeta().getHash(),
                                blockInfoDTO.getMeta().getGenerationHash(),
                                Optional.of(toBigInt(blockInfoDTO.getMeta().getTotalFee())),
                                Optional.of(blockInfoDTO.getMeta().getNumTransactions().intValue()),
                                blockInfoDTO.getBlock().getSignature(),
                                new PublicAccount(blockInfoDTO.getBlock().getSigner(), networkType),
                                networkType,
                                (int) Long.parseLong(Integer.toHexString(blockInfoDTO.getBlock().getVersion().intValue()).substring(2, 4), 16),
                                blockInfoDTO.getBlock().getType().intValue(),
                                toBigInt(blockInfoDTO.getBlock().getHeight()),
                                toBigInt(blockInfoDTO.getBlock().getTimestamp()),
                                toBigInt(blockInfoDTO.getBlock().getDifficulty()),
                                blockInfoDTO.getBlock().getPreviousBlockHash(),
                                blockInfoDTO.getBlock().getBlockTransactionsHash())));
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
                .getAbs(this.url + "/block/" + height + "/transactions" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                .as(BodyCodec.jsonArray())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> new JsonArray(json.toString()).stream().map(s -> (JsonObject) s).collect(Collectors.toList()))
                .flatMapIterable(item -> item)
                .map(new TransactionMapping())
                .toList()
                .toObservable();
    }

    @Override
    public Observable<BigInteger> getBlockchainHeight() {
        return this.client
                .getAbs(this.url + "/chain/height")
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> objectMapper.readValue(json.toString(), HeightDTO.class))
                .map(blockchainHeight -> toBigInt(blockchainHeight.getHeight()));
    }

    public Observable<BigInteger> getBlockchainScore() {
        return this.client
                .getAbs(this.url + "/chain/score")
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> objectMapper.readValue(json.toString(), BlockchainScoreDTO.class))
                .map(BlockchainScoreDTOUtils::toBigInt);
    }

    @Override
    public Observable<BlockchainStorageInfo> getBlockchainStorage() {
        return this.client
                .getAbs(this.url + "/diagnostic/storage")
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> objectMapper.readValue(json.toString(), BlockchainStorageInfoDTO.class))
                .map(blockchainStorageInfoDTO -> new BlockchainStorageInfo(blockchainStorageInfoDTO.getNumAccounts(),
                        blockchainStorageInfoDTO.getNumBlocks(),
                        blockchainStorageInfoDTO.getNumBlocks()));
    }
}
