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

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.BlockchainRepository;
import io.proximax.sdk.gen.model.BlockInfoDTO;
import io.proximax.sdk.gen.model.BlockchainScoreDTO;
import io.proximax.sdk.gen.model.HeightInfoDTO;
import io.proximax.sdk.gen.model.MerkleProofInfoDTO;
import io.proximax.sdk.gen.model.NodeInfoDTO;
import io.proximax.sdk.gen.model.NodeTimeDTO;
import io.proximax.sdk.gen.model.StorageInfoDTO;
import io.proximax.sdk.model.blockchain.BlockInfo;
import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;
import io.proximax.sdk.model.blockchain.BlocksLimit;
import io.proximax.sdk.model.blockchain.MerklePath;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.blockchain.NodeInfo;
import io.proximax.sdk.model.blockchain.NodeTime;
import io.proximax.sdk.model.blockchain.Receipts;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.utils.GsonUtils;
import io.proximax.sdk.utils.dto.BlockchainScoreDTOUtils;
import io.proximax.sdk.utils.dto.UInt64Utils;
import io.reactivex.Observable;

/**
 * Blockchain http repository.
 *
 * @since 1.0
 */
public class BlockchainHttp extends Http implements BlockchainRepository {
   private static final String BLOCK = "/block/";
   private static final String CHAIN_HEIGHT = "/chain/height";
   private static final String CHAIN_SCORE = "/chain/score";
   
   public BlockchainHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<BlockInfo> getBlockByHeight(BigInteger height) {
        return this.client
                        .get(BLOCK + height.toString())
                        .map(Http::mapStringOrError)
                        .map(str -> objectMapper.readValue(str, BlockInfoDTO.class))
                        .map(blockInfoDTO -> BlockInfo.fromDto(blockInfoDTO, api.getNetworkType()));
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
                .get(BLOCK + height + "/transactions" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
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
                .get(CHAIN_HEIGHT)
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, HeightInfoDTO.class))
                .map(blockchainHeight -> UInt64Utils.toBigInt(blockchainHeight.getHeight()));
    }

    public Observable<BigInteger> getBlockchainScore() {
        return this.client
                .get(CHAIN_SCORE)
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, BlockchainScoreDTO.class))
                .map(BlockchainScoreDTOUtils::toBigInt);
    }

    @Override
    public Observable<BlockchainStorageInfo> getBlockchainStorage() {
        return this.client
                .get("/diagnostic/storage")
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, StorageInfoDTO.class))
                .map(storageInfo -> new BlockchainStorageInfo(storageInfo.getNumAccounts(),
                        storageInfo.getNumBlocks(),
                        storageInfo.getNumBlocks()));
    }

   @Override
   public Observable<NodeInfo> getNodeInfo() {
      return this.client.get("/node/info")
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, NodeInfoDTO.class))
            .map(NodeInfo::fromDto);
   }

   @Override
   public Observable<NodeTime> getNodeTime() {
      return this.client.get("/node/time")
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, NodeTimeDTO.class))
            .map(NodeTime::fromDto);
   }
   
   @Override
   public Observable<NetworkType> getNetworkType() {
      return this.client
              .get("/network")
              .map(Http::mapStringOrError)
              .map(GsonUtils::mapToJsonObject)
              .map(obj -> obj.get("name").getAsString())
              .map(name -> {
                  if (name.equalsIgnoreCase("mijinTest"))
                      return NetworkType.MIJIN_TEST;
                  else if (name.equalsIgnoreCase("mijin"))
                      return NetworkType.MIJIN;
                  else if (name.equalsIgnoreCase("publicTest"))
                      return NetworkType.TEST_NET;
                  else if (name.equalsIgnoreCase("public"))
                      return NetworkType.MAIN_NET;
                  else if (name.equalsIgnoreCase("privateTest"))
                      return NetworkType.PRIVATE_TEST;
                  else if (name.equalsIgnoreCase("private"))
                      return NetworkType.PRIVATE;
                  else {
                      throw new IllegalArgumentException("Network " + name + " is not supported by the sdk");
                  }
              });
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
            .map(str -> objectMapper.readValue(str, MerkleProofInfoDTO.class))
            .map(MerkleProofInfoDTO::getPayload)
            .map(MerklePath::fromDto);
   }
   
   @Override
   public Observable<MerklePath> getTransactionMerklePath(BigInteger height, String trsansactionHash) {
      return this.client.get(BLOCK + height.toString() + "/transaction/" + trsansactionHash + "/merkle")
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, MerkleProofInfoDTO.class))
            .map(MerkleProofInfoDTO::getPayload)
            .map(MerklePath::fromDto);
   }

   @Override
   public Observable<List<BlockInfo>> getBlocksByHeightWithLimit(BigInteger height, BlocksLimit limit) {
      return this.client.get("/blocks/" + height.toString() + "/limit/" + limit.getLimit())
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<BlockInfoDTO>>readValue(str, new TypeReference<List<BlockInfoDTO>>() {}))
            .flatMapIterable(item -> item)
            .map(blockInfoDTO -> BlockInfo.fromDto(blockInfoDTO, api.getNetworkType()))
            .toList().toObservable();
   }
}
