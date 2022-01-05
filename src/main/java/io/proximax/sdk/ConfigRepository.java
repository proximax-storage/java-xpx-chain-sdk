package io.proximax.sdk;

import io.proximax.sdk.model.config.BlockchainConfig;
import io.reactivex.Observable;
import java.math.BigInteger;

public interface ConfigRepository {
    /**
     * retrieve network configuration
     * 
     * @param height the block height at which retrieved configuration was/is valid
     * @return network configuration at give height
     */
    Observable<BlockchainConfig> getBlockchainConfiguration(BigInteger height);

}
