/*
 * Copyright 2019 NEM
 * Copyright 2019 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.model.mosaic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import io.proximax.sdk.gen.model.MosaicPropertyDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * The mosaic properties structure describes mosaic properties.
 */
public class MosaicProperties {
   /** flag indicating that mosaic supply is mutable */
   public static final int FLAG_SUPPLY_MUTABLE = 1;
   /** flag indicating that mosaic ownership is transferable */
   public static final int FLAG_TRANSFERABLE = 2;

   /**
    * The creator can choose between a definition that allows a mosaic supply change at a later point or an immutable
    * supply. Allowed values for the property are "true" and "false". The default value is "false".
    */
   private final boolean supplyMutable;
   /**
    * The creator can choose if the mosaic definition should allow for transfers of the mosaic among accounts other than
    * the creator. If the property 'transferable' is set to "false", only transfer transactions having the creator as
    * sender or as recipient can transfer mosaics of that type. If set to "true" the mosaics can be transferred to and
    * from arbitrary accounts. Allowed values for the property are thus "true" and "false". The default value is "true".
    */
   private final boolean transferable;
   /**
    * The divisibility determines up to what decimal place the mosaic can be divided into. Thus a divisibility of 3
    * means that a mosaic can be divided into smallest parts of 0.001 mosaics i.e. milli mosaics is the smallest
    * sub-unit. When transferring mosaics via a transfer transaction the quantity transferred is given in multiples of
    * those smallest parts. The divisibility must be in the range of 0 and 6. The default value is "0".
    */
   private final int divisibility;
   /**
    * The duration in blocks a mosaic will be available. After the duration finishes mosaic is inactive and can be
    * renewed. Duration is optional when defining the mosaic
    */
   private final Optional<BigInteger> duration;

   /**
    * create new instance of mosaic properties
    *
    * @param supplyMutable supply mutability
    * @param transferable ownership transferability
    * @param divisibility number of decimal places
    * @param duration optional mosaic lifetime in number of blocks
    */
   public MosaicProperties(boolean supplyMutable, boolean transferable, int divisibility,
         Optional<BigInteger> duration) {
      Validate.notNull(duration, "Duration cannot be null");
      Validate.inclusiveBetween(0, 6, divisibility, "divisibility needs to be in range 0 to 6");
      this.supplyMutable = supplyMutable;
      this.transferable = transferable;
      this.divisibility = divisibility;
      // if duration is 0 then consider it undefined
      if (duration.isPresent() && duration.get().equals(BigInteger.ZERO)) {
         this.duration = Optional.empty();
      } else {
         this.duration = duration;
      }
   }

   /**
    * create new instance
    *
    * @param flags flags field
    * @param divisibility divisibility value
    * @param duration optional duration
    * @return instance of mosaic properties
    */
   public static MosaicProperties create(int flags, int divisibility, Optional<BigInteger> duration) {
      return new MosaicProperties(checkFlag(flags, FLAG_SUPPLY_MUTABLE), checkFlag(flags, FLAG_TRANSFERABLE),
            divisibility, duration);
   }

   /**
    * Returns true if supply is mutable
    *
    * @return if supply is mutable
    */
   public boolean isSupplyMutable() {
      return supplyMutable;
   }

   /**
    * Returns true if mosaic is transferable between non-owner accounts
    *
    * @return if the mosaic is transferable between non-owner accounts
    */
   public boolean isTransferable() {
      return transferable;
   }

   /**
    * Returns the number of blocks from height it will be active
    *
    * @return the number of blocks from height it will be active
    */
   public Optional<BigInteger> getDuration() {
      return duration;
   }

   /**
    * Returns the mosaic divisibility.
    *
    * @return mosaic divisibility
    */
   public int getDivisibility() {
      return divisibility;
   }

   /**
    * builder for mosaic properties
    */
   public static class Builder {
      private boolean supplyMutable = false;
      private boolean transferable = true;
      private int divisibility = 0;
      private BigInteger duration;

      public Builder supplyMutable(boolean supplyMutable) {
         this.supplyMutable = supplyMutable;
         return this;
      }

      public Builder transferable(boolean transferable) {
         this.transferable = transferable;
         return this;
      }

      public Builder divisibility(int divisibility) {
         this.divisibility = divisibility;
         return this;
      }

      public Builder duration(BigInteger duration) {
         this.duration = duration;
         return this;
      }

      public MosaicProperties build() {
         return new MosaicProperties(this.supplyMutable, this.transferable, this.divisibility,
               Optional.ofNullable(this.duration));
      }
   }

   @Override
   public String toString() {
      return "MosaicProperties [supplyMutable=" + supplyMutable + ", transferable=" + transferable + ", divisibility="
            + divisibility + ", duration=" + duration + "]";
   }

   /**
    * Convert array of numbers into named properties
    *
    * @param mosaicPropertiesDTO array of numeric values
    * @return mosaic properties instance
    */
   public static MosaicProperties fromDto(List<MosaicPropertyDTO> mosaicPropertiesDTO) {
      // load properties
      BigInteger flags = getPropertyValue(mosaicPropertiesDTO, MosaicPropertyId.FLAGS).orElseThrow(() -> new IllegalStateException("flags are mandatory"));
      BigInteger divisibility = getPropertyValue(mosaicPropertiesDTO, MosaicPropertyId.DIVISIBILITY).orElseThrow(() -> new IllegalStateException("divisibility is mandatory"));
      Optional<BigInteger> duration = getPropertyValue(mosaicPropertiesDTO, MosaicPropertyId.DURATION);
      // create new instance
      return MosaicProperties.create(flags.intValue(), divisibility.intValue(), duration);
   }

   /**
    * check the flag presence
    *
    * @param flags all flags
    * @param flag flag we are searching for
    * @return true if flag is present, false otherwise
    */
   private static boolean checkFlag(int flags, int flag) {
      return (flags & flag) == flag;
   }

   /**
    * get property value from the DTO
    *
    * @param mosaicProperties all the properties
    * @param id id of desired property
    * @return optional value of the property
    */
   private static Optional<BigInteger> getPropertyValue(List<MosaicPropertyDTO> mosaicProperties, MosaicPropertyId id) {
      return mosaicProperties.stream()
         .filter(prop -> prop.getId().getValue().intValue() == id.getCode())
         .map(MosaicPropertyDTO::getValue)
         .map(v -> UInt64Utils.toBigInt(new ArrayList<>(v)))
         .findFirst();
   }

   @Override
   public int hashCode() {
      return Objects.hash(divisibility, duration, supplyMutable, transferable);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      MosaicProperties other = (MosaicProperties) obj;
      return divisibility == other.divisibility && Objects.equals(duration, other.duration)
            && supplyMutable == other.supplyMutable && transferable == other.transferable;
   }
}