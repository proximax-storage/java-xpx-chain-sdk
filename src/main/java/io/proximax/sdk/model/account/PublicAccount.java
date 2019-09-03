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

import java.util.Objects;

import io.proximax.sdk.model.blockchain.NetworkType;

/**
 * The public account structure contains account's address and public key.
 *
 * @since 1.0
 */
public class PublicAccount {
    private final Address address;
    private final String publicKey;

    public PublicAccount(String publicKey, NetworkType networkType) {
        this.address = Address.createFromPublicKey(publicKey, networkType);
        this.publicKey = publicKey;
    }

    /**
     * Create a PublicAccount from a public key and network type.
     *
     * @param publicKey   Public key
     * @param networkType NetworkType
     * @return {@link PublicAccount}
     */
    public static PublicAccount createFromPublicKey(String publicKey, NetworkType networkType) {
        return new PublicAccount(publicKey, networkType);
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
     * Return account public key.
     *
     * @return String
     */
    public String getPublicKey() {
        return publicKey;
    }

	@Override
	public String toString() {
		return "PublicAccount [address=" + address + ", publicKey=" + publicKey + "]";
	}

   @Override
   public int hashCode() {
      return Objects.hash(address, publicKey);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      PublicAccount other = (PublicAccount) obj;
      return Objects.equals(address, other.address) && Objects.equals(publicKey, other.publicKey);
   }

}
