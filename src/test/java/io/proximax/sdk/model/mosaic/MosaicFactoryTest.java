/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * {@link MosaicFactory} tests
 */
class MosaicFactoryTest {
   private static final UInt64Id ID = new NamespaceId("prx.xpx");
   private static final int DIVISIBILITY = 6;
   private static final BigInteger INITIALSUPPLY = BigInteger.valueOf(9000000000000000l);
   private static final boolean TRANSFERABLE = true;
   private static final boolean SUPPLYMUTABLE = false;

   @Test
   void testConstructor() {
      MosaicFactory fac = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY,
            Optional.empty());
      assertEquals(ID, fac.getMosaicId());
      assertEquals(DIVISIBILITY, fac.getProperties().getDivisibility());
      assertEquals(INITIALSUPPLY, fac.getInitialSupply());
      assertEquals(TRANSFERABLE, fac.getProperties().isTransferable());
      assertEquals(SUPPLYMUTABLE, fac.getProperties().isSupplyMutable());
   }

   @Test
   void testCreate() {
      MosaicFactory fac = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY,
            Optional.empty());
      assertEquals(BigInteger.valueOf(1_500_000), fac.create(BigDecimal.valueOf(1.5)).getAmount());
      assertEquals(BigInteger.valueOf(1_500_000), fac.createAbsolute(BigInteger.valueOf(1_500_000)).getAmount());
   }

   @Test
   void checkHashCode() {
      MosaicFactory a1 = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY,
            Optional.empty());
      MosaicFactory a2 = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY,
            Optional.empty());
      MosaicFactory b = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, 3,
            Optional.empty());
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }

   @Test
   void checkEquals() {
      MosaicFactory a1 = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY,
            Optional.empty());
      MosaicFactory a2 = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, DIVISIBILITY,
            Optional.empty());
      MosaicFactory b = new MosaicFactory(ID, INITIALSUPPLY, SUPPLYMUTABLE, TRANSFERABLE, 3,
            Optional.empty());
      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "othertype");
   }
}
