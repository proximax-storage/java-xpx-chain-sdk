package io.proximax.sdk.model.transaction;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.sdk.infrastructure.TransactionMapping;

public class TransactionList {

    private final List<Transaction> transactions;

    public TransactionList(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    /**
     * Returns list of transaction
     * 
     * @return List of {@link Pagination}
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public static TransactionList fromJson(JsonArray input) {
        // JsonArray data = input.getAsJsonArray("data");
        // JsonObject pagination = input.getAsJsonObject("pagination");
        List<Transaction> trx = new ArrayList<>();

        // if (data != null) {
        for (int i = 0; i < input.size(); i++) {
            JsonObject transaction = input.get(i).getAsJsonObject();
            JsonObject Trx = transaction.getAsJsonObject("transaction").getAsJsonObject();
            if (Trx != null) {
                trx.add(new TransactionMapping().apply(transaction));
            }
        }

        return new TransactionList(trx);
    }
}
