// automatically generated by the FlatBuffers compiler, do not modify

package io.proximax.sdk.gen.buffers;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class AccountPropertiesTransactionBuffer extends Table {
  public static AccountPropertiesTransactionBuffer getRootAsAccountPropertiesTransactionBuffer(ByteBuffer _bb) { return getRootAsAccountPropertiesTransactionBuffer(_bb, new AccountPropertiesTransactionBuffer()); }
  public static AccountPropertiesTransactionBuffer getRootAsAccountPropertiesTransactionBuffer(ByteBuffer _bb, AccountPropertiesTransactionBuffer obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public AccountPropertiesTransactionBuffer __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public long size() { int o = __offset(4); return o != 0 ? (long)bb.getInt(o + bb_pos) & 0xFFFFFFFFL : 0L; }
  public int signature(int j) { int o = __offset(6); return o != 0 ? bb.get(__vector(o) + j * 1) & 0xFF : 0; }
  public int signatureLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer signatureAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer signatureInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }
  public int signer(int j) { int o = __offset(8); return o != 0 ? bb.get(__vector(o) + j * 1) & 0xFF : 0; }
  public int signerLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer signerAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public ByteBuffer signerInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 8, 1); }
  public long version() { int o = __offset(10); return o != 0 ? (long)bb.getInt(o + bb_pos) & 0xFFFFFFFFL : 0L; }
  public int type() { int o = __offset(12); return o != 0 ? bb.getShort(o + bb_pos) & 0xFFFF : 0; }
  public long maxFee(int j) { int o = __offset(14); return o != 0 ? (long)bb.getInt(__vector(o) + j * 4) & 0xFFFFFFFFL : 0; }
  public int maxFeeLength() { int o = __offset(14); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer maxFeeAsByteBuffer() { return __vector_as_bytebuffer(14, 4); }
  public ByteBuffer maxFeeInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 14, 4); }
  public long deadline(int j) { int o = __offset(16); return o != 0 ? (long)bb.getInt(__vector(o) + j * 4) & 0xFFFFFFFFL : 0; }
  public int deadlineLength() { int o = __offset(16); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer deadlineAsByteBuffer() { return __vector_as_bytebuffer(16, 4); }
  public ByteBuffer deadlineInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 16, 4); }
  public int propertyType() { int o = __offset(18); return o != 0 ? bb.get(o + bb_pos) & 0xFF : 0; }
  public int modificationCount() { int o = __offset(20); return o != 0 ? bb.get(o + bb_pos) & 0xFF : 0; }
  public PropertyModificationBuffer modifications(int j) { return modifications(new PropertyModificationBuffer(), j); }
  public PropertyModificationBuffer modifications(PropertyModificationBuffer obj, int j) { int o = __offset(22); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int modificationsLength() { int o = __offset(22); return o != 0 ? __vector_len(o) : 0; }

  public static int createAccountPropertiesTransactionBuffer(FlatBufferBuilder builder,
      long size,
      int signatureOffset,
      int signerOffset,
      long version,
      int type,
      int maxFeeOffset,
      int deadlineOffset,
      int propertyType,
      int modificationCount,
      int modificationsOffset) {
    builder.startObject(10);
    AccountPropertiesTransactionBuffer.addModifications(builder, modificationsOffset);
    AccountPropertiesTransactionBuffer.addDeadline(builder, deadlineOffset);
    AccountPropertiesTransactionBuffer.addMaxFee(builder, maxFeeOffset);
    AccountPropertiesTransactionBuffer.addVersion(builder, version);
    AccountPropertiesTransactionBuffer.addSigner(builder, signerOffset);
    AccountPropertiesTransactionBuffer.addSignature(builder, signatureOffset);
    AccountPropertiesTransactionBuffer.addSize(builder, size);
    AccountPropertiesTransactionBuffer.addType(builder, type);
    AccountPropertiesTransactionBuffer.addModificationCount(builder, modificationCount);
    AccountPropertiesTransactionBuffer.addPropertyType(builder, propertyType);
    return AccountPropertiesTransactionBuffer.endAccountPropertiesTransactionBuffer(builder);
  }

  public static void startAccountPropertiesTransactionBuffer(FlatBufferBuilder builder) { builder.startObject(10); }
  public static void addSize(FlatBufferBuilder builder, long size) { builder.addInt(0, (int)size, (int)0L); }
  public static void addSignature(FlatBufferBuilder builder, int signatureOffset) { builder.addOffset(1, signatureOffset, 0); }
  public static int createSignatureVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startSignatureVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static void addSigner(FlatBufferBuilder builder, int signerOffset) { builder.addOffset(2, signerOffset, 0); }
  public static int createSignerVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startSignerVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static void addVersion(FlatBufferBuilder builder, long version) { builder.addInt(3, (int)version, (int)0L); }
  public static void addType(FlatBufferBuilder builder, int type) { builder.addShort(4, (short)type, (short)0); }
  public static void addMaxFee(FlatBufferBuilder builder, int maxFeeOffset) { builder.addOffset(5, maxFeeOffset, 0); }
  public static int createMaxFeeVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addInt(data[i]); return builder.endVector(); }
  public static void startMaxFeeVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addDeadline(FlatBufferBuilder builder, int deadlineOffset) { builder.addOffset(6, deadlineOffset, 0); }
  public static int createDeadlineVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addInt(data[i]); return builder.endVector(); }
  public static void startDeadlineVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addPropertyType(FlatBufferBuilder builder, int propertyType) { builder.addByte(7, (byte)propertyType, (byte)0); }
  public static void addModificationCount(FlatBufferBuilder builder, int modificationCount) { builder.addByte(8, (byte)modificationCount, (byte)0); }
  public static void addModifications(FlatBufferBuilder builder, int modificationsOffset) { builder.addOffset(9, modificationsOffset, 0); }
  public static int createModificationsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startModificationsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endAccountPropertiesTransactionBuffer(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishAccountPropertiesTransactionBufferBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedAccountPropertiesTransactionBufferBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }
}

