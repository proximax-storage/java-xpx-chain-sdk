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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SchemaTest {

    @Test
    @DisplayName("should copy the serialized object into the result")
    void resultCopy() {
        Schema schema = new Schema(Collections.singletonList(new FakeSchemaAttribute("attribute1")));
        byte[] result = schema.serialize(new byte[]{4, 5, 6});
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    @DisplayName("should concat the different serialized objects into the result")
    void concat() {
        Schema schema = new Schema(Arrays.asList(
                new FakeSchemaAttribute("attribute1"),
                new FakeSchemaAttribute("attribute2")));
        byte[] result = schema.serialize(new byte[]{4, 5, 6});
        assertArrayEquals(new byte[]{1, 2, 3, 1, 2, 3}, result);
    }
}

class FakeSchemaAttribute extends SchemaAttribute {

    FakeSchemaAttribute(String name) {
        super(name);
    }

    @Override
    public byte[] serialize(byte[] buffer, int position, int innerObjectPosition) {
        return new byte[]{1, 2, 3};
    }
}
