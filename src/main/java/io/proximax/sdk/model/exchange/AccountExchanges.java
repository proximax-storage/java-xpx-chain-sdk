/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.AccountExchangeDTO;
import io.proximax.sdk.gen.model.ExchangeDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;

public class AccountExchanges {
    private final PublicAccount owner;
    private final Address ownerAddress;
    private final Integer version;

    private final List<OfferInfo> buyOffers;
    private final List<OfferInfo> sellOffers;

    public AccountExchanges(PublicAccount owner, Address ownerAddress,  Integer version,List<OfferInfo> buyOffers, List<OfferInfo> sellOffers) {
        this.owner = owner;
        this.ownerAddress = ownerAddress;
        this.version = version;
        this.buyOffers = buyOffers;
        this.sellOffers = sellOffers;
    }

    /**
     * @return owner
     */
    public PublicAccount getOwner() {
        return owner;
    }

    /**
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @return ownerAddress
     */
    public Address getOwnerAddress() {
        return ownerAddress;
    }

    /**
     * @return buyOffers
     */
    public List<OfferInfo> getBuyOffers() {
        return buyOffers;
    }

    /**
     * @return sellOffers
     */
    public List<OfferInfo> getSellOffers() {
        return sellOffers;
    }

    /**
     * 
     * @param {@link AccountExchangeDTO}
     * @param {@link NetworkType}
     * 
     * @return {@link AccountExchanges}
     */
    public static AccountExchanges fromDto(ExchangeDTO dto, NetworkType networkType) {

        return new AccountExchanges(
                PublicAccount.createFromPublicKey(dto.getExchangeInfo().getOwner(), networkType),
                Address.createFromEncoded(dto.getExchangeInfo().getOwnerAddress()),
                dto.getExchangeInfo().getVersion(),
                dto.getExchangeInfo()
                        .getBuyOffers().stream()
                .map(buyoffer -> new OfferInfo(new MosaicId(toBigInt(buyoffer.getMosaicId())),
                                toBigInt(buyoffer.getAmount()),
                                buyoffer.getPrice(), toBigInt(buyoffer.getInitialAmount()), toBigInt(buyoffer
                                        .getInitialCost()),
                                toBigInt(buyoffer.getDeadline()),toBigInt(buyoffer.getResidualCost())))
                                        .collect(Collectors.toList()),
                dto.getExchangeInfo().getSellOffers().stream().map(sellOffer -> new OfferInfo(new MosaicId(toBigInt(sellOffer
                         .getMosaicId())), toBigInt(sellOffer.getAmount()),
                        sellOffer.getPrice(), toBigInt(sellOffer.getInitialAmount()), toBigInt(sellOffer
                                 .getInitialCost()),
                         toBigInt(sellOffer.getDeadline()), toBigInt(sellOffer
                                .getResidualCost())))
                                 .collect(Collectors.toList()));
    }
}
