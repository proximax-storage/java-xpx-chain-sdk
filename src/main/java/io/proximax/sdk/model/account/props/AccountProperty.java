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

import java.util.List;

import org.apache.commons.lang3.Validate;

/**
 * Account property structure describes property information.
 */
public class AccountProperty {

   private final AccountPropertyType propertyType;
   private final List<Object> values;

   /**
    * create new account property of specified type with specified values
    * 
    * @param propertyType the type of account property
    * @param values list of property values
    */
   public AccountProperty(AccountPropertyType propertyType, List<Object> values) {
      Validate.notNull(propertyType, "propertyType must not be null");
      this.propertyType = propertyType;
      this.values = values;
   }

   /**
    * @return the account property type
    */
   public AccountPropertyType getPropertyType() {
      return propertyType;
   }

   /**
    * @return list of property values
    */
   public List<Object> getValues() {
      return values;
   }
}