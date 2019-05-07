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

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import io.proximax.core.crypto.CryptoEngine;
import io.proximax.core.crypto.KeyAnalyzer;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PublicKey;

public abstract class KeyAnalyzerTest {

    @Test
    public void isKeyCompressedReturnsTrueForCompressedPublicKey() {
        // Arrange:
        final KeyAnalyzer analyzer = this.getKeyAnalyzer();
        final KeyPair keyPair = this.getCryptoEngine().createKeyGenerator().generateKeyPair();

        // Act + Assert:
        Assert.assertThat(analyzer.isKeyCompressed(keyPair.getPublicKey()), IsEqual.equalTo(true));
    }

    @Test
    public void isKeyCompressedReturnsFalseIfKeyHasWrongLength() {
        // Arrange:
        final KeyAnalyzer analyzer = this.getKeyAnalyzer();
        final KeyPair keyPair = this.getCryptoEngine().createKeyGenerator().generateKeyPair();
        final PublicKey key = new PublicKey(new byte[keyPair.getPublicKey().getRaw().length + 1]);

        // Act + Assert:
        Assert.assertThat(analyzer.isKeyCompressed(key), IsEqual.equalTo(false));
    }

    protected KeyAnalyzer getKeyAnalyzer() {
        return this.getCryptoEngine().createKeyAnalyzer();
    }

    protected abstract CryptoEngine getCryptoEngine();
}
