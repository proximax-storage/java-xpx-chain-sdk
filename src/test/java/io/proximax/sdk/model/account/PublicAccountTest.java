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

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PublicAccountTest {
    private final String publicKey = "b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf";


    @Test
    void shouldCreatePublicAccountViaConstructor() {
        PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.MIJIN_TEST);
        assertEquals(publicKey, publicAccount.getPublicKey());
        assertEquals("SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP", publicAccount.getAddress().plain());
    }

    @Test
    void shouldCreatePublicAccountViaStaticConstructor() {
        PublicAccount publicAccount = PublicAccount.createFromPublicKey(publicKey, NetworkType.MIJIN_TEST);
        assertEquals(publicKey, publicAccount.getPublicKey());
        assertEquals("SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP", publicAccount.getAddress().plain());
    }

    @Test
    void equalityIsBasedOnPublicKeyAndNetwork() {
        PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.MIJIN_TEST);
        PublicAccount publicAccount2 = new PublicAccount(publicKey, NetworkType.MIJIN_TEST);
        assertEquals(publicAccount, publicAccount2);
    }

    @Test
    void equalityReturnsFalseIfNetworkIsDifferent() {
        PublicAccount publicAccount = new PublicAccount(publicKey, NetworkType.MIJIN_TEST);
        PublicAccount publicAccount2 = new PublicAccount(publicKey, NetworkType.MAIN_NET);
        assertNotEquals(publicAccount, publicAccount2);
    }
}