package io.proximax.sdk.infrastructure;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.ExchangeRepository;
import io.proximax.sdk.gen.model.AccountExchangeDTO;
import io.proximax.sdk.gen.model.ExchangesDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.exchange.AccountExchanges;
import io.proximax.sdk.model.exchange.ExchangeMosaicsList;
import io.proximax.sdk.model.exchange.MosaicExchange;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

public class ExchangeHttp extends Http implements ExchangeRepository {
    private static final String exchange = "/exchange";
    private static final String mosaics = "/mosaics";
    private static final String account = "/account";
    private static final Type MOSAIC_EXCHANGE_LIST_TYPE = new TypeToken<List<ExchangesDTO>>() {
    }.getType();

    public ExchangeHttp(BlockchainApi api) {
        super(api);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Observable<AccountExchanges> getAccountExchanges(Address address) {
        return this.client
                .get(account + SLASH + address.plain() + exchange)
                .map(Http::mapStringOrError)
                .map(str -> gson.fromJson(str,
                        AccountExchangeDTO.class))
                .map(dto -> AccountExchanges.fromDto(dto, api.getNetworkType()));
    }

    @Override
    public Observable<List<MosaicExchange>> getExchangeOffers(String offerType, MosaicId mosaicId) {
        return this.client
                .get(exchange + SLASH + offerType + SLASH + mosaicId.getIdAsHex())
                .map(Http::mapStringOrError)
                .map(this::toMosaicExchangeList)
                .flatMapIterable(item -> item)
                .map(dto -> MosaicExchange.fromDto(dto, api.getNetworkType())).toList().toObservable();
    }

    @Override
    public Observable<List<ExchangeMosaicsList>> getOfferList() {
        return this.client
                .get(exchange + mosaics)
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonArray)
                .map(ExchangeMosaicsList::fromJson);
    }

    /**
     * allow use of gson list deserialization in stream
     *
     * @param json json string representing list
     * @return list of block info DTOs
     */
    private List<ExchangesDTO> toMosaicExchangeList(String json) {
        return gson.fromJson(json, MOSAIC_EXCHANGE_LIST_TYPE);
    }

}
