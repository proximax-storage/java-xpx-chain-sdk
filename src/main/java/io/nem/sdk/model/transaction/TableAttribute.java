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

import java.util.List;

class TableAttribute extends SchemaAttribute {
    private final List<SchemaAttribute> schema;

    TableAttribute(String name, List<SchemaAttribute> schema) {
        super(name);
        this.schema = schema;
    }

    @Override
    byte[] serialize(byte[] buffer, int position, int innerObjectPosition) {
        byte[] resultBytes = new byte[0];
        int tableStartPosition = findObjectStartPosition(innerObjectPosition, position, buffer);
        for (int i = 0; i < this.schema.size(); ++i) {
            byte[] tmp = schema.get(i).serialize(buffer, 4 + (i * 2), tableStartPosition);
            resultBytes = Schema.concat(resultBytes, tmp);
        }
        return resultBytes;
    }
}
