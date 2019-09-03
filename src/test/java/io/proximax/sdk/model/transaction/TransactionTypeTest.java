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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TransactionTypeTest {
    @Test
    void aggregateCompleteType() {
        EntityType transactionType = EntityType.AGGREGATE_COMPLETE;
        assertEquals(0x4141, transactionType.getValue());
        assertEquals(16705, transactionType.getValue());
        assertEquals(EntityType.AGGREGATE_COMPLETE, EntityType.rawValueOf(16705));
    }

    @Test
    void aggregateBondedType() {
        EntityType transactionType = EntityType.AGGREGATE_BONDED;
        assertEquals(0x4241, transactionType.getValue());
        assertEquals(16961, transactionType.getValue());
        assertEquals(EntityType.AGGREGATE_BONDED, EntityType.rawValueOf(16961));
    }

    @Test
    void mosaicCreationType() {
        EntityType transactionType = EntityType.MOSAIC_DEFINITION;
        assertEquals(0x414d, transactionType.getValue());
        assertEquals(16717, transactionType.getValue());
        assertEquals(EntityType.MOSAIC_DEFINITION, EntityType.rawValueOf(16717));
    }

    @Test
    void mosaicSupplyChangeType() {
        EntityType transactionType = EntityType.MOSAIC_SUPPLY_CHANGE;
        assertEquals(0x424d, transactionType.getValue());
        assertEquals(16973, transactionType.getValue());
        assertEquals(EntityType.MOSAIC_SUPPLY_CHANGE, EntityType.rawValueOf(16973));
    }

    @Test
    void multisigModificationType() {
        EntityType transactionType = EntityType.MODIFY_MULTISIG_ACCOUNT;
        assertEquals(0x4155, transactionType.getValue());
        assertEquals(16725, transactionType.getValue());
        assertEquals(EntityType.MODIFY_MULTISIG_ACCOUNT, EntityType.rawValueOf(16725));
    }

    @Test
    void namespaceCreationType() {
        EntityType transactionType = EntityType.REGISTER_NAMESPACE;
        assertEquals(0x414e, transactionType.getValue());
        assertEquals(16718, transactionType.getValue());
        assertEquals(EntityType.REGISTER_NAMESPACE, EntityType.rawValueOf(16718));
    }

    @Test
    void transferType() {
        EntityType transactionType = EntityType.TRANSFER;
        assertEquals(0x4154, transactionType.getValue());
        assertEquals(16724, transactionType.getValue());
        assertEquals(EntityType.TRANSFER, EntityType.rawValueOf(16724));
    }

    @Test
    void lockFundsType() {
        EntityType transactionType = EntityType.LOCK;
        assertEquals(0x4148, transactionType.getValue());
        assertEquals(16712, transactionType.getValue());
        assertEquals(EntityType.LOCK, EntityType.rawValueOf(16712));
    }

    @Test
    void secretLockType() {
        EntityType transactionType = EntityType.SECRET_LOCK;
        assertEquals(0x4152, transactionType.getValue());
        assertEquals(16722, transactionType.getValue());
        assertEquals(EntityType.SECRET_LOCK, EntityType.rawValueOf(16722));
    }

    @Test
    void secretProofType() {
        EntityType transactionType = EntityType.SECRET_PROOF;
        assertEquals(0x4252, transactionType.getValue());
        assertEquals(16978, transactionType.getValue());
        assertEquals(EntityType.SECRET_PROOF, EntityType.rawValueOf(16978));
    }

    @Test
    void addressAliasType() {
        EntityType transactionType = EntityType.ADDRESS_ALIAS;
        assertEquals(0x424E, transactionType.getValue());
        assertEquals(16974, transactionType.getValue());
        assertEquals(EntityType.ADDRESS_ALIAS, EntityType.rawValueOf(16974));
    }

    @Test
    void mosaicAliasType() {
        EntityType transactionType = EntityType.MOSAIC_ALIAS;
        assertEquals(0x434E, transactionType.getValue());
        assertEquals(17230, transactionType.getValue());
        assertEquals(EntityType.MOSAIC_ALIAS, EntityType.rawValueOf(17230));
    }

    @Test
    void accountPropertiesAddressType() {
        EntityType transactionType = EntityType.ACCOUNT_PROPERTIES_ADDRESS;
        assertEquals(0x4150, transactionType.getValue());
        assertEquals(16720, transactionType.getValue());
        assertEquals(EntityType.ACCOUNT_PROPERTIES_ADDRESS, EntityType.rawValueOf(16720));
    }

    @Test
    void accountPropertiesMosaic() {
        EntityType transactionType = EntityType.ACCOUNT_PROPERTIES_MOSAIC;
        assertEquals(0x4250, transactionType.getValue());
        assertEquals(16976, transactionType.getValue());
        assertEquals(EntityType.ACCOUNT_PROPERTIES_MOSAIC, EntityType.rawValueOf(16976));
    }

    @Test
    void accountPropertiesEntityType() {
        EntityType transactionType = EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE;
        assertEquals(0x4350, transactionType.getValue());
        assertEquals(17232, transactionType.getValue());
        assertEquals(EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE, EntityType.rawValueOf(17232));
    }

    @Test
    void accountLinkType() {
        EntityType transactionType = EntityType.ACCOUNT_LINK;
        assertEquals(0x414C, transactionType.getValue());
        assertEquals(16716, transactionType.getValue());
        assertEquals(EntityType.ACCOUNT_LINK, EntityType.rawValueOf(16716));
    }
}