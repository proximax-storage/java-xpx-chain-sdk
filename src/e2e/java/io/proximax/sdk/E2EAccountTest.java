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

package io.proximax.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.props.AccountProperties;
import io.proximax.sdk.model.account.props.AccountProperty;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceInfo;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.ModifyAccountPropertyTransaction;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.proximax.sdk.utils.dto.UInt64Utils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2EAccountTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ETransferTest.class);

   private final Account simpleAccount = new Account(new KeyPair(), getNetworkType());

   @BeforeAll
   void addListener() {
      logger.info("Sending transactions to {}", simpleAccount);
      signup(simpleAccount.getAddress());
      // make transfer to and from the test account to make sure it has public key announced
      sendSomeCash(seedAccount, simpleAccount.getAddress(), 1);
      sendSomeCash(simpleAccount, seedAccount.getAddress(), 1);
   }

   @Test
   void addBlockAccountProperty() {
      Account acct = new Account(new KeyPair(), getNetworkType());
      Account blocked = new Account(new KeyPair(), getNetworkType());
      signup(acct.getAddress());
      signup(blocked.getAddress());
      logger.info("going to block {} by {}", blocked.getPublicAccount(), acct.getPublicAccount());

      ModifyAccountPropertyTransaction<Address> trans = transact.accountPropAddress()
            .blocked(Arrays.asList(AccountPropertyModification.add(blocked.getAddress()))).build();
      // announce the transaction
      transactionHttp.announce(trans.signWith(acct, api.getNetworkGenerationHash())).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(acct.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      sleepForAWhile();
      // now check for the block via GET
      AccountProperties aps = accountHttp.getAccountProperties(acct.getAddress())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      testAccountProperties(aps, blocked.getAddress());
      // check for block via POST
      List<AccountProperties> apsList = accountHttp.getAccountProperties(Arrays.asList(acct.getAddress()))
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, apsList.size());
      testAccountProperties(apsList.get(0), blocked.getAddress());
   }

   @Test
   void addAllowMosaicProperty() {
      Account acct = new Account(new KeyPair(), getNetworkType());
      signup(acct.getAddress());
      UInt64Id allowedMosaic = NetworkCurrencyMosaic.ID;
      logger.info("going to allow {} by {}", allowedMosaic, acct.getPublicAccount());
      ModifyAccountPropertyTransaction<UInt64Id> trans = transact.accountPropMosaic()
            .allowed(Arrays.asList(AccountPropertyModification.add(allowedMosaic))).build();
      // announce the transaction
      transactionHttp.announce(trans.signWith(acct, api.getNetworkGenerationHash())).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(acct.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // now check for the block via GET
      sleepForAWhile();
      AccountProperties aps = accountHttp.getAccountProperties(acct.getAddress())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      testAccountPropertiesOnSimpleAccount(aps, allowedMosaic);
      // check for block via POST
      List<AccountProperties> apsList = accountHttp.getAccountProperties(Arrays.asList(acct.getAddress()))
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, apsList.size());
      testAccountPropertiesOnSimpleAccount(apsList.get(0), allowedMosaic);
   }

   @Test
   void addAllowEntityTypeProperty() {
      Account acct = new Account(new KeyPair(), getNetworkType());
      signup(acct.getAddress());
      EntityType allowedTransType = EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE;
      logger.info("going to allow {} by {}", allowedTransType, acct.getPublicAccount());
      ModifyAccountPropertyTransaction<EntityType> trans = transact.accountPropEntityType()
            .allowed(Arrays.asList(AccountPropertyModification.add(allowedTransType))).build();
      // announce the transaction
      transactionHttp.announce(trans.signWith(acct, api.getNetworkGenerationHash())).blockingFirst();
      logger.info("Waiting for  confirmation");
      listener.confirmed(acct.getAddress()).timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      // now check for the block via GET
      sleepForAWhile();
      AccountProperties aps = accountHttp.getAccountProperties(acct.getAddress())
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      testAccountPropertiesOnSimpleAccount(aps, allowedTransType);
      // check for block via POST
      List<AccountProperties> apsList = accountHttp.getAccountProperties(Arrays.asList(acct.getAddress()))
            .timeout(getTimeoutSeconds(), TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, apsList.size());
      testAccountPropertiesOnSimpleAccount(apsList.get(0), allowedTransType);
   }

   /**
    * check that address block is as expected
    * 
    * @param aps account properties
    * @param blockedAddress address that is blocked
    */
   private void testAccountProperties(AccountProperties aps, Address blockedAddress) {
      boolean gotMatch = false;
      for (AccountProperty ap : aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.ALLOW_ADDRESS)) {
            for (Object value : ap.getValues()) {
               // value should be string and should represent encoded address of the blocked account
               if (value instanceof String && blockedAddress.equals(Address.createFromEncoded((String) value))) {
                  gotMatch = true;
               }
            }

         }
      }
      assertTrue(gotMatch);
   }

   /**
    * check that simple account has allowed mosaic as expected
    * 
    * @param aps account properties
    * @param allowedAsset the asset we are looking for
    */
   private void testAccountPropertiesOnSimpleAccount(AccountProperties aps, UInt64Id allowedAsset) {
      MosaicId allowedMosaic = null;
      if (allowedAsset instanceof MosaicId) {
         allowedMosaic = (MosaicId) allowedAsset;
      } else if (allowedAsset instanceof NamespaceId) {
         logger.info("Converting namespace to aliased mosaic");
         NamespaceInfo ns = namespaceHttp.getNamespace((NamespaceId) allowedAsset).blockingFirst();
         allowedMosaic = ns.getMosaicAlias().orElseThrow(() -> new RuntimeException("Missing mosaic alias"));
      } else {
         fail("Unexpected asset " + allowedAsset);
      }
      boolean gotMatch = false;
      for (AccountProperty ap : aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.ALLOW_MOSAIC)) {
            for (Object value : ap.getValues()) {
               logger.info("allowed mosaic: {}", value);
               // value should be string and should represent encoded address of the blocked account
               if (value instanceof List) {
                  UInt64DTO dto = new UInt64DTO();
                  dto.addAll((List<Long>) value);
                  MosaicId retrievedMosaic = new MosaicId(UInt64Utils.toBigInt(dto));
                  if (retrievedMosaic.equals(allowedMosaic)) {
                     gotMatch = true;
                  }
               }
            }

         }
      }
      assertTrue(gotMatch);
   }

   /**
    * check that simple account has block as expected
    * 
    * @param aps account properties
    * @param blockedAddress address that is blocked
    */
   private void testAccountPropertiesOnSimpleAccount(AccountProperties aps, EntityType allowedTransactionType) {
      boolean gotMatch = false;
      for (AccountProperty ap : aps.getProperties()) {
         if (ap.getPropertyType().equals(AccountPropertyType.ALLOW_TRANSACTION)) {
            for (Object value : ap.getValues()) {
               try {
                  if (value instanceof Long && isValidTransactionTypeCode(((Long) value).intValue())) {
                     assertEquals(EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE,
                           EntityType.rawValueOf(((Long) value).intValue()));
                     gotMatch = true;
                  }
               } catch (RuntimeException e) {
                  // do nothing just ignore
               }
            }

         }
      }
      assertTrue(gotMatch);
   }

   private static boolean isValidTransactionTypeCode(int code) {
      try {
         EntityType.rawValueOf(code);
         return true;
      } catch (RuntimeException e) {
         return false;
      }
   }


}