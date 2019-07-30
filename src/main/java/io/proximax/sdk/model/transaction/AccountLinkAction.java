/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

/**
 * Account link action identifies type of action to perform
 */
public enum AccountLinkAction {
   /** link the account */
   LINK((byte) 0), 
   /** unlink he account */
   UNLINK((byte) 1);

   /** the code of the action */
   private final byte code;

   /**
    * create new enum item
    * 
    * @param code code of the item
    */
   AccountLinkAction(final byte code) {
      this.code = code;
   }

   /**
    * @return the code of this account link action
    */
   public byte getCode() {
      return code;
   }
   
   /**
    * get action by the code
    * 
    * @param code code of the account link action
    * @return the account link action matching specified code
    */
   public static AccountLinkAction getByCode(int code) {
      for (AccountLinkAction action: values()) {
         if (action.getCode() == code) {
            return action;
         }
      }
      throw new IllegalArgumentException("Unknown account link action code " + code);
   }
}
