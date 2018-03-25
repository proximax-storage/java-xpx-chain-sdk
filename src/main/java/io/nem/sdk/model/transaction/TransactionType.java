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

package io.nem.sdk.model.transaction;

/**
 * Enum containing transaction type constants.
 *
 * @since 1.0
 */
public enum TransactionType {

    /**
     * Aggregate complete transaction type.
     */
    AGGREGATE_COMPLETE(0x4141),

    /**
     * Aggregate bonded transaction type
     */
    AGGREGATE_BONDED(0x4241),

    /**
     * Mosaic definition transaction type.
     */
    MOSAIC_DEFINITION(0x414d),

    /**
     * Mosaic supply change transaction.
     */
    MOSAIC_SUPPLY_CHANGE(0x424d),

    /**
     * Modify multisig account transaction type.
     */
    MODIFY_MULTISIG_ACCOUNT(0x4155),

    /**
     * Register namespace transaction type.
     */
    REGISTER_NAMESPACE(0x414e),

    /**
     * Transfer Transaction transaction type.
     */
    TRANSFER(0x4154),

    /**
     * Lock transaction type
     */
    LOCK(0x414C),

    /**
     * Secret Lock Transaction type
     */
    SECRET_LOCK(0x424C),

    /**
     * Secret Proof transaction type
     */
    SECRET_PROOF(0x434C);

    private int value;

    TransactionType(int value) {
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
     * Static constructor converting transaction type raw value to enum instance.
     *
     * @return {@link TransactionType}
     */
    public static TransactionType rawValueOf(int value) {
        switch (value) {
            case 16724:
                return TransactionType.TRANSFER;
            case 16718:
                return TransactionType.REGISTER_NAMESPACE;
            case 16717:
                return TransactionType.MOSAIC_DEFINITION;
            case 16973:
                return TransactionType.MOSAIC_SUPPLY_CHANGE;
            case 16725:
                return TransactionType.MODIFY_MULTISIG_ACCOUNT;
            case 16716:
                return TransactionType.LOCK;
            case 16972:
                return TransactionType.SECRET_LOCK;
            case 17228:
                return TransactionType.SECRET_PROOF;
            case 16705:
                return TransactionType.AGGREGATE_COMPLETE;
            case 16961:
                return TransactionType.AGGREGATE_BONDED;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

}
