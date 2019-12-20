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

package io.proximax.sdk.model.blockchain;

/**
 * Static class containing network type constants.
 *
 * @since 1.0
 */
public enum NetworkType {
    /**
     * Main net network
     */
    MAIN_NET(0xb8, "public"),
    /**
     * Test net network
     */
    TEST_NET(0xa8, "publicTest"),
    /**
     * Private network
     */
    PRIVATE(0xc8, "private"),
    /**
     * Private test network
     */
    PRIVATE_TEST(0xb0, "privateTest"),
    /**
     * Mijin net network
     */
    MIJIN(0x60, "mijin"),
    /**
     * Mijin test net network
     */
    MIJIN_TEST(0x90, "mijinTest");

    private final int value;
    private final String name;
    
    NetworkType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Static constructor converting network raw value to enum instance.
     *
     * @param value numeric representation of network type
     * @return {@link NetworkType}
     */
    public static NetworkType rawValueOf(int value) {
        switch (value) {
            case 0xb8:
                return NetworkType.MAIN_NET;
            case 0xa8:
                return NetworkType.TEST_NET;
            case 0xc8:
                return NetworkType.PRIVATE;
            case 0xb0:
                return NetworkType.PRIVATE_TEST;
            case 0x60:
                return NetworkType.MIJIN;
            case 0x90:
                return NetworkType.MIJIN_TEST;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    /**
     * get network type by name
     * 
     * @param name name of network type as indicated by the network
     * @return network type
     */
    public static NetworkType getByName(String name) {
       for (NetworkType tp: values()) {
          if (tp.getName().equals(name)) {
             return tp;
          }
       }
       throw new IllegalArgumentException("Unknown netowrk type name: " + name);
    }
    
    /**
     * Returns enum value.
     *
     * @return int
     */
    public int getValue() {
        return this.value;
    }

   /**
    * @return the name of the network type
    */
   public String getName() {
      return name;
   }
}
