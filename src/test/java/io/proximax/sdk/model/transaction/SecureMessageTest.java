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

package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;

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
        assertTrue(MessageType.SECURE.getCode() == secureMessage.getTypeCode());
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
        assertTrue(MessageType.SECURE.getCode() == secureMessage.getTypeCode());
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
        assertTrue(MessageType.SECURE.getCode() == secureMessage.getTypeCode());
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

   @Test
   @Disabled("Data from NIS1 are not supported (yet?)")
   void decodeFromNis1() {
      String recipientPrivateKeyHex = "9f5db5ba1b77c19f4f12a5236b7804687bad9ab0e1db2fe3fa09f1f31c0f3b96";
      String senderPublicKeyHex = "c735e505512414216a8cf54e25b3a444b16194c1df908ef25b9fdc70b91a84c4";
      PrivateKey recipientPrivateKey = PrivateKey.fromHexString(recipientPrivateKeyHex);
      PublicKey senderPublicKey = PublicKey.fromHexString(senderPublicKeyHex);
      // do some basic test
      String testStr = SecureMessage.create(recipientPrivateKey, senderPublicKey, "ahoj")
            .decrypt(new KeyPair(recipientPrivateKey), new KeyPair(senderPublicKey));
      assertEquals("ahoj", testStr);
      // now try actual message from nis1
      String encryptedMessage = "ffe377bc09e4e44afb4ba8a5fd68b618ffe32b10b8568484a3f65fe41178e7d90662134149c34104c90de6c0db0a10f1eb0db76b7d28c49804a5af859c470b89";
      SecureMessage secMsg = SecureMessage.createFromEncodedPayload(Hex.decode(encryptedMessage));
      String decryptedMessage = secMsg.decrypt(new KeyPair(recipientPrivateKey), new KeyPair(senderPublicKey));
      assertEquals("secret message", decryptedMessage);
   }
    
    private byte[] sampleEncodedPayload() {
        return SecureMessage.create(
                PrivateKey.fromHexString(TEST_SENDER_PRIVATE_KEY),
                PublicKey.fromHexString(TEST_RECIPIENT_PUBLIC_KEY),
                "test-message").getEncodedPayload();
    }
}
