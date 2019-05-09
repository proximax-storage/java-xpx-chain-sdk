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
package io.proximax.sdk.model.metadata;

import java.util.List;

import io.proximax.sdk.model.namespace.NamespaceId;

/**
 * Namespace metadata defining numeric ID
 */
public class NamespaceMetadata extends Metadata {
   private NamespaceId id;

   /**
    * @param fields
    * @param id
    */
   public NamespaceMetadata(List<Field> fields, NamespaceId id) {
      super(MetadataType.NAMESPACE, fields);
      this.id = id;
   }

   /**
    * @return the id
    */
   public NamespaceId getId() {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(NamespaceId id) {
      this.id = id;
   }

}
