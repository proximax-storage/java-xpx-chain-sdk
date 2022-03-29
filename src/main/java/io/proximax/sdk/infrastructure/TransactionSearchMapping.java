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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.transaction.Pagination;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionSearch;
import io.reactivex.functions.Function;

public class TransactionSearchMapping implements Function<JsonObject, TransactionSearch> {
    public TransactionSearch apply(JsonObject input) {
        JsonArray data = input.getAsJsonArray("data");
        JsonObject pagination = input.getAsJsonObject("pagination");

        int totalEntries = pagination.get("totalEntries").getAsInt();
        int pageNumber = pagination.get("pageNumber").getAsInt();
        int pageSize = pagination.get("pageSize").getAsInt();
        int totalPages = pagination.get("totalPages").getAsInt();

        List<Transaction> transactions = new ArrayList<>();

        if(data!=null){
           for (int i = 0; i < data.size(); i++) {
            JsonObject transaction = data.get(i).getAsJsonObject();
            JsonObject Trx = transaction.getAsJsonObject("transaction").getAsJsonObject();
                if(Trx!=null){
                    transactions.add(new TransactionMapping().apply(transaction));
                }
            }
        }
        return new TransactionSearch(transactions, new Pagination(totalEntries, pageNumber, pageSize, totalPages));
      }
}
