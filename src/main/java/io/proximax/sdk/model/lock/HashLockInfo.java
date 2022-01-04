/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.lock;

/**
 * HashLockInfo
 */
public class HashLockInfo {
    private final LockInfo lockInfo;
    private final String status;
    private final String hash;

    public HashLockInfo(LockInfo lockInfo, String status, String hash) {
        this.lockInfo = lockInfo;
        this.status = status;
        this.hash = hash;
    }

    /**
     * @return lockInfo
     */
    public LockInfo getLockInfo() {
        return lockInfo;
    }
  
    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @return hash
     */
    public String getHash() {
        return hash;
    }
}