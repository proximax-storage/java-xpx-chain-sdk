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
 * Enum containing hash type.
 *
 * @since 1.0
 */
public enum HashType {

    /**
     *  hashed using SHA3-256
     *  (Catapult Native)
     */
    SHA3_256(0),
    /**
     *  hashed using Keccak-256
     *  (ETH Compat)
     */
    KECCAK_256(1),
    /**
     * hashed twice: first with SHA-256 and then with RIPEMD-160
     * (BTC Compat)
     */
    HASH_160(2),
    /**
     * Hashed twice with SHA-256
     * (BTC Compat)
     */
    HASH_256(3);

    private int value;

    HashType(int value) {
        this.value = value;
    }

    public static HashType rawValueOf(int value) {
        switch (value) {
            case 0:
                return HashType.SHA3_256;
            case 1:
                return HashType.KECCAK_256;
            case 2:
                return HashType.HASH_160;
            case 3:
                return HashType.HASH_256;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    /**
     * Validate hash algorithm and hash have desired format
     * @param hashType  Hash type
     * @param input     Input hashed
     * @return boolean when format is correct
     */
    public static boolean Validator(HashType hashType, String input) {
        if (hashType == HashType.SHA3_256 && input.matches("-?[0-9a-fA-F]+")) {
            return input.length() == 64;
        }
        else if (hashType == HashType.KECCAK_256 && input.matches("-?[0-9a-fA-F]+")) {
            return input.length() == 64;
        }
        else if (hashType == HashType.HASH_160 && input.matches("-?[0-9a-fA-F]+")) {
            return input.length() == 40;
        }
        else if (hashType == HashType.HASH_256 && input.matches("-?[0-9a-fA-F]+")) {
            return input.length() == 64;
        }
        return false;
    }

    public int getValue() {
        return value;
    }
}
