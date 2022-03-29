/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */

package io.proximax.sdk.model.account;

import java.util.List;
import java.util.stream.Collectors;

import io.proximax.sdk.gen.model.AccountNamesDTO;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceName;

public class AccountNames {
    private final Address address;
    private final List<NamespaceName> namespaceName;

    public AccountNames(Address address, List<NamespaceName> namespaceName) {
        this.address = address;
        this.namespaceName = namespaceName;

    }
    
    /**
     * Returns account address.
     *
     * @return {@link Address}
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns mosaics hold by the account.
     *
     * @return mosaics owned by the account
     */
    public List<NamespaceName> getNamespaceName() {
        return namespaceName;
    }

    public static AccountNames fromDto(AccountNamesDTO dto) {

        return new AccountNames(
                Address.createFromEncoded(dto.getAddress()),
                dto.getNames().stream().map(namespaceName -> new NamespaceName(
             new NamespaceId(namespaceName),namespaceName)).collect(Collectors.toList()));
    }
}
