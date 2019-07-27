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
import java.util.Optional;

import org.junit.jupiter.api.Test;

class MosaicPropertiesTest {
   private static final Optional<BigInteger> TEN = Optional.of(BigInteger.TEN);

    @Test
    void shouldCreateMosaicPropertiesViaConstructor() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true, 1, TEN);
        assertTrue(mosaicProperties.isSupplyMutable());
        assertTrue(mosaicProperties.isTransferable());
        assertTrue(1 == mosaicProperties.getDivisibility());
        assertEquals(TEN, mosaicProperties.getDuration());
    }

    @Test
    void shouldCreateMosaicPropertiesViaBuilder() {
        MosaicProperties mosaicProperties = new MosaicProperties.Builder()
                .supplyMutable(true)
                .transferable(true)
                .divisibility(1)
                .duration(TEN.orElse(BigInteger.ZERO))
                .build();
        assertTrue(mosaicProperties.isSupplyMutable());
        assertTrue(mosaicProperties.isTransferable());
        assertTrue(1 == mosaicProperties.getDivisibility());
        assertEquals(TEN, mosaicProperties.getDuration());
    }
}
