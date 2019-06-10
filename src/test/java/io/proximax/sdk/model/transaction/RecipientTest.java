/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.utils.Base32Encoder;
import io.proximax.sdk.model.account.Account;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Recipient tests
 */
class RecipientTest {

   @Test
   void testForAddress() {
      Account acct = new Account(new KeyPair(), NetworkType.MAIN_NET);
      Recipient rcp = new Recipient(acct.getAddress());
      assertTrue(rcp.getAddress().isPresent());
      assertTrue(!rcp.getNamespaceId().isPresent());
      assertEquals(acct.getAddress(), rcp.getAddress().orElseThrow(RuntimeException::new));
      // test also the static constructor
      assertEquals(rcp, Recipient.from(acct.getAddress()));
      
   }

   @Test
   void testForNamespace() {
      NamespaceId nid = new NamespaceId("test.this");
      Recipient rcp = new Recipient(nid);
      assertTrue(!rcp.getAddress().isPresent());
      assertTrue(rcp.getNamespaceId().isPresent());
      assertEquals(nid, rcp.getNamespaceId().orElseThrow(RuntimeException::new));
      // test also the static constructor
      assertEquals(rcp, Recipient.from(nid));
   }

   @Test
   void testHashCode() {
      Recipient r1 = Recipient.from(new NamespaceId("hello"));
      Recipient r2 = Recipient.from(new NamespaceId("hello"));
      assertEquals(r1.hashCode(), r2.hashCode());
   }
   
   @Test
   void testEqualsNamespace() {
      Recipient r1 = Recipient.from(new NamespaceId("hello"));
      Recipient r2 = Recipient.from(new NamespaceId("hello"));
      Recipient r3 = Recipient.from(new NamespaceId("other"));
      assertEquals(r1, r1);
      assertEquals(r1, r2);
      assertNotEquals(r1, null);
      assertNotEquals(r1, "other class");
      assertNotEquals(r1, r3);
   }
   
   @Test
   void testEqualsAddress() {
      Address addr1 = new Account(new KeyPair(), NetworkType.MAIN_NET).getAddress();
      Address addr2 = new Account(new KeyPair(), NetworkType.MAIN_NET).getAddress();
      Recipient r1 = Recipient.from(addr1);
      Recipient r2 = Recipient.from(addr1);
      Recipient r3 = Recipient.from(addr2);
      assertEquals(r1, r1);
      assertEquals(r1, r2);
      assertNotEquals(r1, null);
      assertNotEquals(r1, "other class");
      assertNotEquals(r1, r3);
   }
   
   @Test
   void serializeAddress() {
      Address addr = Address.createFromRawAddress("SARDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY");
      byte[] addrBytes = Base32Encoder.getBytes(addr.plain());
      byte[] recipientBytes = Recipient.from(addr).getBytes();
      assertEquals(25, recipientBytes.length);
      // test that recipient serializes to expected bytes
      assertArrayEquals(addrBytes, recipientBytes);
   }
   
   @Test
   void serializeNamespace() {
      NamespaceId nsid = new NamespaceId("test");
      byte[] nsidBytes = UInt64Utils.getBytes(nsid.getId());
      // test that recipient serializes to expected bytes
      byte[] recipientBytes = Recipient.from(nsid).getBytes();
      assertEquals(25, recipientBytes.length);
      // first byte should be 0x91
      assertEquals((byte)0x91, recipientBytes[0]);
      // next 8 bytes should be namespace id
      for (int i=1; i<9; i++) {
         assertEquals(nsidBytes[i-1], recipientBytes[i]);
      }
      // remaining bytes are 0
      for (int i=9; i<25; i++) {
         assertEquals(0, recipientBytes[i]);
      }
   }
   
}
