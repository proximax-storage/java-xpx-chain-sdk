/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * {@link AddressMetadata} tests
 */
class AddressMetadataTest {

   @Test
   void checkConstructor() {
      List<Field> fields = Arrays.asList(new Field("key", "value"));
      Address address = new Address("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
      AddressMetadata meta = new AddressMetadata(fields, address);
      assertEquals(MetadataType.ADDRESS, meta.getType());
      assertEquals(fields, meta.getFields());
      assertEquals(address, meta.getAddress());
      assertEquals("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", meta.getId());
   }

}
