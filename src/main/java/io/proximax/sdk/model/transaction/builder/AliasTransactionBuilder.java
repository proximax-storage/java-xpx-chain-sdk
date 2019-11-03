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
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;

/**
 * <p>
 * builder for {@link AliasTransaction}
 * <p>
 * <p>
 * Standard use when managing alias for mosaic: call {@link #link(MosaicId)} or {@link #unlink(MosaicId)}
 * </p>
 * <p>
 * Standard use when managing alias for address: call {@link #link(Address)} or {@link #unlink(Address)}
 * </p>
 */
public class AliasTransactionBuilder extends TransactionBuilder<AliasTransactionBuilder, AliasTransaction> {

   private Optional<MosaicId> mosaicId;
   private Optional<Address> address;
   private NamespaceId namespaceId;
   private AliasAction aliasAction;

   private AliasTransactionBuilder(EntityType type, Integer version) {
      super(type, version);
      // defaults
      mosaicId = Optional.empty();
      address = Optional.empty();
   }

   public static AliasTransactionBuilder createForAddress() {
      return new AliasTransactionBuilder(EntityType.ADDRESS_ALIAS, EntityVersion.ADDRESS_ALIAS.getValue());
   }

   public static AliasTransactionBuilder createForMosaic() {
      return new AliasTransactionBuilder(EntityType.MOSAIC_ALIAS, EntityVersion.MOSAIC_ALIAS.getValue());
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

   /**
    * define mosaic ID for which alias is to be created
    * 
    * @param mosaicId id of the mosaic
    * @return self
    */
   public AliasTransactionBuilder mosaicId(MosaicId mosaicId) {
      Validate.isTrue(getType() == EntityType.MOSAIC_ALIAS,
            "Mosaic ID alias can be created only by mosaic alias builder");
      this.mosaicId = Optional.of(mosaicId);
      return self();
   }

   /**
    * specify address for which the alias is to be created
    * 
    * @param address the address
    * @return self
    */
   public AliasTransactionBuilder address(Address address) {
      Validate.isTrue(getType() == EntityType.ADDRESS_ALIAS,
            "Address alias can be created only by address alias builder");
      this.address = Optional.of(address);
      return self();
   }

   /**
    * specify namespace ID which will be having alias to either address or mosaic
    * 
    * @param namespaceId target namespace ID
    * @return self
    */
   public AliasTransactionBuilder namespaceId(NamespaceId namespaceId) {
      this.namespaceId = namespaceId;
      return self();
   }

   /**
    * action to perform with the alias
    * 
    * @param aliasAction link or unlink
    * @return self
    */
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

   /**
    * create alias for mosaic ID
    * 
    * @param mosaicId the mosaic ID
    * @return self
    */
   public AliasTransactionBuilder link(MosaicId mosaicId) {
      return aliasAction(AliasAction.LINK).mosaicId(mosaicId);
   }

   /**
    * create alias for the address
    * 
    * @param address the address
    * @return self
    */
   public AliasTransactionBuilder link(Address address) {
      return aliasAction(AliasAction.LINK).address(address);
   }

   /**
    * remove alias from mosaic ID
    * 
    * @param mosaicId the mosaic ID
    * @return self
    */
   public AliasTransactionBuilder unlink(MosaicId mosaicId) {
      return aliasAction(AliasAction.UNLINK).mosaicId(mosaicId);
   }

   /**
    * remove link from the address
    * 
    * @param address the address
    * @return self
    */
   public AliasTransactionBuilder unlink(Address address) {
      return aliasAction(AliasAction.UNLINK).address(address);
   }

}
