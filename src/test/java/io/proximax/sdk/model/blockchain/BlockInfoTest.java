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

package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.utils.dto.UInt64Utils;

class BlockInfoTest {

    @Test
    void createANewBlockInfo() {
        BlockInfo blockInfo = new BlockInfo("24E92B511B54EDB48A4850F9B42485FDD1A30589D92C775632DDDD71D7D1D691",
                "57F7DA205008026C776CB6AED843393F04CD458E0AA2D9F1D5F31A402072B2D6",
                Optional.of(UInt64Utils.fromIntArray(new int[]{0, 0})),
                Optional.of(25),
                "37351C8244AC166BE6664E3FA954E99A3239AC46E51E2B32CEA1C72DD0851100A7731868E932E1A9BEF8A27D48E1" +
                        "FFEE401E933EB801824373E7537E51733E0F",
                new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST),
                NetworkType.MIJIN_TEST,
                3,
                32768,
                UInt64Utils.fromIntArray(new int[]{1, 0}),
                UInt64Utils.fromIntArray(new int[]{0, 0}),
                UInt64Utils.fromIntArray(new int[]{276447232, 23283}),
                "702090BA31CEF9E90C62BBDECC0CCCC0F88192B6625839382850357F70DD68A0",
                "0000000000000000000000000000000000000000000000000000000000000000");

        assertEquals("24E92B511B54EDB48A4850F9B42485FDD1A30589D92C775632DDDD71D7D1D691", blockInfo.getHash());
        assertEquals("57F7DA205008026C776CB6AED843393F04CD458E0AA2D9F1D5F31A402072B2D6", blockInfo.getGenerationHash());
        assertEquals(UInt64Utils.fromIntArray(new int[]{0, 0}), blockInfo.getTotalFee().get());
        assertEquals(new Integer(25), blockInfo.getNumTransactions().get());
        assertEquals("37351C8244AC166BE6664E3FA954E99A3239AC46E51E2B32CEA1C72DD0851100A7731868E932E1A9BEF8A27D48E1" +
                "FFEE401E933EB801824373E7537E51733E0F", blockInfo.getSignature());
        Assertions.assertEquals(new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF", NetworkType.MIJIN_TEST), blockInfo.getSigner());
        assertEquals(NetworkType.MIJIN_TEST, blockInfo.getNetworkType());
        assertEquals(3, blockInfo.getVersion());
        assertEquals(32768, blockInfo.getType());
        assertEquals(UInt64Utils.fromIntArray(new int[]{1, 0}), blockInfo.getHeight());
        assertEquals(UInt64Utils.fromIntArray(new int[]{0, 0}), blockInfo.getTimestamp());
        assertEquals(UInt64Utils.fromIntArray(new int[]{276447232, 23283}), blockInfo.getDifficulty());
        assertEquals("702090BA31CEF9E90C62BBDECC0CCCC0F88192B6625839382850357F70DD68A0", blockInfo.getPreviousBlockHash());
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000", blockInfo.getBlockTransactionsHash());
        // test that toString does something
        assertTrue(blockInfo.toString().startsWith("BlockInfo"));
    }
}
