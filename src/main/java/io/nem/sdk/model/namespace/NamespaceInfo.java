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

package io.nem.sdk.model.namespace;

import io.nem.sdk.model.account.PublicAccount;

import java.math.BigInteger;
import java.util.List;

/**
 * NamespaceInfo contains the state information of a namespace.
 *
 * @since 1.0
 */
public class NamespaceInfo {
    private final boolean active;
    private final Integer index;
    private final String metaId;
    private final NamespaceType type;
    private final Integer depth;
    private final List<NamespaceId> levels;
    private final NamespaceId parentId;
    private final PublicAccount owner;
    private final BigInteger startHeight;
    private final BigInteger endHeight;

    public NamespaceInfo(boolean active, Integer index, String metaId, NamespaceType type, Integer depth, List<NamespaceId> levels, NamespaceId parentId, PublicAccount owner, BigInteger startHeight, BigInteger endHeight) {
        this.active = active;
        this.index = index;
        this.metaId = metaId;
        this.type = type;
        this.depth = depth;
        this.levels = levels;
        this.parentId = parentId;
        this.owner = owner;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
    }

    /**
     * Returns true if namespace is active
     *
     * @return true if the namespace is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the namespace expiration status
     *
     * @return true if namespace is expired
     */
    public boolean isExpired() {
        return !active;
    }

    public Integer getIndex() {
        return index;
    }

    public String getMetaId() {
        return metaId;
    }

    /**
     * Returns the namespace type
     *
     * @return the namespace type
     */
    public NamespaceType getType() {
        return type;
    }

    /**
     * Returns the namespace level depth
     *
     * @return the namespace level depth
     */
    public Integer getDepth() {
        return depth;
    }

    /**
     * Returns the different NamespaceIds per level
     *
     * @return the different Namespace IDs per level
     */
    public List<NamespaceId> getLevels() {
        return levels;
    }

    /**
     * Returns the mosaic owner account
     *
     * @return mosaic owner account
     */
    public PublicAccount getOwner() {
        return owner;
    }

    /**
     * Returns the block height the namespace was registered
     *
     * @return the block height the namespace was registered
     */
    public BigInteger getStartHeight() {
        return startHeight;
    }

    /**
     * Returns the block height the namespace expires if not renewed
     *
     * @return the block height the namespace expires
     */
    public BigInteger getEndHeight() {
        return endHeight;
    }

    /**
     * Returns the NamespaceId
     *
     * @return namespace id
     */
    public NamespaceId getId() {
        return this.levels.get(this.levels.size() - 1);
    }

    /**
     * Returns true if namespace is Root
     *
     * @return true if namespace is Root
     */
    public boolean isRoot() {
        return this.type == NamespaceType.RootNamespace;
    }

    /**
     * Returns true if namespace is Subnamespace
     *
     * @return true if namespace is Subnamespace
     */
    public boolean isSubnamespace() {
        return this.type == NamespaceType.SubNamespace;
    }

    /**
     * Returns the Parent Namespace Id
     *
     * @return the Parent Namespace Id
     * @throws Error if it is not a SubNamespace
     */
    public NamespaceId parentNamespaceId() {
        if (this.isRoot()) {
            throw new Error("Is A Root Namespace");
        }
        return this.parentId;
    }

}
