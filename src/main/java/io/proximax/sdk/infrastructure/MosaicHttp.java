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

import static io.proximax.sdk.utils.GsonUtils.getJsonArray;
import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.MosaicRepository;
import io.proximax.sdk.gen.model.MosaicInfoDTO;
import io.proximax.sdk.gen.model.MosaicNamesDTO;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicNames;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;


/**
 * Mosaic HTTP repository.
 */
public class MosaicHttp extends Http implements MosaicRepository {

   private static final String ROUTE = "/mosaic/";
   private static final String NAMES_ROUTE = "/mosaic/names";

   public MosaicHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<MosaicInfo> getMosaic(MosaicId mosaicId) {
      return this.client.get(ROUTE + mosaicId.getIdAsHex())
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.readValue(str, MosaicInfoDTO.class))
            .map(dto -> MosaicInfo.fromDto(dto, api.getNetworkType()));
   }

   @Override
   public Observable<List<MosaicInfo>> getMosaics(List<MosaicId> mosaicIds) {
      JsonObject requestBody = new JsonObject();
      requestBody.add("mosaicIds", getJsonArray(mosaicIds, UInt64Id::getIdAsHex));
      return this.client.post(ROUTE, requestBody)
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<MosaicInfoDTO>>readValue(str, new TypeReference<List<MosaicInfoDTO>>() { }))
            .flatMapIterable(item -> item)
            .map(dto -> MosaicInfo.fromDto(dto, api.getNetworkType()))
            .toList().toObservable();
   }

   @Override
   public Observable<List<MosaicNames>> getMosaicNames(List<MosaicId> mosaicIds) {
      JsonObject requestBody = new JsonObject();
      requestBody.add("mosaicIds", getJsonArray(mosaicIds, UInt64Id::getIdAsHex));
      return this.client.post(NAMES_ROUTE, requestBody)
            .map(Http::mapStringOrError)
            .map(str -> objectMapper.<List<MosaicNamesDTO>>readValue(str, new TypeReference<List<MosaicNamesDTO>>() { }))
            .flatMapIterable(item -> item)
            .map(mosaicNameDTO -> new MosaicNames(new MosaicId(toBigInt(mosaicNameDTO.getMosaicId())),
                  mosaicNameDTO.getNames()))
            .toList().toObservable();
   }
}