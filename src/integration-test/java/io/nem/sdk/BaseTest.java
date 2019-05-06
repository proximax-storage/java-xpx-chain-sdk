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

package io.nem.sdk;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceId;

/**
 * base class for all integration tests
 *
 * @author tonowie
 */
public abstract class BaseTest {
   protected static final String NAMESPACE_PRX_NAME = "cat";
   protected static final NamespaceId PROXIMA_NAMESPACE = new NamespaceId(NAMESPACE_PRX_NAME);

   /** network type for IT tests */
   protected static final NetworkType NETWORK_TYPE = NetworkType.MIJIN_TEST;


   private static final String SYS_ENV_PRIVATE_KEY = "SEED_ACCOUNT_PRIVATE_KEY";

   public String getNodeUrl() throws IOException {
      String url = null;
      final Properties properties = new Properties();
      try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
         if (inputStream == null) {
            throw new IOException("config.properties not found");
         }
         properties.load(inputStream);
         url = properties.getProperty("nem2sdk.conf.nodeurl");
      } catch (IOException ignored) {
      }
      return url;
   }

   /**
    * 
    * @param networkType
    * @return
    */
   protected Account getSeedAccount(NetworkType networkType) {
      String accountPk =  System.getenv(SYS_ENV_PRIVATE_KEY);
      if (accountPk == null) {
         fail("Seed account private key needs to be defined as env variable " + SYS_ENV_PRIVATE_KEY);
      } else {
         return new Account(accountPk, networkType);
      }
      // should never get here since fail throws exception
      throw new IllegalStateException("Test was supposed to fail or return account");
   }
}
