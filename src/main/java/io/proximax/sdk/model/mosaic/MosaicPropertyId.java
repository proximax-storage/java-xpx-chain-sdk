/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.mosaic;

/**
 * Type of mosaic property represented as numeric ID
 */
public enum MosaicPropertyId {
   FLAGS((byte)0),
   DIVISIBILITY((byte)1),
   DURATION((byte)2);
   
   private final byte code;

   /**
    * @param code
    */
   private MosaicPropertyId(byte code) {
      this.code = code;
   }
   
   /**
    * @return the code
    */
   public byte getCode() {
      return code;
   }

   /**
    * retrieve mosaic property ID by its code
    * 
    * @param code code of the property ID
    * @return the enum entry representing the ID
    */
   public static MosaicPropertyId getByCode(byte code) {
      for (MosaicPropertyId type: values()) {
         if (code == type.getCode()) {
            return type;
         }
      }
      throw new IllegalArgumentException("Unknown mosaic property type with code " + code);
   }
}
