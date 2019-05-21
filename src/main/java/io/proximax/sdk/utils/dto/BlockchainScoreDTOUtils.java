/**
 * 
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
