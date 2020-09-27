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

import java.math.BigInteger;
import java.util.Optional;

import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * Transaction used to announce request for drive for new contract
 * 
 * @author tono
 */
public class DrivePrepareTransaction extends Transaction {
   private final Schema schema = new DrivePrepareTransactionSchema();

   private final String owner;
   private final BigInteger duration;
   private final BigInteger billingPeriod;
   private final BigInteger billingPrice;
   private final BigInteger driveSize;
   private final Integer replicas;
   private final Integer minReplicators;
   private final int percentApprovers;

   /**
    * @param type
    * @param networkType
    * @param version
    * @param deadline
    * @param maxFee
    * @param signature
    * @param signer
    * @param transactionInfo
    * @param owner
    * @param duration
    * @param billingPeriod
    * @param billingPrice
    * @param driveSize
    * @param replicas
    * @param minReplicators
    * @param percentApprovers
    */
   public DrivePrepareTransaction(NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, String owner, BigInteger duration, BigInteger billingPeriod,
         BigInteger billingPrice, BigInteger driveSize, Integer replicas, Integer minReplicators,
         int percentApprovers) {
      super(EntityType.DRIVE_PREPARE, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      this.owner = owner;
      this.duration = duration;
      this.billingPeriod = billingPeriod;
      this.billingPrice = billingPrice;
      this.driveSize = driveSize;
      this.replicas = replicas;
      this.minReplicators = minReplicators;
      this.percentApprovers = percentApprovers;
   }

   /**
    * @return the schema
    */
   public Schema getSchema() {
      return schema;
   }

   /**
    * @return the owner
    */
   public String getOwner() {
      return owner;
   }

   /**
    * @return the duration
    */
   public BigInteger getDuration() {
      return duration;
   }

   /**
    * @return the billingPeriod
    */
   public BigInteger getBillingPeriod() {
      return billingPeriod;
   }

   /**
    * @return the billingPrice
    */
   public BigInteger getBillingPrice() {
      return billingPrice;
   }

   /**
    * @return the driveSize
    */
   public BigInteger getDriveSize() {
      return driveSize;
   }

   /**
    * @return the replicas
    */
   public Integer getReplicas() {
      return replicas;
   }

   /**
    * @return the minReplicators
    */
   public Integer getMinReplicators() {
      return minReplicators;
   }

   /**
    * @return the percentApprovers
    */
   public int getPercentApprovers() {
      return percentApprovers;
   }

   @Override
   protected byte[] generateBytes() {
      throw new UnsupportedOperationException("not implemented yet");
   }

   /**
    * calculate payload size excluding the header
    * 
    * @param recipient lock recipient
    * @param proof proof
    * @return the size
    */
   public static int calculatePayloadSize(Recipient recipient, String proof) {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override
   protected int getPayloadSerializedSize() {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      throw new UnsupportedOperationException("not implemented yet");
   }
}
