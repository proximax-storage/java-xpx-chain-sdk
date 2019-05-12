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

import io.proximax.sdk.model.mosaic.MosaicId;

/**
 * Mosaic metadata defining numeric ID
 */
public class MosaicMetadata extends Metadata {
   private MosaicId id;

   /**
    * @param fields metadata fields associated with the mosaic
    * @param id mosaic ID
    */
   public MosaicMetadata(List<Field> fields, MosaicId id) {
      super(MetadataType.MOSAIC, fields);
      this.id = id;
   }

   /**
    * @return the id
    */
   public MosaicId getId() {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(MosaicId id) {
      this.id = id;
   }

}
