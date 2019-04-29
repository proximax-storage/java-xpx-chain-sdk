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

package io.nem.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.nem.sdk.infrastructure.utils.UInt64Utils;
import io.nem.sdk.model.mosaic.IllegalIdentifierException;

class IdGeneratorTest {

    @Test
    void namespacePathGeneratesCorrectWellKnownRootPath() {
        List<BigInteger> ids = IdGenerator.generateNamespacePath("nem");
        assertEquals(ids.size(), 1);
        assertEquals(new BigInteger("-8884663987180930485"), ids.get(0));
    }

    @Test
    void namespacePathGeneratesCorrectWellKnownChildPath() {
        List<BigInteger> ids = IdGenerator.generateNamespacePath("nem.xem");

        assertEquals(ids.size(), 2);
        assertEquals(new BigInteger("-8884663987180930485"), ids.get(0));
        assertEquals(new BigInteger("-3087871471161192663"), ids.get(1));
    }

    @Test
    void namespacePathSupportsMultiLevelNamespaces() {
        List<BigInteger> ids = new ArrayList<BigInteger>();
        ids.add(IdGenerator.generateId("foo", BigInteger.valueOf(0)));
        ids.add(IdGenerator.generateId("bar", ids.get(0)));
        ids.add(IdGenerator.generateId("baz", ids.get(1)));

        assertEquals(IdGenerator.generateNamespacePath("foo.bar.baz"), ids);
    }

    @Test
    void namespacePathRejectsNamesWithTooManyParts() {
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("a.b.c.d");}, "too many parts");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("a.b.c.d.e");}, "too many parts");
    }

    @Test
    void mosaicIdGeneratesCorrectWellKnowId() {
        BigInteger id = IdGenerator.generateMosaicId(0, "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF");
        assertEquals(UInt64Utils.fromIntArray(new int[]{481110499,231112638}), id);
    }

    @Test
    void namespaceInvalid() {
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("");}, "having zero length");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("alpha.bet@.zeta");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("a!pha.beta.zeta");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("alpha.beta.ze^a");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath(".");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("..");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath(".a");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("a..a");}, "invalid part");
        assertThrows(IllegalIdentifierException.class, ()->{IdGenerator.generateNamespacePath("A");}, "invalid part");
    }
}
