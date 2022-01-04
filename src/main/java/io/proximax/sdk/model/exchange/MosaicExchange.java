package io.proximax.sdk.model.exchange;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.proximax.sdk.gen.model.ExchangesDTO;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.UInt64Id;

public class MosaicExchange {
    private final MosaicId mosaicId;
    private final BigInteger amount;
    private final BigInteger initialAmount;
    private final BigInteger initialCost;
    private final BigInteger deadline;
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
    public MosaicExchange(MosaicId mosaicId, BigInteger amount, BigInteger initialAmount, BigInteger initialCost,
            BigInteger deadline, BigDecimal price, PublicAccount owner, ExchangeOfferType type) {
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
     * @return the mosaicId
     */
    public UInt64Id getMosaicId() {
        return mosaicId;
    }

    /**
     * @return the amount
     */
    public BigInteger getAmount() {
        return amount;
    }

    /**
     * @return the initialAmount
     */
    public BigInteger getInitialAmount() {
        return initialAmount;
    }

    /**
     * @return the initialCost
     */
    public BigInteger getInitialCost() {
        return initialCost;
    }

    /**
     * @return the deadline
     */
    public BigInteger getDeadline() {
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
     * @param {@link ExchangesDTO}
     * @param {@link NetworkType}
     * 
     * @return {@link MosaicExchange}
     */
    public static MosaicExchange fromDto(ExchangesDTO dto, NetworkType networkType) {

        return new MosaicExchange(
                new MosaicId(toBigInt(dto.getMosaicId())), toBigInt(dto.getAmount()), toBigInt(dto.getInitialAmount()),
                toBigInt(dto.getInitialCost()), toBigInt(dto.getDeadline()), dto.getPrice(),
                PublicAccount.createFromPublicKey(dto.getOwner(), networkType),
                ExchangeOfferType.getByCode(dto.getType()));

    }

}
