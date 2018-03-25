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

import io.nem.sdk.model.namespace.NamespaceId;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * XEM Mosaic
 *
 * @since 1.0
 */
public class XEM extends Mosaic {
    /**
     * Divisibility
     */
    public static final int DIVISIBILITY = 6;
    /**
     * Initial supply
     */
    public static final BigInteger INITIALSUPPLY = new BigInteger("8999999999");
    /**
     * Is transferable
     */
    public static final boolean TRANSFERABLE = true;
    /**
     * Is supply mutable
     */
    public static final boolean SUPPLYMUTABLE = false;
    /**
     * Namespace id
     */
    public static final NamespaceId NAMESPACEID = new NamespaceId("nem");
    /**
     * Mosaic id
     */
    public static final MosaicId MOSAICID = new MosaicId("nem:xem");

    public XEM(BigInteger amount) {
        super(XEM.MOSAICID, amount);
    }

    /**
     * Create xem with using xem as unit.
     *
     * @param amount amount to send
     * @return a XEM instance
     */
    public static XEM createRelative(BigInteger amount) {
        BigInteger relativeAmount = new BigDecimal(Math.pow(10, XEM.DIVISIBILITY)).toBigInteger().multiply(amount);
        return new XEM(relativeAmount);
    }

    /**
     * Create xem with using micro xem as unit, 1 XEM = 1000000 micro XEM.
     *
     * @param amount amount to send
     * @return a XEM instance
     */
    public static XEM createAbsolute(BigInteger amount) {
        return new XEM(amount);
    }
}
