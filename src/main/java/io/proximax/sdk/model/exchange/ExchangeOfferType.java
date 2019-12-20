/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

/**
 * TODO add proper description
 */
public enum ExchangeOfferType {
   SELL(0),
   BUY(1);
   
   private final int code;

   /**
    * @param code
    */
   private ExchangeOfferType(int code) {
      this.code = code;
   }

   /**
    * @return the code
    */
   public int getCode() {
      return code;
   }

   /**
    * retrieve metadata exchange offer by the code
    * 
    * @param code of the exchange offer type
    * @return exchange offer type
    */
   public static ExchangeOfferType getByCode(int code) {
      for (ExchangeOfferType type : ExchangeOfferType.values()) {
         if (code == type.code) {
            return type;
         }
      }
      throw new IllegalArgumentException("Unsupported exchange offer type code " + code);
   }
}
