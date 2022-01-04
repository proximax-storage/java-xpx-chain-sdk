/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.lock;



public class SecretLockInfo {
    private final LockInfo lockInfo;
    private final Integer status;
    private final Integer hashAlgorithm;
    private final String secret;
    private final String recipient;
    private final String compositeHash;

    public SecretLockInfo(
            LockInfo lockInfo, Integer status,Integer hashAlgorithm,String secret,String recipient,String compositeHash) {
                
        this.lockInfo = lockInfo;
        this.status = status;
        this.hashAlgorithm = hashAlgorithm;
        this.secret = secret;
        this.recipient = recipient;
        this.compositeHash = compositeHash;

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
    public Integer getStatus() {
        return status;
    }

    /**
     * @return hashAlgorithm
     */
    public Integer getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * @return secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @return recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return compositeHash
     */
    public String getCompositeHash() {
        return compositeHash;
    }
}
