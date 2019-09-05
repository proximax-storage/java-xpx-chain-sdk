package io.proximax.sdk.model.transaction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;

public class MessageFactoryTest {

    private static final PrivateKey TEST_SENDER_PRIVATE_KEY =
            PrivateKey.fromHexString("8374B5915AEAB6308C34368B15ABF33C79FD7FEFC0DEAF9CC51BA57F120F1190");
    private static final PublicKey TEST_RECIPIENT_PUBLIC_KEY =
            PublicKey.fromHexString("8E1A94D534EA6A3B02B0B967701549C21724C7644B2E4C20BF15D01D50097ACB");

    @Test
    public void shouldCreatePlainMessage() {
        final Message message = MessageFactory.createMessage(MessageType.PLAIN.getCode(), "test-message".getBytes(StandardCharsets.UTF_8));

        assertThat(message, is(notNullValue()));
        assertThat(message, is(instanceOf(PlainMessage.class)));
        assertThat(message.getEncodedPayload(), is("test-message".getBytes(StandardCharsets.UTF_8)));
        assertThat(message.getPayload(), is("test-message"));
        assertThat(message.getTypeCode(), is(MessageType.PLAIN.getCode()));
    }

    @Test
    public void shouldCreateSecureMessage() {
        final byte[] encodedPayload = sampleEncodedPayload();
        final Message message = MessageFactory.createMessage(MessageType.SECURE.getCode(), encodedPayload);

        assertThat(message, is(notNullValue()));
        assertThat(message, is(instanceOf(SecureMessage.class)));
        assertThat(message.getEncodedPayload(), is(encodedPayload));
        assertThat(((SecureMessage) message).decrypt(new KeyPair(TEST_SENDER_PRIVATE_KEY), new KeyPair(TEST_RECIPIENT_PUBLIC_KEY)),
                is("test-message"));
        assertThat(message.getTypeCode(), is(MessageType.SECURE.getCode()));
    }

    @Test
    public void shouldCreateEmptyPlainMessageForUnknownType() {
        final byte[] encodedPayload = sampleEncodedPayload();
        final Message message = MessageFactory.createMessage(999, encodedPayload);

        assertThat(message, is(PlainMessage.EMPTY));
    }

    private byte[] sampleEncodedPayload() {
        return SecureMessage.create(TEST_SENDER_PRIVATE_KEY, TEST_RECIPIENT_PUBLIC_KEY,
                "test-message").getEncodedPayload();
    }
}
