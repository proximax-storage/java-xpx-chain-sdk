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

enum Constants {
    /**
     * The number of bytes in an `byte`.
     */
    SIZEOF_BYTE(1),
    /**
     * The number of bytes in a `short`.
     */
    SIZEOF_SHORT(2),
    /**
     * The number of bytes in an `int`.
     */
    SIZEOF_INT(4),
    /**
     * The number of bytes in an `float`.
     */
    SIZEOF_FLOAT(4),
    /**
     * The number of bytes in an `long`.
     */
    SIZEOF_LONG(8),
    /**
     * The number of bytes in an `double`.
     */
    SIZEOF_DOUBLE(8),
    /**
     * The number of bytes in a file identifier.
     */
    FILE_IDENTIFIER_LENGTH(4);

    private int value;

    Constants(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


}
