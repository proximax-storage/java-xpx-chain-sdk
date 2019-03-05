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

import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.XEM;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SecretLockTransactionTest {
    static Account account;

    @BeforeAll
    public static void setup() {
        account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.MIJIN_TEST);
    }

    @Test
    @DisplayName("Serialization")
    void serialization() {
        byte[] expected = new byte[]{(byte)-54,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,1,(byte)144,76,66,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,41,(byte)207,
                95,(byte)217,65,(byte)173,37,(byte)213,(byte)128,(byte)150,(byte)152,0,0,0,0,0,100,0,0,0,0,0,0,0,0,

                63, (byte)200, (byte)186, 16, 34, (byte)154, (byte)181, 119, (byte)141, 5, (byte)217, (byte)196, (byte)183, (byte)245,
                102, 118, (byte)168, (byte)139, (byte)249, 41, 92, 24, 90, (byte)207, (byte)192, (byte)249, 97, (byte)219, 84, 8, (byte)202, (byte)254,

                (byte)144,(byte)232,(byte)254, (byte)189,103,29,(byte)212,27,(byte)238,(byte)148,
                (byte)236,59,(byte)165,(byte)131,28,(byte)182,8,(byte)163,18,(byte)194,(byte)242,3,(byte)186,(byte)132,(byte)172,
        };

        String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new FakeDeadline(),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );
        byte[] actual = secretLocktx.generateBytes();
        assertEquals(Hex.toHexString(expected), Hex.toHexString(actual));
    }

    @Test
    @DisplayName("To aggregate")
    void toAggregate() {
        byte[] expected = new byte[]{(byte)122,0,0,0,(byte)-102,73,54,100,6,-84,-87,82,-72,-117,-83,-11,-15,-23,-66,108,-28,-106,-127,
                65,3,90,96,-66,80,50,115,-22,101,69,107,36,1,(byte)144,76,66,41,(byte)207,
                95,(byte)217,65,(byte)173,37,(byte)213,(byte)128,(byte)150,(byte)152,0,0,0,0,0,100,0,0,0,0,0,0,0,0,

                63, (byte)200, (byte)186, 16, 34, (byte)154, (byte)181, 119, (byte)141, 5, (byte)217, (byte)196, (byte)183, (byte)245,
                102, 118, (byte)168, (byte)139, (byte)249, 41, 92, 24, 90, (byte)207, (byte)192, (byte)249, 97, (byte)219, 84, 8, (byte)202, (byte)254,

                (byte)144,(byte)232,(byte)254,(byte)189,103,29,(byte)212,27,(byte)238,(byte)148,
                (byte)236,59,(byte)165,(byte)131,28,(byte)182,8,(byte)163,18,(byte)194,(byte)242,3,(byte)186,(byte)132,(byte)172,
        };

        String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new FakeDeadline(),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );
        byte[] actual = secretLocktx.toAggregate(new PublicAccount("9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24", NetworkType.MIJIN_TEST)).toAggregateTransactionBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    void serializeAndSignTransaction() {
        String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new FakeDeadline(),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );
        SignedTransaction signedTransaction = secretLocktx.signWith(account);
        assertEquals("CA00000048593A86699E4DC2A87842BE7E2D1F4B63CEDD4BC7E740BC31B894612500DC8594C5FFAD1CBE5A8343F0C9180AFBB6D2BBF157549CDEBACB823981566EADEC091026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF3775503904C420000000000000000010000000000000029CF5FD941AD25D580969800000000006400000000000000003FC8BA10229AB5778D05D9C4B7F56676A88BF9295C185ACFC0F961DB5408CAFE90E8FEBD671DD41BEE94EC3BA5831CB608A312C2F203BA84AC", signedTransaction.getPayload());
        assertEquals("D2716B0E1A1A9186B8AE23A3392D7676E801791236755D9BFE2E96D048171ECF", signedTransaction.getHash());
    }

    @Test
    void shouldThrowErrorWhenSecretIsNotValid() {
        assertThrows(IllegalArgumentException.class, ()-> {
            SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                    new FakeDeadline(),
                    XEM.createRelative(BigInteger.valueOf(10)),
                    BigInteger.valueOf(100),
                    HashType.SHA3_256,
                    "non valid hash",
                    Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                    NetworkType.MIJIN_TEST
            );
        }, "not a valid secret");
    }
}


