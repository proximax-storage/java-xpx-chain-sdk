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

import java.net.URL;

import io.proximax.sdk.infrastructure.AccountHttp;
import io.proximax.sdk.infrastructure.BlockchainHttp;
import io.proximax.sdk.infrastructure.MetadataHttp;
import io.proximax.sdk.infrastructure.MosaicHttp;
import io.proximax.sdk.infrastructure.NamespaceHttp;
import io.proximax.sdk.infrastructure.NetworkHttp;
import io.proximax.sdk.infrastructure.TransactionHttp;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * Central API for blockchain interaction
 */
public class BlockchainApi {

   private final URL url;
   private final NetworkType networkType;
   
   /**
    * @param url
    */
   public BlockchainApi(URL url, NetworkType networkType) {
      this.url = url;
      this.networkType = networkType;
   }
   
   public AccountRepository createAccountRepository() {
      return new AccountHttp(this);
   }

   public BlockchainRepository createBlockchainRepository() {
      return new BlockchainHttp(this);
   }

   public MetadataRepository createMetadataRepository() {
      return new MetadataHttp(this);
   }
   
   public MosaicRepository createMosaicRepository() {
      return new MosaicHttp(this);
   }
   
   public NamespaceRepository createNamespaceRepository() {
      return new NamespaceHttp(this);
   }
   
   public NetworkRepository createNetworkRepository() {
      return new NetworkHttp(this);
   }

   public TransactionRepository createTransactionRepository() {
      return new TransactionHttp(this);
   }

   /**
    * @return the networkType
    */
   public NetworkType getNetworkType() {
      return networkType;
   }

   /**
    * @return the url
    */
   public URL getUrl() {
      return url;
   }
   
   
}
