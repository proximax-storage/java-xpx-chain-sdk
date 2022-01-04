package io.proximax.sdk.gen.model;

import com.google.gson.annotations.SerializedName;

import io.proximax.sdk.model.transaction.Pagination;
import io.swagger.annotations.ApiModelProperty;

@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class TransactionSearchDTO {
    public static final String SERIALIZED_NAME_DATA = "data";
    @SerializedName(SERIALIZED_NAME_DATA)
    private TransactionInfoDTO data = null;

    public static final String SERIALIZED_NAME_PAGINATION = "pagination";
    @SerializedName(SERIALIZED_NAME_PAGINATION)
    private Pagination pagination;

    public TransactionSearchDTO data(TransactionInfoDTO data) {
        this.data = data;
        return this;
    }

    @ApiModelProperty(required = true, value = "")
    public TransactionInfoDTO getData() {
        return data;
    }

    public void setData(TransactionInfoDTO data) {
        this.data = data;
    }

    public TransactionSearchDTO pagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    @ApiModelProperty(required = true, value = "")
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
