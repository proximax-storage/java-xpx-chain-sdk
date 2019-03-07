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

import io.nem.sdk.dto.MosaicProperties;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceId;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class MosaicInfoTest {

    @Test
    void createAMosaicInfoViaConstructor() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true,true, 3, BigInteger.valueOf(10));
        NamespaceId namespaceId = new NamespaceId(new BigInteger("-8884663987180930485"));
        MosaicId mosaicId = new MosaicId(new BigInteger("-3087871471161192663"));
        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                namespaceId,
                mosaicId,
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertEquals(true, mosaicInfo.isActive());
        assertTrue(mosaicInfo.getIndex() == 0);
        assertEquals("5A3CD9B09CD1E8000159249B", mosaicInfo.getMetaId());
        assertEquals(namespaceId, mosaicInfo.getNamespaceId());
        assertEquals(mosaicId, mosaicInfo.getMosaicId());
        assertEquals(new BigInteger("100"), mosaicInfo.getSupply());
        assertEquals(new BigInteger("0"), mosaicInfo.getHeight());
        assertEquals(new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST), mosaicInfo.getOwner());
        assertTrue(mosaicInfo.isSupplyMutable());
        assertTrue(mosaicInfo.isTransferable());
        assertTrue(mosaicInfo.isLevyMutable());
        assertEquals(3, mosaicInfo.getDivisibility());
        assertEquals(BigInteger.valueOf(10), mosaicInfo.getDuration());
    }

    @Test
    void shouldReturnIsSupplyMutableWhenIsMutable() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true,true, 3, BigInteger.valueOf(10));

        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                new NamespaceId(new BigInteger("-8884663987180930485")),
                new MosaicId(new BigInteger("-3087871471161192663")),
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertTrue(mosaicInfo.isSupplyMutable());
    }

    @Test
    void shouldReturnIsSupplyMutableWhenIsImmutable() {
        MosaicProperties mosaicProperties = new MosaicProperties(false, true,true, 3, BigInteger.valueOf(10));

        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                new NamespaceId(new BigInteger("-8884663987180930485")),
                new MosaicId(new BigInteger("-3087871471161192663")),
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertFalse(mosaicInfo.isSupplyMutable());
    }

    @Test
    void shouldReturnIsTransferableWhenItsTransferable() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true,true, 3, BigInteger.valueOf(10));

        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                new NamespaceId(new BigInteger("-8884663987180930485")),
                new MosaicId(new BigInteger("-3087871471161192663")),
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertTrue(mosaicInfo.isTransferable());
    }

    @Test
    void shouldReturnIsTransferableWhenItsNotTransferable() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, false,true, 3, BigInteger.valueOf(10));

        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                new NamespaceId(new BigInteger("-8884663987180930485")),
                new MosaicId(new BigInteger("-3087871471161192663")),
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertFalse(mosaicInfo.isTransferable());
    }

    @Test
    void shouldReturnIsTransferableWhenLevyIsMutable() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true,true, 3, BigInteger.valueOf(10));

        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                new NamespaceId(new BigInteger("-8884663987180930485")),
                new MosaicId(new BigInteger("-3087871471161192663")),
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertTrue(mosaicInfo.isLevyMutable());
    }

    @Test
    void shouldReturnIsTransferableWhenLevyIsImmutable() {
        MosaicProperties mosaicProperties = new MosaicProperties(true, true,false, 3, BigInteger.valueOf(10));

        MosaicInfo mosaicInfo = new MosaicInfo(true,
                0,
                "5A3CD9B09CD1E8000159249B",
                new NamespaceId(new BigInteger("-8884663987180930485")),
                new MosaicId(new BigInteger("-3087871471161192663")),
                new BigInteger("100"),
                new BigInteger("0"),
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                mosaicProperties);

        assertFalse(mosaicInfo.isLevyMutable());
    }
}
