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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class MosaicPropertiesTest {

    @Test
    void shouldCreateMosaicPropertiesViaConstructor() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true, true, 1, BigInteger.valueOf(1000));
        assertTrue(mosaicProperties.isSupplyMutable());
        assertTrue(mosaicProperties.isTransferable());
        assertTrue(mosaicProperties.isLevyMutable());
        assertTrue(1 == mosaicProperties.getDivisibility());
        assertEquals(BigInteger.valueOf(1000), mosaicProperties.getDuration());
    }

    @Test
    void shouldCreateMosaicPropertiesViaBuilder() {
        MosaicProperties mosaicProperties = new MosaicProperties.Builder()
                .supplyMutable(true)
                .transferable(true)
                .levyMutable(true)
                .divisibility(1)
                .duration(BigInteger.valueOf(1000))
                .build();
        assertTrue(mosaicProperties.isSupplyMutable());
        assertTrue(mosaicProperties.isTransferable());
        assertTrue(mosaicProperties.isLevyMutable());
        assertTrue(1 == mosaicProperties.getDivisibility());
        assertEquals(BigInteger.valueOf(1000), mosaicProperties.getDuration());
    }
}
