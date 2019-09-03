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

import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.proximax.sdk.infrastructure.AccountHttp;
import io.proximax.sdk.infrastructure.BlockchainHttp;
import io.proximax.sdk.infrastructure.ContractHttp;
import io.proximax.sdk.infrastructure.Listener;
import io.proximax.sdk.infrastructure.MetadataHttp;
import io.proximax.sdk.infrastructure.MosaicHttp;
import io.proximax.sdk.infrastructure.NamespaceHttp;
import io.proximax.sdk.infrastructure.TransactionHttp;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * Central API for blockchain interaction
 */
public class BlockchainApi {
   /** default fee calculation strategy */
   public static final FeeCalculationStrategy DEFAULT_FEE_CALCULATION_STRATEGY = FeeCalculationStrategy.MEDIUM;
   
   /** URL of the node */
   private final URL url;
   /** network type of the node */
   private NetworkType networkType;
   /** network generation hash used for signing */
   private String networkGenerationHash;

   /**
    * create new instance that connects to specified node
    * 
    * @param url URL of the node
    */
   public BlockchainApi(URL url) {
      this.url = url;
   }

   /**
    * create new instance that connects to specified node
    * 
    * @param url URL of the node
    * @param networkType network type of the node
    */
   public BlockchainApi(URL url, NetworkType networkType) {
      this.url = url;
      this.networkType = networkType;
   }

   /**
    * check that the network type matches what is reported by the node
    * 
    * this is useful to make sure that network type is OK. This method also loads network type if not specified in
    * constructor
    * 
    * @return true if reported network type matches expectation, false otherwise
    */
   public boolean isNetworkTypeValid() {
      return queryForNetworkType() == getNetworkType();
   }

   /**
    * create account repository
    * 
    * @return the account repository
    */
   public AccountRepository createAccountRepository() {
      return new AccountHttp(this);
   }

   /**
    * create blockchain repository
    * 
    * @return the blockchain repository
    */
   public BlockchainRepository createBlockchainRepository() {
      return new BlockchainHttp(this);
   }

   /**
    * create contract repository
    * 
    * @return the contract repository
    */
   public ContractRepository createContractRepository() {
      return new ContractHttp(this);
   }

   /**
    * create metadata repository
    * 
    * @return the metadata repository
    */
   public MetadataRepository createMetadataRepository() {
      return new MetadataHttp(this);
   }

   /**
    * create mosaic repository
    * 
    * @return the mosaic repository
    */
   public MosaicRepository createMosaicRepository() {
      return new MosaicHttp(this);
   }

   /**
    * create namespace repository
    * 
    * @return the namespace repository
    */
   public NamespaceRepository createNamespaceRepository() {
      return new NamespaceHttp(this);
   }

   /**
    * create transaction repository
    * 
    * @return the transaction repository
    */
   public TransactionRepository createTransactionRepository() {
      return new TransactionHttp(this);
   }

   /**
    * create listener that allows caller to subscribe to various network events
    * 
    * @return the network listener
    */
   public ListenerRepository createListener() {
      return new Listener(this);
   }

   /**
    * get the configured network type
    * 
    * @return the networkType
    */
   public synchronized NetworkType getNetworkType() {
      if (networkType == null) {
         networkType = queryForNetworkType();
      }
      return networkType;
   }

   /**
    * get the URL of the node
    * 
    * @return the URL of the node
    */
   public URL getUrl() {
      return url;
   }

   /**
    * convenience method for signing of transactions
    * 
    * @param transaction the transaction to be signed
    * @param signer signing account
    * 
    * @return signed transaction instance
    */
   public SignedTransaction sign(Transaction transaction, Account signer) {
      return transaction.signWith(signer, getNetworkGenerationHash());
   }
   
   /**
    * convenience method for signing of transactions
    * 
    * @param transaction the transaction to be signed
    * @param initiator signing account that initiated the transaction
    * @param cosignatories cosigning accounts
    * 
    * @return signed transaction instance
    */
   public SignedTransaction signWithCosigners(AggregateTransaction transaction, Account initiator, List<Account> cosignatories) {
      return transaction.signTransactionWithCosigners(initiator, getNetworkGenerationHash(), cosignatories);
   }
   
   /**
    * get network generation hash for purpose of transaction signing
    * 
    * @return the network generation hash represented as hexadecimal string
    */
   public synchronized String getNetworkGenerationHash() {
      if (networkGenerationHash == null) {
         networkGenerationHash = createBlockchainRepository().getBlockByHeight(BigInteger.ONE)
               .timeout(30, TimeUnit.SECONDS).blockingFirst().getGenerationHash();
      }
      return networkGenerationHash;
   }

   /**
    * query the network blockchain API to retrieve network type as reported by the node
    * 
    * @return network type of the node
    */
   private NetworkType queryForNetworkType() {
      return createBlockchainRepository().getNetworkType().timeout(30, TimeUnit.SECONDS).blockingFirst();
   }
   
   /**
    * @return the factory to get transaction builders
    */
   public TransactionBuilderFactory transact() {
      TransactionBuilderFactory fac = new TransactionBuilderFactory();
      fac.setNetworkType(getNetworkType());
      fac.setFeeCalculationStrategy(DEFAULT_FEE_CALCULATION_STRATEGY);
      return fac;
   }
}
