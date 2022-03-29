/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.exchange;

import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.AccountExchangeDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

public class AccountExchanges {
    private final PublicAccount owner;
    private final Address ownerAddress;

    private final List<OfferInfo> buyOffers;
    private final List<OfferInfo> sellOffers;

    public AccountExchanges(PublicAccount owner, Address ownerAddress, List<OfferInfo> buyOffers, List<OfferInfo> sellOffers) {
        this.owner = owner;
        this.ownerAddress = ownerAddress;
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
     * @param dto {@link AccountExchangeDTO}
     * @param networkType {@link NetworkType}
     * 
     * @return {@link AccountExchanges}
     */
    public static AccountExchanges fromDto(AccountExchangeDTO dto, NetworkType networkType) {

        return new AccountExchanges(
                PublicAccount.createFromPublicKey(dto.getOwner(), networkType),
                Address.createFromEncoded(dto.getOwner()),
                dto.getBuyOffers().stream().map(OfferInfo::fromDTO).collect(Collectors.toList()),
                dto.getSellOffers().stream().map(OfferInfo::fromDTO).collect(Collectors.toList()));
    }
}
