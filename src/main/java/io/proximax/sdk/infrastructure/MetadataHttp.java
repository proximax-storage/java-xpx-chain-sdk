/*
 * Copyright 2019 ProximaX
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

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.MetadataRepository;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.metadata.AddressMetadata;
import io.proximax.sdk.model.metadata.Metadata;
import io.proximax.sdk.model.metadata.MetadataMapper;
import io.proximax.sdk.model.metadata.MosaicMetadata;
import io.proximax.sdk.model.metadata.NamespaceMetadata;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

/**
 * Metadata http repository.
 */
public class MetadataHttp extends Http implements MetadataRepository {
   private static final String URL_METADATA = "/metadata/";
   private static final String URL_ACCOUNT = "/account/";
   private static final String URL_MOSAIC = "/mosaic/";
   private static final String URL_NAMESPACE = "/namespace/";
   
   private static final String URL_SUFFIX_METADATA = "/metadata";

   public MetadataHttp(BlockchainApi api) {
      super(api);
   }

   @Override
   public Observable<Metadata> getMetadata(String metadataId) {
      return this.client
         .get(URL_METADATA + metadataId)
         .map(Http::mapStringOrError)
         .map(GsonUtils::mapToJsonObject)
         .map(MetadataMapper::mapToObject);
   }

   @Override
   public Observable<Metadata> getMetadata(UInt64Id metadataId) {
      return getMetadata(metadataId.getIdAsHex());
   }

   @Override
   public Observable<Metadata> getMetadata(Address address) {
      return getMetadata(AddressMetadata.getIdFromAddress(address));
   }

   @Override
   public Observable<Metadata> getMetadata(List<String> metadataIds) {
      JsonObject requestBody = new JsonObject();
      requestBody.add("metadataIds", getJsonArray(metadataIds));
      return this.client
            .post(URL_METADATA, requestBody)
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonArray)
            .flatMapIterable(item -> item)
            .map(JsonElement::getAsJsonObject)
            .map(MetadataMapper::mapToObject);
   }

   @Override
   public Observable<AddressMetadata> getMetadataFromAddress(Address address) {
      return this.client
            .get(URL_ACCOUNT + AddressMetadata.getIdFromAddress(address) + URL_SUFFIX_METADATA)
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonObject)
            .map(MetadataMapper::mapToObject)
            .map(meta -> (AddressMetadata)meta);
      }

   @Override
   public Observable<MosaicMetadata> getMetadataFromMosaic(MosaicId mosaicId) {
      return this.client
            .get(URL_MOSAIC + mosaicId.getIdAsHex() + URL_SUFFIX_METADATA)
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonObject)
            .map(MetadataMapper::mapToObject)
            .map(meta -> (MosaicMetadata)meta);
      }

   @Override
   public Observable<NamespaceMetadata> getMetadataFromNamespace(NamespaceId namespaceId) {
      return this.client
            .get(URL_NAMESPACE + namespaceId.getIdAsHex() + URL_SUFFIX_METADATA)
            .map(Http::mapStringOrError)
            .map(GsonUtils::mapToJsonObject)
            .map(MetadataMapper::mapToObject)
            .map(meta -> (NamespaceMetadata)meta);
      }
}
