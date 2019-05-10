/*
 * Copyright 2019 NEM
 * Copyright 2019 ProximaX
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
package io.proximax.sdk.model.mosaic;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.Validate;

import io.proximax.core.utils.ByteUtils;

public class MosaicNonce {
   private static final int NONCE_BYTES = 4;
   
    /**
     * Mosaic nonce
     */
    private final byte[] nonce;

    /**
     *
     * @return nonce
     */
    public byte[] getNonce() {
        return nonce;
    }

    /**
     *
     * @return nonce long
     */
    public int getNonceAsInt() {
        return ByteUtils.bytesToInt(this.nonce);
    }

    /**
     * Create MosaicNonce from byte array
     *
     * @param nonce
     */
    public MosaicNonce(byte[] nonce) {
       Validate.notNull(nonce, "Nonce must not be null");
       Validate.isTrue(nonce.length == NONCE_BYTES, "Nonce needs to have size " + NONCE_BYTES);
        this.nonce = nonce;
    }

    /**
     * Create cryptographically safe random MosaicNonce. This might be time and resource intensive 
     * while not really worth it because nonce is not secret
     *
     * @return MosaicNonce nonce
     */
    public static MosaicNonce createSecureRandom() {
        byte[] bytes = new byte[NONCE_BYTES]; 
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalIdentifierException("NoSuchAlgorithmException:" + e);
        }
        return new MosaicNonce(bytes);
    }

    /**
     * Create a random MosaicNonce
     *
     * @return MosaicNonce nonce
     */
    public static MosaicNonce createRandom() {
        byte[] bytes = new byte[NONCE_BYTES];
        new Random().nextBytes(bytes);
        return new MosaicNonce(bytes);
    }

    /**
     * Create a MosaicNonce from hexadecimal notation.
     *
     * @param   hex
     * @throws IllegalIdentifierException
     * @return MosaicNonce
     */
    public static MosaicNonce createFromHex(String hex) {
        byte[] bytes;
        try {
            bytes = Hex.decodeHex(hex);
            if (bytes.length != 4) {
                throw new IllegalIdentifierException("Expected 4 bytes for Nonce but got " + bytes.length + " instead.");
            }
        } catch (DecoderException e) {
            throw new IllegalIdentifierException("DecoderException:" + e);
        }
        return new MosaicNonce(bytes);
    }

    /**
     * Create a MosaicNonce from a BigInteger.
     *
     * @param number
     * @return MosaicNonce
     */
    public static MosaicNonce createFromBigInteger(BigInteger number) {
        return new MosaicNonce(ByteUtils.bigIntToBytesOfSize(number, 4));
    }

   @Override
   public String toString() {
      return "MosaicNonce [nonce=" + getNonceAsInt() + "]";
   }
    
    
}