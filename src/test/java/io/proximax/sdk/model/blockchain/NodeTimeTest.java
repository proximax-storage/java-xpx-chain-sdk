/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.CommunicationTimestamps;
import io.proximax.sdk.gen.model.NodeTimeDTO;
import io.proximax.sdk.gen.model.UInt64DTO;

/**
 * {@link NodeTime} tests
 */
class NodeTimeTest {

   @Test
   void testConstructor() {
      NodeTime nt = new NodeTime(BigInteger.ONE, BigInteger.TEN);
      doAsserts(nt);
   }

   @Test
   void testFromDto() {
      CommunicationTimestamps timestamps = new CommunicationTimestamps();
      timestamps.setSendTimestamp(getDto(BigInteger.ONE));
      timestamps.setReceiveTimestamp(getDto(BigInteger.TEN));
      NodeTimeDTO dto = new NodeTimeDTO();
      dto.setCommunicationTimestamps(timestamps);
      doAsserts(NodeTime.fromDto(dto));
   }
   
   private void doAsserts(NodeTime nt) {
      assertEquals(BigInteger.ONE, nt.getSendTimestamp());
      assertEquals(BigInteger.TEN, nt.getReceiveTimestamp());
   }
   
   /**
    * convert from big integer to uint64. NOTE that this is very rudimentary for the purpose of the test and works only
    * for small numbers
    * 
    * @param smallNumber small positive number in the range of uint
    * @return the uint64 DTO instance
    */
   private UInt64DTO getDto(BigInteger smallNumber) {
      UInt64DTO dto = new UInt64DTO();
      dto.add(smallNumber.longValue());
      dto.add(0l);
      return dto;
   }
}
