package io.nem.sdk.model.transaction;

import io.nem.core.crypto.KeyPair;
import io.nem.core.crypto.PrivateKey;
import io.nem.core.crypto.PublicKey;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MessageFactoryTest {

    private static final PrivateKey TEST_SENDER_PRIVATE_KEY =
            PrivateKey.fromHexString("8374B5915AEAB6308C34368B15ABF33C79FD7FEFC0DEAF9CC51BA57F120F1190");
    private static final PublicKey TEST_RECIPIENT_PUBLIC_KEY =
            PublicKey.fromHexString("8E1A94D534EA6A3B02B0B967701549C21724C7644B2E4C20BF15D01D50097ACB");

    @Test
    public void shouldCreatePlainMessage() {
        final Message message = MessageFactory.createMessage(MessageTypes.PLAIN.getType(), "test-message".getBytes());

        assertThat(message, is(notNullValue()));
        assertThat(message, is(instanceOf(PlainMessage.class)));
        assertThat(message.getEncodedPayload(), is("test-message".getBytes()));
        assertThat(message.getDecodedPayload(new KeyPair(TEST_SENDER_PRIVATE_KEY), new KeyPair(TEST_RECIPIENT_PUBLIC_KEY)),
                is("test-message".getBytes()));
        assertThat(message.getType(), is(MessageTypes.PLAIN.getType()));
    }

    @Test
    public void shouldCreateSecureMessage() {
        final byte[] encodedPayload = sampleEncodedPayload();
        final Message message = MessageFactory.createMessage(MessageTypes.SECURE.getType(), encodedPayload);

        assertThat(message, is(notNullValue()));
        assertThat(message, is(instanceOf(SecureMessage.class)));
        assertThat(message.getEncodedPayload(), is(encodedPayload));
        assertThat(message.getDecodedPayload(new KeyPair(TEST_SENDER_PRIVATE_KEY), new KeyPair(TEST_RECIPIENT_PUBLIC_KEY)),
                is("test-message".getBytes()));
        assertThat(message.getType(), is(MessageTypes.SECURE.getType()));
    }

    @Test
    public void shouldCreateEmptyPlainMessageForUnknownType() {
        final byte[] encodedPayload = sampleEncodedPayload();
        final Message message = MessageFactory.createMessage(999, encodedPayload);

        assertThat(message, is(PlainMessage.Empty));
    }

    private byte[] sampleEncodedPayload() {
        return SecureMessage.createFromDecodedPayload(TEST_SENDER_PRIVATE_KEY, TEST_RECIPIENT_PUBLIC_KEY,
                "test-message".getBytes()).getEncodedPayload();
    }
}
