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

package io.proximax.core.crypto.ed25519;

import java.math.BigInteger;
import java.security.SecureRandom;

import io.proximax.core.crypto.CryptoEngines;
import io.proximax.core.crypto.KeyGenerator;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;
import io.proximax.core.crypto.ed25519.arithmetic.Ed25519EncodedFieldElement;
import io.proximax.core.crypto.ed25519.arithmetic.Ed25519Group;
import io.proximax.core.crypto.ed25519.arithmetic.Ed25519GroupElement;

/**
 * Implementation of the key generator for Ed25519.
 */
public class Ed25519KeyGenerator implements KeyGenerator {
    private final SecureRandom random;

    public Ed25519KeyGenerator() {
        this.random = new SecureRandom();
    }

    @Override
    public KeyPair generateKeyPair() {
        final byte[] seed = new byte[32];
        this.random.nextBytes(seed);

        // seed is the private key.
        final PrivateKey privateKey = new PrivateKey(new BigInteger(seed));

        return new KeyPair(privateKey, CryptoEngines.ed25519Engine());
    }

    @Override
    public PublicKey derivePublicKey(final PrivateKey privateKey) {
        final Ed25519EncodedFieldElement a = Ed25519Utils.prepareForScalarMultiply(privateKey);

        // a * base point is the public key.
        final Ed25519GroupElement pubKey = Ed25519Group.BASE_POINT.scalarMultiply(a);

        // verification of signatures will be about twice as fast when pre-calculating
        // a suitable table of group elements.
        return new PublicKey(pubKey.encode().getRaw());
    }
}
