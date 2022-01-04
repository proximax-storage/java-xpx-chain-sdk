package io.proximax.sdk.infrastructure;

import java.math.BigInteger;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ChainRepository;
import io.proximax.sdk.gen.model.BlockchainScoreDTO;
import io.proximax.sdk.gen.model.HeightInfoDTO;
import io.proximax.sdk.utils.dto.BlockchainScoreDTOUtils;
import io.proximax.sdk.utils.dto.UInt64Utils;
import io.reactivex.Observable;

public class ChainHttp extends Http implements ChainRepository{
    private static final String CHAIN_HEIGHT = "/chain/height";
    private static final String CHAIN_SCORE = "/chain/score";

    public ChainHttp(BlockchainApi api) {
        super(api);
    }
    
    @Override
    public Observable<BigInteger> getBlockchainHeight() {
        return this.client
                .get(CHAIN_HEIGHT)
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, HeightInfoDTO.class))
                .map(blockchainHeight -> UInt64Utils.toBigInt(blockchainHeight.getHeight()));
    }

    public Observable<BigInteger> getBlockchainScore() {
        return this.client
                .get(CHAIN_SCORE)
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, BlockchainScoreDTO.class))
                .map(BlockchainScoreDTOUtils::toBigInt);
    }
}
