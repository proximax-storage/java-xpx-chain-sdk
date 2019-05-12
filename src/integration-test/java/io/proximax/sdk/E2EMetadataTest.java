/*
 * Copyright 2019 ProximaX
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.metadata.AddressMetadata;
import io.proximax.sdk.model.metadata.Field;
import io.proximax.sdk.model.metadata.Metadata;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.metadata.MosaicMetadata;
import io.proximax.sdk.model.metadata.NamespaceMetadata;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.ModifyMetadataTransaction;
import io.proximax.sdk.model.transaction.MosaicDefinitionTransaction;
import io.proximax.sdk.model.transaction.RegisterNamespaceTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.reactivex.Observable;

/**
 * E2E tests to proof work with metadata
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EMetadataTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EMetadataTest.class);

   private List<String> metaIds = new LinkedList<>();
   
   @Test
   void add01MetadataToAccount() {
      Account target = Account.generateNewAccount(NETWORK_TYPE);
      logger.info("Adding metadata to account {}", target);
      List<MetadataModification> mods = Arrays.asList(MetadataModification.add("tono", "a"));
      ModifyMetadataTransaction addMeta = ModifyMetadataTransaction
            .createForAddress(getDeadline(), target.getAddress(), mods, NETWORK_TYPE);
      SignedTransaction signedAddMeta = target.sign(addMeta);
      transactionHttp.announce(signedAddMeta).blockingFirst();
      logger.info("Transfer done. {}",
            listener.confirmed(target.getAddress()).timeout(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).blockingFirst());
      // check the meta
      Metadata meta = metadataHttp.getMetadata(target.getAddress()).blockingFirst();
      checkMeta(meta, MetadataType.ADDRESS, new Field("tono", "a"));
      // add to the list of existing metadata items
      metaIds.add(((AddressMetadata)meta).getId());
   }

   @Test
   void add02MetadataToMosaic() {
      MosaicNonce nonce = MosaicNonce.createRandom();
      MosaicId id = new MosaicId(nonce, seedAccount.getPublicKey());
      logger.info("Creating new mosaic {}", id);
      signup(seedAccount.getAddress());
      // create mosaic
      SignedTransaction mdt = MosaicDefinitionTransaction.create(nonce,
            id,
            getDeadline(),
            new MosaicProperties(true, true, false, 6, BigInteger.valueOf(20)),
            NETWORK_TYPE).signWith(seedAccount);
      Observable<Transaction> confirmation = listener.confirmed(seedAccount.getAddress()).timeout(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      transactionHttp.announce(mdt).blockingFirst();
      logger.info("Mosaic created. {}", confirmation.blockingFirst());
      // now add metadata to the mosaic
      List<MetadataModification> mods = Arrays.asList(MetadataModification.add("tono", "mosaic"));
      SignedTransaction signedAddMeta = ModifyMetadataTransaction.createForMosaic(getDeadline(), id, mods, NETWORK_TYPE)
            .signWith(seedAccount);
      transactionHttp.announce(signedAddMeta).blockingFirst();
      logger.info("Meta added to mosaic. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).blockingFirst());
      // check the meta
      Metadata meta = metadataHttp.getMetadata(id).blockingFirst();
      checkMeta(meta, MetadataType.MOSAIC, new Field("tono", "mosaic"));
      // add to the list of existing metadata items
      metaIds.add(((MosaicMetadata)meta).getId().getIdAsHex());
   }

   @Test
   void add03MetadataToNamespace() {
      String name = "test-root-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();
      NamespaceId rootId = new NamespaceId(name);
      logger.info("Going to create namespace {}", rootId);
      // create root namespace
      RegisterNamespaceTransaction registerNamespaceTransaction = RegisterNamespaceTransaction
            .createRootNamespace(getDeadline(), name, BigInteger.valueOf(100), NETWORK_TYPE);
      SignedTransaction signedTransaction = seedAccount.sign(registerNamespaceTransaction);
      transactionHttp.announce(signedTransaction).blockingFirst();
      logger.info("Registered namespace {}. {}",
            name,
            listener.confirmed(seedAccount.getAddress()).timeout(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).blockingFirst());
      // now add metadata to the namespace
      List<MetadataModification> mods = Arrays.asList(MetadataModification.add("tono", "namespace"));
      SignedTransaction signedAddMeta = ModifyMetadataTransaction
            .createForNamespace(getDeadline(), rootId, mods, NETWORK_TYPE).signWith(seedAccount);
      transactionHttp.announce(signedAddMeta).blockingFirst();
      logger.info("Meta added to namespace. {}",
            listener.confirmed(seedAccount.getAddress()).timeout(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).blockingFirst());
      // check the meta
      Metadata meta = metadataHttp.getMetadata(rootId).blockingFirst();
      checkMeta(meta, MetadataType.NAMESPACE, new Field("tono", "namespace"));
      // add to the list of existing metadata items
      metaIds.add(((NamespaceMetadata)meta).getId().getIdAsHex());
   }

   @Test
   void testBulkRequest() {
      assertEquals(3, metadataHttp.getMetadata(metaIds).count().blockingGet());
   }
   
   /**
    * check the metadata values
    * 
    * @param meta metadata instance to check
    * @param type expected metadata type
    * @param field expected field
    */
   private void checkMeta(Metadata meta, MetadataType type, Field field) {
      assertEquals(type, meta.getType());
      assertEquals(1, meta.getFields().size());
      assertEquals(field.getKey(), meta.getFields().get(0).getKey());
      assertEquals(field.getValue(), meta.getFields().get(0).getValue());
      if (MetadataType.MOSAIC == type) {
         MosaicId metaId = ((MosaicMetadata) meta).getId();
         assertEquals(MosaicMetadata.class, meta.getClass());
         // request the same thing from account
         MosaicMetadata addrMeta = metadataHttp.getMetadataFromMosaic(metaId).blockingFirst();
         assertEquals(meta.getFields().get(0).getKey(), addrMeta.getFields().get(0).getKey());
      } else if (MetadataType.NAMESPACE == type) {
         NamespaceId metaId = ((NamespaceMetadata) meta).getId();
         assertEquals(NamespaceMetadata.class, meta.getClass());
         // request the same thing from account
         NamespaceMetadata addrMeta = metadataHttp.getMetadataFromNamespace(metaId).blockingFirst();
         assertEquals(meta.getFields().get(0).getKey(), addrMeta.getFields().get(0).getKey());
      } else if (MetadataType.ADDRESS == type) {
         Address metaId = ((AddressMetadata) meta).getAddress();
         assertEquals(AddressMetadata.class, meta.getClass());
         // request the same thing from account
         AddressMetadata addrMeta = metadataHttp.getMetadataFromAddress(metaId).blockingFirst();
         assertEquals(meta.getFields().get(0).getKey(), addrMeta.getFields().get(0).getKey());
      }
   }
}
