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
package io.proximax.sdk.model.transaction;

import java.util.Arrays;

/**
 * Schema for metadata modification transaction
 */
public class ModifyMetadataTransactionSchema extends Schema {
   ModifyMetadataTransactionSchema() {
      super(Arrays.asList(
            new ScalarAttribute("size", Constants.SIZEOF_INT),
            new ArrayAttribute("signature", Constants.SIZEOF_BYTE),
            new ArrayAttribute("signer", Constants.SIZEOF_BYTE),
            new ScalarAttribute("version", Constants.SIZEOF_SHORT),
            new ScalarAttribute("type", Constants.SIZEOF_SHORT),
            new ArrayAttribute("maxFee", Constants.SIZEOF_INT),
            new ArrayAttribute("deadline", Constants.SIZEOF_INT),
            
            new ScalarAttribute("metadataType", Constants.SIZEOF_BYTE),
            new ArrayAttribute("metadataId", Constants.SIZEOF_BYTE),
            new TableArrayAttribute("modifications",
                  Arrays.asList(
                        new ScalarAttribute("size", Constants.SIZEOF_INT),
                        new ScalarAttribute("modificationType", Constants.SIZEOF_BYTE),
                        new ScalarAttribute("keySize", Constants.SIZEOF_BYTE),
                        new ArrayAttribute("valueSize", Constants.SIZEOF_BYTE),
                        new ArrayAttribute("key", Constants.SIZEOF_BYTE),
                        new ArrayAttribute("value", Constants.SIZEOF_BYTE)))));
   }
}