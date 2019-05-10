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

import java.util.List;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.metadata.AddressMetadata;
import io.proximax.sdk.model.metadata.Metadata;
import io.proximax.sdk.model.metadata.MosaicMetadata;
import io.proximax.sdk.model.metadata.NamespaceMetadata;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;

/**
 * Metadata interface repository.
 */
public interface MetadataRepository {

   /**
    * Gets the metadata (mosaic or namespace) for a given metadataId
    * 
    * @param metadataId id of the metadata
    * @return metadata information assigned to the id
    */
   Observable<Metadata> getMetadata(UInt64Id metadataId);

   /**
    * Gets the metadata (address) for a given address
    * 
    * @param address of the account
    * @return metadata information assigned to the id
    */
   Observable<Metadata> getMetadata(Address address);
   
   /**
    * Gets the metadata(AccountMetadataInfo, MosaicMetadataInfo or NamespaceMetadataInfo) for a given metadataId
    * 
    * @param metadataId generic string representing the metadata ID
    * @return metadata instance
    */
   Observable<Metadata> getMetadata(String metadataId);
   
   /**
    * Get metadatas(namespace/mosaic/account) for a list of metadata IDs
    * 
    * @param metadataIds List of metadata IDs represented as String values. For Mosaic and Namespace this is
    *    hex-encoded uint64, for Address it is the plain representation of address
    * @return observable of metadata instances
    */
   Observable<Metadata> getMetadata(List<String> metadataIds);
   
   /**
    * Get address metadata
    * 
    * @param address address to retrieve metadata from
    * @return address metadata
    */
   Observable<AddressMetadata> getMetadataFromAddress(Address address);
   
   /**
    * Get mosaic metadata
    * 
    * @param mosaicId id of the mosaic
    * @return mosaic metadata
    */
   Observable<MosaicMetadata> getMetadataFromMosaic(MosaicId mosaicId);
   
   /**
    * Get namespace metadata
    * 
    * @param namespaceId id of the namespace
    * @return namespace metadata
    */
   Observable<NamespaceMetadata> getMetadataFromNamespace(NamespaceId namespaceId);
}
