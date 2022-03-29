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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.MosaicDefinitionDTO;
import io.proximax.sdk.gen.model.MosaicInfoDTO;
import io.proximax.sdk.gen.model.MosaicMetaDTO;
import io.proximax.sdk.gen.model.MosaicPropertyIdEnum;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

class MosaicInfoTest {
   private static final Optional<BigInteger> TEN = Optional.of(BigInteger.TEN);

   @Test
   void createAMosaicInfoViaConstructor() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 3, TEN);
      MosaicId mosaicId = new MosaicId(new BigInteger("-3087871471161192663"));
      MosaicInfo mosaicInfo = new MosaicInfo("5A3CD9B09CD1E8000159249B", mosaicId, new BigInteger("100"),
            new BigInteger("0"), new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicProperties);

      checkMosaicInfo(mosaicInfo, mosaicId);
   }

   @Test
   void checkFromDto() {
      MosaicMetaDTO meta = new MosaicMetaDTO();
      meta.setId("5A3CD9B09CD1E8000159249B");
      MosaicDefinitionDTO mosaic = new MosaicDefinitionDTO();
      mosaic.setHeight(UInt64Utils.dtoFromBigInt(BigInteger.ZERO));
      mosaic.setMosaicId(UInt64Utils.dtoFromBigInt(new BigInteger("-3087871471161192663")));
      mosaic.setOwner("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF");
      mosaic.setProperties(Arrays.asList(
            MosaicPropertiesTest.createProperty(MosaicPropertyIdEnum.NUMBER_0, BigInteger.valueOf(3)),
            MosaicPropertiesTest.createProperty(MosaicPropertyIdEnum.NUMBER_1, BigInteger.valueOf(3)),
            MosaicPropertiesTest.createProperty(MosaicPropertyIdEnum.NUMBER_2, BigInteger.valueOf(10))
            ));
      mosaic.setSupply(UInt64Utils.dtoFromBigInt(BigInteger.valueOf(100)));
      MosaicInfoDTO dto = new MosaicInfoDTO();
      dto.setMeta(meta);
      dto.setMosaic(mosaic);
      
      checkMosaicInfo(MosaicInfo.fromDto(dto, NetworkType.TEST_NET), new MosaicId(new BigInteger("-3087871471161192663")));

   }
   
   private static void checkMosaicInfo(MosaicInfo mosaicInfo, MosaicId mosaicId) {
      assertEquals("5A3CD9B09CD1E8000159249B", mosaicInfo.getMetaId());
      assertEquals(mosaicId, mosaicInfo.getMosaicId());
      assertEquals(new BigInteger("100"), mosaicInfo.getSupply());
      assertEquals(new BigInteger("0"), mosaicInfo.getHeight());
      assertEquals(
            new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicInfo.getOwner());
      assertTrue(mosaicInfo.isSupplyMutable());
      assertTrue(mosaicInfo.isTransferable());
      assertEquals(3, mosaicInfo.getDivisibility());
      assertEquals(TEN, mosaicInfo.getDuration());
   }
   
   @Test
   void shouldReturnIsSupplyMutableWhenIsMutable() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 3, TEN);

      MosaicInfo mosaicInfo = new MosaicInfo("5A3CD9B09CD1E8000159249B",
            new MosaicId(new BigInteger("-3087871471161192663")), new BigInteger("100"), new BigInteger("0"),
            new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicProperties);

      assertTrue(mosaicInfo.isSupplyMutable());
   }

   @Test
   void shouldReturnIsSupplyMutableWhenIsImmutable() {
      MosaicProperties mosaicProperties = new MosaicProperties(false, true, 3, TEN);

      MosaicInfo mosaicInfo = new MosaicInfo("5A3CD9B09CD1E8000159249B",
            new MosaicId(new BigInteger("-3087871471161192663")), new BigInteger("100"), new BigInteger("0"),
            new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicProperties);

      assertFalse(mosaicInfo.isSupplyMutable());
   }

   @Test
   void shouldReturnIsTransferableWhenItsTransferable() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 3, TEN);

      MosaicInfo mosaicInfo = new MosaicInfo("5A3CD9B09CD1E8000159249B",
            new MosaicId(new BigInteger("-3087871471161192663")), new BigInteger("100"), new BigInteger("0"),
            new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicProperties);

      assertTrue(mosaicInfo.isTransferable());
   }

   @Test
   void shouldReturnIsTransferableWhenItsNotTransferable() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, false, 3, TEN);

      MosaicInfo mosaicInfo = new MosaicInfo("5A3CD9B09CD1E8000159249B",
            new MosaicId(new BigInteger("-3087871471161192663")), new BigInteger("100"), new BigInteger("0"),
            new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicProperties);

      assertFalse(mosaicInfo.isTransferable());
   }

   @Test
   void chachToString() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 3, TEN);

      MosaicInfo mosaicInfo = new MosaicInfo("5A3CD9B09CD1E8000159249B",
            new MosaicId(new BigInteger("-3087871471161192663")), new BigInteger("100"), new BigInteger("0"),
            new PublicAccount("B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                  NetworkType.TEST_NET),
            mosaicProperties);

      assertTrue(mosaicInfo.toString().startsWith("MosaicInfo "));
   }
}
