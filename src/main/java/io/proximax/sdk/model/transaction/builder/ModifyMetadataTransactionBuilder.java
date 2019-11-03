/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.ModifyMetadataTransaction;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * <p>
 * builder for {@link ModifyMetadataTransaction}
 * </p>
 * <p>
 * Standard use: first initialize builder by call to one of {@link #forAddress(Address)}, {@link #forMosaic(MosaicId)}
 * or {@link #forNamespace(NamespaceId)}. Then specify modifications to make via call to {@link #modifications(List)} or
 * {@link #modifications(MetadataModification...)}
 * </p>
 */
public class ModifyMetadataTransactionBuilder
      extends TransactionBuilder<ModifyMetadataTransactionBuilder, ModifyMetadataTransaction> {

   private MetadataType metadataType;
   private Optional<UInt64Id> metadataId;
   private Optional<Address> address;
   private List<MetadataModification> modifications;

   public ModifyMetadataTransactionBuilder() {
      super(null, EntityVersion.METADATA_MODIFICATION.getValue());
      // defaults
      metadataId = Optional.empty();
      address = Optional.empty();
      modifications = new ArrayList<>();
   }

   @Override
   protected ModifyMetadataTransactionBuilder self() {
      return this;
   }

   @Override
   public ModifyMetadataTransaction build() {
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(() -> getMaxFeeCalculation(
            ModifyMetadataTransaction.calculatePayloadSize(getAddress().isPresent(), getModifications())));
      // create transaction instance
      return new ModifyMetadataTransaction(getType(), getNetworkType(), getVersion(), getDeadline(), maxFee,
            getSignature(), getSigner(), getTransactionInfo(), getMetadataType(), getMetadataId(), getAddress(),
            getModifications());
   }

   // ------------------------------------- setters ---------------------------------------------//

   /**
    * specify address for which the metadata modification will happen
    * 
    * @param address the address
    * @return self
    */
   public ModifyMetadataTransactionBuilder metadataId(Address address) {
      this.address = Optional.of(address);
      return self();
   }

   /**
    * specify address for which the metadata modification will happen
    * 
    * @param id mosaic ID or namespace ID
    * @return self
    */
   public ModifyMetadataTransactionBuilder metadataId(UInt64Id id) {
      this.metadataId = Optional.of(id);
      return self();
   }

   /**
    * specify the metadata type
    * 
    * @param type the type of metadata
    * @return self
    */
   public ModifyMetadataTransactionBuilder metadataType(MetadataType type) {
      this.metadataType = type;
      return self();
   }

   /**
    * set metadata modifications
    * 
    * @param modifications the modifications
    * @return self
    */
   public ModifyMetadataTransactionBuilder modifications(List<MetadataModification> modifications) {
      this.modifications = modifications;
      return self();
   }

   // -------------------------------------- getters --------------------------------------------//

   /**
    * @return the metadataType
    */
   public MetadataType getMetadataType() {
      return metadataType;
   }

   /**
    * @return the metadataId
    */
   public Optional<UInt64Id> getMetadataId() {
      return metadataId;
   }

   /**
    * @return the address
    */
   public Optional<Address> getAddress() {
      return address;
   }

   /**
    * @return the modifications
    */
   public List<MetadataModification> getModifications() {
      return modifications;
   }

   // -------------------------------------- convenience --------------------------------------------//

   public ModifyMetadataTransactionBuilder forAddress(Address address) {
      return type(EntityType.MODIFY_ADDRESS_METADATA).metadataType(MetadataType.ADDRESS).metadataId(address);
   }

   public ModifyMetadataTransactionBuilder forMosaic(MosaicId mosaicId) {
      return type(EntityType.MODIFY_MOSAIC_METADATA).metadataType(MetadataType.MOSAIC).metadataId(mosaicId);
   }

   public ModifyMetadataTransactionBuilder forNamespace(NamespaceId namespaceId) {
      return type(EntityType.MODIFY_NAMESPACE_METADATA).metadataType(MetadataType.NAMESPACE).metadataId(namespaceId);
   }

   /**
    * set metadata modifications
    * 
    * @param modifications the modifications
    * @return self
    */
   public ModifyMetadataTransactionBuilder modifications(MetadataModification... modifications) {
      return modifications(Arrays.asList(modifications));
   }
}
