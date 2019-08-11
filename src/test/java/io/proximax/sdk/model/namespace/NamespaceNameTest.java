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

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.NamespaceNameDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

class NamespaceNameTest {

    @Test
    void createANamespaceName() {
        NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));
        NamespaceName namespaceName = new NamespaceName(namespaceId, "nem");

        assertEquals(namespaceId, namespaceName.getNamespaceId());
        assertEquals("nem", namespaceName.getName());
    }
    
    @Test
    void checkFromDto() {
       // values for the test
       final String name = "prx.xpx";
       final NamespaceId ns = new NamespaceId(name);
       // prepare DTO
       NamespaceNameDTO dto = new NamespaceNameDTO();
       dto.setName(name);
       dto.setNamespaceId(UInt64Utils.dtoFromBigInt(ns.getId()));
       // convert
       NamespaceName namespaceName = NamespaceName.fromDto(dto);
       // check
       assertEquals(ns, namespaceName.getNamespaceId());
       assertEquals(name, namespaceName.getName());
    }
}
