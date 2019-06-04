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

/**
 * Descriptor for account property modification
 */
public class AccountPropertyModification<T> {

   private final AccountPropertyModificationType type;
   private final T value;

   /**
    * create descriptor for generic account property modification
    * 
    * @param type type of the modification
    * @param value generic value of the modification which depends on the account property type
    */
   public AccountPropertyModification(AccountPropertyModificationType type, T value) {
      this.type = type;
      this.value = value;
   }

   /**
    * create new ADD modification of specified value
    * @param <T> the type of the value object
    * @param value the value to add
    * @return the modification instance
    */
   public static <T> AccountPropertyModification<T> add(T value) {
      return new AccountPropertyModification<>(AccountPropertyModificationType.ADD, value);
   }

   /**
    * create new REMOVE modification of specified value
    * @param <T> the type of the value object
    * @param value the value to remove
    * @return the modification instance
    */
   public static <T> AccountPropertyModification<T> remove(T value) {
      return new AccountPropertyModification<>(AccountPropertyModificationType.REMOVE, value);
   }

   /**
    * @return the type
    */
   public AccountPropertyModificationType getType() {
      return type;
   }

   /**
    * @return the value
    */
   public T getValue() {
      return value;
   }
}
