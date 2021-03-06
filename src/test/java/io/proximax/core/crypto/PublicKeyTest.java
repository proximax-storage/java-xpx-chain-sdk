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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;

public class PublicKeyTest {

    private static final byte[] TEST_BYTES = new byte[]{0x22, (byte) 0xAB, 0x71};
    private static final byte[] MODIFIED_TEST_BYTES = new byte[]{0x22, (byte) 0xAB, 0x72};

    //region constructors / factories

    @Test
    public void canCreateFromBytes() {
        // Arrange:
        final PublicKey key = new PublicKey(TEST_BYTES);

        // Assert:
        MatcherAssert.assertThat(key.getRaw(), IsEqual.equalTo(TEST_BYTES));
    }

    @Test
    public void canCreateFromHexString() {
        // Arrange:
        final PublicKey key = PublicKey.fromHexString("227F");

        // Assert:
        MatcherAssert.assertThat(key.getRaw(), IsEqual.equalTo(new byte[]{0x22, 0x7F}));
    }

    @Test
    public void cannotCreateAroundMalformedHexString() {
        // Act:
       assertThrows(CryptoException.class, () -> PublicKey.fromHexString("22G75"));
    }

    //endregion

    //region serializer

    //endregion

    //region equals / hashCode

    @Test
    public void equalsOnlyReturnsTrueForEquivalentObjects() {
        // Arrange:
        final PublicKey key = new PublicKey(TEST_BYTES);

        // Assert:
        MatcherAssert.assertThat(new PublicKey(TEST_BYTES), IsEqual.equalTo(key));
        MatcherAssert.assertThat(new PublicKey(MODIFIED_TEST_BYTES), IsNot.not(IsEqual.equalTo(key)));
        MatcherAssert.assertThat(null, IsNot.not(IsEqual.equalTo(key)));
        MatcherAssert.assertThat(TEST_BYTES, IsNot.not(IsEqual.equalTo((Object) key)));
    }

    @Test
    public void hashCodesAreEqualForEquivalentObjects() {
        // Arrange:
        final PublicKey key = new PublicKey(TEST_BYTES);
        final int hashCode = key.hashCode();

        // Assert:
        MatcherAssert.assertThat(new PublicKey(TEST_BYTES).hashCode(), IsEqual.equalTo(hashCode));
        MatcherAssert.assertThat(new PublicKey(MODIFIED_TEST_BYTES).hashCode(), IsNot.not(IsEqual.equalTo(hashCode)));
    }

    //endregion

    //region toString

    @Test
    public void toStringReturnsHexRepresentation() {
        // Assert:
        MatcherAssert.assertThat(new PublicKey(TEST_BYTES).toString(), IsEqual.equalTo("22ab71"));
    }

    //endregion
}
