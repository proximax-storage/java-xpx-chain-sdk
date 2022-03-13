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

package io.proximax.sdk.model.namespace;

import io.proximax.sdk.gen.model.NamespaceNameDTO;
import io.proximax.sdk.utils.dto.UInt64Utils;

import java.util.ArrayList;

/**
 * The namespace name info structure describes basic information of a namespace and name.
 */
public class NamespaceName {
    private final NamespaceId namespaceId;
    private final String name;

    /**
     * create new namespace name descriptor
     * 
     * @param namespaceId the ID of the namespace
     * @param name the name of the namespace
     */
    public NamespaceName(NamespaceId namespaceId, String name) {
        this.namespaceId = namespaceId;
        this.name = name;
    }

    /**
     * Returns the namespace id
     *
     * @return the namespace id
     */
    public NamespaceId getNamespaceId() {
        return namespaceId;
    }

    /**
     * Returns the namespace name
     *
     * @return the namespace name
     */
    public String getName() {
        return name;
    }

    /**
     * create new instance from the DTO
     * 
     * @param dto the dto of namespace name
     * @return new instance
     */
    public static NamespaceName fromDto(NamespaceNameDTO dto) {
       return new NamespaceName(
             new NamespaceId(UInt64Utils.toBigInt(new ArrayList<>(dto.getNamespaceId()))),
             dto.getName());
    }
}
