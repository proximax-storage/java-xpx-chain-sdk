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

package io.proximax.sdk.infrastructure;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.infrastructure.QueryParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryParamsTest {

    @Test
    void shouldCreateQueryParamsViaCostructor() {
        QueryParams queryParams = new QueryParams(15, "5A2139FC71C1B9000147D624");
        assertTrue(15 == queryParams.getPageSize());
        assertEquals("5A2139FC71C1B9000147D624", queryParams.getId());
    }

    @Test
    void shouldChangePageSizeTo10WhenSettingNegativeValue() {
        QueryParams queryParams = new QueryParams(-1, "5A2139FC71C1B9000147D624");
        assertTrue(10 == queryParams.getPageSize());
    }

    @Test
    void shouldChangePageSizeTo10WhenSettingValue1000() {
        QueryParams queryParams = new QueryParams(1000, "5A2139FC71C1B9000147D624");
        assertTrue(10 == queryParams.getPageSize());
    }


    @Test
    void shouldGenerateCorrectQueryParamsUrlWhenIdNullAndPageSizeNull() {
        QueryParams queryParams = new QueryParams(null, null);
        assertEquals("?pageSize=10", queryParams.toUrl());
    }

    @Test
    void shouldGenerateCorrectQueryParamsUrlWhenIdEmptyStringAndPageSizeNull() {
        QueryParams queryParams = new QueryParams(null, "");
        assertEquals("?pageSize=10", queryParams.toUrl());
    }

    @Test
    void shouldGenerateCorrectQueryParamsUrlWhenIdNotNullAndPageSizeNull() {
        QueryParams queryParams = new QueryParams(15, null);
        assertEquals("?pageSize=15", queryParams.toUrl());
    }

    @Test
    void shouldGenerateCorrectQueryParamsUrlWhenIdNullAndPageSizeNotNull() {
        QueryParams queryParams = new QueryParams(null, "5A2139FC71C1B9000147D624");
        assertEquals("?pageSize=10&id=5A2139FC71C1B9000147D624", queryParams.toUrl());
    }

    @Test
    void shouldGenerateCorrectQueryParamUrlWhenIdNotNullAndPageSizeNotNull() {
        QueryParams queryParams = new QueryParams(15, "5A2139FC71C1B9000147D624");
        assertEquals("?pageSize=15&id=5A2139FC71C1B9000147D624", queryParams.toUrl());

    }
}
