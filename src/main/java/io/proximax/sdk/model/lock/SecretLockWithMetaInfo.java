/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.lock;

import io.proximax.sdk.gen.model.SecretLockWithMeta;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.MosaicId;

import java.util.ArrayList;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

public class SecretLockWithMetaInfo {
    private final MetaLock id;
    private final SecretLockInfo lock;

    public SecretLockWithMetaInfo(MetaLock id, SecretLockInfo lock) {
        this.id = id;
        this.lock = lock;
    }

    /**
     * @return id
     */
    public MetaLock getId() {
        return id;
    }

    /**
     * @return lock
     */
    public SecretLockInfo getLock() {
        return lock;
    }

    public static SecretLockWithMetaInfo fromDto(SecretLockWithMeta dto) {
        return new SecretLockWithMetaInfo(
                new MetaLock(dto.getMeta().getId()),
                new SecretLockInfo(new LockInfo(dto.getLock().getAccount(),
                        Address.createFromEncoded(dto.getLock().getAccountAddress()),
                        new MosaicId(toBigInt(new ArrayList<>(dto.getLock().getMosaicId()))),
                        toBigInt(new ArrayList<>(dto.getLock().getAmount())),
                        toBigInt(new ArrayList<>(dto.getLock().getHeight()))),
                        dto.getLock().getStatus(),
                        dto.getLock().getHashAlgorithm(),
                        dto.getLock().getSecret(),
                        dto.getLock().getRecipient(),
                        dto.getLock().getCompositeHash()));
    }
}
