package io.proximax.sdk.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.transaction.Transaction;
import io.reactivex.functions.Function;

public class TransactionAggregateMapping implements Function<JsonObject, List<Transaction>> {
    public List<Transaction> apply(JsonObject input) {
        JsonArray data = input.getAsJsonArray("data");
       List<Transaction> transactions = new ArrayList<>();

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                JsonObject transaction = data.get(i).getAsJsonObject();
                JsonObject Trx = transaction.getAsJsonObject("transaction");
                if (Trx != null) {
                    transactions.add(new TransactionMapping().apply(transaction));
                }
            }
        }
        return transactions;
    }
}
