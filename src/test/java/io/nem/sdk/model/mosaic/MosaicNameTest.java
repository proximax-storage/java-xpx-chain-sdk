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

import io.nem.sdk.model.namespace.NamespaceId;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MosaicNameTest {

    @Test
    void createAMosaicName() {
        NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));
        MosaicId mosaicId = new MosaicId(new BigInteger("-3087871471161192663"));
        MosaicName mosaicName = new MosaicName(mosaicId, "xem", namespaceId);

        assertEquals(mosaicId, mosaicName.getMosaicId());
        assertEquals("xem", mosaicName.getName());
        assertEquals(namespaceId, mosaicName.getParentId());
    }
}