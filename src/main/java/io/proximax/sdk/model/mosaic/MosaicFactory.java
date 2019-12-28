/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.mosaic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

import io.proximax.sdk.model.transaction.UInt64Id;

/**
 * Factory that produces Mosaic instances for amounts specified as
 * <ul>
 * <li>token value regardless of the divisibility - call {@link #create(BigDecimal)} with amount 1.5 to create mosaic
 * for 1.5 token</li>
 * <li>absolute value taking divisibility into account - call {@link #createAbsolute(BigInteger)} with amount 1_500_000
 * to create mosaic for 1.5 token assuming token has divisibility 6</li>
 * </ul>
 */
public class MosaicFactory {
   private final UInt64Id mosaicId;
   private final BigInteger initialSupply;
   private final MosaicProperties properties;
   // cache the multiplier for performance reasons
   private final BigDecimal divisibilityMultiplier;

   /**
    * 
    * @param mosaicId
    * @param initialSupply
    * @param supplyMutable
    * @param transferable
    * @param divisibility
    * @param duration
    */
   public MosaicFactory(UInt64Id mosaicId, BigInteger initialSupply, boolean supplyMutable, boolean transferable,
         int divisibility, Optional<BigInteger> duration) {
      this.mosaicId = mosaicId;
      this.initialSupply = initialSupply;
      this.divisibilityMultiplier = BigDecimal.valueOf(Math.pow(10, divisibility));
      this.properties = new MosaicProperties(supplyMutable, transferable, divisibility, duration);
   }

   /**
    * @return the mosaic ID
    */
   public UInt64Id getMosaicId() {
      return mosaicId;
   }

   /**
    * @return the initialSupply
    */
   public BigInteger getInitialSupply() {
      return initialSupply;
   }

   /**
    * @return the mosaic properties
    */
   public MosaicProperties getProperties() {
      return properties;
   }

   /**
    * Create mosaic based on specified amount relative to the divisibility of the mosaic. That means that 1.5 token with
    * divisibility 6 needs to be specified here as 1.5
    *
    * @param amount amount to send
    * @return a mosaic instance
    */
   public Mosaic create(BigDecimal amount) {
      // create instance of the mosaic
      return new Mosaic(mosaicId, getAbsoluteAmount(amount));
   }

   /**
    * Create mosaic with using the base unit as an amount. That means that 1.5 token with divisibility 6 needs to be
    * specified as amount 1_500_000
    *
    * @param amount amount to send
    * @return a XPX instance
    */
   public Mosaic createAbsolute(BigInteger amount) {
      return new Mosaic(mosaicId, amount);
   }

   /**
    * calculate absolute amount based on specified amount relative to the divisibility of the mosaic. That means that
    * 1.5 token with divisibility 6 needs to be specified here as 1.5 and result will be 1_500_000
    *
    * @param amount amount to send
    * @return absolute amount
    */
   public BigInteger getAbsoluteAmount(BigDecimal amount) {
      // multiply specified amount by the 10^divisibility to get the absolute amount
      return divisibilityMultiplier.multiply(amount).toBigInteger();
   }

   @Override
   public int hashCode() {
      return Objects.hash(divisibilityMultiplier, initialSupply, mosaicId, properties);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      MosaicFactory other = (MosaicFactory) obj;
      return Objects.equals(divisibilityMultiplier, other.divisibilityMultiplier)
            && Objects.equals(initialSupply, other.initialSupply) && Objects.equals(mosaicId, other.mosaicId)
            && Objects.equals(properties, other.properties);
   }
}
