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

package io.nem.core.crypto;

import io.nem.core.utils.ExceptionUtils;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.Security;

/**
 * Static class that exposes hash functions.
 */
public class Hashes {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Performs a SHA3-256 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] sha3_256(final byte[]... inputs) {

        return hash("SHA3-256", inputs);
    }

    /**
     * Performs a SHA3-512 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] sha3_512(final byte[]... inputs) {
        return hash("SHA3-512", inputs);
    }

    /**
     * Performs a RIPEMD160 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] ripemd160(final byte[]... inputs) {
        return hash("RIPEMD160", inputs);
    }

    /**
     * Performs a KECCAK_256 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] keccak256(final byte[] inputs) {

        Keccak.Digest256 keccak = new Keccak.Digest256();
        keccak.update(inputs);

        return keccak.digest();
    }

    /**
     * Performs a KECCAK_256 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] keccak256(final byte[][] inputs) {

        Keccak.Digest256 keccak = new Keccak.Digest256();

        byte[] concat_inputs = new byte[0];
        byte[] concat_inputsCopy;

        for(int i=0; i<inputs.length; i++) {

            concat_inputsCopy = new byte[ concat_inputs.length];

            System.arraycopy(concat_inputs, 0, concat_inputsCopy,  0, concat_inputs.length );

            concat_inputs = new byte[ concat_inputsCopy.length + inputs[i].length];

            System.arraycopy(concat_inputsCopy, 0, concat_inputs,  0, concat_inputsCopy.length );
            System.arraycopy(inputs[i], 0, concat_inputs,  concat_inputsCopy.length, inputs[i].length );
        }
        keccak.update(concat_inputs);

        return keccak.digest();
    }

    /**
     * Performs a HASH_160 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] hash160(final byte[]... inputs) {

        byte[] hashed_sha256 = hash("SHA256", inputs);

        return hash("RIPEMD160", Hex.toHexString(hashed_sha256).getBytes() );
    }

    /**
     * Performs a HASH_256 hash of the concatenated inputs.
     *
     * @param inputs The byte arrays to concatenate and hash.
     * @return The hash of the concatenated inputs.
     * @throws CryptoException if the hash operation failed.
     */
    public static byte[] hash256(final byte[]... inputs) {

        byte[] hashed_sha256 = hash("SHA256", inputs);

        return hash("SHA256", Hex.toHexString(hashed_sha256).getBytes() );
    }

    private static byte[] hash(final String algorithm, final byte[]... inputs) {
        return ExceptionUtils.propagate(
                () -> {
                    final MessageDigest digest = MessageDigest.getInstance(algorithm, "BC");

                    for (final byte[] input : inputs) {
                        digest.update(input);
                    }

                    return digest.digest();
                },
                CryptoException::new);
    }

}
