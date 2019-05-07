/*
 * Copyright 2019 NEM
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

import org.apache.commons.lang3.Validate;

/**
 * The mosaic properties structure describes mosaic properties.
 *
 * @since 1.0
 */
public class MosaicProperties {
   /**
    * The creator can choose between a definition that allows a mosaic supply change at a later point or an immutable supply.
    * Allowed values for the property are "true" and "false". The default value is "false".
    */
   private final boolean supplyMutable;
   /**
    * The creator can choose if the mosaic definition should allow for transfers of the mosaic among accounts other than the creator.
    * If the property 'transferable' is set to "false", only transfer transactions
    * having the creator as sender or as recipient can transfer mosaics of that type.
    * If set to "true" the mosaics can be transferred to and from arbitrary accounts.
    * Allowed values for the property are thus "true" and "false". The default value is "true".
    */
   private final boolean transferable;
   /**
    * Levy mutable
    */
   private final boolean levyMutable;
   /**
    * The divisibility determines up to what decimal place the mosaic can be divided into.
    * Thus a divisibility of 3 means that a mosaic can be divided into smallest parts of 0.001 mosaics
    * i.e. milli mosaics is the smallest sub-unit.
    * When transferring mosaics via a transfer transaction the quantity transferred
    * is given in multiples of those smallest parts.
    * The divisibility must be in the range of 0 and 6. The default value is "0".
    */
   private final int divisibility;
   /**
    * The duration in blocks a mosaic will be available.
    * After the duration finishes mosaic is inactive and can be renewed.
    * Duration is optional when defining the mosaic
    */
   private final BigInteger duration;

    public MosaicProperties(boolean supplyMutable, boolean transferable, boolean levyMutable, int divisibility, BigInteger duration) {
        Validate.notNull(duration, "Duration cannot be null");
        this.supplyMutable = supplyMutable;
        this.transferable = transferable;
        this.levyMutable = levyMutable;
        this.divisibility = divisibility;
        this.duration = duration;
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
     * Returns true if the mosaic levy is mutable
     *
     * @return if the mosaic levy is mutable
     */
    public boolean isLevyMutable() {
        return levyMutable;
    }

    /**
     * Returns the number of blocks from height it will be active
     *
     * @return the number of blocks from height it will be active
     */
    public BigInteger getDuration() {
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

    public static class Builder {
        private boolean supplyMutable;
        private boolean transferable;
        private boolean levyMutable;
        private int divisibility;
        private BigInteger duration;

        public Builder supplyMutable(boolean supplyMutable) {
            this.supplyMutable = supplyMutable;
            return this;
        }

        public Builder transferable(boolean transferable) {
            this.transferable = transferable;
            return this;
        }

        public Builder levyMutable(boolean levyMutable) {
            this.levyMutable = levyMutable;
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
            return new MosaicProperties(this.supplyMutable, this.transferable, this.levyMutable, this.divisibility, this.duration);
        }
    }

	@Override
	public String toString() {
		return "MosaicProperties [supplyMutable=" + supplyMutable + ", transferable=" + transferable + ", levyMutable="
				+ levyMutable + ", divisibility=" + divisibility + ", duration=" + duration + "]";
	}
}