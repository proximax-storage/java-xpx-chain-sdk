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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testAddressCreation() {
        Address address = new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN_TEST);
        assertEquals("SDGLFWDSHILTIUHGIBH5UGX2VYF5VNJEKCCDBR26", address.plain());
    }

    @Test
    void testAddressWithSpacesCreation() {
        Address address = new Address(" SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26 ", NetworkType.MIJIN_TEST);
        assertEquals("SDGLFWDSHILTIUHGIBH5UGX2VYF5VNJEKCCDBR26", address.plain());
    }

    @Test
    void testLowerCaseAddressCreation() {
        Address address = new Address("sdglfw-dshilt-iuhgib-h5ugx2-vyf5vn-jekccd-br26", NetworkType.MIJIN_TEST);
        assertEquals("SDGLFWDSHILTIUHGIBH5UGX2VYF5VNJEKCCDBR26", address.plain());
    }

    @Test
    void addressInPrettyFormat() {
        Address address = new Address("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
        assertEquals("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", address.pretty());
    }

    @Test
    void equality() {
        Address address1 = new Address("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
        Address address2 = new Address("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", NetworkType.MIJIN_TEST);
        assertEquals(address1, address2);
    }

    @Test
    void noEquality() {
        Address address1 = new Address("SRRRRR-TTTTTT-555555-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
        Address address2 = new Address("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
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
                Arguments.of("ZDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE)
        );
    }

    private static Stream<Arguments> assertExceptionProvider() {
        return Stream.of(
                Arguments.of("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MIJIN),
                Arguments.of("MDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MAIN_NET),
                Arguments.of("VDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.MAIN_NET),
                Arguments.of("XDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.TEST_NET),
                Arguments.of("WDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE),
                Arguments.of("ZDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26", NetworkType.PRIVATE_TEST)
        );
    }

    private static Stream<Arguments> publicKeys() {
        return Stream.of(
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.MIJIN_TEST, "SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.MIJIN, "MARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJE5K5RYU"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.TEST_NET, "VARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJGOH3FCE"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.MAIN_NET, "XARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJF6CHIGW"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.PRIVATE_TEST, "WARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJHPRCU4F"),
                Arguments.of("b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf", NetworkType.PRIVATE, "ZARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJF2S3UOQ")
        );
    }

}