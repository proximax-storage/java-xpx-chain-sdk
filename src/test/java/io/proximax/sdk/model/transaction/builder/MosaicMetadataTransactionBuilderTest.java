/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.MosaicMetadataTransaction;
import io.proximax.sdk.utils.MetadataCalculationUtils;

public class MosaicMetadataTransactionBuilderTest {
    private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;
    private MosaicMetadataTransactionBuilder builder;
    private static final Logger logger = LoggerFactory.getLogger(MosaicMetadataTransactionBuilderTest.class);

    @BeforeEach
    void setUp() {
        builder = new MosaicMetadataTransactionBuilder();
        builder.networkType(NETWORK_TYPE);
        builder.deadlineDuration(BigInteger.valueOf(60_000));
        builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
    }

    @Test
    void test() {
        var targetPublicKey = PublicAccount
                .createFromPublicKey("42B85DF37E6349B20E48F82ADA20F53E0EED60FA190CDAC792A8E1C02EFEFB85", NETWORK_TYPE);
        MosaicId targetMosaicId = new MosaicId("1656e7a61acf59fa");
        String scopedMetadataKey = "name";
        String value = "test";
        String oldValue = "";
        var ScopedMetadataKey = MetadataCalculationUtils.getScopedMetadataKey(scopedMetadataKey);
        var valueDifferenceBytes = MetadataCalculationUtils.getValueDifferenceBytes(value, oldValue);
        var valueSize = MetadataCalculationUtils.getValueSize(value, oldValue);
        var valueSizeDeltaValue = MetadataCalculationUtils.getValueSizeDeltaValue(value, oldValue);

        System.out.println("valueSizeDeltaValue: " + valueSizeDeltaValue);
        System.out.println("valueDifferenceBytes: " + valueDifferenceBytes.length);

        System.out.println("ValueSize: " + valueSize);

        System.out.println("ScopedMetadataKey: " + ScopedMetadataKey);

        logger.info("valueDifferenceBytes {}", valueDifferenceBytes);

        MosaicMetadataTransaction mosaic_metadata = builder
                .create(targetPublicKey, targetMosaicId, scopedMetadataKey, value, oldValue).build();

        assertEquals(targetPublicKey, mosaic_metadata.getTargetPublicKey());
        assertEquals(targetMosaicId, mosaic_metadata.getTargetMosaicId());
        assertEquals(valueSizeDeltaValue, mosaic_metadata.getValueSizeDelta());
        assertEquals(ScopedMetadataKey, mosaic_metadata.getScopedMetadataKey());
        assertEquals(valueSize, mosaic_metadata.getValueSize());

    }

}
