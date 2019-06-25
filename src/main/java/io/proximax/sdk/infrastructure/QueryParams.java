/*
 * Copyright 2018 NEM
 * Copyright 2019 ProximaX
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

import org.apache.commons.lang3.Validate;

/**
 * The query params structure describes pagination params for requests.
 */
public class QueryParams {
   public static final Integer PAGE_SIZE_MIN = 10;
   public static final Integer PAGE_SIZE_MAX = 100;
   public static final Integer PAGE_SIZE_DEFAULT = 10;

   private final Integer pageSize;
   private final String id;

   /**
    * create new query parameter specifying only page size
    * 
    * @param pageSize page size between 10 and 100
    */
   public QueryParams(Integer pageSize) {
      this(pageSize, null);
   }
   
   /**
    * create new query parameter instance with page size defaulted to 10
    * 
    * @param id id of the object to start from
    */
   public QueryParams(String id) {
      this(PAGE_SIZE_DEFAULT, id);
      Validate.notNull(id, "ID is mandatory when only ID is to be specified");
   }

   /**
    * create new query parameter instance
    * 
    * @param pageSize page size between 10 and 100
    * @param id id of the object to start from
    */
   public QueryParams(Integer pageSize, String id) {
      Validate.notNull(pageSize, "Page size is mandatory");
      Validate.inclusiveBetween(PAGE_SIZE_MIN, PAGE_SIZE_MAX, pageSize.longValue(), "Page size needs to be between 10 and 100 inclusive");
      this.pageSize = pageSize;
      this.id = id;
   }

   /**
    * Returns id after which we want objects to be returned.
    *
    * @return object id
    */
   public String getId() {
      return id;
   }

   /**
    * Returns page size between 10 and 100, otherwise 10.
    *
    * @return page size
    */
   public Integer getPageSize() {
      return pageSize;
   }

   /**
    * Converts query params into url params.
    *
    * @return params in url format
    */
   public String toUrl() {
      return "?pageSize=" + pageSize + (id != null ? "&id=" + id : "");
   }
}
