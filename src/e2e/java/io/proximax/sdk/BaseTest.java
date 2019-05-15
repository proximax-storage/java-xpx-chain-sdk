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


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * base class for all integration tests
 */
public abstract class BaseTest {
   /** timeout in seconds to wait for response */
   private static final Integer DEFAULT_WAIT_TIMEOUT_SECONDS = 30;

   // system environment property names
   private static final String SYS_ENV_PRIVATE_KEY = "E2E_SEED_PRIVATE_KEY";
   private static final String SYS_ENV_NETWORK_TYPE = "E2E_NETWORK_TYPE";
   private static final String SYS_ENV_URL = "E2E_URL";
   private static final String SYS_ENV_TIMEOUT = "E2E_TIMEOUT";
   // config property file keys
   private static final String PROP_KEY_URL = "nem2sdk.conf.url";
   private static final String PROP_KEY_PRIVATE_KEY = "xpxsdk.conf.seed.private_key";
   private static final String PROP_KEY_NETWORK_TYPE = "xpxsdk.conf.network_type";
   private static final String PROP_KEY_TIMEOUT = "xpxsdk.conf.timeout";
   
   /**
    * get node URL
    * 
    * @return the URL of the node to use for tests
    */
   protected String getNodeUrl() {
      return getPropertyValue(SYS_ENV_URL, PROP_KEY_URL, "node url");
   }

   /**
    * get network type
    * 
    * @return network type
    */
   protected NetworkType getNetworkType() {
      String typeName = getPropertyValue(SYS_ENV_NETWORK_TYPE, PROP_KEY_NETWORK_TYPE, "network type");
      return NetworkType.valueOf(typeName);
   }

   /**
    * timeout for requests. note that long timeout can make failed tests long before they fail
    * 
    * @return the timeout in seconds
    */
   protected Integer getTimeoutSeconds() {
      String timeout = getPropertyValue(SYS_ENV_TIMEOUT, PROP_KEY_TIMEOUT, "request timeout");
      return timeout == null ? DEFAULT_WAIT_TIMEOUT_SECONDS : Integer.valueOf(timeout);
   }

   /**
    * seed account used as basis for all transfers
    * 
    * @return account
    */
   protected Account getSeedAccount() {
      String accountPk = getPropertyValue(SYS_ENV_PRIVATE_KEY, PROP_KEY_PRIVATE_KEY, "seed account private key");
      return new Account(accountPk, getNetworkType());
   }
   
   /**
    * get value of a property. first try environment variable and then config.properties
    * 
    * @param envVar name of environment variable to use
    * @param confProperty key of the property in config.properties
    * @param name description for potential error message
    * @return the string value of the property
    */
   private String getPropertyValue(String  envVar, String confProperty, String name) {
      // system variables have precedence
      String value = System.getenv(envVar);
      // if system does not provide the value then go for res bundle property
      if (value == null) {
         final Properties properties = new Properties();
         try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
               throw new IOException("config.properties not found");
            }
            properties.load(inputStream);
            value = properties.getProperty(confProperty);
         } catch (IOException ignored) {
         }
      }
      // return the value or null if nothing was found
      return value;
   }
   
}
