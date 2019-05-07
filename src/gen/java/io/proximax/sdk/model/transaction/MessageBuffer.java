// automatically generated by the FlatBuffers compiler, do not modify

package io.proximax.sdk.model.transaction;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class MessageBuffer extends Table {
  public static MessageBuffer getRootAsMessageBuffer(ByteBuffer _bb) { return getRootAsMessageBuffer(_bb, new MessageBuffer()); }
  public static MessageBuffer getRootAsMessageBuffer(ByteBuffer _bb, MessageBuffer obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public MessageBuffer __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int type() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) & 0xFF : 0; }
  public int payload(int j) { int o = __offset(6); return o != 0 ? bb.get(__vector(o) + j * 1) & 0xFF : 0; }
  public int payloadLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer payloadAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer payloadInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createMessageBuffer(FlatBufferBuilder builder,
      int type,
      int payloadOffset) {
    builder.startObject(2);
    MessageBuffer.addPayload(builder, payloadOffset);
    MessageBuffer.addType(builder, type);
    return MessageBuffer.endMessageBuffer(builder);
  }

  public static void startMessageBuffer(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addType(FlatBufferBuilder builder, int type) { builder.addByte(0, (byte)type, (byte)0); }
  public static void addPayload(FlatBufferBuilder builder, int payloadOffset) { builder.addOffset(1, payloadOffset, 0); }
  public static int createPayloadVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startPayloadVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static int endMessageBuffer(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

