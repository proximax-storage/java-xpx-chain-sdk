/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.account.props;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.gen.model.AccountPropertiesInfoDTO;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.reactivex.Observable;

/**
 * TODO add proper description
 */
class AccountPropertiesTest extends ResourceBasedTest {
   
   @Test
   void testConstructor() {
      Account acct = new Account(new KeyPair(), NetworkType.MAIN_NET);
      String propertyValue = "12345";
      AccountProperty property = new AccountProperty(AccountPropertyType.ALLOW_ADDRESS, Arrays.asList(propertyValue));
      List<AccountProperty> propList = Arrays.asList(property);
      AccountProperties props = new AccountProperties(acct.getAddress(), propList);
      // check that getters return expected values
      assertEquals(acct.getAddress(), props.getAddress());
      assertEquals(1, props.getProperties().size());
      AccountProperty propProperty = props.getProperties().get(0);
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, propProperty.getPropertyType());
      assertEquals(1, propProperty.getValues().size());
      assertEquals(propertyValue, propProperty.getValues().get(0));
   }
   
   @Test
   void checkResourceBundleDeserialization() {
      // init the object mapper
      final Gson gson = new Gson();
      // go through all the items and collect them to a list
      List<AccountProperties> props = Observable.fromIterable(getResources("account_properties", "dtos", "properties"))
         .map(Object::toString)
         .map(str -> gson.fromJson(str, AccountPropertiesInfoDTO.class))
         .map(AccountPropertiesInfoDTO::getAccountProperties)
         .map(AccountProperties::fromDto)
         .toList()
         .blockingGet();
      assertTrue(!props.isEmpty());
   }

}
