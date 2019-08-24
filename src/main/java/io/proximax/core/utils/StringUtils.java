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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Static class that contains string utility functions.
 */
public class StringUtils {
   
   /** standard charset used on the project */
   private static final Charset ENCODING_CHARSET = StandardCharsets.UTF_8;

   private StringUtils() {
      // hiding implicit constructor for utility class
   }

    /**
     * Determines if the specified string is null or empty.
     *
     * @param str The string.
     * @return true if the string is null or empty.
     */
    public static boolean isNullOrEmpty(final String str) {
        return null == str || str.isEmpty();
    }

    /**
     * Determines if the specified string is null or whitespace.
     *
     * @param str The string.
     * @return true if the string is null or whitespace.
     */
    public static boolean isNullOrWhitespace(final String str) {
        if (isNullOrEmpty(str)) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>Repeat a String {@code repeat} times to form a new String.</p>
     *
     * <pre>
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = ""
     * </pre>
     *
     * @param str  the String to repeat, may be null
     * @param repeat  number of times to repeat str, negative treated as zero
     * @return a new String consisting of the original String repeated,
     *  {@code null} if null String input
     */
    public static String repeat(String str, int repeat) {
       return org.apache.commons.lang3.StringUtils.repeat(str, repeat);
    }
    
    /**
     * Replaces a variable contained in a string with a value. A variable is defined as ${variable}.
     * This pattern is replaced by the given value.
     *
     * @param string String that contains variables.
     * @param name   Name of the variable to be replaced with its value.
     * @param value  Value that will replace the variable.
     * @return string with value replacing the variable with the given name
     */
    public static String replaceVariable(final String string, final String name, final String value) {
        return string.replace(String.format("${%s}", name), value);
    }
    
    /**
     * Converts a string to a UTF-8 byte array.
     *
     * @param s The input string.
     * @return The output byte array.
     */
    public static byte[] getBytes(final String s) {
        return s.getBytes(ENCODING_CHARSET);
    }

    /**
     * Converts a UTF-8 byte array to a string.
     *
     * @param bytes The input byte array.
     * @return The output string.
     */
    public static String getString(final byte[] bytes) {
        return new String(bytes, ENCODING_CHARSET);
    }
}
