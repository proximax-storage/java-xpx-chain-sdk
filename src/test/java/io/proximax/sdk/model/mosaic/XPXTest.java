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

import org.junit.jupiter.api.Test;

class XPXTest {

    @Test
    void shouldCreateXEMViaConstructor() {
        NetworkCurrencyMosaic xem = new NetworkCurrencyMosaic(BigInteger.valueOf(0));
        assertEquals(BigInteger.valueOf(0), xem.getAmount());
        assertEquals(NetworkCurrencyMosaic.NAMESPACEID, xem.getId());
    }

    @Test
    void shouldCreateRelativeXEM() {
    	NetworkCurrencyMosaic xem = NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(1));
        assertEquals(BigInteger.valueOf(1000000), xem.getAmount());
        assertEquals(NetworkCurrencyMosaic.NAMESPACEID, xem.getId());
    }

    @Test
    void shouldCreateAbsoluteXEM() {
    	NetworkCurrencyMosaic xem = NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1));
        assertEquals(BigInteger.valueOf(1), xem.getAmount());
        assertEquals(NetworkCurrencyMosaic.NAMESPACEID, xem.getId());
    }
}