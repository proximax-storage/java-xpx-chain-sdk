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

/**
 * Wrapper that allows an object to be set once (or reset and set again).
 *
 * @param <T> The inner type.
 */
public class SetOnce<T> {
    private final T defaultValue;
    private T value;

    /**
     * Creates a wrapper.
     *
     * @param defaultValue The default value.
     */
    public SetOnce(final T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the inner object.
     *
     * @return The inner object.
     */
    public T get() {
        return null == this.value ? this.defaultValue : this.value;
    }

    /**
     * Sets the inner object.
     *
     * @param value The inner object.
     */
    public void set(final T value) {
        if (null != this.value && null != value) {
            throw new IllegalStateException("cannot change value because it is already set");
        }

        this.value = value;
    }
}
