/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.OfferInfoDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

public class OfferInfo {
    private final List<BigInteger> mosaicId;
    private final List<BigInteger> amount;
    private final BigDecimal price;
    private final List<BigInteger> initialAmount;
    private final List<BigInteger> initialCost;
    private final List<BigInteger> deadline;

    public OfferInfo(
            List<BigInteger> mosaicId, List<BigInteger> amount, BigDecimal price, List<BigInteger> initialAmount,
            List<BigInteger> initialCost, List<BigInteger> deadline) {
        this.mosaicId = mosaicId;
        this.amount = amount;
        this.price = price;
        this.initialAmount = initialAmount;
        this.initialCost = initialCost;
        this.deadline = deadline;
    }

    /**
     * @return mosaicIds
     */
    public List<BigInteger> getMosaicId() {
        return mosaicId;
    }

    /**
     * @return amounts
     */
    public List<BigInteger> getAmount() {
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
    public List<BigInteger> getInitialAmount() {
        return initialAmount;
    }

    /**
     * @return initialCost
     */
    public List<BigInteger> getInitialCost() {
        return initialCost;
    }

    /**
     * @return deadline
     */
    public List<BigInteger> getDeadline() {
        return deadline;
    }

    /**
     * 
     * @param dto {@link OfferInfoDTO}
     * @return {@link OfferInfo}
     */
    public static OfferInfo fromDTO(OfferInfoDTO dto) {

        return new OfferInfo(
                dto.getMosaicId().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getAmount().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getPrice(),
                dto.getInitialAmount().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getInitialCost().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getDeadline().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()));
    }
}
