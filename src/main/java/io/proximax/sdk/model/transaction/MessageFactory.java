package io.proximax.sdk.model.transaction;

import java.nio.charset.StandardCharsets;

/**
 * The factory to create Message instance.
 */
public class MessageFactory {

   /* hiding constructor for utility class */
   private MessageFactory() { /* nothing o do here */ }
    /**
     * Create Message instance based on type and encodedPayload
     * @param type the message type
     * @param encodedPayload the encodedPayload in byte array
     * @return the instance of Message
     */
    public static Message createMessage(int type, byte[] encodedPayload) {
        if (type == MessageType.PLAIN.getCode()) {
            return PlainMessage.create(new String(encodedPayload, StandardCharsets.UTF_8));
        } else if (type == MessageType.SECURE.getCode()){
            return SecureMessage.createFromEncodedPayload(encodedPayload);
        }
        return PlainMessage.EMPTY;
    }
}
