/*
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
package io.nem.sdk.infrastructure.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import io.nem.sdk.infrastructure.model.AccountDTO;

/**
 * utility class providing service methods to handle AccountDTOs
 * 
 * @author tonowie
 */
public class AccountDTOUtils {

	/**
	 * utility class private constructor
	 */
	private AccountDTOUtils() {
		// nothing to do, just hiding constructor
	}

	/**
	 * Take decode hexadecimal representation of address and encode it using Base32
	 * 
	 * @param address hexadecimal representation of address
	 * @return Base32 encoded address
	 * @throws DecoderException if provided address is not valid hexadecimal string
	 */
    public static String getAddressEncoded(String address) throws DecoderException {
        return new String(new Base32().encode(Hex.decodeHex(address)));
    }
    
    /**
     * 
     * @param account
     * @return
     * @throws DecoderException
     */
    public static String getAddressEncoded(AccountDTO account) throws DecoderException {
        return getAddressEncoded(account.getAddress());
    }
    
}
