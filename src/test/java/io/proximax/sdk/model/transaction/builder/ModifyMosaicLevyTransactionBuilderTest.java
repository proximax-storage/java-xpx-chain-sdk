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

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicLevy;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.ModifyMosaicLevyTransaction;
import io.proximax.sdk.model.transaction.Recipient;

/**
 * {@link ModifyMosaicLevyTransactionBuilder} tests
 */
 class ModifyMosaicLevyTransactionBuilderTest {
     private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;
     private ModifyMosaicLevyTransactionBuilder builder;
//private MosaicId Id;
     @BeforeEach
     void setUp() {
         builder = new ModifyMosaicLevyTransactionBuilder();
         builder.networkType(NETWORK_TYPE);
         builder.deadlineDuration(BigInteger.ZERO);
         builder.feeCalculationStrategy(FeeCalculationStrategy.ZERO);
       
         
     }

     @Test
     void testMosaicLevy() {
         MosaicId mosid = new MosaicId(BigInteger.valueOf(1234567890l));
            Recipient receipient = Recipient.from(new Account(new KeyPair(), NETWORK_TYPE).getAddress());
         ModifyMosaicLevyTransaction modifyMosaicLevyTransaction = builder.create(MosaicLevy.createWithAbsoluteFee(
                 receipient,
                 mosid, 100), mosid).build();

         assertEquals(NetworkType.TEST_NET, modifyMosaicLevyTransaction.getNetworkType());
         assertEquals(mosid, modifyMosaicLevyTransaction.getMosaicId());
         assertEquals(mosid, modifyMosaicLevyTransaction.getMosaicLevy().getMosaicId());
         assertEquals(receipient, modifyMosaicLevyTransaction.getMosaicLevy().getRecipient());       
     }
}
