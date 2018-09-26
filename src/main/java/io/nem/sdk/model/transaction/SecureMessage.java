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

import io.nem.core.crypto.BlockCipher;
import io.nem.core.crypto.CryptoEngines;
import io.nem.core.crypto.KeyPair;
import io.nem.core.crypto.PrivateKey;
import io.nem.core.crypto.PublicKey;

/**
 * The secure message model defines an encoded payload.
 *
 * @since 1.0
 */
public class SecureMessage extends Message {

    private final byte[] encodedPayload;

    /**
     * Constructor
     */
    private SecureMessage(byte[] encodedPayload) {
        super(MessageTypes.SECURE.getType());
        this.encodedPayload = encodedPayload;
    }

    public static SecureMessage createFromEncodedPayload(byte[] encodedPayload) {
        return new SecureMessage(encodedPayload);
    }

    public static SecureMessage createFromDecodedPayload(PrivateKey senderPrivateKey, PublicKey recipientPublicKey, byte[] decodedPayload) {
        final BlockCipher blockCipher = CryptoEngines.ed25519Engine().createBlockCipher(
                new KeyPair(senderPrivateKey), new KeyPair(recipientPublicKey));
        final byte[] encodedPayload = blockCipher.encrypt(decodedPayload);
        return new SecureMessage(encodedPayload);
    }

    /**
     * Returns the encoded payload
     *
     * @return the encoded payload
     */
    @Override
    public byte[] getEncodedPayload() {
        return encodedPayload;
    }

    /**
     * Decode the payload and returns it
     *
     * @param pairWithPrivateKey the key pair with private key
     * @param otherPair          the key pair of the other party
     * @return the decoded payload
     */
    @Override
    public byte[] getDecodedPayload(KeyPair pairWithPrivateKey, KeyPair otherPair) {
        final BlockCipher blockCipher = CryptoEngines.ed25519Engine().createBlockCipher(
                otherPair, pairWithPrivateKey);
        final byte[] decodedPayload = blockCipher.decrypt(encodedPayload);

        if (decodedPayload == null) {
            throw new MessagePayloadDecodeFailureException("Failed to decode message payload");
        }
        return decodedPayload;
    }
}
