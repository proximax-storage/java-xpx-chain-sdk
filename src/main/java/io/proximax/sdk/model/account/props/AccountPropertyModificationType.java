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
 * Account property modification type
 */
public enum AccountPropertyModificationType {

   ADD(0x00),
   REMOVE(0x01);

   private int code;

   /**
    * get the account property modification type by hexadecimal string representing the code of property modification
    * type
    * 
    * @param hexString hex string representing the property modification type
    * @return the property modification type
    */
   public static AccountPropertyModificationType getByCode(String hexString) {
      Validate.notNull(hexString, "hexString must not be null");
      return getByCode(Integer.parseInt(hexString, 16));
   }

   /**
    * get the account property modification type by its code
    * 
    * @param code the code of the property modification type
    * @return the property modification type
    */
   public static AccountPropertyModificationType getByCode(int code) {
      for (AccountPropertyModificationType prop : values()) {
         if (code == prop.getCode()) {
            return prop;
         }
      }
      throw new IllegalArgumentException(code + " is not a valid account property type");
   }

   /**
    * enum item constructor
    * 
    * @param code the code of the account property modification type
    */
   AccountPropertyModificationType(int code) {
      this.code = code;
   }

   /**
    * @return the code of this property modification type
    */
   public Integer getCode() {
      return this.code;
   }
}