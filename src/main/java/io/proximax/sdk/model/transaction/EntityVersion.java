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
 * Enum containing transaction type versions. Version are declared by server plug-ins
 */
public enum EntityVersion {

   // Mosaic
   /**
    * Mosaic definition transaction type.
    */
   MOSAIC_DEFINITION(3),

   /**
    * Mosaic supply change transaction.
    */
   MOSAIC_SUPPLY_CHANGE(2),

   // Namespace
   /**
    * Register namespace transaction type.
    */
   REGISTER_NAMESPACE(2),

   /**
    * Address alias transaction type.
    */
   ADDRESS_ALIAS(1),

   /**
    * Mosaic alias transaction type.
    */
   MOSAIC_ALIAS(1),

   // Transfer
   /**
    * Transfer Transaction transaction type.
    */
   TRANSFER(3),

   // Multisignature
   /**
    * Modify multisig account transaction type.
    */
   MODIFY_MULTISIG_ACCOUNT(3),

   /**
    * Aggregate complete transaction type.
    */
   AGGREGATE_COMPLETE(3),

   /**
    * Aggregate bonded transaction type
    */
   AGGREGATE_BONDED(3),

   /**
    * Hash Lock transaction type
    */
   LOCK(1),

   // Account filters
   /**
    * Account properties address transaction type
    */
   ACCOUNT_PROPERTIES_ADDRESS(1),

   /**
    * Account properties mosaic transaction type
    */
   ACCOUNT_PROPERTIES_MOSAIC(1),

   /**
    * Account properties entity type transaction type
    */
   ACCOUNT_PROPERTIES_ENTITY_TYPE(1),

   // Cross-chain swaps
   /**
    * Secret Lock Transaction type
    */
   SECRET_LOCK(1),

   /**
    * Secret Proof transaction type
    */
   SECRET_PROOF(1),

   // Remote harvesting
   /**
    * Account link transaction type
    */
   ACCOUNT_LINK(2),
   
   /**
    * modification of metadata
    */
   METADATA_MODIFICATION(1),
   
   /**
    * contract modification transaction version
    */
   MODIFY_CONTRACT(3),
   
   /**
     * modify account metadata v2 transactions version
     */
   ACCOUNT_METADATA_V2(1),

   /**
    * Modify mosaic metadata v2 transactions version
    * 
    */
   MOSAIC_METADATA_V2(1),

   /**
    * modify namespace metadata v2 transactions version
    * 
    */
   NAMESPACE_METADATA_V2(1),

   /**
    * modify mosaic modify levy transactions version
    * 
    */
   MODIFY_MOSAIC_LEVY(1),

   /**
    * modify remove mosaic levy transactions version
    * 
    */
   REMOVE_MOSAIC_LEVY(1),

   /**
    * Blockchain configuration change transaction
    */
   BLOCKCHAIN_CONFIG(1),
   
   /**
    * blockchain version update transaction
    */
   BLOCKCHAIN_UPGRADE(1),
   
   EXCHANGE_OFFER_ADD(4),
   EXCHANGE_OFFER(2),
   EXCHANGE_OFFER_REMOVE(2),

   DRIVE_PREPARE(1),
   DRIVE_JOIN(1),
   DRIVE_FILESYSTEM(1),
   DRIVE_FILES_DEPOSIT(1),
   DRIVE_END(1),
   DRIVE_FILES_REWARD(1),
   DRIVE_VERIFICATION_START(1),
   DRIVE_VERIFICATION_END(1);

   private int code;

   EntityVersion(int code) {
      this.code = code;
   }

   /**
    * Returns enum value.
    *
    * @return enum value
    */
   public int getValue() {
      return this.code;
   }
}