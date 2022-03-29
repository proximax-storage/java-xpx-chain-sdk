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

package io.proximax.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.MosaicRepository;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicLevyInfo;
import io.proximax.sdk.model.mosaic.MosaicNames;
import io.proximax.sdk.model.mosaic.MosaicRichList;
import io.reactivex.schedulers.Schedulers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MosaicHttpTest extends BaseTest {
   private static final MosaicId ID = new MosaicId("6BABA790155EA5C6");

   private static final MosaicId ID_names = new MosaicId("6ad1fa3645ee1987");

   private MosaicRepository mosaicHttp;

   @BeforeAll
   void setup() throws IOException {
      mosaicHttp = new BlockchainApi(new URL(getNodeUrl()), getNetworkType()).createMosaicRepository();
   }

   @Test
   void getMosaic() throws ExecutionException, InterruptedException {
      MosaicInfo mosaicInfo = mosaicHttp.getMosaic(ID).toFuture().get();

      assertEquals(new BigInteger("265120"), mosaicInfo.getHeight());
      assertEquals(ID, mosaicInfo.getMosaicId());
   }

   @Test
   void getMosaics() throws ExecutionException, InterruptedException {
      List<MosaicInfo> mosaicsInfo = mosaicHttp.getMosaics(Collections.singletonList(ID)).toFuture().get();

      assertEquals(ID, mosaicsInfo.get(0).getMosaicId());
   }

   @Test
   void getMosaicNames() throws ExecutionException, InterruptedException {

      List<MosaicNames> mosaicNames = mosaicHttp.getMosaicNames(Collections.singletonList(ID_names)).toFuture().get();

      assertEquals("prx.xpx", mosaicNames.get(0).getNames().get(0));
      assertEquals(ID_names, mosaicNames.get(0).getMosaicId());
   }

   @Test
   void getMosaicLevyInfo() throws ExecutionException, InterruptedException {
      MosaicLevyInfo mosaicLevyInfo = mosaicHttp.getMosaicLevyInfo(ID).toFuture().get();

      assertEquals(1, mosaicLevyInfo.getType().getValue());
      assertEquals("6BABA790155EA5C6", mosaicLevyInfo.getMosaicId().getIdAsHex().toUpperCase());
      assertEquals(Address.createFromEncoded("A88167455099E7676758B38BD8282B2FEC00416C1F4AA6906A"),
            mosaicLevyInfo.getRecipient().getAddress().get());
      assertEquals(100, mosaicLevyInfo.getFee().intValue());
   }

   @Test
   void getMosaicRichList() throws ExecutionException, InterruptedException {

      List<MosaicRichList> mosaicRichListInfo = mosaicHttp.getMosaicRichList(ID).toFuture().get();
      Address address = Address.createFromEncoded(
            "A8F459E424CB8E2323266962B8E48B855578E49E54E22CD9A9");
      String publickey = "42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85";

      assertEquals(address, mosaicRichListInfo.get(0).getAddress());
      assertEquals(
            publickey,
            mosaicRichListInfo.get(0).getPublicKey());
      assertEquals(BigInteger.valueOf(1000000), mosaicRichListInfo.get(0).getAmount());
   }

   @Test
   void throwExceptionWhenMosaicDoesNotExists() {
      mosaicHttp.getMosaic(new MosaicId(BigInteger.valueOf(123456789l))).subscribeOn(Schedulers.single()).test()
            .awaitDone(2, TimeUnit.SECONDS).assertFailure(RuntimeException.class);
   }

}
