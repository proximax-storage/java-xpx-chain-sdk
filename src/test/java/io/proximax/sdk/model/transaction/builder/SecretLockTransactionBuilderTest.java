/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.transaction.HashType;
import io.proximax.sdk.model.transaction.SecretLockTransaction;

/**
 * {@link SecretLockTransactionBuilder} tests
 */
class SecretLockTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private SecretLockTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new SecretLockTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   
   @Test
   void test() {
      Address recipient = new Address("SADD", NETWORK_TYPE);
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";

      SecretLockTransaction trans = builder.mosaic(NetworkCurrencyMosaic.TEN).recipient(recipient).duration(BigInteger.ONE).hashType(HashType.SHA3_256).secret(secret).build();
      
      assertEquals(recipient, trans.getRecipient()); 
      assertEquals(secret, trans.getSecret()); 
      assertEquals(NetworkCurrencyMosaic.TEN, trans.getMosaic()); 
      assertEquals(BigInteger.ONE, trans.getDuration()); 
      assertEquals(HashType.SHA3_256, trans.getHashType()); 
   }

   
   @Test
   void testConvenienceSecret() {
      Address recipient = new Address("SADD", NETWORK_TYPE);
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";

      SecretLockTransaction trans = builder.mosaic(NetworkCurrencyMosaic.TEN).recipient(recipient).duration(BigInteger.ONE).secret(HashType.SHA3_256, secret).build();
      
      assertEquals(recipient, trans.getRecipient()); 
      assertEquals(secret, trans.getSecret()); 
      assertEquals(NetworkCurrencyMosaic.TEN, trans.getMosaic()); 
      assertEquals(BigInteger.ONE, trans.getDuration()); 
      assertEquals(HashType.SHA3_256, trans.getHashType()); 
   }

}
