/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.utils.dto;

import java.math.BigInteger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.utils.GsonUtils;

/**
 * Utility class to help with serialization and deserialization of transaction data
 */
public class TransactionMappingUtils {

   /**
    * hiding constructor for utility class
    */
   private TransactionMappingUtils() { /* utility class does not need instantiation */ }
   
   /**
    * serialize transaction version and network type into version field for serialization
    * 
    * @param transactionVersion version of the transaction
    * @param networkType network type
    * @return integer representing both network type and transaction version
    */
   public static int serializeVersion(int transactionVersion, int networkType) {
      return (networkType << 24) + transactionVersion;
   }
   
   /**
    * extract transaction version from version
    * 
    * @param elem version field of transaction
    * @return transaction version
    */
   public static Integer extractTransactionVersion(JsonElement elem) {
      int version = elem.getAsInt();
      // take last 3 bytes and return them as a version
      return version & 0xFFFFFF;
   }

   /**
    * extract network type from the version
    * 
    * @param elem version field of transaction
    * @return transaction network type
    */
   public static NetworkType extractNetworkType(JsonElement elem) {
      int version = elem.getAsInt();
      // take most significant byte of a version and return it as a number
      return NetworkType.rawValueOf(version >>> 24);
   }
   
   /**
    * retrieve fee from the transaction. listener returns fee as uint64 "fee" and services return string "maxFee" and
    * this method provides support for both
    * 
    * @param transaction transaction object with fee or maxFee field
    * @return value of the fee
    */
   public static BigInteger extractFee(JsonObject transaction) {
      // first get value from a field
      JsonElement feeElement = transaction.get("maxFee");
      if (feeElement == null) {
         feeElement = transaction.get("fee");
      }
      // based on the retrieved value create big integer instance
      if (feeElement instanceof JsonArray) {
         return GsonUtils.getBigInteger((JsonArray) feeElement);
      } else {
         return new BigInteger(feeElement.getAsString());
      }
   }
}
