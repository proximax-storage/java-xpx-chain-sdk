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

package io.nem.sdk.infrastructure;

import static io.nem.sdk.infrastructure.utils.UInt64Utils.toBigInt;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.nem.sdk.infrastructure.model.TransactionStatusDTO;
import io.nem.sdk.model.transaction.CosignatureSignedTransaction;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransactionAnnounceResponse;
import io.nem.sdk.model.transaction.TransactionStatus;
import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/**
 * Transaction http repository.
 *
 * @since 1.0
 */
public class TransactionHttp extends Http implements TransactionRepository {
	/** JSON key containing the message */
	private static final String KEY_MESSAGE = "message";
	
    public TransactionHttp(String host) throws MalformedURLException {
        this(host + "/transaction/", new NetworkHttp(host));
    }

    public TransactionHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<Transaction> getTransaction(String transactionHash) {
        return this.client
                .getAbs(this.url + transactionHash)
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> new JsonObject(json.toString()))
                .map(new TransactionMapping());
    }

    @Override
    public Observable<List<Transaction>> getTransactions(List<String> transactionHashes) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("transactionIds", transactionHashes);
        return this.client
                .postAbs(this.url.toString())
                .as(BodyCodec.jsonArray())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> new JsonArray(json.toString()).stream().map(s -> (JsonObject) s).collect(Collectors.toList()))
                .flatMapIterable(item -> item)
                .map(new TransactionMapping())
                .toList()
                .toObservable();
    }


    @Override
    public Observable<TransactionStatus> getTransactionStatus(String transactionHash) {
        return this.client
                .getAbs(this.url + transactionHash + "/status")
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> objectMapper.readValue(json.toString(), TransactionStatusDTO.class))
                .map(transactionStatusDTO -> new TransactionStatus(transactionStatusDTO.getGroup(),
                        transactionStatusDTO.getStatus(),
                        transactionStatusDTO.getHash(),
                        new Deadline(toBigInt(transactionStatusDTO.getDeadline())),
                        toBigInt(transactionStatusDTO.getHeight())));
    }

    @Override
    public Observable<List<TransactionStatus>> getTransactionStatuses(List<String> transactionHashes) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("hashes", transactionHashes);
        return this.client
                .postAbs(this.url + "/statuses")
                .as(BodyCodec.jsonArray())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> objectMapper.<List<TransactionStatusDTO>>readValue(json.toString(), new TypeReference<List<TransactionStatusDTO>>() {
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
        requestBody.put("payload", signedTransaction.getPayload());
        return this.client
                .putAbs(this.url.toString())
                .as(BodyCodec.jsonObject())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> new TransactionAnnounceResponse(new JsonObject(json.toString()).getString(KEY_MESSAGE)));
    }

    @Override
    public Observable<TransactionAnnounceResponse> announceAggregateBonded(SignedTransaction signedTransaction) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("payload", signedTransaction.getPayload());
        return this.client
                .putAbs(this.url + "/partial")
                .as(BodyCodec.jsonObject())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> new TransactionAnnounceResponse(new JsonObject(json.toString()).getString(KEY_MESSAGE)));
    }

    @Override
    public Observable<TransactionAnnounceResponse> announceAggregateBondedCosignature(CosignatureSignedTransaction cosignatureSignedTransaction) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("parentHash", cosignatureSignedTransaction.getParentHash());
        requestBody.put("signature", cosignatureSignedTransaction.getSignature());
        requestBody.put("signer", cosignatureSignedTransaction.getSigner());
        return this.client
                .putAbs(this.url + "/cosignature")
                .as(BodyCodec.jsonObject())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> new TransactionAnnounceResponse(new JsonObject(json.toString()).getString(KEY_MESSAGE)));
    }
}
