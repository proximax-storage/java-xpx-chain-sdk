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

package io.nem.sdk.model.mosaic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MosaicIdTest {

    @Test
    void createAMosaicIdFromMosaicNameViaConstructor() {
        MosaicId mosaicId = new MosaicId("nem:xem");
        assertEquals(mosaicId.getId(), new BigInteger("-3087871471161192663"));
        assertEquals(mosaicId.getFullName().get(), "nem:xem");
    }

    @Test
    void createAMosaicIdFromIdViaConstructor() {
        MosaicId mosaicId = new MosaicId(new BigInteger("-8884663987180930485"));
        assertEquals(mosaicId.getId(), new BigInteger("-8884663987180930485"));
        assertFalse(mosaicId.getFullName().isPresent());
    }

    @Test
    void shouldCompareMosaicIdsForEquality() {
        MosaicId mosaicId = new MosaicId(new BigInteger("-8884663987180930485"));
        MosaicId mosaicId2 = new MosaicId(new BigInteger("-8884663987180930485"));
        assertTrue(mosaicId.equals(mosaicId2));
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("nem"),
                Arguments.of("nem.xem"),
                Arguments.of(":nem"),
                Arguments.of("nem.xem:"),
                Arguments.of(""),
                Arguments.of("nem:xem:nem")
        );
    }

    @ParameterizedTest
    @MethodSource("provider")
    void throwIllegalMosaicIdentifierExceptionWhenMosaicIsNotValid(String invalidIdentifier) {
        assertThrows(IllegalIdentifierException.class, () -> {
            new MosaicId(invalidIdentifier);
        });
    }
}
