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

import io.proximax.core.test.ExceptionAssert;
import io.proximax.core.utils.MustBe;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

public class MustBeTest {

    //region notNull

	/*
	@Test
	public void notNullThrowsIfObjectIsNull() {
		// Assert:
		ExceptionAssert.assertThrows(
				v -> MustBe.notNull(null, "test"),
				IllegalArgumentException.class,
				ex -> ex.getMessage().contains("test"));
	}*/

    private static void assertNotWhitespaceThrows(final String input) {
        ExceptionAssert.assertThrows(
                v -> MustBe.notWhitespace(input, "input", 10),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("input"));
    }

    //region notWhitespace

    private static void assertMatchThrows(final String input) {
        ExceptionAssert.assertThrows(
                v -> MustBe.match(input, "input", Pattern.compile("[0-9]*"), 10),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("input"));
    }

    private static void assertInRangeThrows(final int input) {
        ExceptionAssert.assertThrows(
                v -> MustBe.inRange(input, "val", -2, 5),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("val"));
    }

    private static void assertLongInRangeThrows(final long input) {
        ExceptionAssert.assertThrows(
                v -> MustBe.inRange(input, "val", -2L, 5L),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("val"));
    }

    @Test
    public void notNullDoesNotThrowIfObjectIsNotNull() {
        // Assert: no exception
        MustBe.notNull(new Object(), "test");
    }

    //endregion

    //region match

    @Test
    public void notWhitespaceThrowsIfStringContainsNoNonWhitespaceChars() {
        // Assert:
        Arrays.asList(null, "", " ", " \t \t  ")
                .forEach(MustBeTest::assertNotWhitespaceThrows);
    }

    @Test
    public void notWhitespaceDoesNotThrowIfStringContainsAtLeastOneNonWhitespaceChar() {
        // Assert: no exception
        for (final String str : Arrays.asList("bar", "foo bar", " \ta\t  ")) {
            MustBe.notWhitespace(str, "test", 10);
        }
    }

    @Test
    public void notWhitespaceThrowsIfStringIsTooLong() {
        // Assert:
        assertNotWhitespaceThrows("01234567890");
    }

    @Test
    public void matchThrowsIfStringIsNull() {
        // Assert:
        assertMatchThrows(null);
    }

    @Test
    public void matchThrowsIfStringIsEmpty() {
        // Assert:
        assertMatchThrows("");
    }

    @Test
    public void matchThrowsIfStringDoesNotMatchPattern() {
        // Assert:
        assertMatchThrows("13G74");
    }

    //endregion

    //region inRange (integer)

    @Test
    public void matchThrowsIfStringIsTooLong() {
        // Assert:
        assertMatchThrows("01234567890");
    }

    @Test
    public void matchDoesNotThrowIfAllConditionsAreSatisfied() {
        // Assert: no exception
        MustBe.match("13674", "input", Pattern.compile("[0-9]*"), 10);
        MustBe.match("0123456789", "input", Pattern.compile("[0-9]*"), 10);
    }

    @Test
    public void inRangeThrowsIfValueIsLessThanMinValue() {
        // Assert:
        assertInRangeThrows(-3);
        assertInRangeThrows(-1000);
    }

    @Test
    public void inRangeThrowsIfValueIsGreaterThanMaxValue() {
        // Assert:
        assertInRangeThrows(6);
        assertInRangeThrows(1000);
    }

    //region inRange (long)

    @Test
    public void inRangeDoesNotThrowIfValueIsInRange() {
        // Assert:
        MustBe.inRange(-2, "val", -2, 5);
        MustBe.inRange(1, "val", -2, 5);
        MustBe.inRange(5, "val", -2, 5);
    }

    @Test
    public void inRangeThrowsIfLongValueIsLessThanMinValue() {
        // Assert:
        assertLongInRangeThrows(-3L);
        assertLongInRangeThrows(-1000L);
    }

    @Test
    public void inRangeThrowsIfLongValueIsGreaterThanMaxValue() {
        // Assert:
        assertLongInRangeThrows(6L);
        assertLongInRangeThrows(1000L);
    }

    @Test
    public void inRangeDoesNotThrowIfLongValueIsInRange() {
        // Assert:
        MustBe.inRange(-2L, "val", -2L, 5L);
        MustBe.inRange(1L, "val", -2L, 5L);
        MustBe.inRange(5L, "val", -2L, 5L);
    }

    //endregion

    //region empty

    @Test
    public void emptyThrowsIfCollectionIsNotEmpty() {
        // Assert:
        ExceptionAssert.assertThrows(
                v -> MustBe.empty(Collections.singletonList(123), "list"),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("list"));
    }

    @Test
    public void emptyDoesNotThrowIfCollectionIsEmpty() {
        // Assert: no exception
        MustBe.empty(Collections.emptyList(), "list");
    }

    //endregion

    //region trueValue / falseValue

    @Test
    public void trueValueThrowsIfValueIsFalse() {
        // Assert:
        ExceptionAssert.assertThrows(
                v -> MustBe.trueValue(false, "bool"),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("bool"));
    }

    @Test
    public void trueValueDoesNotThrowIfValueIsTrue() {
        // Assert: no exception
        MustBe.trueValue(true, "bool");
    }

    @Test
    public void falseValueThrowsIfValueIsTrue() {
        // Assert:
        ExceptionAssert.assertThrows(
                v -> MustBe.falseValue(true, "bool"),
                IllegalArgumentException.class,
                ex -> ex.getMessage().contains("bool"));
    }

    @Test
    public void falseValueDoesNotThrowIfValueIsFalse() {
        // Assert: no exception
        MustBe.falseValue(false, "bool");
    }

    //endregion
}