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

package io.proximax.sdk.model.mosaic;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.utils.GsonUtils;

/**
 * The richlist entry describes the account details and its specific mosaic
 * ownership.
 *
 * @since 1.0
 */
public class MosaicRichList {
    private final Address address;
    private final String publicKey;
    private final BigInteger amount;

    /**
     * create new instance with mosaic detail
     * 
     * @param address   The account address.
     * @param publicKey The account public key.
     * @param amount    The mosaic amount
     */
    public MosaicRichList(Address address, String publicKey, BigInteger amount) {
        this.address = address;
        this.publicKey = publicKey;
        this.amount = amount;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @return the amount
     */
    public BigInteger getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "MosaicRichList [address=" + address + ", publicKey=" + publicKey + ", amount=" + amount + "]";
    }

    /**
     * create mosaic info instance from the DTO
     * 
     * @param dto mosaic info DTO
     *
     * @return mosaic info
     */
    public static List<MosaicRichList> fromJson(JsonArray jsonArray) {
        List<MosaicRichList> richList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject json = jsonArray.get(i).getAsJsonObject();
            var address = json.get("address").getAsString();
            var publicKey = json.get("publicKey").getAsString();
            var amount = GsonUtils.getBigInteger(json.getAsJsonArray("amount"));
            richList.add(new MosaicRichList(Address.createFromEncoded(address), publicKey, amount));
        }
        return richList;

    }
}
