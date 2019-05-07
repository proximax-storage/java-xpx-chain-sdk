package io.proximax.sdk.model.transaction;

/**
 * Enum containing message type constants.
 *
 * @since 1.0
 */
public enum MessageType {

	/**
	 * Message type unknown to the SDK
	 */
	UNKNOWN(Integer.MIN_VALUE),
	
    /**
     * Plain message type
     */
    PLAIN(0),

    /**
     * Secure message type. 
     * Encrypted by the private key of sender and public key of recipient => decrypted by public key of sender and private key of recipient
     */
    SECURE(1);

	/**
	 * code of the message type
	 */
    private int code;

    /**
     * enum element constructor
     * 
     * @param type code of the message type
     */
    MessageType(int code) {
        this.code = code;
    }

    /**
     * Returns int value of message type
     *
     * @return the int value of message type
     */
    public int getCode() {
        return code;
    }
    
    /**
     * retrieve message type by the integer code
     * 
     * @param code the code of the message type
     * @return enum entry representing the message type or UNKNOWN
     */
    public static MessageType getByCode(int code) {
    	switch (code) {
    		case 0: return PLAIN;
    		case 1: return SECURE;
    		default: return UNKNOWN;
    	}
    }
}
