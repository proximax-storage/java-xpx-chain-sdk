/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

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
   private static final PublicAccount REMOTE_ACCOUNT = new PublicAccount(
         "3fc8ba10229ab5778d05d9c4b7f56676a88bf9295c185acfc0f961db5408cafe", NETWORK_TYPE);

   @Test
   void testFullConstructor() {
      TransactionDeadline deadline = new FakeDeadline();
      PublicAccount remoteAccount = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      PublicAccount signer = new Account(new KeyPair(), NETWORK_TYPE).getPublicAccount();
      TransactionInfo transactionInfo = TransactionInfo.create(BigInteger.TEN, "hash", "merkle");
      String signature = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
      AccountLinkTransaction trans = new AccountLinkTransaction(NETWORK_TYPE, 7, deadline, Optional.of(BigInteger.ONE),
            Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo), Optional.empty(), remoteAccount,
            AccountLinkAction.LINK);
      // assert object attributes
      assertEquals(remoteAccount, trans.getRemoteAccount());
      assertEquals(AccountLinkAction.LINK, trans.getAction());
      assertEquals(NETWORK_TYPE, trans.getNetworkType());
      assertEquals(7, trans.getVersion());
      assertEquals(deadline, trans.getDeadline());
      assertEquals(BigInteger.ONE, trans.getMaxFee());
      assertEquals(signature, trans.getSignature().orElse("missing"));
      assertEquals(signer, trans.getSigner().orElse(null));
      assertEquals(transactionInfo, trans.getTransactionInfo().orElse(null));
   }

   @Test
   void serialization() throws IOException {
      TransactionDeadline deadline = new FakeDeadline();
      AccountLinkTransaction trans = new AccountLinkTransaction(NETWORK_TYPE, 7, deadline, Optional.of(BigInteger.ONE),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), REMOTE_ACCOUNT,
            AccountLinkAction.LINK);
      // serialize
      byte[] actual = trans.generateBytes();
//      saveBytes("account_link", actual);
      assertArrayEquals(loadBytes("account_link"), actual);
   }
}
