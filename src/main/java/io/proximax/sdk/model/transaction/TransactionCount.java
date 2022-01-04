/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.transaction;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TransactionCount {
    private final Integer count;
    private final Integer entityType;

    /**
     * @param count      total number of transaction of the entity type
     * @param entityType type of the entity
     * 
     */
    public TransactionCount(Integer count, Integer entityType) {
        this.count = count;
        this.entityType = entityType;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getEntityType() {
        return entityType;
    }

    /**
     * create mosaic levy instance from the DTO
     *
     * @param dto        Transaction Count DTO
     * @param entityType Entity Type
     * @return List of Transaction count
     */
    public static List<TransactionCount> fromJson(JsonArray jsonArray) {
        List<TransactionCount> transactionCount = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject json = jsonArray.get(i).getAsJsonObject();
           // var count = json.getAsJsonObject("count").getAsInt();
           // var type = json.getAsJsonObject("type").getAsInt();

             var count = json.get("count").getAsInt();
             var type = json.get("type").getAsInt();
            transactionCount.add(new TransactionCount(count, type));

        }

        return transactionCount;
    }

    // /**
    // * create Transaction Count instance from the DTO
    // *
    // * @param TransactionCountDTO
    // *
    // * @return metadata v2 info
    // */
    // public static TransactionCount fromDto(TransactionCountDTO dto) {
    // return new TransactionCount(
    // dto.getCount(),
    // dto.getEntityType());
    // }
}
