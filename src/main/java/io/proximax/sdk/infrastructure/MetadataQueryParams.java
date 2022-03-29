package io.proximax.sdk.infrastructure;

import io.proximax.sdk.infrastructure.QueryParams.Order;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;

public class MetadataQueryParams {

    public static final Integer PAGE_SIZE_MIN = 10;
    public static final Integer PAGE_SIZE_MAX = 100;
    public static final Integer PAGE_SIZE_DEFAULT = 20;

    private Integer pageSize;
    private Integer pageNumber;
    private final Order order;
    private final PublicAccount targetKey;
    private final String targetId;
    private final String scopedMetadataKey;
    private final Address sourceAddress;
    private final MetadataSortingField sortField;

    /**
     * create new query parameter instance
     * 
     * @param pageSize          page size between 10 and 100
     * @param pageNumber        page number from 1
     * @param order             ascending or descending order of the sortfield
     * @param sortField         sort data with field
     * @param targetKey         target key of the metadata (public key)
     * @param targetId          namespaceId/mosaidId of the for mosaic/namespace
     *                          metadata
     * @param scopedMetadataKey scoped metadata key representing with Hex string
     * @param sourceAddress     source address representing with address
     */
    public MetadataQueryParams(Integer pageSize, Integer pageNumber, Order order, MetadataSortingField sortField,
            PublicAccount targetKey, String targetId,
            String scopedMetadataKey, Address sourceAddress) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.order = order;
        this.sortField = sortField;
        this.targetKey = targetKey;
        this.targetId = targetId;
        this.scopedMetadataKey = scopedMetadataKey;
        this.sourceAddress = sourceAddress;
    }

    /**
     * Returns page size between 10 and 100, otherwise 20.
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
     * Returns sort field after which we want objects to be returned.
     * 
     * @return object sort field
     */
    public MetadataSortingField getSortField() {

        return sortField;
    }

    /**
     * Returns order after which we want objects to be returned.
     * 
     * @return object order
     */
    public Order getOrder() {

        return order;
    }

    /**
     * Returns target key after which we want objects to be returned.
     *
     * @return object target key
     */
    public PublicAccount getTargetKey() {
        return targetKey;
    }

    /**
     * Returns target Id after which we want objects to be returned.
     *
     * @return object target Id
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * Returns scoped metadata key after which we want objects to be returned.
     *
     * @return object scoped metadata key
     */
    public String getScopedMetadataKey() {
        return scopedMetadataKey;
    }

    /**
     * Returns source address after which we want objects to be returned.
     *
     * @return object source address
     */
    public Address getSourceAddress() {
        return (Address) sourceAddress;
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
                + (targetKey != null ? "&targetKey=" + targetKey.getPublicKey() : "") + (targetId != null
                        ? "&targetId=" + targetId
                        : "")
                + (scopedMetadataKey != null
                        ? "&scopedMetadataKey=" + scopedMetadataKey
                        : "")
                + (sourceAddress != null
                        ? "&sourceAddress=" + sourceAddress.plain()
                        : "");

        query = query.replace("?&", "?");
        return query;
    }
}
