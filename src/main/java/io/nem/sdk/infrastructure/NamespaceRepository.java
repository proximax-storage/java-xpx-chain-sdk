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

package io.nem.sdk.infrastructure;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceInfo;
import io.nem.sdk.model.namespace.NamespaceName;
import io.reactivex.Observable;

import java.util.List;

/**
 * Namespace interface repository.
 *
 * @since 1.0
 */
public interface NamespaceRepository {

    /**
     * Gets the NamespaceInfo for a given namespaceId.
     *
     * @param namespaceId NamespaceId
     * @return Observable of {@link NamespaceInfo}
     */
    Observable<NamespaceInfo> getNamespace(NamespaceId namespaceId);

    /**
     * Gets list of NamespaceInfo for an account.
     * With pagination.
     *
     * @param address Address
     * @param queryParams   QueryParans
     * @return Observable of List of {@link NamespaceInfo}
     */
    Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address, QueryParams queryParams);

    /**
     * Gets list of NamespaceInfo for an account.
     *
     * @param address Address
     * @return Observable of List of {@link NamespaceInfo}
     */
    Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address);

    /**
     * Gets list of NamespaceInfo for different account.
     * With pagination.
     *
     * @param addresses List of Address
     * @param queryParams    QueryParams
     * @return Observable of List of {@link NamespaceInfo}
     */
    Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses, QueryParams queryParams);

    /**
     * Gets list of NamespaceInfo for different account.
     *
     * @param addresses List of Address
     * @return Observable of List of {@link NamespaceInfo}
     */
    Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses);

    /**
     * Gets list of NamespaceName for different namespaceIds.
     *
     * @param namespaceIds List of NamespaceId
     * @return Observable of List of {@link NamespaceName}
     */
    Observable<List<NamespaceName>> getNamespaceNames(List<NamespaceId> namespaceIds);
}
