/*
 * Catapult REST API Reference
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.7.15
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.proximax.sdk.gen.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * InflationReceiptDTOAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class InflationReceiptDTOAllOf {
  public static final String SERIALIZED_NAME_MOSAIC_ID = "mosaicId";
  @SerializedName(SERIALIZED_NAME_MOSAIC_ID)
  private UInt64DTO mosaicId = new UInt64DTO();

  public static final String SERIALIZED_NAME_AMOUNT = "amount";
  @SerializedName(SERIALIZED_NAME_AMOUNT)
  private UInt64DTO amount = new UInt64DTO();

  public InflationReceiptDTOAllOf mosaicId(UInt64DTO mosaicId) {
    this.mosaicId = mosaicId;
    return this;
  }

   /**
   * Get mosaicId
   * @return mosaicId
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getMosaicId() {
    return mosaicId;
  }

  public void setMosaicId(UInt64DTO mosaicId) {
    this.mosaicId = mosaicId;
  }

  public InflationReceiptDTOAllOf amount(UInt64DTO amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Get amount
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getAmount() {
    return amount;
  }

  public void setAmount(UInt64DTO amount) {
    this.amount = amount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InflationReceiptDTOAllOf inflationReceiptDTOAllOf = (InflationReceiptDTOAllOf) o;
    return Objects.equals(this.mosaicId, inflationReceiptDTOAllOf.mosaicId) &&
        Objects.equals(this.amount, inflationReceiptDTOAllOf.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mosaicId, amount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InflationReceiptDTOAllOf {\n");
    sb.append("    mosaicId: ").append(toIndentedString(mosaicId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

