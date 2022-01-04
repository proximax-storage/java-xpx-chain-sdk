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

package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicSupplyType;
import io.proximax.sdk.model.network.NetworkType;

class MosaicSupplyChangeTransactionTest extends ResourceBasedTest {

   @Test
   void createAMosaicSupplyChangeTransactionViaConstructor() {
      MosaicSupplyChangeTransaction mosaicSupplyChangeTx = new MosaicSupplyChangeTransaction(NetworkType.TEST_NET, 2,
            new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            new MosaicId(new BigInteger("6300565133566699912")), MosaicSupplyType.INCREASE, BigInteger.valueOf(10));

      assertEquals(NetworkType.TEST_NET, mosaicSupplyChangeTx.getNetworkType());
      assertTrue(2 == mosaicSupplyChangeTx.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < mosaicSupplyChangeTx.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), mosaicSupplyChangeTx.getMaxFee());
      assertEquals(new BigInteger("6300565133566699912"), mosaicSupplyChangeTx.getMosaicId().getId());
      assertEquals(MosaicSupplyType.INCREASE, mosaicSupplyChangeTx.getMosaicSupplyType());
      assertEquals(BigInteger.valueOf(10), mosaicSupplyChangeTx.getDelta());
   }

   @Test
   @DisplayName("Serialization")
   void serialization() throws IOException {
      MosaicSupplyChangeTransaction mosaicSupplyChangeTx = new MosaicSupplyChangeTransaction(NetworkType.TEST_NET, 2,
            new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            new MosaicId(new BigInteger("6300565133566699912")), MosaicSupplyType.INCREASE, BigInteger.valueOf(10));

      byte[] actual = mosaicSupplyChangeTx.generateBytes();
//        saveBytes("mosaic_supply_change", actual);
      assertArrayEquals(loadBytes("mosaic_supply_change"), actual);
   }
}
