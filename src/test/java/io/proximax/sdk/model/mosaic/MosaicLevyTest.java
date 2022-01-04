/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.gen.model.MosaicLevyInfoDTO;
import io.proximax.sdk.gen.model.MosaicLevyTypeEnum;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * {@link MosaicLevy} tests
 */
class MosaicLevyTest {
    private static final Logger logger = LoggerFactory.getLogger(MosaicLevyTest.class);
    @Test
    void shouldCreateMosaicInfoViaConstructor() {
        MosaicLevy mosaicLevy = new MosaicLevy(MosaicLevyType.LEVYABSOLUTEFEE, new Recipient(Address.createFromEncoded("A88167455099E7676758B38BD8282B2FEC00416C1F4AA6906A")), new MosaicId(new BigInteger("-24100471511526444581")), new BigInteger("100"));
        
        assertEquals(MosaicLevyType.LEVYABSOLUTEFEE, mosaicLevy.getType());
        assertEquals(new Recipient(Address.createFromEncoded("A88167455099E7676758B38BD8282B2FEC00416C1F4AA6906A")),
                mosaicLevy.getRecipient());
        logger.info("Recipent : "+ mosaicLevy.getRecipient().getAddress());
        assertEquals(new MosaicId(new BigInteger("-24100471511526444581")), mosaicLevy.getMosaicId());
        logger.info("MosaicId : " + mosaicLevy.getMosaicId().getIdAsLong());

        assertEquals(new BigInteger("100"), mosaicLevy.getFee());
    }

    @Test
    void fromDto() {
        MosaicLevyInfoDTO mosaicLevyInfo = new MosaicLevyInfoDTO();
        mosaicLevyInfo.setMosaicId(UInt64Utils.dtoFromBigInt(new BigInteger("6556029556961470127")));
        mosaicLevyInfo.setMosaicLevyType(MosaicLevyTypeEnum.NUMBER_0);
        mosaicLevyInfo.setFee(UInt64Utils.dtoFromBigInt(BigInteger.TEN));
        mosaicLevyInfo.setRecipient("A88167455099E7676758B38BD8282B2FEC00416C1F4AA6906A");
        MosaicLevyInfo accountInfo = MosaicLevyInfo.fromDto(mosaicLevyInfo, NetworkType.TEST_NET);
        assertEquals(new BigInteger("10"),accountInfo.getFee());
        assertEquals("5afbb2258fa666af", accountInfo.getMosaicId().getIdAsHex());
        assertEquals(0,accountInfo.getType().getValue());
        assertEquals(Address.createFromEncoded("A88167455099E7676758B38BD8282B2FEC00416C1F4AA6906A"),accountInfo.getRecipient().getAddress().orElseThrow(RuntimeException::new));
    }
}
