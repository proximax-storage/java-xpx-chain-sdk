/*
 * Copyright 2022 ProximaX
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
package io.proximax.sdk;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.AccountMetadataTransaction;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.LockFundsTransaction;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.MosaicMetadataTransaction;
import io.proximax.sdk.model.transaction.MosaicSupplyChangeTransaction;
import io.proximax.sdk.model.transaction.NamespaceMetadataTransaction;
import io.proximax.sdk.model.transaction.RegisterNamespaceTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;

/**
 * E2E tests to proof work with metadata
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EMetadataTest extends E2EBaseTest {
      /** logger */
      private static final Logger logger = LoggerFactory.getLogger(E2EMetadataTest.class);
      private static final String test_name = "test-metadata-"
                  + new BigDecimal(Math.floor(Math.random() * 10000)).intValue();
      private static final String ROOT_NAME = "test-metadata-namespace-"
                  + new BigDecimal(Math.floor(Math.random() * 10000)).intValue();
      private static final String test_value = "test-metadata-value-"
                  + new BigDecimal(Math.floor(Math.random() * 10000)).intValue();

      @BeforeAll
      void initStuff() {
      }

      @AfterAll
      void closeDown() {
      }

      @Test
      void add01MetadataToAccount() {

            AccountMetadataTransaction accountMetadataTransaction = transact.accountMetadata().create(
                        seedAccount.getPublicAccount(), test_name, test_value, "").build();

            AggregateTransaction aggregateTransaction = transact.aggregateBonded()
                        .innerTransactions(accountMetadataTransaction.toAggregate(seedAccount.getPublicAccount()))
                        .build();

            // sign the transaction
            SignedTransaction signedAggregateTransaction = api.sign(aggregateTransaction, seedAccount);
            logger.info("Account Metadata Payload: {}", signedAggregateTransaction.getPayload());
            LockFundsTransaction lock = transact.lockFunds().mosaic(NetworkCurrencyMosaic.TEN)
                        .duration(BigInteger.valueOf(480)).signedTransaction(signedAggregateTransaction).build();

            logger.info("announcing {}", lock);
            SignedTransaction signedLock = api.sign(lock, seedAccount);

            logger.info("Account Signed Lock: {}", signedLock.getHash());
            // announce the request
            transactionHttp.announce(signedLock).blockingFirst();
            // wait for acceptance
            logger.info("Signed Lock Transaction done {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();

            transactionHttp.announceAggregateBonded(signedAggregateTransaction).blockingFirst();
            // wait for aggregateTransaction confirmation
            logger.info("signedAggregateTransaction hash: {}", signedAggregateTransaction.getHash());

            listener.aggregateBondedAdded(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                        .blockingFirst();
      }

      @Test
      void add02MetadataToMosaic() {

            MosaicNonce nonce = MosaicNonce.createRandom();
            MosaicId Id = new MosaicId(
                        nonce,
                        PublicAccount.createFromPublicKey(seedAccount.getPublicKey(), getNetworkType()));

            logger.info("Creating mosaic " + Id.getIdAsHex());

            // create mosaic
            MosaicDefinitionTransaction mosaicDefinition = transact.mosaicDefinition()
                        .init(nonce, Id, new MosaicProperties(true, true, 0, Optional.of(BigInteger.valueOf(100))))
                        .build();
            // add supply of 10000
            MosaicSupplyChangeTransaction increaseSupply = transact.mosaicSupplyChange()
                        .increase(Id, BigInteger.TEN)
                        .build();

            // create aggregate transaction
            AggregateTransaction aggregateTransaction = transact.aggregateComplete()
                        .innerTransactions(mosaicDefinition.toAggregate(seedAccount.getPublicAccount()),
                                    increaseSupply.toAggregate(seedAccount.getPublicAccount()))
                        .build();
            // sign the transaction
            SignedTransaction signedAggregateTransaction = api.sign(aggregateTransaction, seedAccount);
            // announce the request
            transactionHttp.announce(signedAggregateTransaction).blockingFirst();
            // wait for acceptance
            logger.info("SignedAggregateTransaction done {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();

            // now add metadata to the mosaic
            MosaicMetadataTransaction mosaicMetadataTransaction = transact.mosaicMetadata()
                        .create(seedAccount.getPublicAccount(), mosaicDefinition.getMosaicId(), test_name, test_value,
                                    "")
                        .build();

            AggregateTransaction aggregateTransactionmeta = transact.aggregateBonded()
                        .innerTransactions(
                                    mosaicMetadataTransaction.toAggregate(seedAccount.getPublicAccount()))
                        .build();
            SignedTransaction signedMosaicMetaAggregateTransaction = api.sign(aggregateTransactionmeta, seedAccount);

            logger.info("Mosaic Metadata Payload: {}", signedMosaicMetaAggregateTransaction.getPayload());
            LockFundsTransaction lock = transact.lockFunds().mosaic(NetworkCurrencyMosaic.TEN)
                        .duration(BigInteger.valueOf(480)).signedTransaction(signedMosaicMetaAggregateTransaction)
                        .build();
            logger.info("announcing {}", lock);
            SignedTransaction signedLock = api.sign(lock, seedAccount);

            logger.info("Account Signed Lock: {}", signedLock.getHash());
            // announce the request
            transactionHttp.announce(signedLock).blockingFirst();
            // wait for acceptance
            logger.info("Signed Lock Transaction done {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();

            transactionHttp.announceAggregateBonded(signedMosaicMetaAggregateTransaction).blockingFirst();
            // wait for aggregateTransaction confirmation
            logger.info("signedAggregateTransaction hash: {}", signedMosaicMetaAggregateTransaction.getHash());

            listener.aggregateBondedAdded(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                        .blockingFirst();

      }

      @Test
      void add03MetadataToNamespace() {

            NamespaceId rootId = new NamespaceId(ROOT_NAME);
            logger.info("Going to create namespace {}", rootId);
            // create root namespace
            RegisterNamespaceTransaction registerNamespaceTransaction = transact.registerNamespace()
                        .rootNamespace(ROOT_NAME)
                        .duration(BigInteger.valueOf(100)).build();
            SignedTransaction signedTransaction = api.sign(registerNamespaceTransaction, seedAccount);
            transactionHttp.announce(signedTransaction).blockingFirst();
            logger.info("Registered namespace {}. {}",
                        ROOT_NAME,
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();

            // now add metadata to the mosaic
            NamespaceMetadataTransaction namespaceMetadataTransaction = transact.namespaceMetadata()
                        .create(seedAccount.getPublicAccount(),
                                    registerNamespaceTransaction.getNamespaceId(), test_name,
                                    test_value, "")
                        .build();

            AggregateTransaction aggregateTransactionmeta = transact.aggregateBonded()
                        .innerTransactions(
                                    namespaceMetadataTransaction.toAggregate(seedAccount.getPublicAccount()))
                        .build();
            SignedTransaction signedMosaicMetaAggregateTransaction = api.sign(aggregateTransactionmeta, seedAccount);

            logger.info("Mosaic Metadata Payload: {}", signedMosaicMetaAggregateTransaction.getPayload());
            LockFundsTransaction lock = transact.lockFunds().mosaic(NetworkCurrencyMosaic.TEN)
                        .duration(BigInteger.valueOf(480)).signedTransaction(signedMosaicMetaAggregateTransaction)
                        .build();
            logger.info("announcing {}", lock);
            SignedTransaction signedLock = api.sign(lock, seedAccount);

            logger.info("Account Signed Lock: {}", signedLock.getHash());
            // announce the request
            transactionHttp.announce(signedLock).blockingFirst();
            // wait for acceptance
            logger.info("Signed Lock Transaction done {}",
                        listener.confirmed(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                                    .blockingFirst());
            sleepForAWhile();

            transactionHttp.announceAggregateBonded(signedMosaicMetaAggregateTransaction).blockingFirst();
            // wait for aggregateTransaction confirmation
            logger.info("signedAggregateTransaction hash: {}", signedMosaicMetaAggregateTransaction.getHash());

            listener.aggregateBondedAdded(seedAccount.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS)
                        .blockingFirst();

      }

}
