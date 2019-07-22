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
    * Create new instance of account link transaction
    * 
    * @param remoteAccount account to manage link for
    * @param action link action to perform
    * @param networkType network type
    * @param version transaction version
    * @param deadline transaction deadline
    * @param fee transaction fee
    * @param signature signature
    * @param signer signer
    * @param transactionInfo transaction information
    */
   private AccountLinkTransaction(PublicAccount remoteAccount, AccountLinkAction action, NetworkType networkType,
         Integer version, TransactionDeadline deadline, BigInteger fee, Optional<String> signature,
         Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
      super(TransactionType.ACCOUNT_LINK, networkType, version, deadline, fee, signature, signer, transactionInfo);
      Validate.notNull(remoteAccount, "remoteAccount has to be specified");
      Validate.notNull(action, "action has to be specified");
      this.remoteAccount = remoteAccount;
      this.action = action;
   }

   /**
    * Create new instance of account link transaction
    * 
    * @param remoteAccount account to manage link for
    * @param action link action to perform
    * @param networkType network type
    * @param version transaction version
    * @param deadline transaction deadline
    * @param fee transaction fee
    * @param signature signature
    * @param signer signer
    * @param transactionInfo transaction information
    */
   public AccountLinkTransaction(PublicAccount remoteAccount, AccountLinkAction action, NetworkType networkType,
         Integer version, TransactionDeadline deadline, BigInteger fee, String signature, PublicAccount signer,
         TransactionInfo transactionInfo) {
      this(remoteAccount, action, networkType, version, deadline, fee, Optional.of(signature), Optional.of(signer),
            Optional.of(transactionInfo));
   }

   /**
    * Create new instance of account link transaction
    * 
    * @param remoteAccount account to manage link for
    * @param action link action to perform
    * @param networkType network type
    * @param deadline transaction deadline
    * @param fee transaction fee
    */
   public AccountLinkTransaction(PublicAccount remoteAccount, AccountLinkAction action, NetworkType networkType,
         TransactionDeadline deadline, BigInteger fee) {
      this(remoteAccount, action, networkType, TransactionVersion.ACCOUNT_LINK.getValue(), deadline, fee,
            Optional.empty(), Optional.empty(), Optional.empty());
   }

   /**
    * Create new instance of account link transaction
    * 
    * @param deadline transaction deadline
    * @param fee transaction fee
    * @param remoteAccount account to manage link for
    * @param action link action to perform
    * @param networkType network type
    * @return account link transaction instance
    */
   public static AccountLinkTransaction create(TransactionDeadline deadline, BigInteger fee,
         PublicAccount remoteAccount, AccountLinkAction action, NetworkType networkType) {
      return new AccountLinkTransaction(remoteAccount, action, networkType, deadline, fee);
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
   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
      // prepare data
      BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
      int version = (int) Long
            .parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);
      byte[] remoteAccountPublicKey = HexEncoder.getBytes(getRemoteAccount().getPublicKey());

      // Create Vectors
      int signatureVector = AccountLinkTransactionBuffer.createSignatureVector(builder, new byte[64]);
      int signerVector = AccountLinkTransactionBuffer.createSignerVector(builder, new byte[32]);
      int deadlineVector = AccountLinkTransactionBuffer.createDeadlineVector(builder,
            UInt64Utils.fromBigInteger(deadlineBigInt));
      int feeVector = AccountLinkTransactionBuffer.createMaxFeeVector(builder, UInt64Utils.fromBigInteger(getFee()));
      int remoteAccountVector = AccountLinkTransactionBuffer.createRemoteAccountKeyVector(builder,
            remoteAccountPublicKey);

      // total size of transaction
      int size =
            // header
            120 +
            // remote account public key
            32 +
            // link action
            1;

      // flatbuffer serialization
      AccountLinkTransactionBuffer.startAccountLinkTransactionBuffer(builder);
      AccountLinkTransactionBuffer.addSize(builder, size);
      AccountLinkTransactionBuffer.addSignature(builder, signatureVector);
      AccountLinkTransactionBuffer.addSigner(builder, signerVector);
      AccountLinkTransactionBuffer.addVersion(builder, version);
      AccountLinkTransactionBuffer.addType(builder, getType().getValue());
      AccountLinkTransactionBuffer.addMaxFee(builder, feeVector);
      AccountLinkTransactionBuffer.addDeadline(builder, deadlineVector);
      AccountLinkTransactionBuffer.addRemoteAccountKey(builder, remoteAccountVector);
      AccountLinkTransactionBuffer.addLinkAction(builder, getAction().getCode());

      int codedTransaction = AccountLinkTransactionBuffer.endAccountLinkTransactionBuffer(builder);
      builder.finish(codedTransaction);

      // validate size
      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized form has incorrect length");
      return output;
   }

}
