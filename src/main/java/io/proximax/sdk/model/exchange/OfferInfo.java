/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.proximax.sdk.gen.model.OfferInfoDTO;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.transaction.UInt64Id;

public class OfferInfo {
    private final UInt64Id mosaicId;
    private final BigInteger amount;
    private final BigDecimal price;
    private final BigInteger initialAmount;
    private final BigInteger initialCost;
    private final BigInteger deadline;
    private final BigInteger residualCost;

    public OfferInfo(
            UInt64Id mosaicId, BigInteger amount, BigDecimal price, BigInteger initialAmount,
            BigInteger initialCost, BigInteger deadline, BigInteger residualCost) {
        this.mosaicId = mosaicId;
        this.amount = amount;
        this.price = price;
        this.initialAmount = initialAmount;
        this.initialCost = initialCost;
        this.deadline = deadline;
        this.residualCost = residualCost;
    }

    /**
     * @return mosaicId
     */
    public UInt64Id getMosaicId() {
        return mosaicId;
    }

    /**
     * @return amount
     */
    public BigInteger getAmount() {
        return amount;
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return initialAmount
     */
    public BigInteger getInitialAmount() {
        return initialAmount;
    }

    /**
     * @return initialCost
     */
    public BigInteger getInitialCost() {
        return initialCost;
    }

    /**
     * @return deadline
     */
    public BigInteger getDeadline() {
        return deadline;
    }

    /**
     * @return residualCost
     */
    public BigInteger getResidualCost() {
        return residualCost;
    }

    /**
     * 
     * @param dto {@link OfferInfoDTO}
     * @return {@link OfferInfo}
     */
    public static OfferInfo fromDTO(OfferInfoDTO dto) {

        return new OfferInfo(
                new MosaicId(toBigInt(dto.getMosaicId())), toBigInt(dto.getAmount()), dto.getPrice(),
                toBigInt(dto.getInitialAmount()), toBigInt(dto.getInitialCost()), toBigInt(dto.getDeadline()), toBigInt(dto.getResidualCost()));
    }

}
