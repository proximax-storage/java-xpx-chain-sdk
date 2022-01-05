package io.proximax.sdk.infrastructure;

import java.math.BigInteger;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ConfigRepository;
import io.proximax.sdk.gen.model.NetworkConfigDTO;
import io.proximax.sdk.model.config.BlockchainConfig;
import io.reactivex.Observable;

public class ConfigHttp extends Http implements ConfigRepository {
    
    private static final String CONFIG = "/config";

    public ConfigHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<BlockchainConfig> getBlockchainConfiguration(BigInteger height) {
        return this.client.get(CONFIG + SLASH + height.toString())
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str, NetworkConfigDTO.class))
                .map(NetworkConfigDTO::getNetworkConfig)
                .map(BlockchainConfig::fromDto);
    }
}
