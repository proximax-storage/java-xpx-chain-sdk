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

package io.proximax.core.utils;

import org.spongycastle.util.encoders.DecoderException;
import org.spongycastle.util.encoders.Hex;

import io.proximax.core.crypto.CryptoException;

/**
 * Static class that contains utility functions for converting hex strings to and from bytes.
 */
public class HexEncoder {

   /**
    * hidden utility constructor
    */
   private HexEncoder() {
      // nothing to do
   }

   /**
    * Converts a hex string to a byte array.
    *
    * @param hexString The input hex string.
    * @return The output byte array.
    */
   public static byte[] getBytes(final String hexString) {
      String padded;
      // prefix 0 if string has invalid length
      if (hexString.length() % 2 == 1) {
         padded = "0" + hexString;
      } else {
         padded = hexString;
      }
      // simply decode the string
      try {
         return Hex.decode(padded);
      } catch (DecoderException e) {
         throw new CryptoException("Failed to decode hex string", e);
      }
   }

   /**
    * <p>Converts a byte array to a hex string of at minimum specified length prefixed by 0 characters as needed</p>
    *
    * <p>This implementation is specifically intended to make sure that if key is 32 bytes then output is 64
    * hexadecimal characters even if first bytes were 0 and input was shorter</p>
    * 
    * @param bytes The input byte array
    * @param targetByteCount prefix 0 to make the source desired size
    * @return The output hex string.
    */
   public static String getString(final byte[] bytes, int targetByteCount) {
      String hexString = getString(bytes);
      return StringUtils.repeat("00", targetByteCount - bytes.length) + hexString;
   }
   
   /**
    * Converts a byte array to a hex string.
    *
    * @param bytes The input byte array.
    * @return The output hex string.
    */
   public static String getString(final byte[] bytes) {
      try {
         return Hex.toHexString(bytes);
      } catch (DecoderException e) {
         throw new CryptoException("Failed to generate hex string", e);
      }
   }
}
