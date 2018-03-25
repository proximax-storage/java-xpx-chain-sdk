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

package io.nem.core.crypto;

import io.nem.core.test.Utils;
import org.junit.Test;
import org.mockito.Mockito;

public class CipherTest {

    @Test
    public void canCreateCipherFromKeyPairs() {
        // Act:
        new Cipher(new KeyPair(), new KeyPair());

        // Assert: no exceptions
    }

    @Test
    public void canCreateCipherFromCipher() {
        // Arrange:
        final BlockCipher blockCipher = Mockito.mock(BlockCipher.class);

        // Act:
        new Cipher(blockCipher);

        // Assert: no exceptions
    }

    @Test
    public void ctorDelegatesToEngineCreateBlockCipher() {
        // Arrange:
        final KeyPair keyPair1 = new KeyPair();
        final KeyPair keyPair2 = new KeyPair();
        final CryptoEngine engine = Mockito.mock(CryptoEngine.class);

        // Act:
        new Cipher(keyPair1, keyPair2, engine);

        // Assert:
        Mockito.verify(engine, Mockito.only()).createBlockCipher(keyPair1, keyPair2);
    }

    @Test
    public void encryptDelegatesToBlockCipher() {
        // Arrange:
        final BlockCipher blockCipher = Mockito.mock(BlockCipher.class);
        final Cipher cipher = new Cipher(blockCipher);
        final byte[] data = Utils.generateRandomBytes();

        // Act:
        cipher.encrypt(data);

        // Assert:
        Mockito.verify(blockCipher, Mockito.only()).encrypt(data);
    }

    @Test
    public void decryptDelegatesToBlockCipher() {
        // Arrange:
        final BlockCipher blockCipher = Mockito.mock(BlockCipher.class);
        final Cipher cipher = new Cipher(blockCipher);
        final byte[] data = Utils.generateRandomBytes();

        // Act:
        final byte[] encryptedData = cipher.encrypt(data);
        cipher.decrypt(encryptedData);

        // Assert:
        Mockito.verify(blockCipher, Mockito.times(1)).decrypt(encryptedData);
    }
}
