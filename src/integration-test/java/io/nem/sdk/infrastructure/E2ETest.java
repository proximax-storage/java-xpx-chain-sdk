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

package io.nem.sdk.infrastructure;

import io.nem.core.crypto.Hashes;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicProperties;
import io.nem.sdk.model.mosaic.MosaicSupplyType;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.transaction.*;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2ETest extends BaseTest {
    private TransactionHttp transactionHttp;
    private Account account;
    private Account multisigAccount;
    private Account cosignatoryAccount;
    private Account cosignatoryAccount2;
    private NamespaceId namespaceId = new NamespaceId(new BigInteger("-1999805136990834023")); // This namespace is created in functional testing
    private String namespaceName = "nem2-tests";
    private MosaicId mosaicId = new MosaicId(new BigInteger("4532189107927582222")); // This mosaic is created in functional testing
    private Listener listener;

    @BeforeAll
    void setup() throws ExecutionException, InterruptedException, IOException {
        transactionHttp = new TransactionHttp(this.getNodeUrl());
        account = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NetworkType.MIJIN_TEST);
        multisigAccount = new Account("5edebfdbeb32e9146d05ffd232c8af2cf9f396caf9954289daa0362d097fff3b", NetworkType.MIJIN_TEST);
        cosignatoryAccount = new Account("2a2b1f5d366a5dd5dc56c3c757cf4fe6c66e2787087692cf329d7a49a594658b", NetworkType.MIJIN_TEST);
        cosignatoryAccount2 = new Account("b8afae6f4ad13a1b8aad047b488e0738a437c7389d4ff30c359ac068910c1d59", NetworkType.MIJIN_TEST);
        listener = new Listener(this.getNodeUrl());
        listener.open().get();
    }

    @Test
    void standaloneTransferTransaction() throws ExecutionException, InterruptedException {
        TransferTransaction transferTransaction = TransferTransaction.create(
                new Deadline(2, HOURS),
                new Address("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", NetworkType.MIJIN_TEST),
                Collections.singletonList(
                        XEM.createAbsolute(BigInteger.valueOf(1))
                ),
                new PlainMessage("message"),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction signedTransaction = this.account.sign(transferTransaction);
        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateTransferTransaction() throws ExecutionException, InterruptedException {
        TransferTransaction transferTransaction = TransferTransaction.create(
                new Deadline(2, HOURS),
                new Address("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", NetworkType.MIJIN_TEST),
                Collections.singletonList(
                        XEM.createAbsolute(BigInteger.valueOf(1))
                ),
                new PlainMessage("messageloooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "oooooooong"), // Use long message to test if size of inner transaction is calculated correctly
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        transferTransaction.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneRootRegisterNamespaceTransaction() throws ExecutionException, InterruptedException {
        String namespaceName = "test-root-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createRootNamespace(
                new Deadline(2, HOURS),
                namespaceName,
                BigInteger.valueOf(100),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction signedTransaction = this.account.sign(registerNamespaceTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateRootRegisterNamespaceTransaction() throws ExecutionException, InterruptedException {
        String namespaceName = "test-root-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createRootNamespace(
                new Deadline(2, HOURS),
                namespaceName,
                BigInteger.valueOf(100),
                NetworkType.MIJIN_TEST
        );
        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        registerNamespaceTransaction.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneSubNamespaceRegisterNamespaceTransaction() throws ExecutionException, InterruptedException {
        String namespaceName = "test-sub-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createSubNamespace(
                new Deadline(2, HOURS),
                namespaceName,
                this.namespaceId,
                NetworkType.MIJIN_TEST
        );

        SignedTransaction signedTransaction = this.account.sign(registerNamespaceTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateSubNamespaceRegisterNamespaceTransaction() throws ExecutionException, InterruptedException {
        String namespaceName = "test-sub-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction.createSubNamespace(
                new Deadline(2, HOURS),
                namespaceName,
                this.namespaceId,
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        registerNamespaceTransaction.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneMosaicDefinitionTransaction() throws ExecutionException, InterruptedException {
        String mosaicName = "test-mosaic-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        MosaicDefinitionTransaction mosaicDefinitionTransaction = MosaicDefinitionTransaction.create(
                new Deadline(2, HOURS),
                mosaicName,
                this.namespaceName,
                new MosaicProperties(true, true,true, 4, BigInteger.valueOf(100)),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction signedTransaction = this.account.sign(mosaicDefinitionTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateMosaicDefinitionTransaction() throws ExecutionException, InterruptedException {
        String mosaicName = "test-mosaic-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        MosaicDefinitionTransaction mosaicDefinitionTransaction = MosaicDefinitionTransaction.create(
                new Deadline(2, HOURS),
                mosaicName,
                this.namespaceName,
                new MosaicProperties(true, false,false, 4, BigInteger.valueOf(100)),
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        mosaicDefinitionTransaction.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneMosaicSupplyChangeTransaction() throws ExecutionException, InterruptedException {
        MosaicSupplyChangeTransaction mosaicSupplyChangeTransaction = MosaicSupplyChangeTransaction.create(
                new Deadline(2, HOURS),
                this.mosaicId,
                MosaicSupplyType.INCREASE,
                BigInteger.valueOf(10),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction signedTransaction = this.account.sign(mosaicSupplyChangeTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateMosaicSupplyChangeTransaction() throws ExecutionException, InterruptedException {
        MosaicSupplyChangeTransaction mosaicSupplyChangeTransaction = MosaicSupplyChangeTransaction.create(
                new Deadline(2, HOURS),
                this.mosaicId,
                MosaicSupplyType.INCREASE,
                BigInteger.valueOf(10),
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        mosaicSupplyChangeTransaction.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void shouldSignModifyMultisigAccountTransactionWithCosignatories() throws ExecutionException, InterruptedException {
        ModifyMultisigAccountTransaction modifyMultisigAccountTransaction = ModifyMultisigAccountTransaction.create(
                new Deadline(2, HOURS),
                0,
                0,
                Collections.singletonList(
                        new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD, PublicAccount.createFromPublicKey("B0F93CBEE49EEB9953C6F3985B15A4F238E205584D8F924C621CBE4D7AC6EC24", NetworkType.MIJIN_TEST))
                ),
                NetworkType.MIJIN_TEST);
        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        modifyMultisigAccountTransaction.toAggregate(this.multisigAccount.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.cosignatoryAccount.signTransactionWithCosignatories(aggregateTransaction,
                Collections.singletonList(this.cosignatoryAccount2));

        LockFundsTransaction lockFundsTransaction = LockFundsTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );

        SignedTransaction lockFundsSignedTransaction = this.cosignatoryAccount.sign(lockFundsTransaction);

        transactionHttp.announce(lockFundsSignedTransaction).toFuture().get();

        listener.confirmed(this.cosignatoryAccount.getAddress()).take(1).toFuture().get();

        transactionHttp.announceAggregateBonded(signedTransaction).toFuture().get();

        this.validateAggregateBondedTransactionAnnounceCorrectly(this.cosignatoryAccount.getAddress(), signedTransaction.getHash());
    }

    @Test
    void CosignatureTransaction() throws ExecutionException, InterruptedException {
        TransferTransaction transferTransaction = TransferTransaction.create(
                new Deadline(2, HOURS),
                new Address("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", NetworkType.MIJIN_TEST),
                Collections.singletonList(
                        XEM.createAbsolute(BigInteger.valueOf(1))
                ),
                PlainMessage.create("test-message"),
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        transferTransaction.toAggregate(this.multisigAccount.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);
        SignedTransaction signedTransaction = this.cosignatoryAccount.sign(aggregateTransaction);

        LockFundsTransaction lockFundsTransaction = LockFundsTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );

        SignedTransaction lockFundsSignedTransaction = this.cosignatoryAccount.sign(lockFundsTransaction);

        transactionHttp.announce(lockFundsSignedTransaction).toFuture().get();

        listener.confirmed(this.cosignatoryAccount.getAddress()).take(1).toFuture().get();

        transactionHttp.announceAggregateBonded(signedTransaction).toFuture().get();

        this.validateAggregateBondedTransactionAnnounceCorrectly(this.cosignatoryAccount.getAddress(), signedTransaction.getHash());

        CosignatureTransaction cosignatureTransaction = CosignatureTransaction.create(aggregateTransaction);

        CosignatureSignedTransaction cosignatureSignedTransaction = this.cosignatoryAccount2.signCosignatureTransaction(cosignatureTransaction);

        transactionHttp.announceAggregateBondedCosignature(cosignatureSignedTransaction).toFuture().get();

        this.validateAggregateBondedCosignatureTransactionAnnounceCorrectly(this.cosignatoryAccount.getAddress(), cosignatureSignedTransaction.getParentHash());
    }

    @Test
    void standaloneLockFundsTransaction() throws ExecutionException, InterruptedException {
        AggregateTransaction aggregateTransaction = AggregateTransaction.createBonded(new Deadline(2, HOURS), Collections.emptyList(), NetworkType.MIJIN_TEST);
        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);
        LockFundsTransaction lockFundstx = LockFundsTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );

        SignedTransaction lockFundsTransactionSigned = this.account.sign(lockFundstx);
        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), lockFundsTransactionSigned.getHash());
    }

    @Test
    void aggregateLockFundsTransaction() throws ExecutionException, InterruptedException {
        AggregateTransaction aggregateTransaction = AggregateTransaction.createBonded(new Deadline(2, HOURS), Collections.emptyList(), NetworkType.MIJIN_TEST);
        SignedTransaction signedTransaction = this.account.sign(aggregateTransaction);
        LockFundsTransaction lockFundstx = LockFundsTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction lockFundsAggregatetx = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        lockFundstx.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsTransactionSigned = this.account.sign(lockFundsAggregatetx);

        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), lockFundsTransactionSigned.getHash());
    }

    @Test
    void standaloneSecretLockTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction secretLockTransactionSigned = this.account.sign(secretLocktx);

        transactionHttp.announce(secretLockTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), secretLockTransactionSigned.getHash());
    }

    @Test
    void aggregateSecretLockTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction secretLockAggregatetx = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        secretLocktx.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction secretLockTransactionSigned = this.account.sign(secretLockAggregatetx);

        transactionHttp.announce(secretLockTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), secretLockTransactionSigned.getHash());
    }

    @Test
    void standaloneSecretProofTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        String proof = Hex.encodeHexString(secretBytes);
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction lockFundsTransactionSigned = this.account.sign(secretLocktx);

        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), lockFundsTransactionSigned.getHash());

        SecretProofTransaction secretProoftx = SecretProofTransaction.create(
                new Deadline(2, HOURS),
                HashType.SHA3_256,
                secret,
                proof,
                NetworkType.MIJIN_TEST
        );

        SignedTransaction secretProofTransactionSigned = this.account.sign(secretProoftx);

        transactionHttp.announce(secretProofTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), secretProofTransactionSigned.getHash());
    }

    @Test
    void aggregateSecretProofTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        String proof = Hex.encodeHexString(secretBytes);
        SecretLockTransaction secretLocktx = SecretLockTransaction.create(
                new Deadline(2, HOURS),
                XEM.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST
        );

        SignedTransaction lockFundsTransactionSigned = this.account.sign(secretLocktx);

        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), lockFundsTransactionSigned.getHash());

        SecretProofTransaction secretProoftx = SecretProofTransaction.create(
                new Deadline(2, HOURS),
                HashType.SHA3_256,
                secret,
                proof,
                NetworkType.MIJIN_TEST
        );

        AggregateTransaction secretProofAggregatetx = AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                        secretProoftx.toAggregate(this.account.getPublicAccount())
                ),
                NetworkType.MIJIN_TEST);

        SignedTransaction secretProofTransactionSigned = this.account.sign(secretProofAggregatetx);

        transactionHttp.announce(secretProofTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(this.account.getAddress(), secretProofTransactionSigned.getHash());
    }

    void validateTransactionAnnounceCorrectly(Address address, String transactionHash) throws ExecutionException, InterruptedException {
        Transaction transaction = listener.confirmed(address).take(1).toFuture().get();

        assertEquals(transactionHash, transaction.getTransactionInfo().get().getHash().get());
    }

    void validateAggregateBondedTransactionAnnounceCorrectly(Address address, String transactionHash) throws ExecutionException, InterruptedException {
        AggregateTransaction aggregateTransaction = listener.aggregateBondedAdded(address).take(1).toFuture().get();
        assertEquals(transactionHash, aggregateTransaction.getTransactionInfo().get().getHash().get());
    }

    void validateAggregateBondedCosignatureTransactionAnnounceCorrectly(Address address, String transactionHash) throws ExecutionException, InterruptedException {
        String hash = listener.cosignatureAdded(address).take(1).toFuture().get().getParentHash();
        assertEquals(transactionHash, hash);
    }
}
