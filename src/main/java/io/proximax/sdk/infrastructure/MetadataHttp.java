/*
 * Copyright 2022 ProximaX
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
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.MetadataRepository;
import io.proximax.sdk.gen.model.MetadataInfoDTO;
import io.proximax.sdk.model.metadata.MetadataEntry;
import io.proximax.sdk.model.metadata.MetadataSearch;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

/**
 * Metadata http repository.
 */
public class MetadataHttp extends Http implements MetadataRepository {
   private static final String URL_METADATA = "/metadata_v2";
   private static final Type METADATA_V2_INFO = new TypeToken<List<MetadataInfoDTO>>() {
   }.getType();

   public MetadataHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<MetadataEntry> getMetadata(String compositeHash) {
      return this.client
            .get(URL_METADATA + SLASH + compositeHash)
            .map(Http::mapStringOrError)
            .map(str -> gson.fromJson(str,
                  MetadataInfoDTO.class))
            .map(dto -> MetadataEntry.fromDto(dto));
   }

   @Override
   public Observable<List<MetadataEntry>> getMetadatas(List<String> compositeHashes) {
      JsonObject requestBody = new JsonObject();
      requestBody.add("compositeHashes", getJsonArray(compositeHashes));
      return this.client
            .post(URL_METADATA, requestBody)
            .map(Http::mapStringOrError)
            .map(this::toMetadataV2Info)
            .flatMapIterable(item -> item)
            .map(dto -> MetadataEntry.fromDto(dto))
            .toList().toObservable();
   }

   @Override
   public Observable<MetadataSearch> MetadataEntrySearch(MetadataQueryParams queryParams) {
      return this.MetadataEntrySearch(Optional.of(queryParams));
   }

   private Observable<MetadataSearch> MetadataEntrySearch(Optional<MetadataQueryParams> queryParams) {
      return this.client.get(URL_METADATA + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonObject)
            .map(MetadataSearch::fromJson);

   }

   /**
    * allow use of gson list deserialization in stream
    * 
    * @param json json string representing list
    * @return list of metadata entry DTOs
    */
   private List<MetadataInfoDTO> toMetadataV2Info(String json) {
      return gson.fromJson(json, METADATA_V2_INFO);
   }
}
