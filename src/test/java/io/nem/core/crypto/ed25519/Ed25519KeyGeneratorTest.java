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

package io.nem.core.crypto.ed25519;

import io.nem.core.crypto.*;
import io.nem.core.crypto.ed25519.arithmetic.Ed25519EncodedGroupElement;
import io.nem.core.crypto.ed25519.arithmetic.MathUtils;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class Ed25519KeyGeneratorTest extends KeyGeneratorTest {

    @Test
    public void derivedPublicKeyIsValidPointOnCurve() {
        // Arrange:
        final KeyGenerator generator = this.getKeyGenerator();
        for (int i = 0; i < 100; i++) {
            final KeyPair kp = generator.generateKeyPair();

            // Act:
            final PublicKey publicKey = generator.derivePublicKey(kp.getPrivateKey());

            // Assert (throws if not on the curve):
            new Ed25519EncodedGroupElement(publicKey.getRaw()).decode();
        }
    }

    @Test
    public void derivePublicKeyReturnsExpectedPublicKey() {
        // Arrange:
        final KeyGenerator generator = this.getKeyGenerator();
        for (int i = 0; i < 100; i++) {
            final KeyPair kp = generator.generateKeyPair();

            // Act:
            final PublicKey publicKey1 = generator.derivePublicKey(kp.getPrivateKey());
            final PublicKey publicKey2 = MathUtils.derivePublicKey(kp.getPrivateKey());

            // Assert:
            Assert.assertThat(publicKey1, IsEqual.equalTo(publicKey2));
        }
    }

    @Test
    public void derivePublicKey() {
        final KeyGenerator generator = this.getKeyGenerator();
        final KeyPair keyPair = new KeyPair(PrivateKey.fromHexString("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d"));

        final PublicKey publicKey = generator.derivePublicKey(keyPair.getPrivateKey());

        final PublicKey expected = PublicKey.fromHexString("1026d70e1954775749c6811084d6450a3184d977383f0e4282cd47118af37755");
        Assert.assertThat(publicKey, IsEqual.equalTo(expected));
    }

    @Override
    protected CryptoEngine getCryptoEngine() {
        return CryptoEngines.ed25519Engine();
    }
}
