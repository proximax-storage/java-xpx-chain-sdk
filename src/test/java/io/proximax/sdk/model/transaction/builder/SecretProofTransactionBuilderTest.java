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
import io.proximax.sdk.model.transaction.HashType;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.SecretProofTransaction;

/**
 * {@link SecretProofTransactionBuilder} tests
 */
class SecretProofTransactionBuilderTest {

   private static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;

   private SecretProofTransactionBuilder builder;

   @BeforeEach
   void setUp() {
      builder = new SecretProofTransactionBuilder();
      builder.networkType(NETWORK_TYPE);
      builder.deadlineDuration(BigInteger.valueOf(60_000));
      builder.feeCalculationStrategy(FeeCalculationStrategy.MEDIUM);
   }
   
   @Test
   void test() {
      Recipient recipient = Recipient.from(new Address("SADD", NETWORK_TYPE));
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";

      SecretProofTransaction trans = builder.recipient(recipient).proof("CAFE").hashType(HashType.SHA3_256).secret(secret).build();
      
      assertEquals(recipient, trans.getRecipient()); 
      assertEquals(secret, trans.getSecret()); 
      assertEquals(HashType.SHA3_256, trans.getHashType()); 
      assertEquals("CAFE", trans.getProof());
   }
   
   @Test
   void testConvenienceSecret() {
      Recipient recipient = Recipient.from(new Address("SADD", NETWORK_TYPE));
      String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";

      SecretProofTransaction trans = builder.recipient(recipient).proof("CAFE").secret(HashType.SHA3_256, secret).build();
      
      assertEquals(recipient, trans.getRecipient()); 
      assertEquals(secret, trans.getSecret()); 
      assertEquals(HashType.SHA3_256, trans.getHashType()); 
      assertEquals("CAFE", trans.getProof());
   }
}
