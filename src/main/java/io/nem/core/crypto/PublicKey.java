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

import io.nem.core.utils.HexEncoder;

import java.util.Arrays;

/**
 * Represents a public key.
 */
public class PublicKey {
    private final byte[] value;

    /**
     * Creates a new public key.
     *
     * @param bytes The raw public key value.
     */
    public PublicKey(final byte[] bytes) {
        this.value = bytes;
    }

    /**
     * Creates a public key from a hex string.
     *
     * @param hex The hex string.
     * @return The new public key.
     */
    public static PublicKey fromHexString(final String hex) {
        try {
            return new PublicKey(HexEncoder.getBytes(hex));
        } catch (final IllegalArgumentException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * Gets the raw public key value.
     *
     * @return The raw public key value.
     */
    public byte[] getRaw() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof PublicKey)) {
            return false;
        }

        final PublicKey rhs = (PublicKey) obj;
        return Arrays.equals(this.value, rhs.value);
    }

    @Override
    public String toString() {
        return HexEncoder.getString(this.value);
    }
}
