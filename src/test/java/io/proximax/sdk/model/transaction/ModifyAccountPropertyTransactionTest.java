/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyModificationType;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * basic tests for account property modification transactions
 */
class ModifyAccountPropertyTransactionTest {

   @Test
   void testAddressAccountPropertyModificationTransaction() {
      Address address = new Account(new KeyPair(), NetworkType.MIJIN_TEST).getAddress();
      Deadline deadline = Deadline.create(10, ChronoUnit.MINUTES);
      ModifyAccountPropertyTransaction<Address> trans = ModifyAccountPropertyTransaction.createForAddress(
            deadline,
            BigInteger.ZERO,
            AccountPropertyType.ALLOW_ADDRESS,
            Arrays.asList(new AccountPropertyModification<>(AccountPropertyModificationType.ADD, address)),
            NetworkType.MIJIN_TEST);
      // check that values are as expected
      assertEquals(BigInteger.ZERO, trans.getFee());
      assertEquals(TransactionType.ACCOUNT_PROPERTIES_ADDRESS, trans.getType());
      assertEquals(TransactionVersion.ACCOUNT_PROPERTIES_ADDRESS.getValue(), trans.getVersion());
      assertEquals(deadline.getInstant(), trans.getDeadline().getInstant());
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, trans.getPropertyType());
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(address, trans.getPropertyModifications().get(0).getValue());
      // check that serialization does not fail
      assertTrue(trans.generateBytes().length > 0);
   }

}
