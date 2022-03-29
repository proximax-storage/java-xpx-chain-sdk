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

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.sdk.gen.buffers.RegisterNamespaceTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Accounts can rent a namespace for an amount of blocks and after a this renew the contract. This is done via a
 * RegisterNamespaceTransaction.
 */
public class RegisterNamespaceTransaction extends Transaction {
   private final Schema schema = new RegisterNamespaceTransactionSchema();

   private final String namespaceName;
   private final NamespaceId namespaceId;
   private final Optional<BigInteger> duration;
   private final Optional<NamespaceId> parentId;
   private final NamespaceType namespaceType;

   
   /**
    * @param networkType network type
    * @param version transaction version. Use {@link EntityVersion#REGISTER_NAMESPACE} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param namespaceName name of the namespace
    * @param namespaceId id of the namespace (generated from name and optionally parent)
    * @param duration number of blocks for which the namespace will exist
    * @param parentId parent namespace ID for sub-namespaces
    * @param namespaceType root or sub namespace
    */
   public RegisterNamespaceTransaction(NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, String namespaceName, NamespaceId namespaceId,
         Optional<BigInteger> duration, Optional<NamespaceId> parentId, NamespaceType namespaceType) {
      super(EntityType.REGISTER_NAMESPACE, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      // validations
      Validate.notNull(namespaceName, "NamespaceName must not be null");
      Validate.notNull(namespaceType, "NamespaceType must not be null");
      Validate.notNull(namespaceId, "NamespaceId must not be null");
      if (namespaceType == NamespaceType.ROOT_NAMESPACE) {
         Validate.notNull(duration, "Duration must not be null");
      } else {
         Validate.notNull(parentId, "ParentId must not be null");
      }
      // assignments
      this.namespaceName = namespaceName;
      this.namespaceId = namespaceId;
      this.duration = duration;
      this.parentId = parentId;
      this.namespaceType = namespaceType;
   }

   /**
    * Returns namespace name.
    *
    * @return namespace name
    */
   public String getNamespaceName() {
      return namespaceName;
   }

   /**
    * Returns id of the namespace derived from namespaceName. When creating a sub namespace the namespaceId is derived
    * from namespaceName and parentId.
    *
    * @return namespace id
    */
   public NamespaceId getNamespaceId() {
      return namespaceId;
   }

   /**
    * Returns number of blocks a namespace is active.
    *
    * @return namespace renting duration
    */
   public Optional<BigInteger> getDuration() {
      return duration;
   }

   /**
    * The id of the parent sub namespace.
    *
    * @return sub namespace
    */
   public Optional<NamespaceId> getParentId() {
      return parentId;
   }

   /**
    * Returns namespace type either RootNamespace or SubNamespace.
    *
    * @return namespace type
    */
   public NamespaceType getNamespaceType() {
      return namespaceType;
   }

   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      // duration and parent ID are sent in shared field based on Root/Sub namespace status
      BigInteger durationParentId = getNamespaceType() == NamespaceType.ROOT_NAMESPACE
            ? duration.orElseThrow(() -> new IllegalStateException("Root namespace has to have duration specified"))
            : parentId.orElseThrow(() -> new IllegalStateException("Sub namespace has to have parentId specified"))
                  .getId();

      // Create Vectors
      int signatureVector = RegisterNamespaceTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = RegisterNamespaceTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = RegisterNamespaceTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = RegisterNamespaceTransactionBuffer.createMaxFeeVector(builder,
            UInt64Utils.fromBigInteger(getMaxFee()));
      int namespaceIdVector = RegisterNamespaceTransactionBuffer.createNamespaceIdVector(builder,
            UInt64Utils.fromBigInteger(namespaceId.getId()));
      int durationParentIdVector = RegisterNamespaceTransactionBuffer.createDurationParentIdVector(builder,
            UInt64Utils.fromBigInteger(durationParentId));
      int name = builder.createString(namespaceName);

      int size = getSerializedSize();

      RegisterNamespaceTransactionBuffer.startRegisterNamespaceTransactionBuffer(builder);
      RegisterNamespaceTransactionBuffer.addSize(builder, size);
      RegisterNamespaceTransactionBuffer.addSignature(builder, signatureVector);
      RegisterNamespaceTransactionBuffer.addSigner(builder, signerVector);
      RegisterNamespaceTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      RegisterNamespaceTransactionBuffer.addType(builder, getType().getValue());
      RegisterNamespaceTransactionBuffer.addMaxFee(builder, feeVector);
      RegisterNamespaceTransactionBuffer.addDeadline(builder, deadlineVector);

      RegisterNamespaceTransactionBuffer.addNamespaceType(builder, getNamespaceType().getValue());
      RegisterNamespaceTransactionBuffer.addDurationParentId(builder, durationParentIdVector);
      RegisterNamespaceTransactionBuffer.addNamespaceId(builder, namespaceIdVector);
      RegisterNamespaceTransactionBuffer.addNamespaceNameSize(builder, namespaceName.length());
      RegisterNamespaceTransactionBuffer.addNamespaceName(builder, name);

      int codedTransaction = RegisterNamespaceTransactionBuffer.endRegisterNamespaceTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   /**
    * calculate payload size excluding header
    * 
    * @param namespaceNameLength length of the namespace name
    * @return the size
    */
   public static int calculatePayloadSize(int namespaceNameLength) {
      // ns type, duration, ns id, name size, name
      return 1 + 8 + 8 + 1 + namespaceNameLength;
   }
   
   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize(getNamespaceName().length());
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new RegisterNamespaceTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(), Optional.of(signer), getTransactionInfo(), getNamespaceName(), getNamespaceId(), getDuration(), getParentId(), getNamespaceType());
   }
}
