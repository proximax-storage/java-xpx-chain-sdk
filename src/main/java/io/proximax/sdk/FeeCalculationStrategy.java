/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk;

import java.math.BigInteger;

/**
 * Fee calculation strategies for transactions
 */
public enum FeeCalculationStrategy {
   ZERO(0),
   LOW(15000),
   MEDIUM(150000),
   HIGH(1500000);
   
   private static final int MAX_FEE = 75000000;
   
   private final int coefficient;

   /**
    * @param coefficient
    */
   private FeeCalculationStrategy(int coefficient) {
      this.coefficient = coefficient;
   }

   /**
    * @return the coefficient
    */
   public int getCoefficient() {
      return coefficient;
   }
   
   /**
    * calculate fee based on the transaction size
    * 
    * @param transactionSize size of transaction when serialized
    * @return the default maxFee
    */
   public BigInteger calculateFee(int transactionSize) {
      return BigInteger.valueOf(Math.min(MAX_FEE, transactionSize * getCoefficient()));
   }
}
