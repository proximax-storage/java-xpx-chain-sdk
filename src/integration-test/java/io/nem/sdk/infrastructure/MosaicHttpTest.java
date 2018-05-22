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

package io.nem.sdk.infrastructure;

import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.model.mosaic.MosaicName;
import io.nem.sdk.model.mosaic.XEM;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MosaicHttpTest extends BaseTest {
    private MosaicHttp mosaicHttp;

    @BeforeAll
    void setup() throws IOException {
        mosaicHttp = new MosaicHttp(this.getNodeUrl());
    }

    @Test
    void getMosaic() throws ExecutionException, InterruptedException {
        MosaicInfo mosaicInfo = mosaicHttp
                .getMosaic(XEM.MOSAICID)
                .toFuture()
                .get();

        assertEquals(new BigInteger("1"), mosaicInfo.getHeight());
        assertEquals(XEM.NAMESPACEID, mosaicInfo.getNamespaceId());
        assertEquals(XEM.MOSAICID, mosaicInfo.getMosaicId());
    }

    @Test
    void getMosaics() throws ExecutionException, InterruptedException {
        List<MosaicInfo> mosaicsInfo = mosaicHttp
                .getMosaics(Collections.singletonList(XEM.MOSAICID))
                .toFuture()
                .get();

        assertEquals(XEM.NAMESPACEID, mosaicsInfo.get(0).getNamespaceId());
        assertEquals(XEM.MOSAICID, mosaicsInfo.get(0).getMosaicId());
    }

    @Test
    void getMosaicsFromNamespace() throws ExecutionException, InterruptedException {
        List<MosaicInfo> mosaicsInfo = mosaicHttp
                .getMosaicsFromNamespace(XEM.NAMESPACEID)
                .toFuture()
                .get();

        assertEquals(XEM.NAMESPACEID, mosaicsInfo.get(0).getNamespaceId());
        assertEquals(XEM.MOSAICID, mosaicsInfo.get(0).getMosaicId());
    }

    @Test
    void getMosaicNames() throws ExecutionException, InterruptedException {
        List<MosaicName> mosaicNames = mosaicHttp
                .getMosaicNames(Collections.singletonList(XEM.MOSAICID))
                .toFuture()
                .get();

        assertEquals("xem", mosaicNames.get(0).getName());
        assertEquals(XEM.MOSAICID, mosaicNames.get(0).getMosaicId());
    }

    @Test
    void throwExceptionWhenMosaicDoesNotExists() {
        TestObserver<MosaicInfo> testObserver = new TestObserver<>();
        mosaicHttp
                .getMosaic(new MosaicId("nem:nem"))
                .subscribeOn(Schedulers.single())
                .test()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertFailure(RuntimeException.class);
    }

}
