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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.MosaicRepository;
import io.proximax.sdk.gen.model.MosaicInfoDTO;
import io.proximax.sdk.gen.model.MosaicLevyInfoDTO;
import io.proximax.sdk.gen.model.MosaicNamesDTO;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicLevyInfo;
import io.proximax.sdk.model.mosaic.MosaicNames;
import io.proximax.sdk.model.mosaic.MosaicRichList;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.proximax.sdk.utils.GsonUtils;
import io.proximax.sdk.utils.dto.UInt64Utils;
import io.reactivex.Observable;

/**
 * Mosaic HTTP repository.
 */
public class MosaicHttp extends Http implements MosaicRepository {

   private static final String ROUTE = "/mosaic";
   private static final String NAMES_ROUTE = "/mosaic/names";
   private static final Type MOSAIC_INFO_LIST_TYPE = new TypeToken<List<MosaicInfoDTO>>() {
   }.getType();
   private static final Type MOSAIC_NAMES_LIST_TYPE = new TypeToken<List<MosaicNamesDTO>>() {
   }.getType();

   public MosaicHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<MosaicInfo> getMosaic(MosaicId mosaicId) {
      return this.client.get(ROUTE + SLASH + mosaicId.getIdAsHex())
            .map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, MosaicInfoDTO.class))
            .map(dto -> MosaicInfo.fromDto(dto, api.getNetworkType()));
   }

   @Override
   public Observable<List<MosaicInfo>> getMosaics(List<MosaicId> mosaicIds) {
      JsonObject requestBody = new JsonObject();
      requestBody.add("mosaicIds", getJsonArray(mosaicIds, UInt64Id::getIdAsHex));
      return this.client.post(ROUTE, requestBody)
            .map(Http::mapStringOrError)
            .map(this::toMosaicInfoList)
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
            .map(this::toMosaicNamesList)
            .flatMapIterable(item -> item)
            .map(mosaicNameDTO -> new MosaicNames(new MosaicId(UInt64Utils.toBigInt(new ArrayList<>(mosaicNameDTO.getMosaicId()))),
                  mosaicNameDTO.getNames()))
            .toList().toObservable();
   }

   @Override
   public Observable<List<MosaicRichList>> getMosaicRichList(MosaicId mosaicId) {
      return this.getMosaicRichList(mosaicId, Optional.empty());
   }

   @Override
   public Observable<List<MosaicRichList>> getMosaicRichList(MosaicId mosaicId, QueryParams queryParams) {
      return this.getMosaicRichList(mosaicId, Optional.of(queryParams));
   }

   private Observable<List<MosaicRichList>> getMosaicRichList(MosaicId mosaicId, Optional<QueryParams> queryParams) {

      return this.client.get(ROUTE + SLASH + mosaicId.getIdAsHex() + SLASH + "richlist")
      .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonArray)
            .map(MosaicRichList::fromJson);

   }

   @Override
   public Observable<MosaicLevyInfo> getMosaicLevyInfo(MosaicId mosaicId) {

      return this.client.get(ROUTE + SLASH + mosaicId.getIdAsHex() + SLASH + "levy").map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str, MosaicLevyInfoDTO.class))
            .map(dto -> MosaicLevyInfo.fromDto(dto, api.getNetworkType()));
   }


   private List<MosaicInfoDTO> toMosaicInfoList(String json) {
      return gson.fromJson(json, MOSAIC_INFO_LIST_TYPE);
   }

   private List<MosaicNamesDTO> toMosaicNamesList(String json) {
      return gson.fromJson(json, MOSAIC_NAMES_LIST_TYPE);
   }
}