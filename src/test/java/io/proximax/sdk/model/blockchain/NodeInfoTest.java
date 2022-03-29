/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.gen.model.NodeInfoDTO;
import io.proximax.sdk.gen.model.RolesTypeEnum;
import io.proximax.sdk.model.node.NodeInfo;

/**
 * {@link NodeInfo} tests
 */
class NodeInfoTest {

   @Test
   void testConstructor() {
      NodeInfo ni = new NodeInfo("pubkey", 1, 2, 3, "OWNER", "host", "friend");
      doAsserts(ni);
   }

   @Test
   void testFromDto() {
      NodeInfoDTO dto = new NodeInfoDTO();
      dto.setPublicKey("pubkey");
      dto.setPort(1);
      dto.setNetworkIdentifier(2);
      dto.setVersion(3);
      dto.setRoles(RolesTypeEnum.OWNER);
      dto.setHost("host");
      dto.setFriendlyName("friend");
      doAsserts(NodeInfo.fromDto(dto));
   }
   
   private void doAsserts(NodeInfo ni) {
      assertEquals("pubkey", ni.getPublicKey());
      assertEquals(1, ni.getPort());
      assertEquals(2, ni.getNetworkIdentifier());
      assertEquals(3, ni.getVersion());
      assertEquals(RolesTypeEnum.OWNER.getValue(), ni.getRoles());
      assertEquals("host", ni.getHost());
      assertEquals("friend", ni.getFriendlyName());
   }
}
