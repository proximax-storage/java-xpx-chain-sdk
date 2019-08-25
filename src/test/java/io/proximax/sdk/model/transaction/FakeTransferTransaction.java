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

import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

public class FakeTransferTransaction extends Transaction {

   public FakeTransferTransaction(TransactionType type, NetworkType networkType, Integer version,
         TransactionDeadline deadline, Optional<BigInteger> fee, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo,
         Optional<FeeCalculationStrategy> feeCalculationStrategy) {
      super(type, networkType, version, deadline, fee, signature, signer, transactionInfo, feeCalculationStrategy);
   }

   public FakeTransferTransaction(NetworkType networkType, int version, Deadline deadline, BigInteger fee) {
      this(TransactionType.TRANSFER, networkType, version, deadline, Optional.of(fee), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty());
   }

   public FakeTransferTransaction(NetworkType networkType, int version, Deadline deadline, BigInteger fee,
         String signature, PublicAccount signer, TransactionInfo transInfo) {
      this(TransactionType.TRANSFER, networkType, version, deadline, Optional.of(fee), Optional.of(signature),
            Optional.of(signer), Optional.of(transInfo), Optional.empty());
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
