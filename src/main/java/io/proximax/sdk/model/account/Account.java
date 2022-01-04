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

package io.proximax.sdk.model.account;

import java.util.List;

import org.apache.commons.lang3.Validate;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.AggregateTransaction;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.proximax.sdk.model.transaction.CosignatureTransaction;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;

/**
 * The account structure describes an account private key, public key, address and allows signing transactions.
 *
 * @since 1.0
 */
public class Account {
   private final KeyPair keyPair;
   private final PublicAccount publicAccount;

   /**
    * Constructor
    *
    * @param privateKey String
    * @param networkType NetworkType
    */
   public Account(String privateKey, NetworkType networkType) {
      Validate.notNull(privateKey, "privateKey can not be null");
      Validate.notNull(networkType, "networkType can not be null");
      this.keyPair = new KeyPair(PrivateKey.fromHexString(privateKey));
      this.publicAccount = new PublicAccount(this.getPublicKey(), networkType);
   }

   public Account(KeyPair keyPair, NetworkType networkType) {
      Validate.notNull(keyPair, "privateKey can not be null");
      Validate.notNull(networkType, "networkType can not be null");
      this.keyPair = keyPair;
      this.publicAccount = new PublicAccount(this.getPublicKey(), networkType);
   }

   /**
    * Create an Account from a given private key.
    *
    * @param privateKey Private key from an account
    * @param networkType NetworkType
    * @return {@link Account}
    */
   public static Account createFromPrivateKey(String privateKey, NetworkType networkType) {
      return new Account(privateKey, networkType);
   }

   /**
    * Create an new Account
    *
    * @param networkType the network type or account
    * @return new random account for specified network type
    */
   public static Account generateNewAccount(NetworkType networkType) {
      KeyPair keyPair = new KeyPair();
      return new Account(keyPair.getPrivateKey().toString(), networkType);
   }

   /**
    * Account public key.
    *
    * @return {@link String}
    */
   public String getPublicKey() {
      return this.keyPair.getPublicKey().toString().toUpperCase();
   }

   /**
    * Account private key.
    *
    * @return {@link String}
    */
   public String getPrivateKey() {
      return this.keyPair.getPrivateKey().toString().toUpperCase();
   }

   /**
    * Account keyPair containing public and private key.
    *
    * @return {@link KeyPair}
    */
   public KeyPair getKeyPair() {
      return keyPair;
   }

   /**
    * Account address.
    *
    * @return {@link Address}
    */
   public Address getAddress() {
      return this.publicAccount.getAddress();
   }

   /**
    * Public account.
    *
    * @return {@link PublicAccount}
    */
   public PublicAccount getPublicAccount() {
      return publicAccount;
   }

   /**
    * Sign a transaction.
    *
    * @param transaction The transaction to be signed.
    * @param generationHash network generation hash retrieved from block 1
    * @return {@link SignedTransaction}
    */
   public SignedTransaction sign(Transaction transaction, String generationHash) {
      return transaction.signWith(this, generationHash);
   }

   /**
    * Sign aggregate signature transaction.
    *
    * @param cosignatureTransaction The aggregate signature transaction.
    * @return {@link CosignatureSignedTransaction}
    */
   public CosignatureSignedTransaction signCosignatureTransaction(CosignatureTransaction cosignatureTransaction) {
      return cosignatureTransaction.signWith(this);
   }

   /**
    * Sign transaction with cosignatories creating a new SignedTransaction.
    *
    * @param transaction The aggregate transaction to be signed.
    * @param generationHash network generation hash retrieved from block 1
    * @param cosignatories The list of accounts that will cosign the transaction
    * @return {@link SignedTransaction}
    */
   public SignedTransaction signTransactionWithCosignatories(AggregateTransaction transaction, String generationHash,
         List<Account> cosignatories) {
      return transaction.signTransactionWithCosigners(this, generationHash, cosignatories);
   }

   @Override
   public String toString() {
      return "Account [keyPair=" + keyPair + ", publicAccount=" + publicAccount + "]";
   }

}
