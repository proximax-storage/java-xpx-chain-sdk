/*
 * Copyright 2022 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

public class Pagination {
    private final int totalEntries;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    public Pagination(int totalEntries, int pageNumber, int pageSize, int totalPages)
    {
        this.totalEntries = totalEntries;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    /**
     * Returns total entries.
     *
     * @return int
     */
    public int getTotalEntries() {
        return totalEntries;
    }
    
    /**
     * Returns page number.
     *
     * @return int
     */
    public int getPageNumber() {
        return pageNumber;
    }
    
    /**
     * Returns page size.
     *
     * @return int
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /**
     * Returns total pages.
     *
     * @return int
     */
    public int getTotalPages() {
        return totalPages;
    }
}
