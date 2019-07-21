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
    * Create new instance of account link presentation
    * 
    * @param remoteAccount
    * @param action
    * @param networkType
    * @param version
    * @param deadline
    * @param fee
    * @param signature
    * @param signer
    * @param transactionInfo
    */
   public AccountLinkTransaction(PublicAccount remoteAccount, AccountLinkAction action, NetworkType networkType, Integer version,
         TransactionDeadline deadline, BigInteger fee, Optional<String> signature, Optional<PublicAccount> signer,
         Optional<TransactionInfo> transactionInfo) {
      super(TransactionType.ACCOUNT_LINK, networkType, version, deadline, fee,
            signature, signer, transactionInfo);
      Validate.notNull(remoteAccount, "remoteAccount has to be specified");
      Validate.notNull(action, "action has to be specified");
      this.remoteAccount = remoteAccount;
      this.action = action;
   }

   public AccountLinkTransaction(PublicAccount remoteAccount, AccountLinkAction action, NetworkType networkType,
         TransactionDeadline deadline, BigInteger fee) {
      super(TransactionType.ACCOUNT_LINK, networkType, TransactionVersion.ACCOUNT_LINK.getValue(), deadline, fee,
            Optional.empty(), Optional.empty(), Optional.empty());
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
   byte[] generateBytes() {
      FlatBufferBuilder builder = new FlatBufferBuilder();
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

      byte[] output = schema.serialize(builder.sizedByteArray());
      Validate.isTrue(output.length == size, "Serialized form has incorrect length");
      return output;
   }

}
