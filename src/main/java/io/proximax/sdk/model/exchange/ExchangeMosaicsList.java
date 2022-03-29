/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ExchangeMosaicsList {
    private final String mosaicId;

    public ExchangeMosaicsList(String mosaicId) {
        this.mosaicId = mosaicId;

    }

    /**
     * @return mosaicId
     */
    public String getMosaicId() {
        return mosaicId;
    }

    /**
     * 
     * @param json the data form server
     * @return List of {@link ExchangeMosaicsList}
     */
    public static List<ExchangeMosaicsList> fromJson(JsonArray json) {
        List<ExchangeMosaicsList> mosaics = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            JsonObject mosaicId = json.get(i).getAsJsonObject();
           var mosaid_id = mosaicId.get("mosaicId").getAsString();
            mosaics.add(new ExchangeMosaicsList(mosaid_id));
        }
        return mosaics;
    }

}
