package io.nem.sdk.model.transaction;

/**
 * Enum containing message type constants.
 *
 * @since 1.0
 */
public enum MessageTypes {

    /**
     * Plain message type
     */
    PLAIN(0),

    /**
     * Secure message type
     */
    SECURE(1);

    private int type;

    MessageTypes(int type) {
        this.type = type;
    }

    /**
     * Returns int value of message type
     *
     * @return the int value of message type
     */
    public int getType() {
        return type;
    }
}
