/*
 * Copyright 2022 ProximaX
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

package io.proximax.sdk.infrastructure;

import io.proximax.sdk.infrastructure.QueryParams.Order;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.transaction.EntityType;

public class TransactionQueryParams {
    public static final Integer PAGE_SIZE_MIN = 10;
    public static final Integer PAGE_SIZE_MAX = 100;
    public static final Integer PAGE_SIZE_DEFAULT = 20;

    private Integer pageSize;
    private Integer pageNumber;
    private final Order order;
    private final EntityType type;
    private final String embedded;
    private final TransactionSortingField sortField;
    private final Integer toHeight;
    private final Integer fromHeight;
    private final Integer height;
    private final PublicAccount signerPublicKey;
    private final Address recipientAddress;
    private final Address address;

    /**
     * create new query parameter instance
     * 
     * @param pageSize         page size between 10 and 100
     * @param pageNumber       page number from 1
     * @param order            ascending or descending order of the sortfield
     * @param embedded         true/false
     * @param sortField        sort data with field should be work with order param
     * @param toHeight         to height
     * @param fromHeight       from height
     * @param height           height
     * @param signerPublicKey  signer public key
     * @param recipientAddress recipient address
     * @param address          address
     * 
     */
    public TransactionQueryParams(Integer pageSize, Integer pageNumber, Order order,
            EntityType type,
            String embedded, TransactionSortingField sortField, Integer toHeight, Integer fromHeight, Integer height,
            PublicAccount signerPublicKey, Address recipientAddress, Address address) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.order = order;
        this.sortField = sortField;
        this.type = type;
        this.embedded = embedded;
        this.toHeight = toHeight;
        this.fromHeight = fromHeight;
        this.height = height;
        this.signerPublicKey = signerPublicKey;
        this.recipientAddress = recipientAddress;
        this.address = address;
    }

    /**
     * Returns page size between 10 and 100, otherwise 10.
     *
     * @return page size
     */
    public Integer getPageSize() {
        if (pageSize <= 0) {
            pageSize = PAGE_SIZE_DEFAULT;
        } else if (pageSize < 10) {
            pageSize = PAGE_SIZE_MIN;
        } else if (pageSize > 100) {
            pageSize = PAGE_SIZE_MAX;
        }
        return pageSize;
    }

    /**
     * Returns page number.
     *
     * @return page number
     */
    public Integer getPageNumber() {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }
        return pageNumber;
    }

    /**
     * Returns order
     * 
     * @return object order
     */
    public Order getOrder() {

        return order;
    }

    /**
     * Returns entity type
     *
     * @return object type
     */
    public EntityType getType() {
        return type;
    }

    /**
     * Returns embedded
     * 
     * @return object embedded
     */
    public String getEmbedded() {
        return embedded;
    }

    /**
     * Returns sort field
     * 
     * @return object sort field
     */
    public TransactionSortingField getSortField() {

        return sortField;
    }

    /**
     * Returns to height
     * 
     * @return object to height
     */
    public Integer getToHeight() {
        return toHeight;
    }

    /**
     * Returns from height
     * 
     * @return object from height
     */
    public Integer getFromHeight() {
        return fromHeight;
    }

    /**
     * Returns height
     * 
     * @return object height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Returns signer publicKey from provided public account
     * 
     * @return object signer public key
     */
    public PublicAccount getSignerPublicKey() {
        return signerPublicKey;
    }

    /**
     * Returns recipient address
     * 
     * @return object recipient address
     */
    public Address getRecipientAddress() {
        return recipientAddress;
    }

    /**
     * Returns address
     * 
     * @return object address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Converts query params into url params.
     *
     * @return params in url format
     */
    public String toUrl() {
        String query = "?" + (pageSize != null ? "&pageSize=" + getPageSize() : "")
                + (pageNumber != null ? "&pageNumber=" + getPageNumber() : "")
                + (order != null ? (order == Order.ASC ? "&order=asc"
                        : "&order=desc") : "")
                + (sortField != null ? "&sortField=" + sortField.getValue() : "")
                + (type != null ? "&type[]=" + type.getValue() : "") + (embedded != null ? "&embedded=" + embedded : "")
                + (toHeight != null ? "&toHeight=" + toHeight : "")
                + (fromHeight != null ? "&fromHeight=" + fromHeight : "") + (height != null ? "&height=" + height : "")
                + (signerPublicKey != null ? "&signerPublicKey=" + signerPublicKey.getPublicKey() : "")
                + (recipientAddress != null ? "&recipientAddress=" + recipientAddress.plain() : "")
                + (address != null ? "&address=" + address.plain() : "");
        query = query.replace("?&", "?");
        return query;
    }
}
