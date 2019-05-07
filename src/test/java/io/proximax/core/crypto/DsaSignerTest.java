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

package io.proximax.core.crypto;

import io.proximax.core.crypto.CryptoEngine;
import io.proximax.core.crypto.CryptoException;
import io.proximax.core.crypto.DsaSigner;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.Signature;
import io.proximax.core.test.Utils;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigInteger;

public abstract class DsaSignerTest {

    @Test
    public void signedDataCanBeVerified() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final Signature signature = dsaSigner.sign(input);

        // Assert:
        Assert.assertThat(dsaSigner.verify(input, signature), IsEqual.equalTo(true));
    }

    @Test
    public void dataSignedWithKeyPairCannotBeVerifiedWithDifferentKeyPair() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp1 = KeyPair.random(engine);
        final KeyPair kp2 = KeyPair.random(engine);
        final DsaSigner dsaSigner1 = this.getDsaSigner(kp1);
        final DsaSigner dsaSigner2 = this.getDsaSigner(kp2);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final Signature signature1 = dsaSigner1.sign(input);
        final Signature signature2 = dsaSigner2.sign(input);

        // Assert:
        Assert.assertThat(dsaSigner1.verify(input, signature1), IsEqual.equalTo(true));
        Assert.assertThat(dsaSigner1.verify(input, signature2), IsEqual.equalTo(false));
        Assert.assertThat(dsaSigner2.verify(input, signature1), IsEqual.equalTo(false));
        Assert.assertThat(dsaSigner2.verify(input, signature2), IsEqual.equalTo(true));
    }

    @Test
    public void signaturesReturnedBySignAreDeterministic() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final Signature signature1 = dsaSigner.sign(input);
        final Signature signature2 = dsaSigner.sign(input);

        // Assert:
        Assert.assertThat(signature1, IsEqual.equalTo(signature2));
    }

    @Test(expected = CryptoException.class)
    public void cannotSignPayloadWithoutPrivateKey() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = new KeyPair(KeyPair.random(engine).getPublicKey(), engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        dsaSigner.sign(input);
    }

    @Test
    public void isCanonicalReturnsTrueForCanonicalSignature() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final Signature signature = dsaSigner.sign(input);

        // Assert:
        Assert.assertThat(dsaSigner.isCanonicalSignature(signature), IsEqual.equalTo(true));
    }

    @Test
    public void verifyCallsIsCanonicalSignature() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = Mockito.spy(this.getDsaSigner(kp));
        final byte[] input = Utils.generateRandomBytes();
        final Signature signature = new Signature(BigInteger.ONE, BigInteger.ONE);

        // Act:
        dsaSigner.verify(input, signature);

        // Assert:
        Mockito.verify(dsaSigner, Mockito.times(1)).isCanonicalSignature(signature);
    }

    protected DsaSigner getDsaSigner(final KeyPair keyPair) {
        return this.getCryptoEngine().createDsaSigner(keyPair);
    }

    protected abstract CryptoEngine getCryptoEngine();
}
