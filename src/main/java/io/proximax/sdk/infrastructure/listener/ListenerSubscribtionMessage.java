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

package io.proximax.sdk.infrastructure.listener;

/**
 * message submitted to server to establish subscription
 */
public class ListenerSubscribtionMessage {
    private final String uid;
    private final String subscription;

    /**
     * create new subscription message
     * 
     * @param uid UID of the listener
     * @param subscription identification of the subscription. 
     */
    public ListenerSubscribtionMessage(String uid, String subscription) {
        this.uid = uid;
        this.subscription = subscription;
    }

    /**
     * @return UID of the listener
     */
    public String getUid() {
        return uid;
    }

    /**
     * @return desired subscription identification
     */
    public String getSubscription() {
        return subscription;
    }
}
