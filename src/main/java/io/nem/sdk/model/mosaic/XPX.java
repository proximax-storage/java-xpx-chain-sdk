/*
 * Copyright 2018 NEM
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

package io.nem.sdk.model.mosaic;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * XPX Mosaic
 */
public class XPX extends Mosaic {
    /**
     * Divisibility
     */
    public static final int DIVISIBILITY = 6;
    /**
     * Initial supply
     */
    public static final BigInteger INITIALSUPPLY = BigInteger.valueOf(9000000000000000l);
    /**
     * Is transferable
     */
    public static final boolean TRANSFERABLE = true;
    /**
     * Is supply mutable
     */
    public static final boolean SUPPLYMUTABLE = false;
    /**
     * Mosaic id
     */
    public static final MosaicId MOSAICID = new MosaicId(BigInteger.valueOf(992621222383397347l));

    /**
     * create specified amount of micro XPX
     * 
     * @param amount amount of micro XPX
     */
    public XPX(BigInteger amount) {
        super(XPX.MOSAICID, amount);
    }

    /**
     * Create XPX with using XPX as unit.
     *
     * @param amount amount to send
     * @return a XPX instance
     */
    public static XPX createRelative(BigInteger amount) {
        BigInteger relativeAmount = BigDecimal.valueOf(Math.pow(10, XPX.DIVISIBILITY)).toBigInteger().multiply(amount);
        return new XPX(relativeAmount);
    }

    /**
     * Create XPX with using micro XPX as unit, 1 XPX = 1000000 micro XPX.
     *
     * @param amount amount to send
     * @return a XPX instance
     */
    public static XPX createAbsolute(BigInteger amount) {
        return new XPX(amount);
    }
}
