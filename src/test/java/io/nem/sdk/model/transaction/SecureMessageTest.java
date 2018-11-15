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

import io.nem.core.crypto.KeyPair;
import io.nem.core.crypto.PrivateKey;
import io.nem.core.crypto.PublicKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecureMessageTest {

    private static final String TEST_SENDER_PRIVATE_KEY = "8374B5915AEAB6308C34368B15ABF33C79FD7FEFC0DEAF9CC51BA57F120F1190";
    private static final String TEST_SENDER_PUBLIC_KEY = "9E7930144DA0845361F650BF78A36791ABF2577E251706ECA45480998FE61D18";

    private static final String TEST_RECIPIENT_PRIVATE_KEY = "369CB3195F88A16F8326DABBD37DA5F8458B55AA5DA6F7E2F756A12BE6CAA546";
    private static final String TEST_RECIPIENT_PUBLIC_KEY = "8E1A94D534EA6A3B02B0B967701549C21724C7644B2E4C20BF15D01D50097ACB";

    private static final String TEST_OTHER_PRIVATE_KEY = "8534E476C13A736645035D535EDF2759295FF1EF65E7FFBDA31501A3C1F3CB99";
    private static final String TEST_OTHER_PUBLIC_KEY = "10F3D152493F173EC9ED55F70606392FFB4E21A333EDD192AD4770AA8DF911ED";

    @Test
    void shouldCreateSecureMessageFromDecodedPayload() {
        SecureMessage secureMessage = SecureMessage.create(
                PrivateKey.fromHexString(TEST_SENDER_PRIVATE_KEY),
                PublicKey.fromHexString(TEST_RECIPIENT_PUBLIC_KEY),
                "test-message");

        assertEquals("test-message", secureMessage.decrypt(
                new KeyPair(PrivateKey.fromHexString(TEST_SENDER_PRIVATE_KEY)),
                new KeyPair(PublicKey.fromHexString(TEST_RECIPIENT_PUBLIC_KEY))
        ));
        assertNotNull(secureMessage.getEncodedPayload());
        assertTrue(MessageTypes.SECURE.getType() == secureMessage.getType());
    }

    @Test
    void shouldRetrieveDecodedPayloadUsingSenderPrivateKey() {
        final byte[] sampleEncodedPayload = sampleEncodedPayload();

        final SecureMessage secureMessage = SecureMessage.createFromEncodedPayload(sampleEncodedPayload);

        assertEquals("test-message", secureMessage.decrypt(
                new KeyPair(PrivateKey.fromHexString(TEST_SENDER_PRIVATE_KEY)),
                new KeyPair(PublicKey.fromHexString(TEST_RECIPIENT_PUBLIC_KEY))
        ));
        assertEquals(sampleEncodedPayload, secureMessage.getEncodedPayload());
        assertTrue(MessageTypes.SECURE.getType() == secureMessage.getType());
    }

    @Test
    void shouldRetrieveDecodedPayloadUsingRecipientPrivateKey() {
        final byte[] sampleEncodedPayload = sampleEncodedPayload();

        final SecureMessage secureMessage = SecureMessage.createFromEncodedPayload(sampleEncodedPayload);

        assertEquals("test-message", secureMessage.decrypt(
                new KeyPair(PrivateKey.fromHexString(TEST_RECIPIENT_PRIVATE_KEY)),
                new KeyPair(PublicKey.fromHexString(TEST_SENDER_PUBLIC_KEY))

        ));
        assertEquals(sampleEncodedPayload, secureMessage.getEncodedPayload());
        assertTrue(MessageTypes.SECURE.getType() == secureMessage.getType());
    }

    @Test
    void failRetrievingDecodedPayloadUsingWrongPrivateKey() {
        final byte[] sampleEncodedPayload = sampleEncodedPayload();

        final SecureMessage secureMessage = SecureMessage.createFromEncodedPayload(sampleEncodedPayload);

        assertThrows(MessagePayloadDecodeFailureException.class, () -> secureMessage.decrypt(
                new KeyPair(PrivateKey.fromHexString(TEST_OTHER_PRIVATE_KEY)),
                new KeyPair(PublicKey.fromHexString(TEST_SENDER_PUBLIC_KEY))
        ));
    }

    @Test
    void failRetrievingDecodedPayloadUsingWrongPublicKey() {
        final byte[] sampleEncodedPayload = sampleEncodedPayload();

        final SecureMessage secureMessage = SecureMessage.createFromEncodedPayload(sampleEncodedPayload);

        assertThrows(MessagePayloadDecodeFailureException.class, () -> secureMessage.decrypt(
                new KeyPair(PrivateKey.fromHexString(TEST_SENDER_PRIVATE_KEY)),
                new KeyPair(PublicKey.fromHexString(TEST_OTHER_PUBLIC_KEY))
        ));
    }

    @Test
    void failRetrievingDecodedPayloadUsingWrongBothKeys() {
        final byte[] sampleEncodedPayload = sampleEncodedPayload();

        final SecureMessage secureMessage = SecureMessage.createFromEncodedPayload(sampleEncodedPayload);

        assertThrows(MessagePayloadDecodeFailureException.class, () -> secureMessage.decrypt(
                new KeyPair(PrivateKey.fromHexString(TEST_OTHER_PRIVATE_KEY)),
                new KeyPair(PublicKey.fromHexString(TEST_OTHER_PUBLIC_KEY))
        ));
    }

    private byte[] sampleEncodedPayload() {
        return SecureMessage.create(
                PrivateKey.fromHexString(TEST_SENDER_PRIVATE_KEY),
                PublicKey.fromHexString(TEST_RECIPIENT_PUBLIC_KEY),
                "test-message").getEncodedPayload();
    }
}
