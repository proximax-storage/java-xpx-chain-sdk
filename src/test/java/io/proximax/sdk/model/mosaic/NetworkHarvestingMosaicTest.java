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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class NetworkHarvestingMosaicTest {

    @Test
    void shouldCreateViaConstructor() {
        NetworkHarvestMosaic mosaic = new NetworkHarvestMosaic(BigInteger.valueOf(0));
        assertEquals(BigInteger.valueOf(0), mosaic.getAmount());
        assertEquals(NetworkHarvestMosaic.ID, mosaic.getId());
    }

    @Test
    void shouldCreateRelative() {
       NetworkHarvestMosaic mosaic = NetworkHarvestMosaic.createRelative(BigDecimal.valueOf(1.5));
        assertEquals(BigInteger.valueOf(1500), mosaic.getAmount());
        assertEquals(NetworkHarvestMosaic.ID, mosaic.getId());
    }

    @Test
    void shouldCreateAbsolute() {
       NetworkHarvestMosaic mosaic = NetworkHarvestMosaic.createAbsolute(BigInteger.valueOf(1));
        assertEquals(BigInteger.valueOf(1), mosaic.getAmount());
        assertEquals(NetworkHarvestMosaic.ID, mosaic.getId());
    }
}