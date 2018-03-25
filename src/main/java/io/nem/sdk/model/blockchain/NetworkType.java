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

package io.nem.sdk.model.blockchain;

/**
 * Static class containing network type constants.
 *
 * @since 1.0
 */
public enum NetworkType {
    /**
     * Main net network
     */
    MAIN_NET(104),
    /**
     * Test net network
     */
    TEST_NET(152),
    /**
     * Mijin net network
     */
    MIJIN(96),
    /**
     * Mijin test net network
     */
    MIJIN_TEST(144);

    private final int value;

    NetworkType(int value) {
        this.value = value;
    }

    /**
     * Static constructor converting network raw value to enum instance.
     *
     * @return {@link NetworkType}
     */
    public static NetworkType rawValueOf(int value) {
        switch (value) {
            case 104:
                return NetworkType.MAIN_NET;
            case 152:
                return NetworkType.TEST_NET;
            case 96:
                return NetworkType.MIJIN;
            case 144:
                return NetworkType.MIJIN_TEST;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    /**
     * Returns enum value.
     *
     * @return int
     */
    public int getValue() {
        return this.value;
    }
}
