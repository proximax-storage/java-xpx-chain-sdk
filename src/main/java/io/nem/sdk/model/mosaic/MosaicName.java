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

import io.nem.sdk.model.namespace.NamespaceId;

/**
 * The mosaic name info structure describes basic information of a mosaic and name.
 *
 * @since 1.0
 */
public class MosaicName {
    private final MosaicId mosaicId;
    private final String name;
    private final NamespaceId parentId;

    /**
     * @param mosaicId mosaic identifier
     * @param name     mosaic name
     * @param parentId namespace identifier
     */
    public MosaicName(MosaicId mosaicId, String name, NamespaceId parentId) {
        this.mosaicId = mosaicId;
        this.name = name;
        this.parentId = parentId;
    }

    /**
     * Returns mosaic identifier
     *
     * @return mosaic identifier
     */
    public MosaicId getMosaicId() {
        return mosaicId;
    }

    /**
     * Returns mosaic name
     *
     * @return mosaic name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns namespace identifier it belongs to
     *
     * @return namespace identifier
     */
    public NamespaceId getParentId() {
        return parentId;
    }

	@Override
	public String toString() {
		return "MosaicName [mosaicId=" + mosaicId + ", name=" + name + ", parentId=" + parentId + "]";
	}
}
