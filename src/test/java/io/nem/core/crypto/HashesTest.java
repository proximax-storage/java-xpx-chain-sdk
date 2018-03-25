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

import io.nem.core.test.Utils;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

public class HashesTest {
    private static final HashTester SHA3_256_TESTER = new HashTester(Hashes::sha3_256, 32);
    private static final HashTester SHA3_512_TESTER = new HashTester(Hashes::sha3_512, 64);
    private static final HashTester RIPEMD160_TESTER = new HashTester(Hashes::ripemd160, 20);

    //region sha3_256

    private static void assertHashesAreDifferent(
            final Function<byte[], byte[]> hashFunction1,
            final Function<byte[], byte[]> hashFunction2) {

        // Arrange:
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final byte[] hash1 = hashFunction1.apply(input);
        final byte[] hash2 = hashFunction2.apply(input);

        // Assert:
        Assert.assertThat(hash2, IsNot.not(IsEqual.equalTo(hash1)));
    }

    @Test
    public void sha3_256HashHasExpectedByteLength() {
        // Assert:
        SHA3_256_TESTER.assertHashHasExpectedLength();
    }

    @Test
    public void sha3_256GeneratesSameHashForSameInputs() {
        // Assert:
        SHA3_256_TESTER.assertHashIsSameForSameInputs();
    }

    @Test
    public void sha3_256GeneratesSameHashForSameMergedInputs() {
        // Assert:
        SHA3_256_TESTER.assertHashIsSameForSplitInputs();
    }

    //endregion

    //region sha3_512

    @Test
    public void sha3_256GeneratesDifferentHashForDifferentInputs() {
        // Assert:
        SHA3_256_TESTER.assertHashIsDifferentForDifferentInputs();
    }

    @Test
    public void sha3_512HashHasExpectedByteLength() {
        // Assert:
        SHA3_512_TESTER.assertHashHasExpectedLength();
    }

    @Test
    public void sha3_512GeneratesSameHashForSameInputs() {
        // Assert:
        SHA3_512_TESTER.assertHashIsSameForSameInputs();
    }

    @Test
    public void sha3_512GeneratesSameHashForSameMergedInputs() {
        // Assert:
        SHA3_512_TESTER.assertHashIsSameForSplitInputs();
    }

    //endregion

    //region ripemd160

    @Test
    public void sha3_512GeneratesDifferentHashForDifferentInputs() {
        // Assert:
        SHA3_512_TESTER.assertHashIsDifferentForDifferentInputs();
    }

    @Test
    public void ripemd160HashHasExpectedByteLength() {
        // Assert:
        RIPEMD160_TESTER.assertHashHasExpectedLength();
    }

    @Test
    public void ripemd160GeneratesSameHashForSameInputs() {
        // Assert:
        RIPEMD160_TESTER.assertHashIsSameForSameInputs();
    }

    @Test
    public void ripemd160GeneratesSameHashForSameMergedInputs() {
        // Assert:
        RIPEMD160_TESTER.assertHashIsSameForSplitInputs();
    }

    //endregion

    //region different hash algorithm

    @Test
    public void ripemd160GeneratesDifferentHashForDifferentInputs() {
        // Assert:
        RIPEMD160_TESTER.assertHashIsDifferentForDifferentInputs();
    }

    @Test
    public void sha3_256AndRipemd160GenerateDifferentHashForSameInputs() {
        // Assert:
        assertHashesAreDifferent(Hashes::sha3_256, Hashes::ripemd160);
    }

    @Test
    public void sha3_256AndSha3_512GenerateDifferentHashForSameInputs() {
        // Assert:
        assertHashesAreDifferent(Hashes::sha3_256, Hashes::sha3_512);
    }

    @Test
    public void sha3_512AndRipemd160GenerateDifferentHashForSameInputs() {
        // Assert:
        assertHashesAreDifferent(Hashes::sha3_512, Hashes::ripemd160);
    }

    //endregion

    private static class HashTester {
        private final Function<byte[], byte[]> hashFunction;
        private final Function<byte[][], byte[]> hashMultipleFunction;
        private final int expectedHashLength;

        public HashTester(final Function<byte[][], byte[]> hashMultipleFunction, final int expectedHashLength) {
            this.hashMultipleFunction = hashMultipleFunction;
            this.hashFunction = input -> hashMultipleFunction.apply(new byte[][]{input});
            this.expectedHashLength = expectedHashLength;
        }

        private static byte[][] split(final byte[] input) {
            return new byte[][]{
                    Arrays.copyOfRange(input, 0, 17),
                    Arrays.copyOfRange(input, 17, 100),
                    Arrays.copyOfRange(input, 100, input.length)
            };
        }

        public void assertHashHasExpectedLength() {
            // Arrange:
            final byte[] input = Utils.generateRandomBytes();

            // Act:
            final byte[] hash = this.hashFunction.apply(input);

            // Assert:
            Assert.assertThat(hash.length, IsEqual.equalTo(this.expectedHashLength));
        }

        public void assertHashIsSameForSameInputs() {
            // Arrange:
            final byte[] input = Utils.generateRandomBytes();

            // Act:
            final byte[] hash1 = this.hashFunction.apply(input);
            final byte[] hash2 = this.hashFunction.apply(input);

            // Assert:
            Assert.assertThat(hash2, IsEqual.equalTo(hash1));
        }

        public void assertHashIsSameForSplitInputs() {
            // Arrange:
            final byte[] input = Utils.generateRandomBytes();

            // Act:
            final byte[] hash1 = this.hashFunction.apply(input);
            final byte[] hash2 = this.hashMultipleFunction.apply(split(input));

            // Assert:
            Assert.assertThat(hash2, IsEqual.equalTo(hash1));
        }

        public void assertHashIsDifferentForDifferentInputs() {
            // Arrange:
            final byte[] input1 = Utils.generateRandomBytes();
            final byte[] input2 = Utils.generateRandomBytes();

            // Act:
            final byte[] hash1 = this.hashFunction.apply(input1);
            final byte[] hash2 = this.hashFunction.apply(input2);

            // Assert:
            Assert.assertThat(hash2, IsNot.not(IsEqual.equalTo(hash1)));
        }
    }
}