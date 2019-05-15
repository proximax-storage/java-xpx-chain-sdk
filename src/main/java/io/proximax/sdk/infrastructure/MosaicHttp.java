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

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.proximax.sdk.gen.model.MosaicInfoDTO;
import io.proximax.sdk.gen.model.MosaicNamesDTO;
import io.proximax.sdk.gen.model.MosaicPropertiesDTO;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicNames;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/**
 * Mosaic http repository.
 *
 * @since 1.0
 */
public class MosaicHttp extends Http implements MosaicRepository {

    public MosaicHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
    }

    public MosaicHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<MosaicInfo> getMosaic(UInt64Id mosaicId) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/mosaic/" + mosaicId.getIdAsHex())
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), MosaicInfoDTO.class))
                        .map(mosaicInfoDTO -> new MosaicInfo(
                                mosaicInfoDTO.getMeta().getId(),
                                new MosaicId(toBigInt(mosaicInfoDTO.getMosaic().getMosaicId())),
                                toBigInt(mosaicInfoDTO.getMosaic().getSupply()),
                                toBigInt(mosaicInfoDTO.getMosaic().getHeight()),
                                new PublicAccount(mosaicInfoDTO.getMosaic().getOwner(), networkType),
                                extractMosaicProperties(mosaicInfoDTO.getMosaic().getProperties())
                        )));
    }

    @Override
    public Observable<List<MosaicInfo>> getMosaics(List<UInt64Id> mosaicIds) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("mosaicIds", mosaicIds.stream().map(UInt64Id::getIdAsHex).collect(Collectors.toList()));
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .postAbs(this.url + "/mosaic")
                        .as(BodyCodec.jsonArray())
                        .rxSendJson(requestBody)
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<MosaicInfoDTO>>readValue(json.toString(), new TypeReference<List<MosaicInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(mosaicInfoDTO -> new MosaicInfo(
                                mosaicInfoDTO.getMeta().getId(),
                                new MosaicId(toBigInt(mosaicInfoDTO.getMosaic().getMosaicId())),
                                toBigInt(mosaicInfoDTO.getMosaic().getSupply()),
                                toBigInt(mosaicInfoDTO.getMosaic().getHeight()),
                                new PublicAccount(mosaicInfoDTO.getMosaic().getOwner(), networkType),
                                extractMosaicProperties(mosaicInfoDTO.getMosaic().getProperties())
                        ))
                        .toList()
                        .toObservable());
    }

    @Override
    public Observable<List<MosaicNames>> getMosaicNames(List<UInt64Id> mosaicIds) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("mosaicIds", mosaicIds.stream().map(UInt64Id::getIdAsHex).collect(Collectors.toList()));
        return this.client
                .postAbs(this.url + "/mosaic/names")
                .as(BodyCodec.jsonArray())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> {
                   System.out.println(json);
                   return objectMapper.<List<MosaicNamesDTO>>readValue(json.toString(), new TypeReference<List<MosaicNamesDTO>>() {});
                })
                .flatMapIterable(item -> item)
                .map(mosaicNameDTO -> new MosaicNames(new MosaicId(toBigInt(mosaicNameDTO.getMosaicId())),
                        mosaicNameDTO.getNames()))
                .toList()
                .toObservable();
    }

    private MosaicProperties extractMosaicProperties(MosaicPropertiesDTO mosaicPropertiesDTO) {
        String flags = "00" + Integer.toBinaryString(toBigInt(mosaicPropertiesDTO.get(0)).intValue());
        String bitMapFlags = flags.substring(flags.length() - 3, flags.length());
        return new MosaicProperties(bitMapFlags.charAt(2) == '1',
                bitMapFlags.charAt(1) == '1',
                bitMapFlags.charAt(0) == '1',
                toBigInt(mosaicPropertiesDTO.get(1)).intValue(),
                toBigInt(mosaicPropertiesDTO.get(2)));
    }
}