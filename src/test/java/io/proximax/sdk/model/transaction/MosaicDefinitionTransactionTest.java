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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.network.NetworkType;

class MosaicDefinitionTransactionTest extends ResourceBasedTest {
      private static final String PUB_KEY = "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF";
   
      private static final NetworkType networkType = NetworkType.TEST_NET;

   @Test
   void constructor() {
      MosaicNonce nonce = new MosaicNonce(new byte[4]);
      MosaicDefinitionTransaction mosaicCreationTx = new MosaicDefinitionTransaction(NetworkType.TEST_NET, 23,
            new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), nonce,
            new MosaicId(nonce, PublicAccount.createFromPublicKey(PUB_KEY, networkType)),
            new MosaicProperties(true, true, 3, Optional.of(BigInteger.valueOf(10))));

      assertEquals(BigInteger.valueOf(992621222383397347l), mosaicCreationTx.getMosaicId().getId());
      assertEquals(true, mosaicCreationTx.getMosaicProperties().isSupplyMutable());
      assertEquals(true, mosaicCreationTx.getMosaicProperties().isTransferable());
      assertEquals(3, mosaicCreationTx.getMosaicProperties().getDivisibility());
      assertEquals(Optional.of(BigInteger.valueOf(10)), mosaicCreationTx.getMosaicProperties().getDuration());
      assertEquals(nonce, mosaicCreationTx.getNonce());
   }

   @Test
   void serialization() throws IOException {
      MosaicNonce nonce = MosaicNonce.createFromBigInteger(BigInteger.valueOf(7));
      MosaicDefinitionTransaction mosaicCreationTx = new MosaicDefinitionTransaction(NetworkType.TEST_NET, 3,
            new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), nonce, new MosaicId(nonce, PublicAccount.createFromPublicKey(PUB_KEY, networkType)),  new MosaicProperties(true, true, 4, Optional.of(BigInteger.valueOf(1000))));

      byte[] actual = mosaicCreationTx.generateBytes();
//      saveBytes("mosaic_definition", actual);
      assertArrayEquals(loadBytes("mosaic_definition"), actual);
   }
}
