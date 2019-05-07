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
package io.proximax.sdk.model.metadata;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.infrastructure.model.UInt64DTO;
import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Mapper for metadata types
 */
public class MetadataMapper {

   private static final String META_KEY_METADATA = "metadata";
   private static final String META_KEY_TYPE = "metadataType";
   private static final String META_KEY_FIELDS = "fields";
   private static final String META_KEY_ID = "metadataId";
   private static final String META_KEY_FIELD_KEY = "key";
   private static final String META_KEY_FIELD_VALUE = "value";

   /**
    * hiding constructor of utility class
    */
   private MetadataMapper() {
      // utility class
   }
   
   /**
    * map JSON object to metadata instance
    * 
    * @param json object representing the metadata
    * @return metadata instance
    */
   public static Metadata mapToObject(JsonObject json) {
      JsonObject meta = json.getJsonObject(META_KEY_METADATA);
      MetadataType type = MetadataType.getByCode(meta.getInteger(META_KEY_TYPE));
      List<Field> fields = loadFields(meta.getJsonArray(META_KEY_FIELDS));
      switch (type) {
      case NONE:
         return getNoneMetadata(fields);
      case ADDRESS:
         return getAddressMetadata(fields, meta);
      case MOSAIC:
         return getMosaicMetadata(fields, meta);
      case NAMESPACE:
         return getNamespaceMetadata(fields, meta);
      default:
         throw new UnsupportedOperationException("Unhandled metadata type " + type);
      }
   }

   /**
    * none metadata factory
    * 
    * @param fields metadata fields
    * @return the metadata instance
    */
   private static Metadata getNoneMetadata(List<Field> fields) {
      return new Metadata(MetadataType.NONE, fields);
   }
   
   /**
    * address metadata factory
    * 
    * @param fields metadata fields
    * @return the metadata instance
    */
   private static Metadata getAddressMetadata(List<Field> fields, JsonObject json) {
      return new AddressMetadata(fields, json.getString(META_KEY_ID));
   }
   
   /**
    * mosaic metadata factory
    * 
    * @param fields metadata fields
    * @return the metadata instance
    */
   private static Metadata getMosaicMetadata(List<Field> fields, JsonObject json) {
      return new MosaicMetadata(fields, new MosaicId(extractBigInteger(json.getJsonArray(META_KEY_ID))));
   }
   
   /**
    * namespace metadata factory
    * 
    * @param fields metadata fields
    * @return the metadata instance
    */
   private static Metadata getNamespaceMetadata(List<Field> fields, JsonObject json) {
      return new NamespaceMetadata(fields, new MosaicId(extractBigInteger(json.getJsonArray(META_KEY_ID))));
   }
   
   /**
    * take JSON array encoding Uint64 and convert it to BigInteger
    * 
    * @param json array of unsigned integers
    * @return uint64 value
    */
   private static BigInteger extractBigInteger(JsonArray json) {
      UInt64DTO uint = new UInt64DTO();
      uint.add(json.getLong(0));
      uint.add(json.getLong(1));
      return UInt64Utils.toBigInt(uint);
   }
   
   /**
    * load fields from JSON array to list
    * 
    * @param jsonFields JSON array with key-value pairs
    * @return List of fields
    */
   private static List<Field> loadFields(JsonArray jsonFields) {
      return jsonFields.stream()
         .map( obj -> (JsonObject)obj)
         .map(json -> new Field(json.getString(META_KEY_FIELD_KEY), json.getString(META_KEY_FIELD_VALUE)))
         .collect(Collectors.toList());
   }
}
