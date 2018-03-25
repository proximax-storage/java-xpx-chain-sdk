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
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class SignatureTest {

    //region constructor

    private static Signature createSignature(final String r, final String s) {
        return new Signature(new BigInteger(r, 16), new BigInteger(s, 16));
    }

    private static Signature createSignature(final int r, final int s) {
        return new Signature(new BigInteger(String.format("%d", r)), new BigInteger(String.format("%d", s)));
    }

    @Test
    public void bigIntegerCtorInitializesFields() {
        // Arrange:
        final BigInteger r = new BigInteger("99512345");
        final BigInteger s = new BigInteger("12351234");

        // Act:
        final Signature signature = new Signature(r, s);

        // Assert:
        Assert.assertThat(signature.getR(), IsEqual.equalTo(r));
        Assert.assertThat(signature.getS(), IsEqual.equalTo(s));
    }

    @Test
    public void byteArrayCtorInitializesFields() {
        // Arrange:
        final Signature originalSignature = createSignature("99512345", "12351234");

        // Act:
        final Signature signature = new Signature(originalSignature.getBytes());

        // Assert:
        Assert.assertThat(signature.getR(), IsEqual.equalTo(originalSignature.getR()));
        Assert.assertThat(signature.getS(), IsEqual.equalTo(originalSignature.getS()));
    }

    @Test
    public void binaryCtorInitializesFields() {
        // Arrange:
        final Signature originalSignature = createSignature("99512345", "12351234");

        // Act:
        final Signature signature = new Signature(originalSignature.getBinaryR(), originalSignature.getBinaryS());

        // Assert:
        Assert.assertThat(signature.getR(), IsEqual.equalTo(originalSignature.getR()));
        Assert.assertThat(signature.getS(), IsEqual.equalTo(originalSignature.getS()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bigIntegerCtorFailsIfRIsToLarge() {
        // Arrange:
        final BigInteger r = BigInteger.ONE.shiftLeft(256);
        final BigInteger s = new BigInteger("12351234");

        // Act:
        new Signature(r, s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bigIntegerCtorFailsIfSIsToLarge() {
        // Arrange:
        final BigInteger r = new BigInteger("12351234");
        final BigInteger s = BigInteger.ONE.shiftLeft(256);

        // Act:
        new Signature(r, s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void byteArrayCtorFailsIfByteArrayIsTooSmall() {
        // Act:
        new Signature(new byte[63]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void byteArrayCtorFailsIfByteArrayIsTooLarge() {
        // Act:
        new Signature(new byte[65]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void binaryCtorFailsIfByteArrayOfRIsTooLarge() {
        // Act:
        new Signature(new byte[33], new byte[32]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void binaryCtorFailsIfByteArrayOfSIsTooLarge() {
        // Act:
        new Signature(new byte[32], new byte[33]);
    }

    //endregion

    //region getBytes

    @Test
    public void byteArrayCtorSucceedsIfByteArrayIsCorrectLength() {
        // Act:
        final Signature signature = new Signature(new byte[64]);

        // Assert:
        Assert.assertThat(signature.getR(), IsEqual.equalTo(BigInteger.ZERO));
        Assert.assertThat(signature.getS(), IsEqual.equalTo(BigInteger.ZERO));
    }

    @Test
    public void binaryCtorSucceedsIfRAndSHaveCorrectLength() {
        // Act:
        final Signature signature = new Signature(new byte[32], new byte[32]);

        // Assert:
        Assert.assertThat(signature.getR(), IsEqual.equalTo(BigInteger.ZERO));
        Assert.assertThat(signature.getS(), IsEqual.equalTo(BigInteger.ZERO));
    }

    @Test
    public void getBytesReturns64Bytes() {
        // Assert:
        for (final Signature signature : this.createRoundtripTestSignatures()) {
            Assert.assertThat(signature.getBytes().length, IsEqual.equalTo(64));
        }
    }

    //endregion

    //region getBinaryR / getBinaryS

    @Test
    public void canRoundtripBinarySignature() {
        // Assert:
        for (final Signature signature : this.createRoundtripTestSignatures()) {
            Assert.assertThat(new Signature(signature.getBytes()), IsEqual.equalTo(signature));
        }
    }

    private Signature[] createRoundtripTestSignatures() {
        return new Signature[]{
                createSignature(Utils.createString('F', 64), Utils.createString('0', 64)),
                createSignature(Utils.createString('0', 64), Utils.createString('F', 64)),
                createSignature("99512345", "12351234")
        };
    }

    //endregion

    //region equals / hashCode

    @Test
    public void getBinaryRReturnsRAsByteArray() {
        // Arrange:
        final byte[] originalR = new byte[32];
        originalR[15] = 123;
        final byte[] s = new byte[32];
        final Signature signature = new Signature(originalR, s);

        // Act:
        final byte[] r = signature.getBinaryR();

        // Assert:
        Assert.assertThat(r, IsEqual.equalTo(originalR));
    }

    @Test
    public void getBinarySReturnsSAsByteArray() {
        // Arrange:
        final byte[] r = new byte[32];
        final byte[] originalS = new byte[32];
        originalS[15] = 123;
        final Signature signature = new Signature(r, originalS);

        // Act:
        final byte[] s = signature.getBinaryS();

        // Assert:
        Assert.assertThat(s, IsEqual.equalTo(originalS));
    }

    //endregion

    //region inline serialization

    //endregion

    // region toString

    @Test
    public void equalsOnlyReturnsTrueForEquivalentObjects() {
        // Arrange:
        final Signature signature = createSignature(1235, 7789);

        // Assert:
        Assert.assertThat(createSignature(1235, 7789), IsEqual.equalTo(signature));
        Assert.assertThat(createSignature(1234, 7789), IsNot.not(IsEqual.equalTo(signature)));
        Assert.assertThat(createSignature(1235, 7790), IsNot.not(IsEqual.equalTo(signature)));
        Assert.assertThat(null, IsNot.not(IsEqual.equalTo(signature)));
        Assert.assertThat(new BigInteger("1235"), IsNot.not(IsEqual.equalTo((Object) signature)));
    }

    //endregion

    @Test
    public void hashCodesAreEqualForEquivalentObjects() {
        // Arrange:
        final Signature signature = createSignature(1235, 7789);
        final int hashCode = signature.hashCode();

        // Assert:
        Assert.assertThat(createSignature(1235, 7789).hashCode(), IsEqual.equalTo(hashCode));
        Assert.assertThat(createSignature(1234, 7789).hashCode(), IsNot.not(IsEqual.equalTo(hashCode)));
        Assert.assertThat(createSignature(1235, 7790).hashCode(), IsNot.not(IsEqual.equalTo(hashCode)));
    }

    @Test
    public void toStringReturnsHexRepresentation() {
        // Arrange:
        final Signature signature = createSignature(12, 513);

        // Assert:
        final String expectedSignature =
                "0c00000000000000000000000000000000000000000000000000000000000000" +
                        "0102000000000000000000000000000000000000000000000000000000000000";
        Assert.assertThat(signature.toString(), IsEqual.equalTo(expectedSignature));
    }
}
