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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;

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
      String accountPk = "28FCECEA252231D2C86E1BCF7DD541552BDBBEFBB09324758B3AC199B4AA7B78"; // local
//    String accountPk = "FA4EB55A4C0B462095F22F7768B7EDCC1DFC9287B16614E019E1326AC6E625D9"; // bcstage
//      String accountPk =  System.getenv(SYS_ENV_PRIVATE_KEY);
      if (accountPk == null) {
         fail("Seed account private key needs to be defined as env variable " + SYS_ENV_PRIVATE_KEY);
      } else {
         return new Account(accountPk, networkType);
      }
      // should never get here since fail throws exception
      throw new IllegalStateException("Test was supposed to fail or return account");
   }
}
