/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */


 package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicLevy;
import io.proximax.sdk.model.mosaic.MosaicLevyType;
import io.proximax.sdk.model.network.NetworkType;

public class ModifyMosaicLevyTransactionTest extends ResourceBasedTest{
    
    @Test
    void createAModifyMosaicLevyTransactionViaConstructor() {
        ModifyMosaicLevyTransaction modifyMosaicLevyTransaction = new ModifyMosaicLevyTransaction(
                NetworkType.TEST_NET, 1, new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(),
                Optional.empty(), Optional.empty(), MosaicLevy.createWithAbsoluteFee(Recipient.from(Address.createFromRawAddress("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z")), new MosaicId("34b40b8ad0cee3f3"), 10),new MosaicId("34b40b8ad0cee3f3"));


        assertEquals(NetworkType.TEST_NET, modifyMosaicLevyTransaction.getNetworkType());
        assertTrue(1 == modifyMosaicLevyTransaction.getVersion());
        long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
        assertTrue(nowSinceNemesis < modifyMosaicLevyTransaction.getDeadline().getInstant());
        assertEquals(BigInteger.valueOf(0), modifyMosaicLevyTransaction.getMaxFee());

        assertEquals(MosaicLevyType.LEVYABSOLUTEFEE,
                        modifyMosaicLevyTransaction.getMosaicLevy().getType());
              
    }

    // @Test
    // @DisplayName("Serialization")
    // void serialization() throws IOException {
    //    ModifyMosaicLevyTransaction modifyMosaicLevyTransaction = new ModifyMosaicLevyTransaction(
    //             NetworkType.TEST_NET, EntityVersion.MODIFY_MOSAIC_LEVY.getValue(), new FakeDeadline(), BigInteger.ZERO, Optional.empty(), Optional.empty(),
    //             Optional.empty(), MosaicLevy.createWithAbsoluteFee(Recipient.from(Address.createFromRawAddress("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z")), new MosaicId("34b40b8ad0cee3f3"), 10),new MosaicId("34b40b8ad0cee3f3"));

    //     byte[] actual = modifyMosaicLevyTransaction.generateBytes();
    //     // saveBytes("modify_multisig_trans", actual);
    //   //  assertEquals(loadBytes("modify_mosic_levy"), actual);
    // }
}
