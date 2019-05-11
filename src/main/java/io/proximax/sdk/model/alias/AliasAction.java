/*
 * Copyright 2019 ProximaX
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
package io.proximax.sdk.model.alias;

/**
 * Alias actions
 */
public enum AliasAction {
   LINK(0),
   UNLINK(1);
   
   private int code;

   AliasAction(int code) {
       this.code = code;
   }

   /**
    * Returns enum value.
    *
    * @return enum value
    */
   public int getCode() {
       return this.code;
   }

   /**
    * retrieve alias action by the code
    * 
    * @param code of the alias action
    * @return alias action
    */
   public static AliasAction getBycode(int code) {
      for (AliasAction type : AliasAction.values()) {
         if (code == type.code) {
            return type;
         }
      }
      throw new IllegalArgumentException("Unsupported alias action code " + code);
   }
}
