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

package io.proximax.sdk.model.transaction;

import java.util.Objects;

import io.proximax.sdk.model.account.PublicAccount;

/**
 * The multisig cosignatory modifications are part of the NEM's multisig account system.
 * With a multisig cosignatory modification a cosignatory is added to or deleted from a multisig account.
 * Multisig cosignatory modifications are part of a modify multisig account transactions.
 *
 * @since 1.0
 */
public class MultisigCosignatoryModification {
    private final MultisigCosignatoryModificationType type;
    private final PublicAccount cosignatoryPublicAccount;

    public MultisigCosignatoryModification(MultisigCosignatoryModificationType type, PublicAccount cosignatoryPublicAccount) {
        this.type = type;
        this.cosignatoryPublicAccount = cosignatoryPublicAccount;
    }

    /**
     * Returns multisig modification type.
     *
     * @return {@link MultisigCosignatoryModificationType}
     */
    public MultisigCosignatoryModificationType getType() {
        return type;
    }

    /**
     * Returns cosignatory public account.
     *
     * @return {@link PublicAccount}
     */
    public PublicAccount getCosignatoryPublicAccount() {
        return cosignatoryPublicAccount;
    }
    
    /**
     * create modification to add public account
     * 
     * @param publicAccount public account to add
     * @return the modification to add
     */
    public static MultisigCosignatoryModification add(PublicAccount publicAccount) {
       return new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD, publicAccount);
    }
    
    /**
     * create modification to remove public account
     * 
     * @param publicAccount public account to remove
     * @return the modification to remove
     */
    public static MultisigCosignatoryModification remove(PublicAccount publicAccount) {
       return new MultisigCosignatoryModification(MultisigCosignatoryModificationType.REMOVE, publicAccount);
    }

   @Override
   public int hashCode() {
      return Objects.hash(cosignatoryPublicAccount, type);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      MultisigCosignatoryModification other = (MultisigCosignatoryModification) obj;
      return Objects.equals(cosignatoryPublicAccount, other.cosignatoryPublicAccount) && type == other.type;
   }
}


