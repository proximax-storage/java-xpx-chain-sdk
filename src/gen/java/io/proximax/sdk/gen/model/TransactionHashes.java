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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TransactionHashes
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class TransactionHashes {
  public static final String SERIALIZED_NAME_HASHES = "hashes";
  @SerializedName(SERIALIZED_NAME_HASHES)
  private List<String> hashes = new ArrayList<>();

  public TransactionHashes hashes(List<String> hashes) {
    this.hashes = hashes;
    return this;
  }

  public TransactionHashes addHashesItem(String hashesItem) {
    if (this.hashes == null) {
      this.hashes = new ArrayList<>();
    }
    this.hashes.add(hashesItem);
    return this;
  }

   /**
   * The array of transaction hashes.
   * @return hashes
  **/
  @ApiModelProperty(example = "[\"5AF72FE39C0515E823903918A0BFE9625DD752C7BD63969C8869F25E9D155DE5\"]", value = "The array of transaction hashes.")
  public List<String> getHashes() {
    return hashes;
  }

  public void setHashes(List<String> hashes) {
    this.hashes = hashes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionHashes transactionHashes = (TransactionHashes) o;
    return Objects.equals(this.hashes, transactionHashes.hashes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashes);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionHashes {\n");
    sb.append("    hashes: ").append(toIndentedString(hashes)).append("\n");
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

