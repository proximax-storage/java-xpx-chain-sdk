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

package io.nem.core.utils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A two-level map of items.
 * <br>
 * Items are automatically created on access.
 * Item associations are order-dependent.
 */
public abstract class AbstractTwoLevelMap<TKey, TValue> {
    private final Map<TKey, Map<TKey, TValue>> impl = new ConcurrentHashMap<>();

    /**
     * Gets the TValue associated with key1 and key2.
     *
     * @param key1 The first key.
     * @param key2 The second key.
     * @return The value associated with key and key2.
     */
    public TValue getItem(final TKey key1, final TKey key2) {
        final Map<TKey, TValue> keyOneValues = this.getItems(key1);

        TValue value = keyOneValues.get(key2);
        if (null == value) {
            value = this.createValue();
            keyOneValues.put(key2, value);
        }

        return value;
    }

    /**
     * Gets the (TKey, TValue) map associated with key.
     *
     * @param key The first key.
     * @return The map associated with key.
     */
    public Map<TKey, TValue> getItems(final TKey key) {
        Map<TKey, TValue> keyValues = this.impl.get(key);
        if (null == keyValues) {
            keyValues = new ConcurrentHashMap<>();
            this.impl.put(key, keyValues);
        }

        return keyValues;
    }

    /**
     * Removes a key from the map.
     *
     * @param key The key to remove.
     */
    public void remove(final TKey key) {
        this.impl.remove(key);
    }

    /**
     * Gets the key set of this map.
     *
     * @return The key set.
     */
    public Set<TKey> keySet() {
        return this.impl.keySet();
    }

    /**
     * Creates a new blank value.
     *
     * @return A new value.
     */
    protected abstract TValue createValue();
}
