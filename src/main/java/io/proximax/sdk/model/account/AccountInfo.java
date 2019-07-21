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

package io.proximax.sdk.model.account;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.AccountDTO;
import io.proximax.sdk.gen.model.AccountInfoDTO;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.utils.dto.AccountDTOUtils;

/**
 * The account info structure describes basic information for an account.
 *
 * @since 1.0
 */
public class AccountInfo {
    private final Address address;
    private final BigInteger addressHeight;
    private final String publicKey;
    private final BigInteger publicKeyHeight;
    private final List<Mosaic> mosaics;
    private final String linkedAccountKey;

    public AccountInfo(Address address, BigInteger addressHeight, String publicKey, BigInteger publicKeyHeight,List<Mosaic> mosaics, String linkedAccountKey) {
        this.address = address;
        this.addressHeight = addressHeight;
        this.publicKey = publicKey;
        this.publicKeyHeight = publicKeyHeight;
        this.mosaics = mosaics;
        this.linkedAccountKey = linkedAccountKey;
    }

    /**
     * Returns account address.
     *
     * @return {@link Address}
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns height when the address was published.
     *
     * @return BigInteger
     */
    public BigInteger getAddressHeight() {
        return addressHeight;
    }

    /**
     * Returns public key of the account.
     *
     * @return String
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Returns height when the public key was published.
     *
     * @return BigInteger
     */
    public BigInteger getPublicKeyHeight() {
        return publicKeyHeight;
    }

    /**
     * Returns mosaics hold by the account.
     *
     * @return mosaics owned by the account
     */
    public List<Mosaic> getMosaics() {
        return mosaics;
    }

    /**
    * @return the linkedAccountKey
    */
   public String getLinkedAccountKey() {
      return linkedAccountKey;
   }

   /**
     * Returns height when the address was published.
     *
     * @return {@link PublicAccount}
     */
    public PublicAccount getPublicAccount() {
        return PublicAccount.createFromPublicKey(this.publicKey, this.address.getNetworkType());
    }
    
    public static AccountInfo fromDto(AccountInfoDTO dto) {
       AccountDTO accountDTO = dto.getAccount();
       return new AccountInfo(
             Address.createFromRawAddress(AccountDTOUtils.getAddressEncoded(accountDTO)),
             toBigInt(accountDTO.getAddressHeight()), accountDTO.getPublicKey(),
             toBigInt(accountDTO.getPublicKeyHeight()),
             accountDTO.getMosaics().stream()
                   .map(mosaicDTO -> new Mosaic(new MosaicId(toBigInt(mosaicDTO.getId())),
                         toBigInt(mosaicDTO.getAmount())))
                   .collect(Collectors.toList()),
             accountDTO.getLinkedAccountKey());
    }
}
