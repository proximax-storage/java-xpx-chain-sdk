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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.infrastructure.model.UInt64DTO;
import io.proximax.sdk.infrastructure.utils.UInt64Utils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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
       long count = getResources("mosaic_names", "dtos", "names").stream()
       .map(obj -> (JsonObject)obj)
       .map(json -> new MosaicNames(
             getMosaicID(json),
             getNames(json)))
       .count();
       assertEquals(1, count);
    }
    
    private static MosaicId getMosaicID(JsonObject json) {
       JsonArray ints = json.getJsonArray("mosaicId");
       UInt64DTO dto = new UInt64DTO();
       dto.add(ints.getLong(0));
       dto.add(ints.getLong(1));
       return new MosaicId(UInt64Utils.toBigInt(dto));
    }
    
    private static List<String> getNames(JsonObject json) {
       return json.getJsonArray("names").stream()
             .map(obj -> (String)obj)
             .collect(Collectors.toList());
    }
}