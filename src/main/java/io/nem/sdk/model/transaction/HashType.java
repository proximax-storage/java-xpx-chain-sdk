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
     * SHA3 512
     */
    SHA3_512(0);

    private int value;

    HashType(int value) {
        this.value = value;
    }

    public static HashType rawValueOf(int value) {
        switch (value) {
            case 0:
                return HashType.SHA3_512;
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
        if (hashType == HashType.SHA3_512 && input.matches("-?[0-9a-fA-F]+")) {
            return input.length() == 128;
        }
        return false;
    }

    public int getValue() {
        return value;
    }
}
