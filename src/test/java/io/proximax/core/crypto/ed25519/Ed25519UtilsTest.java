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

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.test.Utils;

public class Ed25519UtilsTest {

    //region prepareForScalarMultiply

    @Test
    public void prepareForScalarMultiplyReturnsClampedValue() {
        // Arrange:
        final PrivateKey privateKey = new PrivateKey(new BigInteger(Utils.generateRandomBytes(32)));

        // Act:
        final byte[] a = Ed25519Utils.prepareForScalarMultiply(privateKey).getRaw();

        // Assert:
        MatcherAssert.assertThat(a[31] & 0x40, IsEqual.equalTo(0x40));
        MatcherAssert.assertThat(a[31] & 0x80, IsEqual.equalTo(0x0));
        MatcherAssert.assertThat(a[0] & 0x7, IsEqual.equalTo(0x0));
    }

    //endregion
}
