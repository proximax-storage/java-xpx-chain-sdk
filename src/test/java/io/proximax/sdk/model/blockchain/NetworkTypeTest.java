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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.network.NetworkType;

public class NetworkTypeTest {

   @Test
   void networkTypes() {
      testNetworkType(NetworkType.MAIN_NET, 0xb8, 184, "public");
      testNetworkType(NetworkType.TEST_NET, 0xa8, 168, "publicTest");
      testNetworkType(NetworkType.PRIVATE, 0xc8, 200,"private");
      testNetworkType(NetworkType.PRIVATE_TEST, 0xb0, 176,"privateTest");
      testNetworkType(NetworkType.MIJIN, 0x60, 96, "mijin");
      testNetworkType(NetworkType.MIJIN_TEST, 0x90, 144,"mijinTest");
      // make sure we have them all
      assertEquals(6, NetworkType.values().length);
      // check that exception is thrown for invalid value
      assertThrows(IllegalArgumentException.class, () -> NetworkType.rawValueOf(-5678));
      assertThrows(IllegalArgumentException.class, () -> NetworkType.getByName("nonexistent"));
   }

   private void testNetworkType(NetworkType networkType, int hexaValue, int decimalValue, String name) {
      assertEquals(decimalValue, networkType.getValue());
      assertEquals(networkType, NetworkType.rawValueOf(decimalValue));
      assertEquals(decimalValue, hexaValue);
      assertEquals(networkType, NetworkType.getByName(name));
      assertEquals(name, networkType.getName());
   }
}
