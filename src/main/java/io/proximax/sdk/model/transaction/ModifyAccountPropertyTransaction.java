/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.sdk.gen.buffers.AccountPropertiesTransactionBuffer;
import io.proximax.sdk.gen.buffers.PropertyModificationBuffer;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyModificationType;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Transaction to modify account properties
 */
public abstract class ModifyAccountPropertyTransaction<T> extends Transaction {
   private final ModifyAccountPropertyTransactionSchema schema = new ModifyAccountPropertyTransactionSchema();
   
   private final AccountPropertyType propertyType;
   private final List<AccountPropertyModification<T>> modifications;
   
   /**
    * Create new transaction to modify account property
    * 
    * @param transactionType transaction type
    * @param networkType network type
    * @param version version
    * @param deadline deadline
    * @param maxFee max fee
    * @param propertyType type of the account property
    * @param propertyModifications modifications to the account property
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    */
   private ModifyAccountPropertyTransaction(TransactionType transactionType, NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger maxFee, AccountPropertyType propertyType, List<AccountPropertyModification<T>> propertyModifications, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
      super(transactionType, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(propertyType, "propertyType must not be null");
      Validate.notEmpty(propertyModifications, "modifications must not be empty");
      this.propertyType = propertyType;
      this.modifications = propertyModifications;
   }

   /**
    * create new transaction to modify address account properties
    * 
    * @param networkType network type
    * @param deadline transaction deadline
    * @param maxFee maximum fee
    * @param propertyType property type
    * @param propertyModifications property modifications
    * @return the transaction instance
    */
   public static ModifyAccountPropertyTransaction<Address> createForAddress(TransactionDeadline deadline, BigInteger maxFee,
         AccountPropertyType propertyType,
         List<AccountPropertyModification<Address>> propertyModifications, NetworkType networkType) {
      return new AddressModification(networkType, TransactionVersion.ACCOUNT_PROPERTIES_ADDRESS.getValue(), deadline, maxFee, propertyType, propertyModifications, Optional.empty(), Optional.empty(), Optional.empty());
   }
   
   /**
    * create new transaction to modify mosaic account properties
    * 
    * @param networkType network type
    * @param deadline transaction deadline
    * @param maxFee maximum fee
    * @param propertyType property type
    * @param propertyModifications property modifications
    * @return the transaction instance
    */
   public static ModifyAccountPropertyTransaction<UInt64Id> createForMosaic(TransactionDeadline deadline, BigInteger maxFee,
         AccountPropertyType propertyType,
         List<AccountPropertyModification<UInt64Id>> propertyModifications, NetworkType networkType) {
      return new MosaicModification(networkType, TransactionVersion.ACCOUNT_PROPERTIES_MOSAIC.getValue(), deadline, maxFee, propertyType, propertyModifications, Optional.empty(), Optional.empty(), Optional.empty());
   }
   
   /**
    * create new transaction to modify entity type account properties
    * 
    * @param networkType network type
    * @param deadline transaction deadline
    * @param maxFee maximum fee
    * @param propertyType property type
    * @param propertyModifications property modifications
    * @return the transaction instance
    */
   public static ModifyAccountPropertyTransaction<TransactionType> createForEntityType(TransactionDeadline deadline, BigInteger maxFee,
         AccountPropertyType propertyType,
         List<AccountPropertyModification<TransactionType>> propertyModifications, NetworkType networkType) {
      return new EntityTypeModification(networkType, TransactionVersion.ACCOUNT_PROPERTIES_MOSAIC.getValue(), deadline, maxFee, propertyType, propertyModifications, Optional.empty(), Optional.empty(), Optional.empty());
   }
   
   /**
    * @return the account property type
    */
   public AccountPropertyType getPropertyType() {
      return propertyType;
   }

   /**
    * @return the account property modifications
    */
   public List<? extends AccountPropertyModification<T>> getPropertyModifications() {
      return modifications;
   }
   
   /**
    * <p>serialize account property modification value to byte array</p>
    * 
    * <p>NOTE that implementation should also verify that the size of byte array is expected per server requirements</p>
    * 
    * @param mod account property modification
    * @return byte array of expected size
    */
   protected abstract byte[] getValueBytesFromModification(AccountPropertyModification<T> mod);
   
   @Override
   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();

      // prepare data for serialization
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());

      // track the size of the whole transaction
      int totalSize = 0;
      // load modifications
      int[] modificationOffsets = new int[modifications.size()];
      for (int i = 0; i < modificationOffsets.length; i++) {
         AccountPropertyModification<T> mod = modifications.get(i);
         AccountPropertyModificationType modType = mod.getType();
         // prepare intermediate data
         byte[] valueBytes = getValueBytesFromModification(mod);
         // prepare vectors for collections
         int valueOffset = PropertyModificationBuffer.createValueVector(builder, valueBytes);

         // compute number of bytes: modType + value bytes
         int modSize = 1 + valueBytes.length;
         // increase total size
         totalSize += modSize;

         // populate flat-buffer
         PropertyModificationBuffer.startPropertyModificationBuffer(builder);
         PropertyModificationBuffer.addValue(builder, valueOffset);
         PropertyModificationBuffer.addModificationType(builder, modType.getCode());
         modificationOffsets[i] = PropertyModificationBuffer.endPropertyModificationBuffer(builder);
      }

      // create vectors
      int signatureOffset = AccountPropertiesTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerOffset = AccountPropertiesTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineOffset = AccountPropertiesTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeOffset = AccountPropertiesTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      int modificationsOffset = AccountPropertiesTransactionBuffer.createModificationsVector(builder, modificationOffsets);

      // add size of the header + size of prop type (1) + size of mod count (1)
      totalSize += HEADER_SIZE + 1 + 1;

      AccountPropertiesTransactionBuffer.startAccountPropertiesTransactionBuffer(builder);
      AccountPropertiesTransactionBuffer.addDeadline(builder, deadlineOffset);
      AccountPropertiesTransactionBuffer.addMaxFee(builder, feeOffset);
      AccountPropertiesTransactionBuffer.addSigner(builder, signerOffset);
      AccountPropertiesTransactionBuffer.addSignature(builder, signatureOffset);
      AccountPropertiesTransactionBuffer.addSize(builder, totalSize);
      AccountPropertiesTransactionBuffer.addType(builder, getType().getValue());
      AccountPropertiesTransactionBuffer.addVersion(builder, getTxVersionforSerialization());

      AccountPropertiesTransactionBuffer.addPropertyType(builder, getPropertyType().getCode());
      AccountPropertiesTransactionBuffer.addModificationCount(builder, getPropertyModifications().size());
      AccountPropertiesTransactionBuffer.addModifications(builder, modificationsOffset);

      int codedTransaction = AccountPropertiesTransactionBuffer.endAccountPropertiesTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == totalSize, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }
   
   /**
    * Address account property modification transaction implementation
    */
   public static class AddressModification extends ModifyAccountPropertyTransaction<Address> {
      private static final int VALUE_BYTES_LENGTH = 25;

      /**
       * <p>Account property modification for address</p>
       * 
       * <p>use
       * {@link ModifyAccountPropertyTransaction#createForAddress(TransactionDeadline, BigInteger, AccountPropertyType, List, NetworkType)}
       * to create new transaction to modify account property</p>
       * 
       * @param networkType networkType
       * @param version version
       * @param deadline deadline
       * @param maxFee max fee
       * @param propertyType property type
       * @param propertyModifications list of property modifications
       * @param signature optional signature
       * @param signer optional signer
       * @param transactionInfo optional transaction info
       */
      public AddressModification(NetworkType networkType, Integer version, TransactionDeadline deadline,
            BigInteger maxFee, AccountPropertyType propertyType,
            List<AccountPropertyModification<Address>> propertyModifications, Optional<String> signature,
            Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
         super(TransactionType.ACCOUNT_PROPERTIES_ADDRESS, networkType, version, deadline, maxFee, propertyType,
               propertyModifications, signature, signer, transactionInfo);
      }

      @Override
      protected byte[] getValueBytesFromModification(AccountPropertyModification<Address> mod) {
         // get the bytes from string
         byte[] valueBytes = Base32Encoder.getBytes(mod.getValue().plain());
         // check that length is 25 bytes
         Validate.isTrue(valueBytes.length == VALUE_BYTES_LENGTH,
               "Address should be serialized to %d bytes but was %d from %s",
               VALUE_BYTES_LENGTH,
               valueBytes.length,
               mod.getValue());
         // return the value
         return valueBytes;
      }
   }
   
   /**
    * Mosaic account property modification transaction implementation
    */
   public static class MosaicModification extends ModifyAccountPropertyTransaction<UInt64Id> {
      private static final int VALUE_BYTES_LENGTH = 8;
      /**
       * <p>Account property modification for mosaic</p>
       * 
       * <p>use
       * {@link ModifyAccountPropertyTransaction#createForMosaic(TransactionDeadline, BigInteger, AccountPropertyType, List, NetworkType)}
       * to create new transaction to modify account property</p>
       * 
       * @param networkType networkType
       * @param version version
       * @param deadline deadline
       * @param maxFee max fee
       * @param propertyType property type
       * @param list list of property modifications
       * @param signature optional signature
       * @param signer optional signer
       * @param transactionInfo optional transaction info
       */
      public MosaicModification(NetworkType networkType, Integer version, TransactionDeadline deadline,
            BigInteger maxFee, AccountPropertyType propertyType,
            List<AccountPropertyModification<UInt64Id>> list, Optional<String> signature,
            Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
         super(TransactionType.ACCOUNT_PROPERTIES_MOSAIC, networkType, version, deadline, maxFee, propertyType,
               list, signature, signer, transactionInfo);
      }

      @Override
      protected byte[] getValueBytesFromModification(AccountPropertyModification<UInt64Id> mod) {
         // get the bytes from string
         byte[] valueBytes = UInt64Utils.getBytes(mod.getValue().getId());
         // check that length is as expected
         Validate.isTrue(valueBytes.length == VALUE_BYTES_LENGTH,
               "MosaicId should be serialized to %d bytes but was %d from %s",
               VALUE_BYTES_LENGTH,
               valueBytes.length,
               mod.getValue());
         // return the value
         return valueBytes;
      }
   }
   
   /**
    * Transaction type account property modification transaction implementation
    */
   public static class EntityTypeModification extends ModifyAccountPropertyTransaction<TransactionType> {
      private static final int VALUE_BYTES_LENGTH = 2;
      /**
       * <p>Account property modification for mosaic</p>
       * 
       * <p>use
       * {@link ModifyAccountPropertyTransaction#createForMosaic(TransactionDeadline, BigInteger, AccountPropertyType, List, NetworkType)}
       * to create new transaction to modify account property</p>
       * 
       * @param networkType networkType
       * @param version version
       * @param deadline deadline
       * @param maxFee max fee
       * @param propertyType property type
       * @param propertyModifications list of property modifications
       * @param signature optional signature
       * @param signer optional signer
       * @param transactionInfo optional transaction info
       */
      public EntityTypeModification(NetworkType networkType, Integer version, TransactionDeadline deadline,
            BigInteger maxFee, AccountPropertyType propertyType,
            List<AccountPropertyModification<TransactionType>> propertyModifications, Optional<String> signature,
            Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
         super(TransactionType.ACCOUNT_PROPERTIES_ENTITY_TYPE, networkType, version, deadline, maxFee, propertyType,
               propertyModifications, signature, signer, transactionInfo);
      }

      @Override
      protected byte[] getValueBytesFromModification(AccountPropertyModification<TransactionType> mod) {
         // get the bytes from string
         byte[] valueBytes = new byte[VALUE_BYTES_LENGTH];
         ByteBuffer.wrap(valueBytes).order(ByteOrder.LITTLE_ENDIAN).putShort((short)mod.getValue().getValue());
         // return the value
         return valueBytes;
      }
   }
}
