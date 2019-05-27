/*
 * Copyright 2019 NEM
 * Copyright 2019 ProximaX
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

package io.proximax.sdk.model.account.props;

import org.apache.commons.lang3.Validate;

/**
 * Account property type 
 * 
 * 0x01 The property type is an address. 
 * 0x02 The property type is mosaic id. 
 * 0x03 The property type is a transaction type.
 * 0x04 Property type sentinel. 
 * 0x80 + type The property is interpreted as a blocking operation.
 */
public enum AccountPropertyType {
   /** allow specified address and block all addresses that are not explicitly allowed */
   ALLOW_ADDRESS(0x01),
   /** allow specified mosaic and block all mosaics that are not explicitly allowed */
   ALLOW_MOSAIC(0x02),
   /** allow specified transaction type and block all transaction types that are not explicitly allowed */
   ALLOW_TRANSACTION(0x04),
   /** this account property type is currently not supported */
   SENTINEL(0x05),
   /** block specified address */
   BLOCK_ADDRESS(0x80 + 0x01),
   /** block specified mosaic */
   BLOCK_MOSAIC(0x80 + 0x02),
   /** block specified transaction type */
   BLOCK_TRANSACTION(0x80 + 0x04);

   private int code;

   /**
    * get the account property type by hexadecimal string representing the code of property type
    * 
    * @param hexString hex string representing the proeprty type
    * @return the property type
    */
   public static AccountPropertyType getByCode(String hexString) {
      Validate.notNull(hexString, "hexString must not be null");
      return getByCode(Integer.parseInt(hexString, 16));
   }

   /**
    * get the account property type by the code of property type
    * @param code the code of the property type
    * @return the property type
    */
   public static AccountPropertyType getByCode(int code) {
      for (AccountPropertyType prop: values()) {
         if (code == prop.getCode()) {
            return prop;
         }
      }
      throw new IllegalArgumentException(code + " is not a valid account property type");
   }

   /**
    * new instance for the enum
    * 
    * @param code the code
    */
   AccountPropertyType(int code) {
      this.code = code;
   }

   /**
    * @return the code of this property type
    */
   public int getCode() {
      return this.code;
   }

}