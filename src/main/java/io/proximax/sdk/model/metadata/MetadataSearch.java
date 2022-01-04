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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.transaction.Pagination;
import io.proximax.sdk.utils.GsonUtils;

/**
 * Metadata Search
 */
public class MetadataSearch {
    private final List<MetadataEntry> metadataEntries;
    private final Pagination pagination;

    /**
     * @param metadataEntries list of {@link MetadataEntry}
     * @param pagination      {@link Pagination}
     */
    public MetadataSearch(List<MetadataEntry> metadataEntries, Pagination pagination) {
        this.metadataEntries = metadataEntries;
        this.pagination = pagination;
    }
   
    /**
     * @return list of {@link MetadataEntry}
     */
    public List<MetadataEntry> getMetadataEntries() {
        return metadataEntries;
    }

    /**
     * @return {@link Pagination}
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * create receipts instance from raw JsonObject
     * 
     * @param json the data form server
     * @return the receipts
     */
    public static MetadataSearch fromJson(JsonObject input) {
       JsonArray data = input.getAsJsonArray("data");
        JsonObject pagination = input.getAsJsonObject("pagination");

        int totalEntries = pagination.get("totalEntries").getAsInt();
        int pageNumber = pagination.get("pageNumber").getAsInt();
        int pageSize = pagination.get("pageSize").getAsInt();
        int totalPages = pagination.get("totalPages").getAsInt();
        
        List<MetadataEntry> metadataList = new ArrayList<>();

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                JsonObject transaction = data.get(i).getAsJsonObject();
                JsonObject metadata = transaction.getAsJsonObject("metadataEntry");
                JsonObject meta = transaction.getAsJsonObject("meta");

                var version = metadata.get("version").getAsInt();
                var compositeHash = metadata.get("compositeHash").getAsString();
                var sourceAddress = metadata.get("sourceAddress").getAsString();
                var targetKey = metadata.get("targetKey").getAsString();
                var scopedMetadataKey = GsonUtils.getBigInteger(metadata.getAsJsonArray("scopedMetadataKey"));
                var targetId = GsonUtils.getBigInteger(metadata.getAsJsonArray("targetId"));
                var metadataType = metadata.get("metadataType").getAsInt();
                var valueSize = metadata.get("valueSize").getAsInt();
                var value = metadata.get("value").getAsString();
                var id = meta.get("id").getAsString();

                metadataList.add(new MetadataEntry(version, compositeHash, Address.createFromEncoded(sourceAddress),
                        targetKey, scopedMetadataKey, targetId, MetadataType.getByCode(metadataType), valueSize, value,
                        id));
            }
        }
        return new MetadataSearch(metadataList, new Pagination(totalEntries, pageNumber, pageSize, totalPages));

    
    }
}
