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
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyModificationType;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.mosaic.MosaicId;

/**
 * basic tests for account property modification transactions
 */
class ModifyAccountPropertyTransactionTest extends ResourceBasedTest {

   @Test
   void serializationAddress() throws IOException {
      Address address = Address.createFromRawAddress("SBMJIUDBTCFVS24UHGND2ZJ3OX5EFKIPESXCTWLO");
      System.out.println(address);
      Deadline deadline = new FakeDeadline();

      ModifyAccountPropertyTransaction<Address> trans = new ModifyAccountPropertyTransaction.AddressModification(
            NetworkType.MIJIN_TEST, 1, deadline, BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            AccountPropertyType.ALLOW_ADDRESS,
            Arrays.asList(new AccountPropertyModification<>(AccountPropertyModificationType.ADD, address)));
      // check that values are as expected
      assertEquals(BigInteger.ZERO, trans.getMaxFee());
      assertEquals(TransactionType.ACCOUNT_PROPERTIES_ADDRESS, trans.getType());
      assertEquals(1, trans.getVersion());
      assertEquals(deadline.getInstant(), trans.getDeadline().getInstant());
      assertEquals(AccountPropertyType.ALLOW_ADDRESS, trans.getPropertyType());
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(address, trans.getPropertyModifications().get(0).getValue());
      // check that serialization does not fail
      byte[] actual = trans.generateBytes();
//      saveBytes("account_property_address", actual);
      assertArrayEquals(loadBytes("account_property_address"), actual);
   }

   @Test
   void serializationMosaic() throws IOException {
      Deadline deadline = new FakeDeadline();
      ModifyAccountPropertyTransaction<UInt64Id> trans = new ModifyAccountPropertyTransaction.MosaicModification(
            NetworkType.MIJIN_TEST, 1, deadline, BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            AccountPropertyType.ALLOW_MOSAIC,
            Arrays.asList(new AccountPropertyModification<>(AccountPropertyModificationType.ADD,
                  new MosaicId(BigInteger.ONE))));
      // check that values are as expected
      assertEquals(BigInteger.ZERO, trans.getMaxFee());
      assertEquals(TransactionType.ACCOUNT_PROPERTIES_MOSAIC, trans.getType());
      assertEquals(1, trans.getVersion());
      assertEquals(deadline.getInstant(), trans.getDeadline().getInstant());
      assertEquals(AccountPropertyType.ALLOW_MOSAIC, trans.getPropertyType());
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(new MosaicId(BigInteger.ONE), trans.getPropertyModifications().get(0).getValue());
      // check that serialization does not fail
      byte[] actual = trans.generateBytes();
//      saveBytes("account_property_mosaic", actual);
      assertArrayEquals(loadBytes("account_property_mosaic"), actual);
   }

   @Test
   void serializationEntity() throws IOException {
      Deadline deadline = new FakeDeadline();
      ModifyAccountPropertyTransaction<TransactionType> trans = new ModifyAccountPropertyTransaction.EntityTypeModification(
            NetworkType.MIJIN_TEST, 1, deadline, BigInteger.ZERO, Optional.empty(), Optional.empty(), Optional.empty(),
            AccountPropertyType.BLOCK_TRANSACTION, Arrays.asList(
                  new AccountPropertyModification<>(AccountPropertyModificationType.ADD, TransactionType.LOCK)));
      // check that values are as expected
      assertEquals(BigInteger.ZERO, trans.getMaxFee());
      assertEquals(TransactionType.ACCOUNT_PROPERTIES_ENTITY_TYPE, trans.getType());
      assertEquals(TransactionVersion.ACCOUNT_PROPERTIES_ENTITY_TYPE.getValue(), trans.getVersion());
      assertEquals(deadline.getInstant(), trans.getDeadline().getInstant());
      assertEquals(AccountPropertyType.BLOCK_TRANSACTION, trans.getPropertyType());
      assertEquals(NetworkType.MIJIN_TEST, trans.getNetworkType());
      assertEquals(1, trans.getPropertyModifications().size());
      assertEquals(TransactionType.LOCK, trans.getPropertyModifications().get(0).getValue());
      // check that serialization does not fail
      byte[] actual = trans.generateBytes();
//      saveBytes("account_property_entity", actual);
      assertArrayEquals(loadBytes("account_property_entity"), actual);
   }

}
