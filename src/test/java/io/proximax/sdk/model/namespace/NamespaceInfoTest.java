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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.NamespaceDTO;
import io.proximax.sdk.gen.model.NamespaceInfoDTO;
import io.proximax.sdk.gen.model.NamespaceMetaDTO;
import io.proximax.sdk.gen.model.NamespaceTypeEnum;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

class NamespaceInfoTest {

    @Test
    void createANamespaceInfoViaConstructor() {
        NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));
        NamespaceInfo namespaceInfo = new NamespaceInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                NamespaceType.ROOT_NAMESPACE,
                1,
                Arrays.asList(namespaceId),
                new NamespaceId(new BigInteger("0")),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                new BigInteger("1"),
                new BigInteger("-1"),
                Optional.empty(),
                Optional.empty());

        assertEquals(true, namespaceInfo.isActive());
        assertEquals(namespaceInfo.isActive(), !namespaceInfo.isExpired());
        assertTrue(namespaceInfo.getIndex() == 0);
        assertEquals("5A3CD9B09CD1E8000159249B", namespaceInfo.getMetaId());
        assertTrue(namespaceInfo.getType() == NamespaceType.ROOT_NAMESPACE);
        assertTrue(namespaceInfo.getDepth() == 1);
        assertEquals(namespaceId, namespaceInfo.getLevels().get(0));
        Assertions.assertEquals(new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST), namespaceInfo.getOwner());
        assertEquals(new BigInteger("1"), namespaceInfo.getStartHeight());
        assertEquals(new BigInteger("-1"), namespaceInfo.getEndHeight());
        assertEquals(Optional.empty(), namespaceInfo.getAddressAlias());
        assertEquals(Optional.empty(), namespaceInfo.getMosaicAlias());
    }

    @Test
    void shouldReturnRootNamespaceId() {
        NamespaceInfo namespaceInfo = createRootNamespaceInfo();
        assertEquals(new BigInteger("-8884663987180930485"), namespaceInfo.getId().getId());
    }

    @Test
    void shouldReturnSubNamespaceId() {
        NamespaceInfo namespaceInfo = createSubNamespaceInfo();
        assertEquals(new BigInteger("-1087871471161192663"), namespaceInfo.getId().getId());
    }

    @Test
    void shouldReturnRootTrueWhenNamespaceInfoIsFromRootNamespace() {
        NamespaceInfo namespaceInfo = createRootNamespaceInfo();
        assertTrue(namespaceInfo.isRoot());
    }

    @Test
    void shouldReturnRootFalseWhenNamespaceInfoIsFromSubNamespace() {
        NamespaceInfo namespaceInfo = createSubNamespaceInfo();
        assertFalse(namespaceInfo.isRoot());
    }

    @Test
    void shouldReturnSubNamespaceFalseWhenNamespaceInfoIsFromRootNamespace() {
        NamespaceInfo namespaceInfo = createRootNamespaceInfo();
        assertFalse(namespaceInfo.isSubnamespace());
    }

    @Test
    void shouldReturnSubNamespaceTrueWhenNamespaceInfoIsFromSubNamespace() {
        NamespaceInfo namespaceInfo = createSubNamespaceInfo();
        assertTrue(namespaceInfo.isSubnamespace());
    }

    @Test
    void shouldReturnParentNamespaceIdWhenNamespaceInfoIsFromSubNamespace() {
        NamespaceInfo namespaceInfo = createSubNamespaceInfo();
        assertEquals(new BigInteger("-3087871471161192663"), namespaceInfo.parentNamespaceId().getId());
    }

    @Test
    void shouldParentNamespaceIdThrowErrorWhenNamespaceInfoIsFromRootNamespace() {
        NamespaceInfo namespaceInfo = createRootNamespaceInfo();
        assertThrows(Error.class, ()->{namespaceInfo.parentNamespaceId();}, "Is A Root Namespace");
    }

    @Test
    void testFromDto() {
       NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));

       // namespace meta data
       NamespaceMetaDTO nmdto = new NamespaceMetaDTO();
       nmdto.setActive(true);
       nmdto.setId("5A3CD9B09CD1E8000159249B");
       nmdto.setIndex(0);
       // namespace description
       NamespaceDTO ndto = new NamespaceDTO();
       ndto.setType(NamespaceTypeEnum.NUMBER_0);
       ndto.setParentId(UInt64Utils.dtoFromBigInt(BigInteger.ZERO));
       ndto.setOwner("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF");
       ndto.setStartHeight(UInt64Utils.dtoFromBigInt(BigInteger.ONE));
       ndto.setEndHeight(UInt64Utils.dtoFromBigInt(BigInteger.TEN));
       ndto.setDepth(1);
       ndto.setLevel0(UInt64Utils.dtoFromBigInt(BigInteger.valueOf(-8884663987180930485l)));
       // namespace info
       NamespaceInfoDTO nidto = new NamespaceInfoDTO();
       nidto.setMeta(nmdto);
       nidto.setNamespace(ndto);
       
       NamespaceInfo namespaceInfo = NamespaceInfo.fromDto(nidto, NetworkType.MIJIN_TEST);
       
       assertEquals(true, namespaceInfo.isActive());
       assertEquals(namespaceInfo.isActive(), !namespaceInfo.isExpired());
       assertEquals(0, namespaceInfo.getIndex());
       assertEquals("5A3CD9B09CD1E8000159249B", namespaceInfo.getMetaId());
       assertEquals(NamespaceType.ROOT_NAMESPACE, namespaceInfo.getType());
       assertEquals(1, namespaceInfo.getDepth());
       assertEquals(namespaceId, namespaceInfo.getLevels().get(0));
       Assertions.assertEquals(new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST), namespaceInfo.getOwner());
       assertEquals(new BigInteger("1"), namespaceInfo.getStartHeight());
       assertEquals(BigInteger.TEN, namespaceInfo.getEndHeight());
       assertEquals(Optional.empty(), namespaceInfo.getAddressAlias());
       assertEquals(Optional.empty(), namespaceInfo.getMosaicAlias());
    }

    NamespaceInfo createRootNamespaceInfo() {
        return new NamespaceInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                NamespaceType.ROOT_NAMESPACE,
                1,
                Arrays.asList(new NamespaceId(new BigInteger("-8884663987180930485"))),
                new NamespaceId(new BigInteger("0")),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                new BigInteger("1"),
                new BigInteger("-1"),
                Optional.empty(),
                Optional.empty());
    }

    NamespaceInfo createSubNamespaceInfo() {
        return new NamespaceInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                NamespaceType.SUB_NAMESPACE,
                1,
                Arrays.asList(new NamespaceId(new BigInteger("-3087871471161192663")), new NamespaceId(new BigInteger("-1087871471161192663"))),
                new NamespaceId(new BigInteger("-3087871471161192663")),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                new BigInteger("1"),
                new BigInteger("-1"),
                Optional.empty(),
                Optional.empty());
    }
}

