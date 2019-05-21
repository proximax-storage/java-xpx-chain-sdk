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

package io.proximax.sdk.model.mosaic;

import static io.proximax.sdk.utils.GsonUtils.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

class MosaicNameTest extends ResourceBasedTest {

    @Test
    void createAMosaicName() {
        MosaicId mosaicId = new MosaicId(BigInteger.valueOf(-3087871471161192663l));
        MosaicNames mosaicName = new MosaicNames(mosaicId, Arrays.asList("xem"));

        assertEquals(mosaicId, mosaicName.getMosaicId());
        assertEquals(Arrays.asList("xem"), mosaicName.getNames());
    }
    
    @Test
    void validateMapper() {
       long count = stream(getResources("mosaic_names", "dtos", "names"))
       .map(JsonElement::getAsJsonObject)
       .map(json -> new MosaicNames(
             getMosaicID(json),
             getNames(json)))
       .count();
       assertEquals(1, count);
    }
    
    private static MosaicId getMosaicID(JsonObject json) {
       JsonArray ints = json.get("mosaicId").getAsJsonArray();
       UInt64DTO dto = new UInt64DTO();
       dto.add(ints.get(0).getAsLong());
       dto.add(ints.get(1).getAsLong());
       return new MosaicId(UInt64Utils.toBigInt(dto));
    }
    
    private static List<String> getNames(JsonObject json) {
       return stream(json.get("names").getAsJsonArray())
             .map(el -> el.getAsString())
             .collect(Collectors.toList());
    }
}