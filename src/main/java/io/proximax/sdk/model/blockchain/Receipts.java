/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.blockchain;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * <p>
 * Conditional state changes in the background enable complex transactions.
 * </p>
 * <p>
 * For example, a hash lock concludes as soon as the aggregate bonded transaction is confirmed. When the locked funds
 * are automatically returned to the account, there is no additional transaction recorded. This might appear as a hidden
 * change that increases the account balance. Receipts provide proof for every hidden change.
 * </p>
 * <p>
 * The collection of receipts are hashed into a merkle tree and linked to a block. The block header stores the root
 * hash, which is different from zero when the block has receipts.
 * </p>
 * 
 * @see <a href=
 * "https://bcdocs.xpxsirius.io/concepts/receipt.html">https://bcdocs.xpxsirius.io/concepts/receipt.html</a>
 */
public class Receipts {
   private final JsonArray transactionStatements;
   private final JsonArray addressResolutionStatements;
   private final JsonArray mosaicResolutionStatements;
   
   /**
    * create new instance
    * 
    * @param transactionStatements A transaction statement is a collection of receipts linked with a transaction in a particular block
    * @param addressResolutionStatements An account alias was used in the block
    * @param mosaicResolutionStatements A mosaic alias was used in the block
    */
   public Receipts(JsonArray transactionStatements, JsonArray addressResolutionStatements,
         JsonArray mosaicResolutionStatements) {
      this.transactionStatements = transactionStatements;
      this.addressResolutionStatements = addressResolutionStatements;
      this.mosaicResolutionStatements = mosaicResolutionStatements;
   }

   /**
    * @return the transactionStatements
    */
   public JsonArray getTransactionStatements() {
      return transactionStatements;
   }

   /**
    * @return the addressResolutionStatements
    */
   public JsonArray getAddressResolutionStatements() {
      return addressResolutionStatements;
   }

   /**
    * @return the mosaicResolutionStatements
    */
   public JsonArray getMosaicResolutionStatements() {
      return mosaicResolutionStatements;
   }

   /**
    * create receipts instance from raw JsonObject
    * 
    * @param json the data form server
    * @return the receipts
    */
   public static Receipts fromJson(JsonObject json) {
      return new Receipts(json.getAsJsonArray("transactionStatements"), json.getAsJsonArray("addressResolutionStatements"), json.getAsJsonArray("mosaicResolutionStatements"));
   }
}
