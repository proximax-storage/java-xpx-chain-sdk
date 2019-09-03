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

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.PlainMessage;
import io.proximax.sdk.model.transaction.Recipient;
import io.proximax.sdk.model.transaction.EntityType;
import io.proximax.sdk.model.transaction.EntityVersion;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * builder for {@link TransferTransaction}
 */
public class TransferTransactionBuilder extends TransactionBuilder<TransferTransactionBuilder, TransferTransaction> {
   private Recipient recipient;
   private List<Mosaic> mosaics;
   private Message message;

   public TransferTransactionBuilder() {
      super(EntityType.TRANSFER, EntityVersion.TRANSFER.getValue());
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
      // use or calculate maxFee
      BigInteger maxFee = getMaxFee().orElseGet(
            () -> getMaxFeeCalculation(TransferTransaction.calculatePayloadSize(getMessage(), getMosaics().size())));
      // create transaction instance
      return new TransferTransaction(getNetworkType(), getVersion(), getDeadline(), maxFee, getSignature(), getSigner(),
            getTransactionInfo(), getRecipient(), getMosaics(), getMessage());
   }

   // ------------------------------------ setters ------------------------------------//

   /**
    * transfer recipient
    * 
    * @param recipient the recipient
    * @return self
    */
   public TransferTransactionBuilder recipient(Recipient recipient) {
      this.recipient = recipient;
      return this;
   }

   /**
    * mosaics to be transferred to recipient
    * 
    * @param mosaics list of mosaics for transfer
    * @return self
    */
   public TransferTransactionBuilder mosaics(List<Mosaic> mosaics) {
      this.mosaics = mosaics;
      return this;
   }

   /**
    * transfer message for the recipient
    * 
    * @param message the message
    * @return self
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
    * @return self
    */
   public TransferTransactionBuilder to(Recipient recipient) {
      return recipient(recipient);
   }

   /**
    * convenience call to {@link TransferTransactionBuilder#recipient(Recipient)}
    * 
    * @param address the recipient to set
    * @return self
    */
   public TransferTransactionBuilder to(Address address) {
      return recipient(Recipient.from(address));
   }

   /**
    * convenience call to {@link TransferTransactionBuilder#recipient(Recipient)}
    * 
    * @param namespaceId the recipient to set
    * @return self
    */
   public TransferTransactionBuilder to(NamespaceId namespaceId) {
      return recipient(Recipient.from(namespaceId));
   }

   /**
    * @param mosaics the mosaics to set
    * @return self
    */
   public TransferTransactionBuilder mosaics(Mosaic ... mosaics) {
      return mosaics(Arrays.asList(mosaics));
   }

}
