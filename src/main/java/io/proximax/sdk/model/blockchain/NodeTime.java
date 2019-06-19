/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import java.math.BigInteger;

import io.proximax.sdk.gen.model.CommunicationTimestamps;
import io.proximax.sdk.gen.model.NodeTimeDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * Node time representation
 */
public class NodeTime {
   private final BigInteger sendTimestamp;
   private final BigInteger receiveTimestamp;

   /**
    * create new instance
    * 
    * @param sendTimestamp send timestamp
    * @param receiveTimestamp receive timestamp
    */
   public NodeTime(BigInteger sendTimestamp, BigInteger receiveTimestamp) {
      this.sendTimestamp = sendTimestamp;
      this.receiveTimestamp = receiveTimestamp;
   }

   /**
    * @return the sendTimestamp
    */
   public BigInteger getSendTimestamp() {
      return sendTimestamp;
   }

   /**
    * @return the receiveTimestamp
    */
   public BigInteger getReceiveTimestamp() {
      return receiveTimestamp;
   }

   /**
    * create new node time instance from the DTO
    * 
    * @param dto the DTO with data
    * @return the node time instance
    */
   public static NodeTime fromDto(NodeTimeDTO dto) {
      CommunicationTimestamps timestamps = dto.getCommunicationTimestamps();
      return new NodeTime(UInt64Utils.toBigInt(timestamps.getSendTimestamp()),
            UInt64Utils.toBigInt(timestamps.getReceiveTimestamp()));
   }
}
