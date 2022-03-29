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
import io.proximax.sdk.model.network.NetworkType;

public class FakeTransferTransaction extends Transaction {

   public FakeTransferTransaction(NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo) {
      super(EntityType.TRANSFER, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
   }

   public FakeTransferTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger maxFee) {
      this(networkType, version, deadline, maxFee, Optional.empty(), Optional.empty(), Optional.empty());
   }

   public FakeTransferTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline, BigInteger maxFee,
         String signature, PublicAccount signer, TransactionInfo info) {
      this(networkType, version, deadline, maxFee, Optional.of(signature), Optional.of(signer), Optional.of(info));
   }

   @Override
   protected byte[] generateBytes() {
      throw new UnsupportedOperationException("Method no implemented");
   }

   @Override
   protected int getPayloadSerializedSize() {
      throw new UnsupportedOperationException("Method no implemented");
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      throw new UnsupportedOperationException("Method no implemented");
   }
}
