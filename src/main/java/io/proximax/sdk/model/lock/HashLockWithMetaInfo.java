/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.lock;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;
import io.proximax.sdk.gen.model.HashLockWithMeta;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.mosaic.MosaicId;

import java.util.ArrayList;

public class HashLockWithMetaInfo {
    private final MetaLock id;
    private final HashLockInfo lock;

    public HashLockWithMetaInfo(MetaLock id, HashLockInfo lock) {
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
    public HashLockInfo getLock() {
        return lock;
    }

    public static HashLockWithMetaInfo fromDto(HashLockWithMeta dto) {
        return new HashLockWithMetaInfo(
                new MetaLock(dto.getMeta().getId()),
                new HashLockInfo(new LockInfo(dto.getLock().getAccount(),
                        Address.createFromEncoded(dto.getLock().getAccountAddress()),
                        new MosaicId(toBigInt(new ArrayList<>(dto.getLock().getMosaicId()))), toBigInt(new ArrayList<>(dto.getLock().getAmount())),
                        toBigInt(new ArrayList<>(dto.getLock().getHeight()))), dto.getLock().getStatus(),dto.getLock().getHash()));
    }

}