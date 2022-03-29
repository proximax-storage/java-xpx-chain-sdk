package io.proximax.sdk.infrastructure;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.NetworkRepository;
import io.proximax.sdk.model.network.*;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

public class NetworkHttp extends Http implements NetworkRepository
{
    public NetworkHttp(BlockchainApi api) {
        super(api);
    }
    @Override
    public Observable<NetworkType> getNetworkType() {
        return this.client
                .get("/network")
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(obj -> obj.get("name").getAsString())
                .map(NetworkType::getByName);
    }   
}
