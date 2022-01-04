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

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.network.NetworkType;

class PublicAccountTest {
    private final String publicKey = "0D22E9D42F124072E14C4F804E4FC7F5431C831EAF03BEFD55D521B9A9D0B89D";


    @Test
    void shouldCreatePublicAccountViaConstructor() {
        PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.TEST_NET);
        assertEquals(publicKey, publicAccount.getPublicKey());
        assertEquals("VDPQS6FBYDN3SD2QJPHUWRYWNHSSOQ2Q35VI7TDP", publicAccount.getAddress().plain());
    }

    @Test
    void shouldCreatePublicAccountViaStaticConstructor() {
        PublicAccount publicAccount = PublicAccount.createFromPublicKey(publicKey, NetworkType.TEST_NET);
        assertEquals(publicKey, publicAccount.getPublicKey());
        assertEquals("VDPQS6FBYDN3SD2QJPHUWRYWNHSSOQ2Q35VI7TDP", publicAccount.getAddress().plain());
    }

    @Test
    void equalityIsBasedOnPublicKeyAndNetwork() {
        PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.TEST_NET);
        PublicAccount publicAccount2 = new PublicAccount(publicKey, NetworkType.TEST_NET);
        assertEquals(publicAccount, publicAccount2);
    }

    @Test
    void equalityReturnsFalseIfNetworkIsDifferent() {
        PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.TEST_NET);
        PublicAccount publicAccount2 = new PublicAccount(publicKey, NetworkType.MAIN_NET);
        assertNotEquals(publicAccount, publicAccount2);
    }
    
    @Test
    void equalityReturnsFalseForNull() {
       PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.TEST_NET);
       assertNotEquals(publicAccount, null);
    }
    
    @Test
    void equalityReturnsFalseForDifferentClass() {
       PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.TEST_NET);
       assertNotEquals(publicAccount, "hello");
    }
}