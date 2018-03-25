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

/**
 * An abstract message class that serves as the base class of all message types.
 *
 * @since 1.0
 */
public abstract class Message {
    private final int type;
    private final String payload;

    public Message(int type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * Returns message type.
     *
     * @return int
     */
    public int getType() {
        return type;
    }

    /**
     * Returns message payload.
     *
     * @return String
     */
    public String getPayload() {
        return payload;
    }
}
