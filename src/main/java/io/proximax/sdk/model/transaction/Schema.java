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


import java.util.Arrays;
import java.util.List;

class Schema {
    private final List<SchemaAttribute> schemaDefinition;

    Schema(List<SchemaAttribute> schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }

    static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public byte[] serialize(byte[] bytes) {
        byte[] resultBytes = new byte[0];
        for (int i = 0; i < schemaDefinition.size(); ++i) {
            byte[] tmp = schemaDefinition.get(i).serialize(bytes, 4 + (i * 2));
            resultBytes = Schema.concat(resultBytes, tmp);
        }
        return resultBytes;
    }
}
