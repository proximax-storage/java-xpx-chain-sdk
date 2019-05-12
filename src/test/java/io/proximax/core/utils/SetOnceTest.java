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

package io.proximax.core.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

import io.proximax.core.test.ExceptionAssert;

public class SetOnceTest {

    @Test
    public void defaultValueIsConstructorParameter() {
        // Arrange:
        final SetOnce<Integer> wrapper = new SetOnce<>(17);

        // Act:
        final Integer value = wrapper.get();

        // Assert:
        MatcherAssert.assertThat(value, IsEqual.equalTo(17));
    }

    @Test
    public void defaultValueCanBeChangedToCustomValue() {
        // Arrange:
        final SetOnce<Integer> wrapper = new SetOnce<>(17);

        // Act:
        wrapper.set(54);
        final Integer value = wrapper.get();

        // Assert:
        MatcherAssert.assertThat(value, IsEqual.equalTo(54));
    }

    @Test
    public void defaultValueCanBeReset() {
        // Arrange:
        final SetOnce<Integer> wrapper = new SetOnce<>(17);

        // Act:
        wrapper.set(54);
        wrapper.set(null);
        final Integer value = wrapper.get();

        // Assert:
        MatcherAssert.assertThat(value, IsEqual.equalTo(17));
    }

    @Test
    public void defaultValueCannotBeChangedAfterBeingSet() {
        // Arrange:
        final SetOnce<Integer> wrapper = new SetOnce<>(17);
        wrapper.set(54);

        // Act:
        ExceptionAssert.assertThrows(
                v -> wrapper.set(77),
                IllegalStateException.class);
    }
}