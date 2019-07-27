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

package io.proximax.sdk.model.transaction;

import java.util.function.Supplier;

import org.spongycastle.util.encoders.Hex;

import io.proximax.core.crypto.Signer;
import io.proximax.sdk.model.account.Account;

/**
 * The cosignature transaction is used to sign an aggregate transactions with missing cosignatures.
 */
public class CosignatureTransaction {
   private static final Supplier<IllegalArgumentException> ANNOUNCE_BEFORE_COSIGN = () -> new IllegalArgumentException(
         "Transaction to cosign should be announced before being able to cosign it");
   private final AggregateTransaction transactionToCosign;

   /**
    * Constructor
    *
    * @param transactionToCosign Aggregate transaction that will be cosigned.
    */
   public CosignatureTransaction(AggregateTransaction transactionToCosign) {
      getHashOrFail(transactionToCosign);
      this.transactionToCosign = transactionToCosign;
   }

   /**
    * Create a cosignature transaction.
    *
    * @param transactionToCosign Aggregate transaction that will be cosigned.
    * @return {@link CosignatureTransaction}
    */
   public static CosignatureTransaction create(AggregateTransaction transactionToCosign) {
      return new CosignatureTransaction(transactionToCosign);
   }

   /**
    * Returns transaction to cosign.
    *
    * @return {@link AggregateTransaction}
    */
   public AggregateTransaction getTransactionToCosign() {
      return transactionToCosign;
   }

   private static String getHashOrFail(AggregateTransaction transactionToCosign) {
      return transactionToCosign.getTransactionInfo().orElseThrow(ANNOUNCE_BEFORE_COSIGN).getHash()
            .orElseThrow(ANNOUNCE_BEFORE_COSIGN);
   }

   /**
    * Serialize and sign transaction creating a new SignedTransaction.
    *
    * @param account Account
    * @return {@link CosignatureSignedTransaction}
    */
   public CosignatureSignedTransaction signWith(Account account) {
      String hash = getHashOrFail(getTransactionToCosign());
      return new CosignatureSignedTransaction(hash, cosignTransaction(hash, account), account.getPublicKey());
   }
   
   /**
    * get cosignature for given transaction hash as hexadecimal string
    * 
    * @param transactionHash hash of the transaction that is being cosigned
    * @param cosignatory the account to cosign the transaction
    * @return hexadecimal string with signature
    */
   public static String cosignTransaction(String transactionHash, Account cosignatory) {
      Signer signer = new Signer(cosignatory.getKeyPair());
      byte[] bytes = Hex.decode(transactionHash);
      byte[] signatureBytes = signer.sign(bytes).getBytes();
      return Hex.toHexString(signatureBytes);

   }
}
