/*
 * Copyright 2018 NEM
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

package io.proximax.sdk.model.transaction;

/**
 * Enum containing transaction type constants.
 */
public enum EntityType {

   // Mosaic
   /**
    * Mosaic definition transaction type.
    */
   MOSAIC_DEFINITION(0x414D),

   /**
    * Mosaic supply change transaction.
    */
   MOSAIC_SUPPLY_CHANGE(0x424D),

   // Namespace
   /**
    * Register namespace transaction type.
    */
   REGISTER_NAMESPACE(0x414E),

   /**
    * Address alias transaction type.
    */
   ADDRESS_ALIAS(0x424E),

   /**
    * Mosaic alias transaction type.
    */
   MOSAIC_ALIAS(0x434E),

   // Transfer
   /**
    * Transfer Transaction transaction type.
    */
   TRANSFER(0x4154),

   // Multisignature
   /**
    * Modify multisig account transaction type.
    */
   MODIFY_MULTISIG_ACCOUNT(0x4155),

   /**
    * Aggregate complete transaction type.
    */
   AGGREGATE_COMPLETE(0x4141),

   /**
    * Aggregate bonded transaction type
    */
   AGGREGATE_BONDED(0x4241),

   /**
    * Hash Lock transaction type
    */
   LOCK(0x4148),

   // Account filters
   /**
    * Account properties address transaction type
    */
   ACCOUNT_PROPERTIES_ADDRESS(0x4150),

   /**
    * Account properties mosaic transaction type
    */
   ACCOUNT_PROPERTIES_MOSAIC(0x4250),

   /**
    * Account properties entity type transaction type
    */
   ACCOUNT_PROPERTIES_ENTITY_TYPE(0x4350),

   // Cross-chain swaps
   /**
    * Secret Lock Transaction type
    */
   SECRET_LOCK(0x4152),

   /**
    * Secret Proof transaction type
    */
   SECRET_PROOF(0x4252),

   // Remote harvesting
   /**
    * Account link transaction type
    */
   ACCOUNT_LINK(0x414C),

   /**
    * transaction to modify address meta datata
    */
   MODIFY_ADDRESS_METADATA(0x413d),

   /**
    * transaction to modify mosaic meta datata
    */
   MODIFY_MOSAIC_METADATA(0x423d),

   /**
    * transaction to modify namespace meta datata
    */
   MODIFY_NAMESPACE_METADATA(0x433d),

   /**
    * transaction to modify account contract
    */
   MODIFY_CONTRACT(0x4157), 
   
   /**
    * Blockchain configuration change transaction
    */
   BLOCKCHAIN_CONFIG(0x4159),
   
   /**
    * blockchain version update transaction
    */
   BLOCKCHAIN_UPGRADE(0x4158),

   EXCHANGE_OFFER_ADD(0x415D),
   EXCHANGE_OFFER(0x425D),
   EXCHANGE_OFFER_REMOVE(0x435D),

   DRIVE_PREPARE(0x415A),
   DRIVE_JOIN(0x425A),
   DRIVE_FILESYSTEM(0x435A),
   DRIVE_FILES_DEPOSIT(0x445A),
   DRIVE_END(0x455A),
   DRIVE_FILES_REWARD(0x465A),
   DRIVE_VERIFICATION_START(0x475A),
   DRIVE_VERIFICATION_END(0x485A);

   private int value;

   EntityType(int value) {
      this.value = value;
   }

   /**
    * Returns enum value.
    *
    * @return enum value
    */
   public int getValue() {
      return this.value;
   }

   /**
    * retrieve transaction type by the code
    * 
    * @param code of the transaction type
    * @return transaction type
    */
   public static EntityType rawValueOf(int code) {
      for (EntityType type : EntityType.values()) {
         if (code == type.value) {
            return type;
         }
      }
      throw new IllegalArgumentException("Unsupported transaction type code " + code);
   }
}
