/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import java.math.BigInteger;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

/**
 * Version of the blockchain
 */
public class BlockchainVersion {

   /** maximum value of unsigned short (2 bytes) */
   private static final int MAX_VALUE = 65535;

   private static final int MASK_2_BYTES = 0xFFFF;
   
   private final int major;
   private final int minor;
   private final int revision;
   private final int build;

   /**
    * @param major
    * @param minor
    * @param revision
    * @param build
    */
   public BlockchainVersion(int major, int minor, int revision, int build) {
      // validate input is in range
      Validate.inclusiveBetween(0, MAX_VALUE, major);
      Validate.inclusiveBetween(0, MAX_VALUE, minor);
      Validate.inclusiveBetween(0, MAX_VALUE, revision);
      Validate.inclusiveBetween(0, MAX_VALUE, build);
      // assign values
      this.major = major;
      this.minor = minor;
      this.revision = revision;
      this.build = build;
   }

   /**
    * @return the major
    */
   public int getMajor() {
      return major;
   }

   /**
    * @return the minor
    */
   public int getMinor() {
      return minor;
   }

   /**
    * @return the revision
    */
   public int getRevision() {
      return revision;
   }

   /**
    * @return the build
    */
   public int getBuild() {
      return build;
   }

   public BigInteger getVersionValue() {
      return BigInteger.valueOf(major)
            .shiftLeft(16)
            .add(BigInteger.valueOf(minor))
            .shiftLeft(16)
            .add(BigInteger.valueOf(revision))
            .shiftLeft(16)
            .add(BigInteger.valueOf(build));
   }

   /**
    * create new instance using the big integer of 8 bytes
    * 
    * @param version
    * @return the version instance representing the version value
    */
   public static BlockchainVersion fromVersionValue(BigInteger version) {
      // 8 bytes can be stored in the long
      long rawValue = version.longValue();
      int build = (int) rawValue & MASK_2_BYTES;
      int rev = (int) (rawValue >>> 16) & MASK_2_BYTES;
      int minor = (int) (rawValue >>> 32) & MASK_2_BYTES;
      int major = (int) (rawValue >>> 48) & MASK_2_BYTES;
      // create new instance
      return new BlockchainVersion(major, minor, rev, build);
   }

   @Override
   public int hashCode() {
      return Objects.hash(build, major, minor, revision);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      BlockchainVersion other = (BlockchainVersion) obj;
      return build == other.build && major == other.major && minor == other.minor && revision == other.revision;
   }

}
