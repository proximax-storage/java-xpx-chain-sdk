/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import io.proximax.sdk.gen.model.NodeInfoDTO;

/**
 * information about node
 */
public class NodeInfo {
   private final String publicKey;
   private final Integer port;
   private final Integer networkIdentifier;
   private final Integer version;
   private final Integer roles;
   private final String host;
   private final String friendlyName;
   
   /**
    * create new instance
    * 
    * @param publicKey the public key of the node
    * @param port port serviced by the node
    * @param networkIdentifier network identifier
    * @param version version of the node
    * @param roles number of roles
    * @param host host name
    * @param friendlyName friendly node name
    */
   public NodeInfo(String publicKey, Integer port, Integer networkIdentifier, Integer version, Integer roles,
         String host, String friendlyName) {
      this.publicKey = publicKey;
      this.port = port;
      this.networkIdentifier = networkIdentifier;
      this.version = version;
      this.roles = roles;
      this.host = host;
      this.friendlyName = friendlyName;
   }

   /**
    * @return the publicKey
    */
   public String getPublicKey() {
      return publicKey;
   }

   /**
    * @return the port
    */
   public Integer getPort() {
      return port;
   }

   /**
    * @return the networkIdentifier
    */
   public Integer getNetworkIdentifier() {
      return networkIdentifier;
   }

   /**
    * @return the version
    */
   public Integer getVersion() {
      return version;
   }

   /**
    * @return the roles
    */
   public Integer getRoles() {
      return roles;
   }

   /**
    * @return the host
    */
   public String getHost() {
      return host;
   }

   /**
    * @return the friendlyName
    */
   public String getFriendlyName() {
      return friendlyName;
   }
   
   /**
    * create new NodeInfo from the DTO object
    * 
    * @param dto the DTO with data
    * @return the node info
    */
   public static NodeInfo fromDto(NodeInfoDTO dto) {
      return new NodeInfo(dto.getPublicKey(), dto.getPort(), dto.getNetworkIdentifier(), dto.getVersion(), dto.getRoles(), dto.getHost(), dto.getFriendlyName());
   }
}
