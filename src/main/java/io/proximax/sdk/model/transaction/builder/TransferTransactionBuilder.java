/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction.builder;

import java.util.ArrayList;
import java.util.List;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.TransactionType;
import io.proximax.sdk.model.transaction.TransactionVersion;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * builder for transfer transactions
 */
public class TransferTransactionBuilder extends TransactionBuilder<TransferTransactionBuilder, TransferTransaction> {
   private Recipient recipient;
   private List<Mosaic> mosaics;
   private Message message;
   
   public TransferTransactionBuilder() {
      super(TransactionType.TRANSFER, TransactionVersion.TRANSFER.getValue());
      // make default message empty
      message = PlainMessage.Empty;
      // make the list of mosaics to transfer empty
      mosaics = new ArrayList<>();
   }

   
   @Override
   protected TransferTransactionBuilder self() {
      return this;
   }

   @Override
   public TransferTransaction build() {
      return new TransferTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getRecipient(),
            getMosaics(), getMessage(), getSignature(), getSigner(), getTransactionInfo(), getFeeCalculationStrategy());
   }

   // ------------------------------------ setters ------------------------------------//
   
   /**
    * @param recipient the recipient to set
    */
   public TransferTransactionBuilder recipient(Recipient recipient) {
      this.recipient = recipient;
      return this;
   }

   /**
    * @param mosaics the mosaics to set
    */
   public TransferTransactionBuilder mosaics(List<Mosaic> mosaics) {
      this.mosaics = mosaics;
      return this;
   }

   /**
    * @param message the message to set
    */
   public TransferTransactionBuilder message(Message message) {
      this.message = message;
      return this;
   }

   // ------------------------------------------- getters ------------------------------------------//
   /**
    * @return the recipient
    */
   public Recipient getRecipient() {
      return recipient;
   }

   /**
    * @return the mosaics
    */
   public List<Mosaic> getMosaics() {
      return mosaics;
   }

   /**
    * @return the message
    */
   public Message getMessage() {
      return message;
   }
   
   // ----------------------------------------- convenience methods -------------------------------------//
   
   /**
    * convenience call to {@link TransferTransactionBuilder#recipient(Recipient)}
    * 
    * @param recipient the recipient to set
    */
   public TransferTransactionBuilder to(Recipient recipient) {
      return recipient(recipient);
   }

   /**
    * convenience call to {@link TransferTransactionBuilder#recipient(Recipient)}
    * 
    * @param address the recipient to set
    */
   public TransferTransactionBuilder to(Address address) {
      return recipient(Recipient.from(address));
   }

   /**
    * convenience call to {@link TransferTransactionBuilder#recipient(Recipient)}
    * 
    * @param namespaceId the recipient to set
    */
   public TransferTransactionBuilder to(NamespaceId namespaceId) {
      return recipient(Recipient.from(namespaceId));
   }


   /**
    * @param mosaic add mosaic to the list of transferred mosaics
    */
   public TransferTransactionBuilder addMosaic(Mosaic mosaic) {
      mosaics.add(mosaic);
      return this;
   }

   /**
    * @param mosaic the mosaics to set
    */
   public TransferTransactionBuilder mosaic(Mosaic mosaic) {
      this.mosaics = new ArrayList<>();
      this.mosaics.add(mosaic);
      return this;
   }

}
