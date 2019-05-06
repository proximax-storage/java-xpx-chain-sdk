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

package io.proximax.core.test;

import java.math.BigInteger;
import java.security.SecureRandom;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;
import io.proximax.core.crypto.Signature;
import io.proximax.core.utils.ExceptionUtils;

/**
 * Static class containing test utilities.
 */
public class Utils {

    /**
     * Generates a random public key.
     *
     * @return A random public key.
     */
    public static PublicKey generateRandomPublicKey() {
        final KeyPair pair = new KeyPair();
        return pair.getPublicKey();
    }


    public static Long generateRandomId() {
        final SecureRandom rand = new SecureRandom();
        return rand.nextLong();
    }

    /**
     * Generates a random signature.
     *
     * @return A random signature.
     */
    public static Signature generateRandomSignature() {
        final byte[] bytes = Utils.generateRandomBytes(64);
        return new Signature(bytes);
    }

    /**
     * Generates a byte array containing random data.
     *
     * @return A byte array containing random data.
     */
    public static byte[] generateRandomBytes() {
        return generateRandomBytes(214);
    }

    /**
     * Generates a byte array containing random data.
     *
     * @param numBytes The number of bytes to generate.
     * @return A byte array containing random data.
     */
    public static byte[] generateRandomBytes(final int numBytes) {
        final SecureRandom rand = new SecureRandom();
        final byte[] input = new byte[numBytes];
        rand.nextBytes(input);
        return input;
    }


    /**
     * Increments a single character in the specified string.
     *
     * @param s     The string
     * @param index The index of the character to increment
     * @return The resulting string
     */
    public static String incrementAtIndex(final String s, final int index) {
        final char[] chars = s.toCharArray();
        chars[index] = (char) (chars[index] + 1);
        return new String(chars);
    }

    /**
     * Changes a single character in the specified base 32 string.
     *
     * @param s     A base 32 string
     * @param index The index of the character to change
     * @return The resulting base 32 string
     */
    public static String modifyBase32AtIndex(final String s, final int index) {
        final char[] chars = s.toCharArray();
        final char currentChar = chars[index];

        char newChar = (char) (currentChar + 1);
        switch (currentChar) {
            case 'Z':
            case '7':
                newChar = 'A';
        }

        chars[index] = newChar;
        return new String(chars);
    }

    /**
     * Increments a single byte in the specified byte array.
     *
     * @param bytes The byte array
     * @param index The index of the byte to increment
     * @return The resulting byte array
     */
    private static byte[] incrementAtIndex(final byte[] bytes, final int index) {
        final byte[] copy = new byte[bytes.length];
        System.arraycopy(bytes, 0, copy, 0, bytes.length);
        ++copy[index];
        return copy;
    }

    /**
     * Creates a string initialized with a single character.
     *
     * @param ch       The character used in the string.
     * @param numChars The number of characters in hte string.
     * @return A string of length numChars initialized to ch.
     */
    public static String createString(final char ch, final int numChars) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numChars; ++i) {
            builder.append(ch);
        }

        return builder.toString();
    }

    /**
     * Waits on the specified monitor.
     *
     * @param monitor The monitor.
     */
    public static void monitorWait(final Object monitor) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (monitor) {
            ExceptionUtils.propagateVoid(monitor::wait);
        }
    }

    /**
     * Signals the specified monitor.
     *
     * @param monitor The monitor.
     */
    public static void monitorSignal(final Object monitor) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    /**
     * Mutates key into a slightly different key.
     *
     * @param key The original key.
     * @return A slightly different key
     */
    public static PublicKey mutate(final PublicKey key) {
        return new PublicKey(Utils.incrementAtIndex(key.getRaw(), 12));
    }

    /**
     * Mutates key into a slightly different key.
     *
     * @param key The original key.
     * @return A slightly different key
     */
    public static PrivateKey mutate(final PrivateKey key) {
        return new PrivateKey(key.getRaw().add(BigInteger.ONE));
    }

    //endregion
}
