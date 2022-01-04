/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.lock;

public class MetaLock {
    private final String id;

    public MetaLock(String id) {
        this.id = id;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }
}
