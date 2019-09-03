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

package io.proximax.sdk.model.namespace;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.proximax.sdk.gen.model.AliasDTO;
import io.proximax.sdk.gen.model.NamespaceInfoDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;

/**
 * NamespaceInfo contains the state information of a namespace.
 *
 * @since 1.0
 */
public class NamespaceInfo {
   private final boolean active;
   private final Integer index;
   private final String metaId;
   private final NamespaceType type;
   private final Integer depth;
   private final List<NamespaceId> levels;
   private final NamespaceId parentId;
   private final PublicAccount owner;
   private final BigInteger startHeight;
   private final BigInteger endHeight;
   private final Optional<Address> addressAlias;
   private final Optional<MosaicId> mosaicAlias;

   public NamespaceInfo(boolean active, Integer index, String metaId, NamespaceType type, Integer depth,
         List<NamespaceId> levels, NamespaceId parentId, PublicAccount owner, BigInteger startHeight,
         BigInteger endHeight,Optional<Address> addressAlias, Optional<MosaicId> mosaicAlias) {
      this.active = active;
      this.index = index;
      this.metaId = metaId;
      this.type = type;
      this.depth = depth;
      this.levels = levels;
      this.parentId = parentId;
      this.owner = owner;
      this.startHeight = startHeight;
      this.endHeight = endHeight;
      this.addressAlias = addressAlias;
      this.mosaicAlias = mosaicAlias;
   }

   /**
    * Returns true if namespace is active
    *
    * @return true if the namespace is active
    */
   public boolean isActive() {
      return active;
   }

   /**
    * Returns the namespace expiration status
    *
    * @return true if namespace is expired
    */
   public boolean isExpired() {
      return !active;
   }

   public Integer getIndex() {
      return index;
   }

   public String getMetaId() {
      return metaId;
   }

   /**
    * Returns the namespace type
    *
    * @return the namespace type
    */
   public NamespaceType getType() {
      return type;
   }

   /**
    * Returns the namespace level depth
    *
    * @return the namespace level depth
    */
   public Integer getDepth() {
      return depth;
   }

   /**
    * Returns the different NamespaceIds per level
    *
    * @return the different Namespace IDs per level
    */
   public List<NamespaceId> getLevels() {
      return levels;
   }

   /**
    * Returns the mosaic owner account
    *
    * @return mosaic owner account
    */
   public PublicAccount getOwner() {
      return owner;
   }

   /**
    * Returns the block height the namespace was registered
    *
    * @return the block height the namespace was registered
    */
   public BigInteger getStartHeight() {
      return startHeight;
   }

   /**
    * Returns the block height the namespace expires if not renewed
    *
    * @return the block height the namespace expires
    */
   public BigInteger getEndHeight() {
      return endHeight;
   }

   /**
    * @return the addressAlias
    */
   public Optional<Address> getAddressAlias() {
      return addressAlias;
   }

   /**
    * @return the mosaicAlias
    */
   public Optional<MosaicId> getMosaicAlias() {
      return mosaicAlias;
   }

   /**
    * Returns the NamespaceId
    *
    * @return namespace id
    */
   public NamespaceId getId() {
      return this.levels.get(this.levels.size() - 1);
   }

   /**
    * Returns true if namespace is Root
    *
    * @return true if namespace is Root
    */
   public boolean isRoot() {
      return this.type == NamespaceType.ROOT_NAMESPACE;
   }

   /**
    * Returns true if namespace is Subnamespace
    *
    * @return true if namespace is Subnamespace
    */
   public boolean isSubnamespace() {
      return this.type == NamespaceType.SUB_NAMESPACE;
   }

   /**
    * Returns the Parent Namespace Id
    *
    * @return the Parent Namespace Id
    * @throws Error if it is not a SubNamespace
    */
   public NamespaceId parentNamespaceId() {
      if (this.isRoot()) {
         throw new Error("Is A Root Namespace");
      }
      return this.parentId;
   }

   /**
    * create namespace info from the DTO
    * 
    * @param dto namespace info DTO
    * @param networkType network type
    * @return the namespace info
    */
   public static NamespaceInfo fromDto(NamespaceInfoDTO dto, NetworkType networkType) {
      Optional<Address> addressAlias = Optional.empty();
      Optional<MosaicId> mosaicAlias = Optional.empty();
      AliasDTO alias = dto.getNamespace().getAlias();
      // check whether we have alias
      if (alias != null) {
         // see address reference
         String address = alias.getAddress();
         if (address != null) {
            addressAlias = Optional.ofNullable(Address.createFromEncoded(address));
         }
         // see mosaic reference
         UInt64DTO mosaicId = alias.getMosaicId();
         if (mosaicId != null && mosaicId.size() == 2) {
            mosaicAlias = Optional.of(new MosaicId(toBigInt(mosaicId)));
         }
      }
      // create new instance
      return new NamespaceInfo(dto.getMeta().getActive(), dto.getMeta().getIndex(),
            dto.getMeta().getId(),
            NamespaceType.rawValueOf(dto.getNamespace().getType().getValue()),
            dto.getNamespace().getDepth(), extractLevels(dto),
            new NamespaceId(toBigInt(dto.getNamespace().getParentId())),
            new PublicAccount(dto.getNamespace().getOwner(), networkType),
            toBigInt(dto.getNamespace().getStartHeight()),
            toBigInt(dto.getNamespace().getEndHeight()),
            addressAlias,
            mosaicAlias);
   }

   /**
    * extract levels from the DTO
    * 
    * @param namespaceInfoDTO the DTO
    * @return list of levels
    */
   private static List<NamespaceId> extractLevels(NamespaceInfoDTO namespaceInfoDTO) {
      List<NamespaceId> levels = new ArrayList<>();
      addLevel(levels, namespaceInfoDTO.getNamespace().getLevel0());
      addLevel(levels, namespaceInfoDTO.getNamespace().getLevel1());
      addLevel(levels, namespaceInfoDTO.getNamespace().getLevel2());
      return levels;
   }

   /**
    * if there is some level then add it to the list
    * 
    * @param levels list of levels
    * @param levelDto the level DTO
    */
   private static void addLevel(List<NamespaceId> levels, UInt64DTO levelDto) {
      if (levelDto != null && !levelDto.isEmpty()) {
         levels.add(new NamespaceId(toBigInt(levelDto)));
      }
   }
}
