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

package io.proximax.sdk.model.namespace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class NamespaceIdTest {

    @Test
    void createANamespaceIdFromRootNamespaceNameViaConstructor() {
        NamespaceId namespaceId = new NamespaceId("nem");
        assertEquals(namespaceId.getId(), new BigInteger("-8884663987180930485"));
        assertEquals(namespaceId.getFullName().get(), "nem");
    }

    @Test
    void createANamespaceIdFromSubNamespaceNameViaConstructor() {
        NamespaceId namespaceId = new NamespaceId("nem.xem");
        assertEquals(namespaceId.getId(), new BigInteger("-3087871471161192663"));
        assertEquals(namespaceId.getFullName().get(), "nem.xem");
    }

    @Test
    void createANamespaceIdFromIdViaConstructor() {
        NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));
        assertEquals(namespaceId.getId(), new BigInteger("-8884663987180930485"));
        assertFalse(namespaceId.getFullName().isPresent());
    }


    @Test
    void shouldCompareNamespaceIdsForEquality() {
        NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));
        NamespaceId namespaceId2 = new NamespaceId(new BigInteger("-8884663987180930485"));
        assertTrue(namespaceId.equals(namespaceId2));
    }
}
