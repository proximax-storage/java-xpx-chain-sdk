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

package io.proximax.sdk.model.mosaic;

import java.util.List;

/**
 * The mosaic name info structure describes basic information of a mosaic and name.
 *
 * @since 1.0
 */
public class MosaicNames {
    private final MosaicId mosaicId;
    private final List<String> names;
   /**
    * @param mosaicId
    * @param names
    */
   public MosaicNames(MosaicId mosaicId, List<String> names) {
      this.mosaicId = mosaicId;
      this.names = names;
   }
   /**
    * @return the mosaicId
    */
   public MosaicId getMosaicId() {
      return mosaicId;
   }
   /**
    * @return the names
    */
   public List<String> getNames() {
      return names;
   }
   @Override
   public String toString() {
      return "MosaicNames [mosaicId=" + mosaicId + ", names=" + names + "]";
   }

}
