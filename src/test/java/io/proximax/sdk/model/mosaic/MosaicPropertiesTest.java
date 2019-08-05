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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.MosaicPropertyDTO;
import io.proximax.sdk.gen.model.MosaicPropertyIdEnum;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * 
 * {@link MosaicProperties} tests
 */
class MosaicPropertiesTest {
   private static final Optional<BigInteger> ZERO = Optional.of(BigInteger.ZERO);
   private static final Optional<BigInteger> TEN = Optional.of(BigInteger.TEN);

   @Test
   void shouldCreateMosaicPropertiesViaConstructor() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 1, TEN);
      assertTrue(mosaicProperties.isSupplyMutable());
      assertTrue(mosaicProperties.isTransferable());
      assertEquals(1, mosaicProperties.getDivisibility());
      assertEquals(TEN, mosaicProperties.getDuration());
   }

   @Test
   void staticConstructor() {
      MosaicProperties mosaicProperties = MosaicProperties.create(3, 1, TEN);
      assertTrue(mosaicProperties.isSupplyMutable());
      assertTrue(mosaicProperties.isTransferable());
      assertEquals(1, mosaicProperties.getDivisibility());
      assertEquals(TEN, mosaicProperties.getDuration());
   }

   @Test
   void zeroDurationIsInfnity() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 1, ZERO);
      assertTrue(mosaicProperties.isSupplyMutable());
      assertTrue(mosaicProperties.isTransferable());
      assertEquals(1, mosaicProperties.getDivisibility());
      assertFalse(mosaicProperties.getDuration().isPresent());
   }

   @Test
   void missingDurationIsInfnity() {
      MosaicProperties mosaicProperties = new MosaicProperties(true, true, 1, Optional.empty());
      assertTrue(mosaicProperties.isSupplyMutable());
      assertTrue(mosaicProperties.isTransferable());
      assertEquals(1, mosaicProperties.getDivisibility());
      assertFalse(mosaicProperties.getDuration().isPresent());
   }

   @Test
   void shouldCreateMosaicPropertiesViaBuilder() {
      MosaicProperties mosaicProperties = new MosaicProperties.Builder().supplyMutable(true).transferable(true)
            .divisibility(1).duration(TEN.orElse(BigInteger.ZERO)).build();
      assertTrue(mosaicProperties.isSupplyMutable());
      assertTrue(mosaicProperties.isTransferable());
      assertTrue(1 == mosaicProperties.getDivisibility());
      assertEquals(TEN, mosaicProperties.getDuration());
   }

   @Test
   void checkToString() {
      assertTrue(MosaicProperties.create(3, 1, TEN).toString().startsWith("MosaicProperties"));
   }

   @Test
   void deserialization() {
      List<MosaicPropertyDTO> mosaicPropertiesDTO = Arrays.asList(
            createProperty(MosaicPropertyIdEnum.NUMBER_0, BigInteger.valueOf(3)),
            createProperty(MosaicPropertyIdEnum.NUMBER_1, BigInteger.valueOf(1)),
            createProperty(MosaicPropertyIdEnum.NUMBER_2, BigInteger.valueOf(10))
      );
      MosaicProperties props = MosaicProperties.fromDto(mosaicPropertiesDTO);
      
      assertTrue(props.isSupplyMutable());
      assertTrue(props.isTransferable());
      assertEquals(1, props.getDivisibility());
      assertEquals(TEN, props.getDuration());
   }

   public static MosaicPropertyDTO createProperty(MosaicPropertyIdEnum id, BigInteger value) {
      MosaicPropertyDTO prop = new MosaicPropertyDTO();
      prop.setId(id);
      prop.setValue(UInt64Utils.dtoFromBigInt(value));
      return prop;
   }
}
