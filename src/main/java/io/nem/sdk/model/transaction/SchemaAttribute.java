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


import java.util.Arrays;

abstract class SchemaAttribute {
    final private String name;

    SchemaAttribute(String name) {
        this.name = name;
    }

    abstract byte[] serialize(byte[] buffer, int position, int innerObjectPosition);

    byte[] serialize(byte[] buffer, int position) {
        return serialize(buffer, position, buffer[0]);
    }

    public String getName() {
        return name;
    }

    protected byte[] findParam(int innerObjectPosition, int position, byte[] buffer, Constants typeSize) {
        int offset = __offset(innerObjectPosition, position, buffer);
        return offset == 0 ? new byte[]{0} : Arrays.copyOfRange(buffer, offset + innerObjectPosition, offset + innerObjectPosition + typeSize.getValue());
    }

    protected byte[] findVector(int innerObjectPosition, int position, byte[] buffer, Constants typeSize) {
        int offset = __offset(innerObjectPosition, position, buffer);
        int offsetLong = offset + innerObjectPosition;
        int vecStart = __vector(offsetLong, buffer);
        int vecLength = __vector_length(offsetLong, buffer) * typeSize.getValue();
        return offset == 0 ? new byte[]{0} : Arrays.copyOfRange(buffer, vecStart, vecStart + vecLength);
    }

    protected int findObjectStartPosition(int innerObjectPosition, int position, byte[] buffer) {
        int offset = __offset(innerObjectPosition, position, buffer);
        return __indirect(offset + innerObjectPosition, buffer);
    }

    protected int findArrayLength(int innerObjectPosition, int position, byte[] buffer) {
        int offset = __offset(innerObjectPosition, position, buffer);
        return offset == 0 ? 0 : __vector_length(innerObjectPosition + offset, buffer);
    }

    protected int findObjectArrayElementStartPosition(int innerObjectPosition, int position, byte[] buffer, int startPosition) {
        int offset = __offset(innerObjectPosition, position, buffer);
        int vector = __vector(innerObjectPosition + offset, buffer);
        return __indirect(vector + startPosition * 4, buffer);
    }

    protected int readInt32(int offset, byte[] buffer) {
        /*final ByteBuffer bb = ByteBuffer.wrap(new byte[]{buffer[offset], buffer[offset + 1], buffer[offset + 2], buffer[offset + 3]});
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();*/
        int value = (buffer[offset + 3] << (Byte.SIZE * 3));
        value |= (buffer[offset + 2] & 0xFF) << (Byte.SIZE * 2);
        value |= (buffer[offset + 1] & 0xFF) << (Byte.SIZE);
        value |= (buffer[offset] & 0xFF);
        return value;
    }

    protected int readInt16(int offset, byte[] buffer) {
        /*final ByteBuffer bb = ByteBuffer.wrap(new byte[]{buffer[offset], buffer[offset + 1]});
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort();*/
        int value = (buffer[offset + 1] & 0xFF) << (Byte.SIZE);
        value |= (buffer[offset] & 0xFF);
        return value;
    }

    protected int __offset(int innerObjectPosition, int position, byte[] buffer) {
        int vtable = innerObjectPosition - readInt32(innerObjectPosition, buffer);
        return position < readInt16(vtable, buffer) ? readInt16(vtable + position, buffer) : 0;
    }

    protected int __vector_length(int offset, byte[] buffer) {
        return readInt32(offset + readInt32(offset, buffer), buffer);
    }

    protected int __indirect(int offset, byte[] buffer) {
        return offset + readInt32(offset, buffer);
    }

    protected int __vector(int offset, byte[] buffer) {
        return offset + readInt32(offset, buffer) + 4;
    }
}
