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

package io.proximax.sdk.model.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.proximax.sdk.model.network.NetworkType;

class AddressTest {

    @Test
    void testAddressCreation() {
        Address address = new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET);
        assertEquals("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", address.plain());
    }

    @Test
    void testAddressWithSpacesCreation() {
        Address address = new Address(" VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z ", NetworkType.TEST_NET);
        assertEquals("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", address.plain());
    }

    @Test
    void testLowerCaseAddressCreation() {
        Address address = new Address("vczgeq-biosjm-ww3vwm-vl4plm-zntmso-ii246p-ih6z", NetworkType.TEST_NET);
        assertEquals("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", address.plain());
    }

    @Test
    void addressInPrettyFormat() {
        Address address = new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET);
        assertEquals("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", address.pretty());
    }

    @Test
    void equality() {
        Address address1 = new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET);
        Address address2 = new Address("VCZGEQBIOSJMWW3VWMVL4PLMZNTMSOII246PIH6Z", NetworkType.TEST_NET);
        assertEquals(address1, address2);
    }

    @Test
    void noEquality() {
        Address address1 = new Address("VCZGEQ-BIOSJM-WW3VWM-VL4PLM-ZNTMSO-II246P-IH6Z", NetworkType.TEST_NET);
        Address address2 = new Address("VD3YBI-RQMWXS-6HHUJB-XFZJ56-62BJPX-OTKOOV-6HDO", NetworkType.TEST_NET);
        assertNotEquals(address1, address2);
    }

    @ParameterizedTest
    @MethodSource("assertExceptionProvider")
    @DisplayName("NetworkType")
    void testThrowErrorWhenNetworkTypeIsNotTheSameAsAddress(String rawAddress, NetworkType networkType) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Address(rawAddress, networkType);
        });
    }

    @ParameterizedTest
    @MethodSource("provider")
    @DisplayName("NetworkType")
    void testUInt64FromBigInteger(String rawAddress, NetworkType input) {
        Address address = new Address(rawAddress, input);
        assertEquals(input, address.getNetworkType());
    }

    @ParameterizedTest
    @MethodSource("publicKeys")
    @DisplayName("AddressFromPublicKey")
    void testCreateAddressFromPublicKey(String publicKey, NetworkType networkType, String input) {
        Address address = Address.createFromPublicKey(publicKey, networkType);
        assertEquals(input, address.plain());
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST),
                Arguments.of("MDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN),
                Arguments.of("VDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.TEST_NET),
                Arguments.of("XDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MAIN_NET),
                Arguments.of("WDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE_TEST),
                Arguments.of("ZDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE));
    }

    private static Stream<Arguments> assertExceptionProvider() {
        return Stream.of(
                Arguments.of("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN),
                Arguments.of("MDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MAIN_NET),
                Arguments.of("VDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MAIN_NET),
                Arguments.of("XDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.TEST_NET),
                Arguments.of("WDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE),
                Arguments.of("ZDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE_TEST),
                Arguments.of("WDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST));
    }

    private static Stream<Arguments> publicKeys() {
        return Stream.of(
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.MIJIN_TEST,
                        "SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.MIJIN,
                        "MARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJE5K5RYU"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.TEST_NET,
                        "VARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJGOH3FCE"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.MAIN_NET,
                        "XARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJF6CHIGW"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf",
                        NetworkType.PRIVATE_TEST, "WARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJHPRCU4F"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.PRIVATE,
                        "ZARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJF2S3UOQ"));
    }

}