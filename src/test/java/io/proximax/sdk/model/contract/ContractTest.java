/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.gen.model.ContractInfoDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.reactivex.Observable;

/**
 * {@link Contract} tests
 */
class ContractTest extends ResourceBasedTest {
   private static final List<PublicKey> CUSTOMERS = Arrays.asList(new KeyPair().getPublicKey());
   private static final List<PublicKey> EXECUTORS = Arrays.asList(new KeyPair().getPublicKey(), new KeyPair().getPublicKey());
   private static final List<PublicKey> VERIFIERS = Arrays.asList(new KeyPair().getPublicKey(), new KeyPair().getPublicKey(), new KeyPair().getPublicKey());
   
   @Test
   void checkConstructor() {
      Address address = new Address("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
      BigInteger start = BigInteger.ONE;
      BigInteger duration = BigInteger.TEN;
      Contract c = new Contract("multisigstr", address, start, duration, "contentHash", CUSTOMERS, EXECUTORS, VERIFIERS);
      
      assertEquals("multisigstr", c.getMultisig());
      assertEquals("contentHash", c.getContentHash());
      assertEquals(address, c.getMultisigAddress());
      assertEquals(start, c.getStart());
      assertEquals(duration, c.getDuration());
      assertEquals(CUSTOMERS, c.getCustomers());
      assertEquals(EXECUTORS, c.getExecutors());
      assertEquals(VERIFIERS, c.getVerifiers());
   }
   
   @Test
   void testDeserialization() {
      // get the object mapper
      final Gson gson = new Gson();
      // deserialize bundle items
      List<Contract> items = Observable.fromIterable(getResources("contracts", "dtos", "contracts"))
            .map(JsonElement::toString)
            .map(str -> gson.fromJson(str, ContractInfoDTO.class))
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto)
            .toList().blockingGet();
      // make sure that something was read from the bundle
      assertTrue(!items.isEmpty());
   }

   private static Contract createContract(String contentHash) {
      Address address = new Address("SDRDGF-TDLLCB-67D4HP-GIMIHP-NSRYRJ-RT7DOB-GWZY", NetworkType.MIJIN_TEST);
      BigInteger start = BigInteger.ONE;
      BigInteger duration = BigInteger.TEN;
      return new Contract("multisigstr", address, start, duration, contentHash, CUSTOMERS, EXECUTORS, VERIFIERS);
   }
   
   @Test
   void checkHashCode() {
      assertEquals(createContract("a").hashCode(), createContract("a").hashCode());
      assertNotEquals(createContract("a").hashCode(), createContract("b").hashCode());
   }
   
   @Test
   void checkEquals() {
      Contract a1 = createContract("a");
      Contract a2 = createContract("a");
      Contract b = createContract("b");
      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "whatever");
   }
}
