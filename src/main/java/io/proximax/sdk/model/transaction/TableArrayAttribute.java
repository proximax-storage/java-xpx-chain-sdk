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

import java.util.List;

class TableArrayAttribute extends SchemaAttribute {
    private final List<SchemaAttribute> schema;

    TableArrayAttribute(String name, List<SchemaAttribute> schema) {
        super(name);
        this.schema = schema;
    }

    @Override
    byte[] serialize(byte[] buffer, int position, int innerObjectPosition) {
        byte[] resultBytes = new byte[0];
        int arrayLength = findArrayLength(innerObjectPosition, position, buffer);

        for (int i = 0; i < arrayLength; i++) {
            int startArrayPosition = findObjectArrayElementStartPosition(innerObjectPosition, position, buffer, i);
            for (int j = 0; j < this.schema.size(); ++j) {
                byte[] tmp = this.schema.get(j).serialize(buffer, 4 + j * 2, startArrayPosition);
                resultBytes = Schema.concat(resultBytes, tmp);
            }
        }
        return resultBytes;
    }
}
