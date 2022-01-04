/*
 * Copyright 2022 ProximaX
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

package io.proximax.sdk.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.proximax.core.utils.StringUtils;

public class MetadataCalculationUtils {

    private MetadataCalculationUtils() {
        // hidden constructor
    }

    // private static final String OUTPUT_FORMAT = "%-20s:%s";

    public static Short getValueSizeDeltaValue(String value, String oldValue) {

        byte[] OldValue;
        if (oldValue != null) {
            OldValue = StringUtils.getBytes(oldValue);

        } else {
            OldValue = StringUtils.getBytes("");

        }
        byte[] NewValue = StringUtils.getBytes(value);
        var max = Math.max(OldValue.length, NewValue.length);
        var min = Math.min(OldValue.length, NewValue.length);
        Integer valueSizeDelta = (max - min);

        Short valueSizeDeltaValue = valueSizeDelta.shortValue();

        return valueSizeDeltaValue;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static BigInteger getScopedMetadataKey(String scopedMetadataKey) {
        byte[] ScopedKey = scopedMetadataKey.getBytes(StandardCharsets.UTF_8);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] scopeMetadatakey = md.digest(ScopedKey);
        // return result;

        BigInteger ScopedMetadataKey = new BigInteger(scopeMetadatakey);
        System.out.println(ScopedMetadataKey);
        return ScopedMetadataKey;
    }

    public static byte[] getValueDifferenceBytes(String value, String oldValue) {

        byte[] OldValue;
        if (oldValue != null) {
            OldValue = StringUtils.getBytes(oldValue);
        } else {
            OldValue = StringUtils.getBytes("");
        }
        byte[] NewValue = StringUtils.getBytes(value);
        Integer ValueSize = Math.max(OldValue.length, NewValue.length);

        byte[] valueDifferenceBytes = new byte[ValueSize];
        byte[] newValueByteArray = new byte[ValueSize];
        byte[] oldValueByteArray = new byte[ValueSize];
        System.arraycopy(NewValue, 0, newValueByteArray, 0, NewValue.length);
        System.arraycopy(OldValue, 0, oldValueByteArray, 0, OldValue.length);

        for (int i = 0; i < ValueSize; i++) {
            valueDifferenceBytes[i] = (byte) (newValueByteArray[i] ^ oldValueByteArray[i]);
        }

        return valueDifferenceBytes;
    }

    public static Short getValueSize(String value, String oldValue) {
        byte[] OldValue;
        if (oldValue != null) {
            OldValue = StringUtils.getBytes(oldValue);
        } else {
            OldValue = StringUtils.getBytes("");
        }

        byte[] NewValue = StringUtils.getBytes(value);
        Integer ValueSize = Math.max(OldValue.length, NewValue.length);
        return ValueSize.shortValue();
    }

}
