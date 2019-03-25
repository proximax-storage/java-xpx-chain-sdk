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
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecretProofTransactionTest {
    static Account account;

    @BeforeAll
    public static void setup() {
        account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.MIJIN_TEST);
    }

    @Test
    @DisplayName("Serialization")
    void serialization() {

        byte[] expected = new byte[]{(byte)159,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,3,(byte)144,82,66,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,

                63, -56, -70, 16, 34, -102, -75, 119, -115, 5, -39, -60, -73, -11, 102, 118, -88, -117, -7, 41, 92, 24, 90, -49, -64, -7, 97, -37, 84, 8, -54, -2

                ,4,0,(byte)154,73,54,100
        };

        String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
        String secretSeed = "9a493664";
        SecretProofTransaction secretProoftx = SecretProofTransaction.create(
                new FakeDeadline(),
                HashType.SHA3_256,
                secret,
                secretSeed,
                NetworkType.MIJIN_TEST
        );
        byte[] actual = secretProoftx.generateBytes();
        assertEquals(Hex.toHexString(expected), Hex.toHexString(actual));
    }

    @Test
    @DisplayName("To aggregate")
    void toAggregate() {
        byte[] expected = new byte[]{(byte)79,0,0,0,-102,73,54,100,6,-84,-87,82,-72,-117,-83,-11,-15,-23,-66,108,-28,-106,-127,
                65,3,90,96,-66,80,50,115,-22,101,69,107,36,3,(byte)144,82,66,0,

                63, -56, -70, 16, 34, -102, -75, 119, -115, 5, -39, -60, -73, -11, 102, 118, -88, -117, -7, 41, 92, 24, 90, -49, -64, -7, 97, -37, 84, 8, -54, -2

                ,4,0,(byte)154,73,54,100
        };

        String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
        String secretSeed = "9a493664";
        SecretProofTransaction secretProoftx = SecretProofTransaction.create(
                new FakeDeadline(),
                HashType.SHA3_256,
                secret,
                secretSeed,
                NetworkType.MIJIN_TEST
        );
        byte[] actual = secretProoftx.toAggregate(new PublicAccount("9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24", NetworkType.MIJIN_TEST)).toAggregateTransactionBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    void serializeAndSignTransaction() {
        String secret = "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe";
        String secretSeed = "9a493664";
        SecretProofTransaction secretProoftx = SecretProofTransaction.create(
                new FakeDeadline(),
                HashType.SHA3_256,
                secret,
                secretSeed,
                NetworkType.MIJIN_TEST
        );
        SignedTransaction signedTransaction = secretProoftx.signWith(account);
        assertEquals("9F000000B5F704A8ADC74F09EB5EAE13F9265DE542480A3FED07CD1C2068D4203322272048DA36F22433E596BC6754F0ABEEC9D3E21EB713B0CD2E440DAEFC40B3883D041026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF377550390524200000000000000000100000000000000003FC8BA10229AB5778D05D9C4B7F56676A88BF9295C185ACFC0F961DB5408CAFE04009A493664", signedTransaction.getPayload());
        assertEquals("5F56250D59BD5818F85B2D5E3E85FC7EF92F4BAF7C3695A4A4645AEF592EA6EA", signedTransaction.getHash());
    }

    @Test
    void shouldThrowErrorWhenSecretIsNotValid() {
        String proof = "B778A39A3663719DFC5E48C9D78431B1E45C2AF9DF538782BF199C189DABEAC7680ADA57DCEC8EEE91" +
                "C4E3BF3BFA9AF6FFDE90CD1D249D1C6121D7B759A001B1";
        assertThrows(IllegalArgumentException.class, ()-> {
            SecretProofTransaction secretProoftx = SecretProofTransaction.create(
                    new FakeDeadline(),
                    HashType.SHA3_256,
                    "non valid hash",
                    proof,
                    NetworkType.MIJIN_TEST
            );
        }, "not a valid secret");
    }
}

