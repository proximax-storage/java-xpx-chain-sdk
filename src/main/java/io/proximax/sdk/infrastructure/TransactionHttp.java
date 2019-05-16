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

import static io.proximax.sdk.utils.GsonUtils.getJsonArray;
import static io.proximax.sdk.utils.GsonUtils.getJsonPrimitive;
import static io.proximax.sdk.utils.GsonUtils.stream;
import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonObject;

import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.TransactionRepository;
import io.proximax.sdk.gen.model.TransactionStatusDTO;
import io.proximax.sdk.model.transaction.CosignatureSignedTransaction;
import io.proximax.sdk.model.transaction.Deadline;
import io.proximax.sdk.model.transaction.SignedTransaction;
import io.proximax.sdk.model.transaction.Transaction;
import io.proximax.sdk.model.transaction.TransactionAnnounceResponse;
import io.proximax.sdk.model.transaction.TransactionStatus;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.Observable;

/**
 * Transaction http repository.
 *
 * @since 1.0
 */
public class TransactionHttp extends Http implements TransactionRepository {
	private static final String ROUTE = "/transaction/";
   private static final String KEY_MESSAGE = "message";
   private static final String KEY_PAYLOAD = "payload";
	
    public TransactionHttp(BlockchainApi api) {
        super(api);
    }

    @Override
    public Observable<Transaction> getTransaction(String transactionHash) {
        return this.client
                .get(ROUTE + transactionHash)
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(new TransactionMapping());
    }

    @Override
    public Observable<List<Transaction>> getTransactions(List<String> transactionHashes) {

       JsonObject requestBody = new JsonObject();
       requestBody.add("transactionIds", getJsonArray(transactionHashes));
        return this.client
                .post(ROUTE, requestBody)
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonArray)
                .map(arr -> stream(arr).map(s -> (JsonObject) s).collect(Collectors.toList()))
                .flatMapIterable(item -> item)
                .map(new TransactionMapping())
                .toList()
                .toObservable();
    }


    @Override
    public Observable<TransactionStatus> getTransactionStatus(String transactionHash) {
        return this.client
                .get(ROUTE + transactionHash + "/status")
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.readValue(str, TransactionStatusDTO.class))
                .map(transactionStatusDTO -> new TransactionStatus(transactionStatusDTO.getGroup(),
                        transactionStatusDTO.getStatus(),
                        transactionStatusDTO.getHash(),
                        new Deadline(toBigInt(transactionStatusDTO.getDeadline())),
                        toBigInt(transactionStatusDTO.getHeight())));
    }

    @Override
    public Observable<List<TransactionStatus>> getTransactionStatuses(List<String> transactionHashes) {
        JsonObject requestBody = new JsonObject();
        requestBody.add("hashes", getJsonArray(transactionHashes));
        return this.client
                .post(ROUTE + "/statuses", requestBody)
                .map(Http::mapStringOrError)
                .map(str -> objectMapper.<List<TransactionStatusDTO>>readValue(str, new TypeReference<List<TransactionStatusDTO>>() {
                }))
                .flatMapIterable(item -> item)
                .map(transactionStatusDTO -> new TransactionStatus(transactionStatusDTO.getGroup(),
                        transactionStatusDTO.getStatus(),
                        transactionStatusDTO.getHash(),
                        new Deadline(toBigInt(transactionStatusDTO.getDeadline())),
                        toBigInt(transactionStatusDTO.getHeight())))
                .toList()
                .toObservable();
    }

    @Override
    public Observable<TransactionAnnounceResponse> announce(SignedTransaction signedTransaction) {
        JsonObject requestBody = new JsonObject();
        requestBody.add(KEY_PAYLOAD, getJsonPrimitive(signedTransaction.getPayload()));
        return this.client
                .put(ROUTE, requestBody)
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(json -> new TransactionAnnounceResponse(json.get(KEY_MESSAGE).getAsString()));
    }

    @Override
    public Observable<TransactionAnnounceResponse> announceAggregateBonded(SignedTransaction signedTransaction) {
        JsonObject requestBody = new JsonObject();
        requestBody.add(KEY_PAYLOAD, getJsonPrimitive(signedTransaction.getPayload()));
        return this.client
                .put(ROUTE + "/partial", requestBody)
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(json -> new TransactionAnnounceResponse(json.get(KEY_MESSAGE).getAsString()));
    }

    @Override
    public Observable<TransactionAnnounceResponse> announceAggregateBondedCosignature(CosignatureSignedTransaction cosignatureSignedTransaction) {
        JsonObject requestBody = new JsonObject();
        requestBody.add("parentHash", getJsonPrimitive(cosignatureSignedTransaction.getParentHash()));
        requestBody.add("signature", getJsonPrimitive(cosignatureSignedTransaction.getSignature()));
        requestBody.add("signer", getJsonPrimitive(cosignatureSignedTransaction.getSigner()));
        return this.client
                .put(ROUTE + "/cosignature", requestBody)
                .map(Http::mapStringOrError)
                .map(GsonUtils::mapToJsonObject)
                .map(json -> new TransactionAnnounceResponse(json.get(KEY_MESSAGE).getAsString()));
    }
}
