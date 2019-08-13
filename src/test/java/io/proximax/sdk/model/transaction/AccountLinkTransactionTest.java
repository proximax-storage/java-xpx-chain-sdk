/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link AccountLinkTransaction} tests
 */
class AccountLinkTransactionTest extends ResourceBasedTest {
   private static final NetworkType NETWORK_TYPE = NetworkType.MAIN_NET;
   private static final PublicAccount REMOTE_ACCOUNT = new PublicAccount("3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe", NETWORK_TYPE);
   
   @Test
   void testFullConstructor() {
      TransactionDeadline deadline = new FakeDeadline();
      PublicAccount remoteAccount = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      PublicAccount signer = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      TransactionInfo transactionInfo = TransactionInfo.create(BigInteger.TEN, "hash", "merkle");
      String signature = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
      AccountLinkTransaction trans = new AccountLinkTransaction(remoteAccount, AccountLinkAction.LINK, NETWORK_TYPE, 7,
            deadline, BigInteger.ONE, signature, signer, transactionInfo);
      // assert object attributes
      assertEquals(remoteAccount, trans.getRemoteAccount());
      assertEquals(AccountLinkAction.LINK, trans.getAction());
      assertEquals(NETWORK_TYPE, trans.getNetworkType());
      assertEquals(7, trans.getVersion());
      assertEquals(deadline, trans.getDeadline());
      assertEquals(BigInteger.ONE, trans.getFee());
      assertEquals(signature, trans.getSignature().orElse("missing"));
      assertEquals(signer, trans.getSigner().orElse(null));
      assertEquals(transactionInfo, trans.getTransactionInfo().orElse(null));
   }

   @Test
   void testShortConstructor() {
      TransactionDeadline deadline = new FakeDeadline();
      PublicAccount remoteAccount = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      AccountLinkTransaction trans = new AccountLinkTransaction(remoteAccount, AccountLinkAction.LINK, NETWORK_TYPE,
            deadline, BigInteger.ONE);
      // assert object attributes
      assertEquals(remoteAccount, trans.getRemoteAccount());
      assertEquals(AccountLinkAction.LINK, trans.getAction());
      assertEquals(NETWORK_TYPE, trans.getNetworkType());
      assertEquals(TransactionVersion.ACCOUNT_LINK.getValue(), trans.getVersion());
      assertEquals(deadline, trans.getDeadline());
      assertEquals(BigInteger.ONE, trans.getFee());
      assertFalse(trans.getSignature().isPresent());
      assertFalse(trans.getSigner().isPresent());
      assertFalse(trans.getTransactionInfo().isPresent());
   }

   @Test
   void testStaticConstructor() {
      TransactionDeadline deadline = new FakeDeadline();
      PublicAccount remoteAccount = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      AccountLinkTransaction trans = AccountLinkTransaction
            .create(deadline, BigInteger.ONE, remoteAccount, AccountLinkAction.LINK, NETWORK_TYPE);
      // assert object attributes
      assertEquals(remoteAccount, trans.getRemoteAccount());
      assertEquals(AccountLinkAction.LINK, trans.getAction());
      assertEquals(NETWORK_TYPE, trans.getNetworkType());
      assertEquals(TransactionVersion.ACCOUNT_LINK.getValue(), trans.getVersion());
      assertEquals(deadline, trans.getDeadline());
      assertEquals(BigInteger.ONE, trans.getFee());
      assertFalse(trans.getSignature().isPresent());
      assertFalse(trans.getSigner().isPresent());
      assertFalse(trans.getTransactionInfo().isPresent());
   }

   @Test
   void serialization() throws IOException {
      TransactionDeadline deadline = new FakeDeadline();
      AccountLinkTransaction trans = AccountLinkTransaction
            .create(deadline, BigInteger.ONE, REMOTE_ACCOUNT, AccountLinkAction.LINK, NETWORK_TYPE);
      // serialize
      byte[] actual = trans.generateBytes();
//      saveBytes("account_link", actual);
      assertArrayEquals(loadBytes("account_link"), actual);
   }
}
