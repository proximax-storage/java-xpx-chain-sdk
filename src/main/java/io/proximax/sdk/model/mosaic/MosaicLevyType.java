/*
 * Copyright 2022 ProximaX
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

package io.proximax.sdk.model.mosaic;

/**
 * Type of the Mosaic Levy
 */
public enum MosaicLevyType {
    NONE((byte)0), LEVYABSOLUTEFEE((byte)1), LEVYPERCENTILEFEE((byte)2);

    private final byte type;

    /**
     * @param type
     */
     MosaicLevyType(final byte type) {
        this.type = type;
    }
   
    /**
     * Returns enum value.
     *
     * @return enum value
     */
    public byte getValue() {
        return type;
    }

    /**
     * retrieve mosaic levy type by the raw value
     * 
     * @param code of the mosaic levy type
     * @return mosaic levy type
     */
    public static MosaicLevyType rawValueOf(int code) {
        for (MosaicLevyType mosaiclevytype : MosaicLevyType.values()) {
            if (code == mosaiclevytype.type) {
                return mosaiclevytype;
            }
        }
        throw new IllegalArgumentException("Unsupported mosaic levy value " + code);
    }
}
