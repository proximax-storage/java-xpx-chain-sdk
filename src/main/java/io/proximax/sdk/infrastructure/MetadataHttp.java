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

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.model.metadata.AddressMetadata;
import io.proximax.sdk.model.metadata.Metadata;
import io.proximax.sdk.model.metadata.MetadataMapper;
import io.proximax.sdk.model.metadata.MosaicMetadata;
import io.proximax.sdk.model.metadata.NamespaceMetadata;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/**
 * Metadata http repository.
 */
public class MetadataHttp extends Http implements MetadataRepository {
   private static final String URL_METADATA = "/metadata/";
   private static final String URL_ACCOUNT = "/account/";
   private static final String URL_MOSAIC = "/mosaic/";
   private static final String URL_NAMESPACE = "/namespace/";
   
   private static final String URL_SUFFIX_METADATA = "/metadata";

   public MetadataHttp(String host) throws MalformedURLException {
      this(host, new NetworkHttp(host));
   }

   public MetadataHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
      super(host, networkHttp);
   }

   @Override
   public Observable<Metadata> getMetadata(String metadataId) {
      return this.client
         .getAbs(this.url + URL_METADATA + metadataId)
         .as(BodyCodec.jsonObject())
         .rxSend()
         .toObservable()
         .map(Http::mapJsonObjectOrError)
         .map(MetadataMapper::mapToObject);
   }

   @Override
   public Observable<Metadata> getMetadata(UInt64Id metadataId) {
      return getMetadata(metadataId.getIdAsHex());
   }

   @Override
   public Observable<Metadata> getMetadata(List<String> metadataIds) {
      JsonObject requestBody = new JsonObject();
      requestBody.put("metadataIds", metadataIds);
      return this.client
            .postAbs(this.url + URL_METADATA)
            .as(BodyCodec.jsonArray())
            .rxSendJson(requestBody)
            .toObservable()
            .map(Http::mapJsonArrayOrError)
            .map(json -> new JsonArray(json.toString()).stream().map(s -> (JsonObject) s).collect(Collectors.toList()))
            .flatMapIterable(item -> item)
            .map(MetadataMapper::mapToObject);
   }

   @Override
   public Observable<AddressMetadata> getMetadataFromAddress(String address) {
      return this.client
            .getAbs(this.url + URL_ACCOUNT + address + URL_SUFFIX_METADATA)
            .as(BodyCodec.jsonObject())
            .rxSend()
            .toObservable()
            .map(Http::mapJsonObjectOrError)
            .map(MetadataMapper::mapToObject)
            .map(meta -> (AddressMetadata)meta);
      }

   @Override
   public Observable<MosaicMetadata> getMetadataFromMosaic(MosaicId mosaicId) {
      return this.client
            .getAbs(this.url + URL_MOSAIC + mosaicId.getIdAsHex() + URL_SUFFIX_METADATA)
            .as(BodyCodec.jsonObject())
            .rxSend()
            .toObservable()
            .map(Http::mapJsonObjectOrError)
            .map(MetadataMapper::mapToObject)
            .map(meta -> (MosaicMetadata)meta);
      }

   @Override
   public Observable<NamespaceMetadata> getMetadataFromNamespace(NamespaceId namespaceId) {
      return this.client
            .getAbs(this.url + URL_NAMESPACE + namespaceId.getIdAsHex() + URL_SUFFIX_METADATA)
            .as(BodyCodec.jsonObject())
            .rxSend()
            .toObservable()
            .map(Http::mapJsonObjectOrError)
            .map(MetadataMapper::mapToObject)
            .map(meta -> (NamespaceMetadata)meta);
      }
}
