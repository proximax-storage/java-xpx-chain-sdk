/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.util.Arrays;

class ModifyMosaicLevyTransactionSchema extends Schema{
    public ModifyMosaicLevyTransactionSchema() {
        super(Arrays.asList(
                new ScalarAttribute("size", Constants.SIZEOF_INT),
                new ArrayAttribute("signature", Constants.SIZEOF_BYTE),
                new ArrayAttribute("signer", Constants.SIZEOF_BYTE),
                new ScalarAttribute("version", Constants.SIZEOF_INT),
                new ScalarAttribute("type", Constants.SIZEOF_SHORT),
                new ArrayAttribute("maxFee", Constants.SIZEOF_INT),
                new ArrayAttribute("deadline", Constants.SIZEOF_INT),
                
                new ArrayAttribute("mosaicId", Constants.SIZEOF_INT),
                new TableAttribute("levy", Arrays.asList(
                    new ScalarAttribute("type", Constants.SIZEOF_BYTE),
                    new ArrayAttribute("recipient", Constants.SIZEOF_BYTE),
                    new ArrayAttribute("mosaicId", Constants.SIZEOF_INT),
                    new ArrayAttribute("fee", Constants.SIZEOF_INT)
              ))
        ));
    }
}
