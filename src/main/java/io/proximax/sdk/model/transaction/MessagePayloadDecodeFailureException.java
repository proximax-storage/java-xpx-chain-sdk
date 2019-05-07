package io.proximax.sdk.model.transaction;

/**
 * The exception when the decoding message payload has failed
 */

public class MessagePayloadDecodeFailureException extends RuntimeException {
    /**
     * Create instance of this exception
     *
     * @param message the exception message
     */
    public MessagePayloadDecodeFailureException(String message) {
        super(message);
    }

    /**
     * Create instance of this exception
     *
     * @param message the exception message
     * @param cause   the cause of this exception
    */
    public MessagePayloadDecodeFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
