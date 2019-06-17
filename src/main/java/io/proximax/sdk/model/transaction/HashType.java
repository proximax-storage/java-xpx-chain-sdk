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

import java.util.function.UnaryOperator;

import io.proximax.core.crypto.Hashes;

/**
 * Enum containing hash type.
 *
 * @since 1.0
 */
public enum HashType {
    /**
     *  hashed using SHA3-256
     *  (Network Native)
     */
    SHA3_256(0, Hashes::sha3_256, "-?[0-9a-fA-F]+", 64),
    /**
     *  hashed using Keccak-256
     *  (ETH Compat)
     */
    KECCAK_256(1, Hashes::keccak256, "-?[0-9a-fA-F]+", 64),
    /**
     * hashed twice: first with SHA-256, then with RIPEMD-160, then add 12 bytes of 0s
     * (BTC Compat)
     */
    HASH_160(2, Hashes::hash160, "-?[0-9a-fA-F]+", 64),
    /**
     * Hashed twice with SHA-256
     * (BTC Compat)
     */
    HASH_256(3, Hashes::hash256,"-?[0-9a-fA-F]+", 64);

    private final int value;
    private final int length;
    private final UnaryOperator<byte[]> hashFunction;
    private final String pattern;
    
    HashType(int value, UnaryOperator<byte[]> hashFunction, String pattern, int length) {
        this.value = value;
        this.hashFunction = hashFunction;
        this.length = length;
        this.pattern = pattern;
    }

    /**
     * retrieve hash type by its code
     * 
     * @param value code of the hash type
     * @return hash type or throw IllegalArgumentException
     */
    public static HashType rawValueOf(int value) {
       for (HashType type : HashType.values()) {
          if (value == type.value) {
             return type;
          }
       }
       throw new IllegalArgumentException("Unsupported hash type code " + value);
    }

    /**
     * Validate hash algorithm and hash have desired format
     * @param hashType  Hash type
     * @param input     Input hashed
     * @return boolean when format is correct
     * 
     * @deprecated use {@link HashType#validate(String)}
     */
    @Deprecated
    public static boolean Validator(HashType hashType, String input) {
        return hashType.validate(input);
    }

    /**
     * validate that input is valid result of hashing using this hash type
     * 
     * @param input hashed value
     * @return true or false indicating whether input is ok
     */
    public boolean validate(String input) {
       return input.length() == length && input.matches(pattern);
    }
    
    /**
     * get the code of the hash type
     * 
     * @return the code of hash type recognized by server
     */
    public int getValue() {
        return value;
    }
    
    /**
     * hash the provided value using this hash type
     * 
     * @param value value to be hashed
     * @return hashed value
     */
    public byte[] hashValue(byte[] value) {
       return hashFunction.apply(value);
    }
}
