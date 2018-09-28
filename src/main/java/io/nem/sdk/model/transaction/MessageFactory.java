package io.nem.sdk.model.transaction;

/**
 * The factory to create Message instance.
 *
 * @since 1.0
 */
public class MessageFactory {

    /**
     * Create Message instance based on type and payload
     * @param type the message type
     * @param payload the payload in byte array
     * @return the instance of Message
     */
    public static Message createMessage(int type, byte[] payload) {
        if (type == MessageTypes.PLAIN.getType()) {
            return PlainMessage.create(payload);
        } else if (type == MessageTypes.SECURE.getType()){
            return SecureMessage.createFromEncodedPayload(payload);
        }
        return PlainMessage.Empty;
    }
}
