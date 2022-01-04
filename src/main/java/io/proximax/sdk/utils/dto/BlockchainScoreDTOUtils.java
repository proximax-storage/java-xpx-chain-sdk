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

import java.math.BigInteger;

import io.proximax.sdk.gen.model.BlockchainScoreDTO;

/**
 * Utility class to manage BlockchainScoreDTO instances
 * 
 * @author tonowie
 */
public class BlockchainScoreDTOUtils {

	/**
	 * utility class should not be instantiated
	 */
	private BlockchainScoreDTOUtils() {
		// hiding utility constructor
	}
	
    /**
     * convert BlockchainScoreDTO to BigInteger instance
     * 
     * @param score BlockchainScoreDTO instance representing low and high score
     * @return BigInteger created from the low and high scores
     */
    public static BigInteger toBigInt(BlockchainScoreDTO score) {
    	int low = UInt64Utils.toBigInt(score.getScoreLow()).intValue();
    	int high = UInt64Utils.toBigInt(score.getScoreHigh()).intValue();
    	return UInt64Utils.fromIntArray(new int[]{low, high});
    }

}
