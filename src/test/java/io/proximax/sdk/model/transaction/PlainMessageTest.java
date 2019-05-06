/*
 * Copyright 2018 NEM
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

package io.proximax.sdk.model.transaction;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.transaction.MessageType;
import io.proximax.sdk.model.transaction.PlainMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlainMessageTest {

    @Test
    void shouldCreatePlainMessageViaConstructor() {
        PlainMessage plainMessage = new PlainMessage("test-message");

        assertEquals("test-message", new String(plainMessage.getEncodedPayload()));
        assertEquals("test-message", plainMessage.getPayload());
        assertTrue(MessageType.PLAIN.getCode() == plainMessage.getTypeCode());
    }


    @Test
    void shouldCreatePlainMessageViaStaticConstructor() {
        PlainMessage plainMessage = PlainMessage.create("test-message");

        assertEquals("test-message", new String(plainMessage.getEncodedPayload()));
        assertEquals("test-message", plainMessage.getPayload());
        assertTrue(MessageType.PLAIN.getCode() == plainMessage.getTypeCode());
    }
}
