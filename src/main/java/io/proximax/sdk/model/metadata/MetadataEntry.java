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
package io.proximax.sdk.model.metadata;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigInteger;
import io.proximax.sdk.gen.model.MetadataInfoDTO;
import io.proximax.sdk.model.account.Address;

/**
 * Metadata entry
 */
public class MetadataEntry {
    private final Integer version;
    private final String compositeHash;
    private final Address sourceAddress;
    private final String targetKey;
    private final BigInteger scopedMetadataKey;
    private final BigInteger targetId;
    private final MetadataType metadataType;
    private final Integer valueSize;
    private final String value;
    private final String id;

    /**
     * @param version
     * @param compositeHash
     * @param sourceAddress
     * @param targetKey
     * @param scopedMetadataKey
     * @param targetId
     * @param metadataType
     * @param valueSize
     * @param value
     * @param id
     */
    public MetadataEntry(int version, String compositeHash, Address sourceAddress, String targetKey,
            BigInteger scopedMetadataKey, BigInteger targetId, MetadataType metadataType, Integer valueSize,
            String value, String id) {
        this.version = version;
        this.compositeHash = compositeHash;
        this.sourceAddress = sourceAddress;
        this.targetKey = targetKey;
        this.scopedMetadataKey = scopedMetadataKey;
        this.targetId = targetId;
        this.metadataType = metadataType;
        this.valueSize = valueSize;
        this.value = value;
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @return the composite hash
     */
    public String getCompositeHash() {
        return compositeHash;
    }

    /**
     * @return the source address
     */
    public Address getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @return the target key
     */
    public String getTargetKey() {
        return targetKey;
    }

    /**
     * @return the scoped metadata key
     */
    public BigInteger getScopedMetadataKey() {
        return scopedMetadataKey;
    }

    /**
     * @return the target Id
     */
    public BigInteger getTargetId() {
        return targetId;
    }

    /**
     * @return the metadata type
     */
    public MetadataType getMetadataType() {
        return metadataType;
    }

    /**
     * @return the value size
     */
    public Integer getValueSize() {
        return valueSize;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MetadataEntry [version=" + version + ", compositeHash=" + compositeHash + ",sourceAddress=" + sourceAddress
                + ", targetKey=" + targetKey + ", scopedMetadataKey=" + scopedMetadataKey + ", targetId=" + targetId
                + ", metadataType=" + metadataType + ", valueSize=" + valueSize + ", value="
                + value + ", id=" + id + "]";
    }

    /**
     * create mosaic info instance from the DTO
     * 
     * @param dto         mosaic info DTO
     * @return metadata v2 info
     */
    public static MetadataEntry fromDto(MetadataInfoDTO dto) {
        return new MetadataEntry(
                dto.getMetadataEntry().getVersion(),
                dto.getMetadataEntry().getCompositeHash(),
                Address.createFromEncoded(dto.getMetadataEntry().getSourceAddress()),
                dto.getMetadataEntry().getTargetKey(),
                toBigInt(dto.getMetadataEntry().getScopedMetadataKey()),
                toBigInt(dto.getMetadataEntry().getTargetId()),
                MetadataType.getByCode(dto.getMetadataEntry().getMetadataType()),
                dto.getMetadataEntry().getValueSize(),
                dto.getMetadataEntry().getValue(),
                dto.getId());
    }
}