/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.helpers;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.transaction.Message;
import io.proximax.sdk.model.transaction.TransferTransaction;

/**
 * Helper for mosaic transfers
 */
public class TransferHelper extends BaseHelper {

   /**
    * @param api
    */
   public TransferHelper(BlockchainApi api) {
      super(api);
   }

   /**
    * <b>BLOCKING</b> transfer mosaic from standard (non-multisig) account
    * 
    * @param from the source account
    * @param to target address
    * @param mosaic mosaic to transfer
    * @param message transfer message
    * @param confirmationTimeoutSeconds seconds to wait for transaction confirmation
    */
   public void transfer(Account from, Address to, Mosaic mosaic, Message message, int confirmationTimeoutSeconds) {
      // prepare transfer transaction
      TransferTransaction transferTx = transact.transfer().mosaics(mosaic).to(to).message(message).build();
      // announce the transaction
      transactionConfirmed(transferTx, from, confirmationTimeoutSeconds);
   }

   /**
    * <b>BLOCKING</b> make transfer from multisignature account. Cosigners are expected to approve the transaction
    * before lockBlocks elapsed
    * 
    * @param from the public key for the multisig account
    * @param initiator the initiator of the transaction (one of cosigners)
    * @param to target account
    * @param mosaic mosaic to transfer
    * @param message transfer message
    * @param confirmationTimeoutSeconds time to wait for transaction confirmation
    * @param lockBlocks number of blocks to wait for cosigners to cosign the transaction
    */
   public void transferFromMultisig(PublicAccount from, Account initiator, Address to, Mosaic mosaic, Message message,
         int confirmationTimeoutSeconds, int lockBlocks) {
      // prepare transfer transaction
      TransferTransaction transferTx = transact.transfer().mosaics(mosaic).to(to).message(message).build();
      // announce as aggregate bonded to allow cosigners to act
      announceAsAggregateBonded(initiator, lockBlocks, confirmationTimeoutSeconds, transferTx.toAggregate(from));
   }
}
