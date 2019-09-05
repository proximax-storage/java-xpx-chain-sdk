/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.Optional;

import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.model.transaction.IdGenerator;
import io.proximax.sdk.model.transaction.RegisterNamespaceTransaction;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;

/**
 * builder for {@link RegisterNamespaceTransaction}
 */
public class RegisterNamespaceTransactionBuilder
      extends TransactionBuilder<RegisterNamespaceTransactionBuilder, RegisterNamespaceTransaction> {

   private String namespaceName;
   private NamespaceId namespaceId;
   private Optional<BigInteger> duration;
   private Optional<NamespaceId> parentId;
   private NamespaceType namespaceType;

   public RegisterNamespaceTransactionBuilder() {
      super(EntityType.REGISTER_NAMESPACE, EntityVersion.REGISTER_NAMESPACE.getValue());
      // defaults
      duration = Optional.empty();
      parentId = Optional.empty();
   }

   @Override
   protected RegisterNamespaceTransactionBuilder self() {
      return this;
   }

   @Override
   public RegisterNamespaceTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(RegisterNamespaceTransaction.calculatePayloadSize(getNamespaceName().length())));
      // create transaction instance
      return new RegisterNamespaceTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getNamespaceName(), getNamespaceId(), getDuration(), getParentId(),
            getNamespaceType());
   }

   // -------------------------------------- setters --------------------------------------------//

   /**
    * name of the namespace to register
    * 
    * @param namespaceName the name
    * @return self
    */
   public RegisterNamespaceTransactionBuilder namespaceName(String namespaceName) {
      this.namespaceName = namespaceName;
      return self();
   }

   /**
    * ID of the namespace to register
    * 
    * @param namespaceId the id
    * @return self
    */
   public RegisterNamespaceTransactionBuilder namespaceId(NamespaceId namespaceId) {
      this.namespaceId = namespaceId;
      return self();
   }

   /**
    * duration of the namespace existence in block count
    * 
    * @param duration number of blocks
    * @return self
    */
   public RegisterNamespaceTransactionBuilder duration(BigInteger duration) {
      this.duration = Optional.of(duration);
      return self();
   }

   /**
    * if registering sub-namespace then define parent
    * 
    * @param parentId parent namespace of this sub-namespace
    * @return self
    */
   public RegisterNamespaceTransactionBuilder parentId(NamespaceId parentId) {
      this.parentId = Optional.of(parentId);
      return self();
   }

   /**
    * type of the namespace - root/sub
    * 
    * @param namespaceType namespace type
    * @return self
    */
   public RegisterNamespaceTransactionBuilder namespaceType(NamespaceType namespaceType) {
      this.namespaceType = namespaceType;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the namespaceName
    */
   public String getNamespaceName() {
      return namespaceName;
   }

   /**
    * @return the namespaceId
    */
   public NamespaceId getNamespaceId() {
      return namespaceId;
   }

   /**
    * @return the duration
    */
   public Optional<BigInteger> getDuration() {
      return duration;
   }

   /**
    * @return the parentId
    */
   public Optional<NamespaceId> getParentId() {
      return parentId;
   }

   /**
    * @return the namespaceType
    */
   public NamespaceType getNamespaceType() {
      return namespaceType;
   }

   // -------------------------------------- convenience --------------------------------------------//

   /**
    * create root namespace
    * 
    * @param name name of the namespace
    * @return self
    */
   public RegisterNamespaceTransactionBuilder rootNamespace(String name) {
      namespaceType(NamespaceType.ROOT_NAMESPACE);
      namespaceName(name);
      namespaceId(new NamespaceId(name));
      return self();
   }
   
   /**
    * create sub-namespace
    * 
    * @param parentId parent of the new sub-namespace
    * @param name name of the new sub-namespace
    * @return self
    */
   public RegisterNamespaceTransactionBuilder subNamespace(NamespaceId parentId, String name) {
      namespaceType(NamespaceType.SUB_NAMESPACE);
      parentId(parentId);
      namespaceName(name);
      namespaceId(new NamespaceId(IdGenerator.generateSubNamespaceIdFromParentId(parentId.getId(), name)));
      return self();
   }
}