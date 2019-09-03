/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.flatbuffers.FlatBufferBuilder;

import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.buffers.AccountLinkTransactionBuffer;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Account linking allows delegated verification. Private key of linked account can be securely shared
 */
public class AccountLinkTransaction extends Transaction {
   private final AccountLinkTransactionSchema schema = new AccountLinkTransactionSchema();

   private final PublicAccount remoteAccount;
   private final AccountLinkAction action;

   /**
    * @param networkType network type
    * @param version transaction version. Use {@link TransactionVersion#ACCOUNT_LINK} for current version
    * @param deadline transaction deadline
    * @param maxFee transaction fee
    * @param signature optional signature
    * @param signer optional signer
    * @param transactionInfo optional transaction info
    * @param remoteAccount remote account
    * @param action link/unlink action
    */
   public AccountLinkTransaction(NetworkType networkType, Integer version, TransactionDeadline deadline,
         BigInteger maxFee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo, PublicAccount remoteAccount, AccountLinkAction action) {
      super(TransactionType.ACCOUNT_LINK, networkType, version, deadline, maxFee, signature, signer, transactionInfo);
      Validate.notNull(remoteAccount, "remoteAccount has to be specified");
      Validate.notNull(action, "action has to be specified");
      this.remoteAccount = remoteAccount;
      this.action = action;
   }

   /**
    * @return the remoteAccount
    */
   public PublicAccount getRemoteAccount() {
      return remoteAccount;
   }

   /**
    * @return the action
    */
   public AccountLinkAction getAction() {
      return action;
   }

   @Override
   protected byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      // prepare data
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      byte[] remoteAccountPublicKey = HexEncoder.getBytes(getRemoteAccount().getPublicKey());

      // Create Vectors
      int signatureVector = AccountLinkTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = AccountLinkTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = AccountLinkTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = AccountLinkTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getMaxFee()));
      int remoteAccountVector = AccountLinkTransactionBuffer.createRemoteAccountKeyVector(builder,
            remoteAccountPublicKey);

      // total size of transaction
      int size = getSerializedSize();

      // flatbuffer serialization
      AccountLinkTransactionBuffer.startAccountLinkTransactionBuffer(builder);
      AccountLinkTransactionBuffer.addSize(builder, size);
      AccountLinkTransactionBuffer.addSignature(builder, signatureVector);
      AccountLinkTransactionBuffer.addSigner(builder, signerVector);
      AccountLinkTransactionBuffer.addVersion(builder, getTxVersionforSerialization());
      AccountLinkTransactionBuffer.addType(builder, getType().getValue());
      AccountLinkTransactionBuffer.addMaxFee(builder, feeVector);
      AccountLinkTransactionBuffer.addDeadline(builder, deadlineVector);
      AccountLinkTransactionBuffer.addRemoteAccountKey(builder, remoteAccountVector);
      AccountLinkTransactionBuffer.addLinkAction(builder, getAction().getCode());

      int codedTransaction = AccountLinkTransactionBuffer.endAccountLinkTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized transaction has incorrect length: " + this.getClass());
      return output;
   }

   public static int calculatePayloadSize() {
      // remote account public key + link action
      return 32 + 1;
   }

   @Override
   protected int getPayloadSerializedSize() {
      return calculatePayloadSize();
   }

   @Override
   protected Transaction copyForSigner(PublicAccount signer) {
      return new AccountLinkTransaction(getNetworkType(), getVersion(), getDeadline(), getMaxFee(), getSignature(),
            Optional.of(signer), getTransactionInfo(), getRemoteAccount(), getAction());
   }

}
