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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.XPX;

class LockFundsTransactionTest {
    static Account account;

    @BeforeAll
    public static void setup() {
        account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.MIJIN_TEST);
    }

    @Test
    @DisplayName("Serialization")
    void serialization() {
        // Generated at nem2-library-js/test/transactions/LockFundsTransaction.spec.js
        byte[] expected = new byte[]{(byte)176,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,3,(byte)144,72,65,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,41,(byte)207,95,
                (byte)217,65,(byte)173,37,(byte)213,(byte)128,(byte)150,(byte)152,0,0,0,0,0,100,0,0,0,0,0,0,0,(byte)132,
                (byte)152,(byte)179,(byte)141,(byte)137,(byte)193,(byte)220,(byte)138,68,(byte)142,(byte)165,(byte)130,73,
                56,(byte)255,(byte)130,(byte)137,38,(byte)205,(byte)159,119,71,(byte)177,(byte)132,75,89,(byte)180,(byte)182,
                (byte)128,126,(byte)135,(byte)139
        };
        SignedTransaction signedTransaction = new SignedTransaction("payload", "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
        LockFundsTransaction lockFundstx = LockFundsTransaction.create(
                new FakeDeadline(),
                XPX.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );
        byte[] actual = lockFundstx.generateBytes();
        assertEquals(Hex.toHexString(expected), Hex.toHexString(actual));
    }

    @Test
    @DisplayName("To aggregate")
    void toAggregate() {
        byte[] expected =  new byte[]{96,0,0,0,-102,73,54,100,6,-84,-87,82,-72,-117,-83,-11,-15,-23,-66,108,-28,-106,-127,
                65,3,90,96,-66,80,50,115,-22,101,69,107,36,3,(byte)144,72,65,41,(byte)207,95,
                (byte)217,65,(byte)173,37,(byte)213,(byte)128,(byte)150,(byte)152,0,0,0,0,0,100,0,0,0,0,0,0,0,(byte)132,
                (byte)152,(byte)179,(byte)141,(byte)137,(byte)193,(byte)220,(byte)138,68,(byte)142,(byte)165,(byte)130,73,
                56,(byte)255,(byte)130,(byte)137,38,(byte)205,(byte)159,119,71,(byte)177,(byte)132,75,89,(byte)180,(byte)182,
                (byte)128,126,(byte)135,(byte)139};

        SignedTransaction signedTransaction = new SignedTransaction("payload", "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
        LockFundsTransaction lockFundstx = LockFundsTransaction.create(
                new FakeDeadline(),
                XPX.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );
        byte[] actual = lockFundstx.toAggregate(new PublicAccount("9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24", NetworkType.MIJIN_TEST)).toAggregateTransactionBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    void serializeAndSignTransaction() {
        SignedTransaction signedTransaction = new SignedTransaction("payload", "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.AGGREGATE_BONDED);
        LockFundsTransaction lockFundstx = LockFundsTransaction.create(
                new FakeDeadline(),
                XPX.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );
        SignedTransaction lockFundsTransactionSigned = lockFundstx.signWith(account);
        assertEquals("B0000000812021C8F4D19FC3D3CE6155FF054D04108C3BABAE7977E2AE721E24D64D14500BC3C148165A39739ECC07DE6B473130BD60ED520623A79ECF8470D2B3A602061026D70E1954775749C6811084D6450A3184D977383F0E4282CD47118AF3775503904841000000000000000001000000000000000100000000000000809698000000000064000000000000008498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", lockFundsTransactionSigned.getPayload());
        assertEquals("7E7B561A6DD6364ECF201C69C0701A04DD2A5DD1BE6680B58D4C11D3DBE94503", lockFundsTransactionSigned.getHash());
    }

    @Test
    void shouldThrowExceptionWhenSignedTransactionIsNotTypeAggregateBonded() {

        SignedTransaction signedTransaction = new SignedTransaction("payload", "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B", TransactionType.TRANSFER);
        assertThrows(IllegalArgumentException.class, ()->{LockFundsTransaction.create(
                new FakeDeadline(),
                XPX.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );}, "Signed transaction must be Aggregate Bonded Transaction");

    }
}
