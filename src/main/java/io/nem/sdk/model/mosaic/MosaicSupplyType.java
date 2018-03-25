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

package io.nem.sdk.model.mosaic;

/**
 * Enum containing mosaic supply type.
 *
 * @since 1.0
 */
public enum MosaicSupplyType {
    /**
     * Decrease the supply.
     */
    DECREASE(0),
    /**
     * Increase the supply.
     */
    INCREASE(1);

    private int value;

    MosaicSupplyType(int value) {
        this.value = value;
    }

    public static MosaicSupplyType rawValueOf(int value) {
        switch (value) {
            case 0:
                return MosaicSupplyType.DECREASE;
            case 1:
                return MosaicSupplyType.INCREASE;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    public int getValue() {
        return value;
    }
}
