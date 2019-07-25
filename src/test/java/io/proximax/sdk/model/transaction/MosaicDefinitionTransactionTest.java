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

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;

class MosaicDefinitionTransactionTest extends ResourceBasedTest {

   @Test
   void constructor() {
      MosaicNonce nonce = new MosaicNonce(new byte[4]);
      MosaicDefinitionTransaction mosaicCreationTx = new MosaicDefinitionTransaction(NetworkType.TEST_NET, 23, new FakeDeadline(), BigInteger.ONE, nonce,
            new MosaicId(nonce, "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF"),
            new MosaicProperties(true, true, 3, Optional.of(BigInteger.valueOf(10))),"signaturestring",
            new PublicAccount(new KeyPair().getPublicKey().getHexString(), NetworkType.MIJIN),
            TransactionInfo.create(BigInteger.ONE, "infohash", "merklehash"));
      assertEquals(BigInteger.valueOf(992621222383397347l), mosaicCreationTx.getMosaicId().getId());
      assertEquals(true, mosaicCreationTx.getMosaicProperties().isSupplyMutable());
      assertEquals(true, mosaicCreationTx.getMosaicProperties().isTransferable());
      assertEquals(3, mosaicCreationTx.getMosaicProperties().getDivisibility());
      assertEquals(Optional.of(BigInteger.valueOf(10)), mosaicCreationTx.getMosaicProperties().getDuration());
      assertEquals(nonce, mosaicCreationTx.getNonce());
   }

   @Test
   void createAMosaicCreationTransactionViaStaticConstructor() {
      MosaicNonce nonce = new MosaicNonce(new byte[4]);
      MosaicDefinitionTransaction mosaicCreationTx = MosaicDefinitionTransaction.create(nonce,
            new MosaicId(nonce, "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF"),
            new Deadline(2, ChronoUnit.HOURS),
            new MosaicProperties(true, true, 3, Optional.of(BigInteger.valueOf(10))),
            NetworkType.TEST_NET);

      assertEquals(NetworkType.TEST_NET, mosaicCreationTx.getNetworkType());
      assertEquals(TransactionVersion.MOSAIC_DEFINITION.getValue(), mosaicCreationTx.getVersion());
      long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
      assertTrue(nowSinceNemesis < mosaicCreationTx.getDeadline().getInstant());
      assertEquals(BigInteger.valueOf(0), mosaicCreationTx.getFee());
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
      MosaicDefinitionTransaction mosaicDefinitionTransaction = MosaicDefinitionTransaction.create(nonce,
            new MosaicId(nonce, "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF"),
            new FakeDeadline(),
            new MosaicProperties(true, true, 4, Optional.of(BigInteger.valueOf(10000))),
            NetworkType.MIJIN_TEST);
      byte[] actual = mosaicDefinitionTransaction.generateBytes();
//      saveBytes("mosaic_definition", actual);
      assertArrayEquals(loadBytes("mosaic_definition"), actual);
   }
}
