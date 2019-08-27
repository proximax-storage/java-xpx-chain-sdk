/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.alias.AliasAction;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.AliasTransaction;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;

/**
 * builder for {@link AliasTransaction}
 */
public class AliasTransactionBuilder extends TransactionBuilder<AliasTransactionBuilder, AliasTransaction> {

   private Optional<MosaicId> mosaicId;
   private Optional<Address> address;
   private NamespaceId namespaceId;
   private AliasAction aliasAction;

   private AliasTransactionBuilder(TransactionType type, Integer version) {
      super(type, version);
   }

   public static AliasTransactionBuilder createForAddress() {
      return new AliasTransactionBuilder(TransactionType.ADDRESS_ALIAS, TransactionVersion.ADDRESS_ALIAS.getValue());
   }

   public static AliasTransactionBuilder createForMosaic() {
      return new AliasTransactionBuilder(TransactionType.MOSAIC_ALIAS, TransactionVersion.MOSAIC_ALIAS.getValue());
   }

   @Override
   protected AliasTransactionBuilder self() {
      return this;
   }

   @Override
   public AliasTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee()
            .orElseGet(() -> getMaxFeeCalculation(AliasTransaction.calculatePayloadSize(getAddress().isPresent())));
      // create transaction instance
      return new AliasTransaction(getType(), getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(),
            getSigner(), getTransactionInfo(), getMosaicId(), getAddress(), getNamespaceId(), getAliasAction());
   }

   // ------------------------------------- setters ---------------------------------------------//

   public AliasTransactionBuilder mosaicId(MosaicId mosaicId) {
      Validate.isTrue(getType() == TransactionType.MOSAIC_ALIAS,
            "Mosaic ID alias can be created only by mosaic alias builder");
      this.mosaicId = Optional.of(mosaicId);
      return self();
   }

   public AliasTransactionBuilder address(Address address) {
      Validate.isTrue(getType() == TransactionType.ADDRESS_ALIAS,
            "Address alias can be created only by address alias builder");
      this.address = Optional.of(address);
      return self();
   }

   public AliasTransactionBuilder namespaceId(NamespaceId namespaceId) {
      this.namespaceId = namespaceId;
      return self();
   }

   public AliasTransactionBuilder aliasAction(AliasAction aliasAction) {
      this.aliasAction = aliasAction;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the mosaicId
    */
   public Optional<MosaicId> getMosaicId() {
      return mosaicId;
   }

   /**
    * @return the address
    */
   public Optional<Address> getAddress() {
      return address;
   }

   /**
    * @return the namespaceId
    */
   public NamespaceId getNamespaceId() {
      return namespaceId;
   }

   /**
    * @return the aliasAction
    */
   public AliasAction getAliasAction() {
      return aliasAction;
   }

   // -------------------------------------- convenience --------------------------------------------//

   public AliasTransactionBuilder link(MosaicId mosaicId) {
      return aliasAction(AliasAction.LINK).mosaicId(mosaicId);
   }

   public AliasTransactionBuilder link(Address address) {
      return aliasAction(AliasAction.LINK).address(address);
   }

   public AliasTransactionBuilder unlink(MosaicId mosaicId) {
      return aliasAction(AliasAction.UNLINK).mosaicId(mosaicId);
   }

   public AliasTransactionBuilder unlink(Address address) {
      return aliasAction(AliasAction.UNLINK).address(address);
   }

}
