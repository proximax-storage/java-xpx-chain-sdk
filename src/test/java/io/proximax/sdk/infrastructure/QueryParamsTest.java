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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class QueryParamsTest {
   private static final String ID = "5A2139FC71C1B9000147D624";
    @Test
    void shouldCreateQueryParamsViaCostructor() {
        QueryParams queryParams = new QueryParams(15, ID);
        assertEquals(15, queryParams.getPageSize());
        assertEquals(ID, queryParams.getId());
    }

    @Test
    void urlIsOK() {
       assertEquals("?pageSize=10&id="+ID, new QueryParams(ID).toUrl());
       assertEquals("?pageSize=15", new QueryParams(15).toUrl());
       assertEquals("?pageSize=15&id="+ID, new QueryParams(15, ID).toUrl());
    }
}
