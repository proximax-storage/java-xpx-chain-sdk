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

import java.math.BigInteger;
import java.util.List;

import io.proximax.sdk.model.mosaic.Mosaic;

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

    public AccountInfo(Address address, BigInteger addressHeight, String publicKey, BigInteger publicKeyHeight,List<Mosaic> mosaics) {
        this.address = address;
        this.addressHeight = addressHeight;
        this.publicKey = publicKey;
        this.publicKeyHeight = publicKeyHeight;
        this.mosaics = mosaics;
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
     * @return {List<Mosaic>}
     */
    public List<Mosaic> getMosaics() {
        return mosaics;
    }

    /**
     * Returns height when the address was published.
     *
     * @return {@link PublicAccount}
     */
    public PublicAccount getPublicAccount() {
        return PublicAccount.createFromPublicKey(this.publicKey, this.address.getNetworkType());
    }
}
