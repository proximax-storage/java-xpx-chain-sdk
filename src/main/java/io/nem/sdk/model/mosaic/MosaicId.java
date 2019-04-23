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

package io.nem.sdk.model.mosaic;

import io.nem.sdk.model.transaction.IdGenerator;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

/**
 * The mosaic id structure describes mosaic id
 *
 * @since 1.0
 */
public class MosaicId {
    private final BigInteger id;
    private final Optional<String> fullName;

    /**
     * Create MosaicId from mosaic and namespace string name (ex: nem:xem or domain.subdom.subdome:token)
     *
     * @param id String representation of mosaic ID
     * @throws IllegalIdentifierException MosaicId identifier
     */
    public MosaicId(String id) {
        if (id.isEmpty()) throw new IllegalIdentifierException(id + " is not valid");
        if (!id.contains(":")) throw new IllegalIdentifierException(id + " is not valid");
        String[] parts = id.split(":");
        if (parts.length != 2) throw new IllegalIdentifierException(id + " is not valid");
        String namespaceName = parts[0];
        String mosaicName = parts[1];
        this.id = IdGenerator.generateMosaicId(namespaceName, mosaicName);
        this.fullName = Optional.of(id);
    }

    /**
     * Create MosaicId from biginteger id
     *
     * @param id numeric representaion of Mosaic ID
     */
    public MosaicId(BigInteger id) {
        this.id = id;
        this.fullName = Optional.empty();
    }

    /**
     * Returns mosaic biginteger id
     *
     * @return mosaic biginteger id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Returns optional mosaic full name, with namespace name (ex: nem:xem)
     *
     * @return namespace full name
     */
    public Optional<String> getFullName() {
        return fullName;
    }

    /**
     * Compares mosaicIds for equality.
     *
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MosaicId)) return false;
        MosaicId mosaicId1 = (MosaicId) o;
        return Objects.equals(id, mosaicId1.id);
    }
}
