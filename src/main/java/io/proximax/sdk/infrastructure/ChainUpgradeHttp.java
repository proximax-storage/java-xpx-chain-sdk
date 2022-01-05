package io.proximax.sdk.infrastructure;

import java.math.BigInteger;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ChainUpgradeRepository;
import io.proximax.sdk.gen.model.BlockchainUpgradeDTO;
import io.proximax.sdk.model.chainUpgrade.BlockchainUpgrade;
import io.reactivex.Observable;

public class ChainUpgradeHttp extends Http implements ChainUpgradeRepository{
    private static final String UPGRADE = "/upgrade";

    public ChainUpgradeHttp(BlockchainApi api) {
        super(api);
    }
    
    @Override
    public Observable<BlockchainUpgrade> getBlockchainUpgrade(BigInteger height) {
        return this.client.get(UPGRADE + SLASH + height.toString())
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, BlockchainUpgradeDTO.class))
                .map(BlockchainUpgradeDTO::getBlockchainUpgrade)
                .map(BlockchainUpgrade::fromDto);
    }
}
