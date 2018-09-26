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

package io.nem.sdk.model.transaction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlainMessageTest {

    @Test
    void shouldCreatePlainMessageViaConstructor() {
        PlainMessage plainMessage = new PlainMessage("test-message".getBytes());

        assertEquals("test-message", new String(plainMessage.getEncodedPayload()));
        assertTrue(MessageTypes.PLAIN.getType() == plainMessage.getType());
    }


    @Test
    void shouldCreatePlainMessageViaStaticConstructor() {
        PlainMessage plainMessage = PlainMessage.create("test-message".getBytes());

        assertEquals("test-message", new String(plainMessage.getEncodedPayload()));
        assertTrue(MessageTypes.PLAIN.getType() == plainMessage.getType());
    }

    @Test
    void shouldHaveSameEncodedAndDecodedPayload() {
        PlainMessage plainMessage = PlainMessage.create("test-message".getBytes());

        assertEquals("test-message", new String(plainMessage.getEncodedPayload()));
        assertEquals("test-message", new String(plainMessage.getDecodedPayload(null, null)));
    }
}
