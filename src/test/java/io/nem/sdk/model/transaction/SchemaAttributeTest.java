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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemaAttributeTest {

    final byte[] buffer = {32, 0, 0, 0, 28, 0, 44, 0, 40, 0, 36, 0, 32, 0, 30, 0, 28, 0, 24, 0, 20, 0, 16, 0, 12, 0, 15, 0, 8, 0, 4, 0, 28, 0, 0, 0,
            (byte) 200, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 28, 0, 0, 0, 68, 0, 0, 0, 52, 0, 0, 0, 1, 65, 3, (byte) 144, 68, 0, 0, 0, 100, 0, 0, 0, (byte) 166, 0, 0,
            0, 25, 0, 0, 0, (byte) 144, (byte) 232, (byte) 254, (byte) 189, 103, 29, (byte) 212, 27, (byte) 238, (byte) 148, (byte) 236, 59,
            (byte) 165, (byte) 131, 28, (byte) 182, 8, (byte) 163, 18, (byte) 194, (byte) 242, 3, (byte) 186, (byte) 132, (byte) 172, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 69, (byte) 164, 14, (byte) 203, 10, 0, 0, 0, 32, 0, 0, 0, (byte) 154, 73, 54, 100, 6, (byte) 172,
            (byte) 169, 82, (byte) 184, (byte) 139, (byte) 173, (byte) 245, (byte) 241, (byte) 233, (byte) 190, 108, (byte) 228, (byte) 150,
            (byte) 129, 65, 3, 90, 96, (byte) 190, 80, 50, 115, (byte) 234, 101, 69, 107, 36, 64, 0, 0, 0, 38, (byte) 167, (byte) 193, (byte) 210,
            7, 30, (byte) 251, (byte) 149, (byte) 236, 15, 91, (byte) 233, 73, (byte) 174, 79, 86, 20, (byte) 133, (byte) 161, (byte) 167, 112,
            102, 42, (byte) 244, (byte) 246, (byte) 239, 78, 29, 104, (byte) 150, (byte) 190, 48, (byte) 230, 111, (byte) 129, (byte) 164, 66,
            29, (byte) 244, 75, 46, (byte) 150, 68, (byte) 242, 76, 26, 69, (byte) 205, (byte) 205, 122, (byte) 253, (byte) 219, (byte) 142,
            (byte) 171, 28, (byte) 217, (byte) 139, 28, (byte) 133, (byte) 247, 59, 100, (byte) 161, 14, 1, 0, 0, 0, 12, 0, 0, 0, 8, 0, 12, 0, 8, 0, 4,
            0, 8, 0, 0, 0, 8, 0, 0, 0, 16, 0, 0, 0, 2, 0, 0, 0, (byte) 128, (byte) 150, (byte) 152, 0, 0, 0, 0, 0, 2, 0, 0, 0, 41, (byte) 207, 95, (byte) 217,
            65, (byte) 173, 37, (byte) 213, 8, 0, 8, 0, 0, 0, 4, 0, 8, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0};

    @Test
    @DisplayName("should read int32")
    void readInt32() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int result = fakeSchemaAttribute.readInt32(3, buffer);
        assertEquals(738204672, result);
    }

    @Test
    @DisplayName("should read int16")
    void readInt16() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int result = fakeSchemaAttribute.readInt16(3, buffer);
        assertEquals(7168, result);
    }

    @Test
    @DisplayName("should calculate offset")
    void offset() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int result = fakeSchemaAttribute.__offset(32, 4, buffer);
        assertEquals(40, result);
    }

    @Test
    @DisplayName("should calculate vector length")
    void vectorLength() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int result = fakeSchemaAttribute.__vector_length(68, buffer);
        assertEquals(64, result);
    }

    @Test
    @DisplayName("should calculate indirect")
    void indirect() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int result = fakeSchemaAttribute.__indirect(68, buffer);
        assertEquals(168, result);
    }

    @Test
    @DisplayName("should find param in byte array")
    void findParam() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        byte[] result = fakeSchemaAttribute.findParam(32, 14, buffer, Constants.SIZEOF_INT);
        assertEquals(52, result[0]);
    }

    @Test
    @DisplayName("should find vector in byte array")
    void findVector() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        byte[] result = fakeSchemaAttribute.findVector(32, 18, buffer, Constants.SIZEOF_BYTE);
        assertArrayEquals(new byte[]{(byte) 144, (byte) 232, (byte) 254, (byte) 189, 103, 29, (byte) 212, 27, (byte) 238, (byte) 148, (byte) 236, 59,
                (byte) 165, (byte) 131, 28, (byte) 182, 8, (byte) 163, 18, (byte) 194, (byte) 242, 3, (byte) 186, (byte) 132, (byte) 172}, result);
    }


    @Test
    @DisplayName("should find vector in start position and byte array")
    void findVectorInStartPosition() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int innerObjectPosition = fakeSchemaAttribute.findObjectStartPosition(32, 24, buffer);
        byte[] result = fakeSchemaAttribute.findVector(innerObjectPosition, 6, buffer, Constants.SIZEOF_BYTE);
        assertEquals(0, result[0]);
    }

    @Test
    @DisplayName("should find object start position")
    void findObjectStartPosition() {
        FakeSchemaAttribute fakeSchemaAttribute = new FakeSchemaAttribute("");
        int result = fakeSchemaAttribute.findObjectStartPosition(32, 24, buffer);
        assertEquals(296, result);
    }
}