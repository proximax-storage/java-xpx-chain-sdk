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
package io.proximax.sdk.utils.dto;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.model.AccountDTO;

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
	 */
    public static String getAddressEncoded(String address) {
       return Base32Encoder.getString(HexEncoder.getBytes(address));
    }
    
    /**
     * get address using the account DTO
     * 
     * @param account DTO of account
     * @return encoded form of address
     */
    public static String getAddressEncoded(AccountDTO account) {
        return getAddressEncoded(account.getAddress());
    }
    
}
