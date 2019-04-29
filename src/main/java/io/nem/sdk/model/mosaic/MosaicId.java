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

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

import io.nem.sdk.model.transaction.IdGenerator;

/**
 * The mosaic id structure describes mosaic id
 *
 * @since 1.0
 */
public class MosaicId {
    private final BigInteger id;
    private final Optional<Integer> nonce;
    private final Optional<String> ownerPublicKeyHex;

    /**
     * Create mosaic from the random nonce and public key of the owner
     * 
     * @param nonce random integer
     * @param ownerPublicKeyHex hexadecimal representation of owner's public key
     */
    public MosaicId(Integer nonce, String ownerPublicKeyHex) {
        this.id = IdGenerator.generateMosaicId(nonce, ownerPublicKeyHex);
        this.nonce = Optional.of(nonce);
        this.ownerPublicKeyHex = Optional.of(ownerPublicKeyHex);
    }

    /**
     * Create MosaicId from BigInteger id
     *
     * @param id id of the mosaic
     */
    public MosaicId(BigInteger id) {
        this.id = id;
        this.nonce = Optional.empty();
        this.ownerPublicKeyHex = Optional.empty();
    }

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @return the nonce
	 */
	public Optional<Integer> getNonce() {
		return nonce;
	}

	/**
	 * @return the ownerPublicKeyHex
	 */
	public Optional<String> getOwnerPublicKeyHex() {
		return ownerPublicKeyHex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MosaicId other = (MosaicId) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "MosaicId [id=" + id + ", nonce=" + nonce + ", ownerPublicKeyHex=" + ownerPublicKeyHex + "]";
	}

}
