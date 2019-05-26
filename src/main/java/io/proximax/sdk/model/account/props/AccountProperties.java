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
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.AccountPropertiesDTO;
import io.proximax.sdk.model.account.Address;

/**
 * Account properties structure describes property information for an account.
 */
public class AccountProperties {

   private final Address address;
   private final List<AccountProperty> properties;

   /**
    * create new account properties object for given address with specified properties
    * 
    * @param address the address to which these properties are associated to
    * @param properties list of account properties
    */
   public AccountProperties(Address address, List<AccountProperty> properties) {
      this.address = address;
      this.properties = properties;
   }

   /**
    * @return the address to which these properties are associated to
    */
   public Address getAddress() {
      return address;
   }

   /**
    * @return list of account properties
    */
   public List<AccountProperty> getProperties() {
      return properties;
   }
   
   public static AccountProperties fromDto(AccountPropertiesDTO dto) {
      return new AccountProperties(
            Address.createFromEncoded(dto.getAddress()),
            dto.getProperties().stream()
                  .map(propDto -> new AccountProperty(
                        AccountPropertyType.getByCode(propDto.getPropertyType()),
                        propDto.getValues()))
                  .collect(Collectors.toList()));
   }
}