package io.proximax.sdk.model.exchange;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.ExchangesDTO;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.utils.dto.UInt64Utils;

public class MosaicExchange {
    private final List<MosaicId> mosaicId;
    private final List<BigInteger> amount;
    private final List<BigInteger> initialAmount;
    private final List<BigInteger> initialCost;
    private final List<BigInteger> deadline;
    private final BigDecimal price;
    private final PublicAccount owner;
    private final ExchangeOfferType type;

    /**
     * @param mosaicId      ID of the mosaic that is being either bought or sold for
     *                      network currency
     * @param amount
     * @param initialAmount
     * @param initialCost
     * @param deadline
     * @param price
     * @param owner
     * @param type
     */
    public MosaicExchange(List<MosaicId> mosaicId, List<BigInteger> amount, List<BigInteger> initialAmount, List<BigInteger> initialCost,
                          List<BigInteger> deadline, BigDecimal price, PublicAccount owner, ExchangeOfferType type) {
        this.mosaicId = mosaicId;
        this.amount = amount;
        this.initialAmount = initialAmount;
        this.initialCost = initialCost;
        this.deadline = deadline;
        this.price = price;
        this.owner = owner;
        this.type = type;
    }

    /**
     * @return the mosaicIds
     */
    public List<MosaicId> getMosaicId() {
        return mosaicId;
    }

    /**
     * @return the amount
     */
    public List<BigInteger> getAmount() {
        return amount;
    }

    /**
     * @return the initialAmount
     */
    public List<BigInteger> getInitialAmount() {
        return initialAmount;
    }

    /**
     * @return the initialCost
     */
    public List<BigInteger> getInitialCost() {
        return initialCost;
    }

    /**
     * @return the deadline
     */
    public List<BigInteger> getDeadline() {
        return deadline;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return the owner
     */
    public PublicAccount getOwner() {
        return owner;
    }

    /**
     * @return the type
     */
    public ExchangeOfferType getType() {
        return type;
    }

    /**
     * @param dto {@link ExchangesDTO}
     * @param networkType {@link NetworkType}
     * 
     * @return {@link MosaicExchange}
     */
    public static MosaicExchange fromDto(ExchangesDTO dto, NetworkType networkType) {

        return new MosaicExchange(
                dto.getMosaicId().stream().map(v -> new MosaicId(toBigInt(new ArrayList<>(v)))).collect(Collectors.toList()),
                dto.getAmount().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getInitialAmount().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getInitialCost().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getDeadline().stream().map(v -> UInt64Utils.toBigInt(new ArrayList<>(v))).collect(Collectors.toList()),
                dto.getPrice(),
                PublicAccount.createFromPublicKey(dto.getOwner(), networkType),
                ExchangeOfferType.getByCode(dto.getType()));
    }
}
