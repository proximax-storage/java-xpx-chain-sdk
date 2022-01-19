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

package io.proximax.sdk;

import java.util.List;

import io.proximax.sdk.infrastructure.MetadataQueryParams;
import io.proximax.sdk.model.metadata.MetadataEntry;
import io.proximax.sdk.model.metadata.MetadataSearch;

import io.reactivex.Observable;

/**
 * Metadata interface repository.
 */
public interface MetadataRepository {

   /**
    * Gets the metadata (mosaic or namespace) for a given metadataId
    * 
    * @param compositeHash compositeHash of the metadata
    * @return MetadataEntry information assigned to the compositeHash
    */
   Observable<MetadataEntry> getMetadata(String compositeHash);
   
   /**
    * Get metadatas(namespace/mosaic/account) for a list of metadata IDs
    * 
    * @param compositeHashes List of compositeHashes represented as String values.
    * @return observable of MetadataEntry instances
    */
   Observable<List<MetadataEntry>> getMetadatas(List<String> compositeHashes);
   
   /**
    * Gets an list of Metadata with pagination.
    *
    * @param queryParams   QueryParams
    * @return Observable of {@link MetadataSearch} list
    */
   Observable<MetadataSearch> MetadataEntrySearch(MetadataQueryParams queryParams);
}
