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

/**
 * StorageInfoDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-08-11T15:04:35.591+02:00[Europe/Prague]")
public class StorageInfoDTO {
  public static final String SERIALIZED_NAME_NUM_BLOCKS = "numBlocks";
  @SerializedName(SERIALIZED_NAME_NUM_BLOCKS)
  private Integer numBlocks;

  public static final String SERIALIZED_NAME_NUM_TRANSACTIONS = "numTransactions";
  @SerializedName(SERIALIZED_NAME_NUM_TRANSACTIONS)
  private Integer numTransactions;

  public static final String SERIALIZED_NAME_NUM_ACCOUNTS = "numAccounts";
  @SerializedName(SERIALIZED_NAME_NUM_ACCOUNTS)
  private Integer numAccounts;

  public StorageInfoDTO numBlocks(Integer numBlocks) {
    this.numBlocks = numBlocks;
    return this;
  }

   /**
   * The number of blocks stored.
   * @return numBlocks
  **/
  @ApiModelProperty(example = "245053", required = true, value = "The number of blocks stored.")
  public Integer getNumBlocks() {
    return numBlocks;
  }

  public void setNumBlocks(Integer numBlocks) {
    this.numBlocks = numBlocks;
  }

  public StorageInfoDTO numTransactions(Integer numTransactions) {
    this.numTransactions = numTransactions;
    return this;
  }

   /**
   * The number of transactions stored.
   * @return numTransactions
  **/
  @ApiModelProperty(example = "58590", required = true, value = "The number of transactions stored.")
  public Integer getNumTransactions() {
    return numTransactions;
  }

  public void setNumTransactions(Integer numTransactions) {
    this.numTransactions = numTransactions;
  }

  public StorageInfoDTO numAccounts(Integer numAccounts) {
    this.numAccounts = numAccounts;
    return this;
  }

   /**
   * The number of accounts created.
   * @return numAccounts
  **/
  @ApiModelProperty(example = "177", required = true, value = "The number of accounts created.")
  public Integer getNumAccounts() {
    return numAccounts;
  }

  public void setNumAccounts(Integer numAccounts) {
    this.numAccounts = numAccounts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StorageInfoDTO storageInfoDTO = (StorageInfoDTO) o;
    return Objects.equals(this.numBlocks, storageInfoDTO.numBlocks) &&
        Objects.equals(this.numTransactions, storageInfoDTO.numTransactions) &&
        Objects.equals(this.numAccounts, storageInfoDTO.numAccounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numBlocks, numTransactions, numAccounts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StorageInfoDTO {\n");
    sb.append("    numBlocks: ").append(toIndentedString(numBlocks)).append("\n");
    sb.append("    numTransactions: ").append(toIndentedString(numTransactions)).append("\n");
    sb.append("    numAccounts: ").append(toIndentedString(numAccounts)).append("\n");
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
