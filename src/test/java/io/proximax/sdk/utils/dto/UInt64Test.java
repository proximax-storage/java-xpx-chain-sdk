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

package io.proximax.sdk.utils.dto;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UInt64Test {

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(new int[]{100, 0}, BigInteger.valueOf(100)),
                Arguments.of(new int[]{1000, 0}, BigInteger.valueOf(1000)),
                Arguments.of(new int[]{100000, 0}, BigInteger.valueOf(100000)),
                Arguments.of(new int[]{12345, 99999}, new BigInteger("429492434645049")),
                Arguments.of(new int[]{1111, 2222}, new BigInteger("9543417332823"))
        );
    }

    @Test
    void hexShouldBePadded() {
        assertEquals("00000001", UInt64Utils.getPaddedHex(1));
        assertEquals("00000010", UInt64Utils.getPaddedHex(16));
        assertEquals("0000000f", UInt64Utils.getPaddedHex(15));
        assertEquals("ffffffff", UInt64Utils.getPaddedHex(-1));
    }

    @Test
    void zeroShouldReturnAnArrayOfTwoWithTwoZeros() {
        int[] result = UInt64Utils.fromBigInteger(BigInteger.valueOf(0));
        assertArrayEquals(new int[]{0, 0}, result);
    }

    @Test
    void oneShouldBeReturnedAsIntArray() {
        int[] result = UInt64Utils.fromBigInteger(BigInteger.valueOf(1));
        assertArrayEquals(new int[]{1, 0}, result);
    }

    @Test
    void bigIntegerNEMToHex() {
        String result = UInt64Utils.bigIntegerToHex(new BigInteger("-8884663987180930485"));
        assertEquals("84b3552d375ffa4b", result);
    }

    @Test
    void bigIntegerXEMToHex() {
        String result = UInt64Utils.bigIntegerToHex(new BigInteger("-3087871471161192663"));
        assertEquals("d525ad41d95fcf29", result);
    }
    
    @ParameterizedTest
    @MethodSource("provider")
    void dtoToBigInteger(int[] input, BigInteger expected) {
        ArrayList<Integer> uint = new ArrayList<>();
    	uint.add(input[0]);
    	uint.add(input[1]);
    	assertEquals(expected, UInt64Utils.toBigInt(uint));
    	
    }
    
    @ParameterizedTest
    @MethodSource("provider")
    void testUInt64FromBigInteger(int[] expected, BigInteger input) {
        int[] result = UInt64Utils.fromBigInteger(input);
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("provider")
    void testUInt64FromIntArray(int[] input, BigInteger expected) {
        BigInteger result = UInt64Utils.fromIntArray(input);
        assertEquals(expected, result);
    }
}