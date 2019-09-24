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
import io.proximax.sdk.gen.model.AccountPropertiesDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * AccountPropertiesInfoDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class AccountPropertiesInfoDTO {
  public static final String SERIALIZED_NAME_ACCOUNT_PROPERTIES = "accountProperties";
  @SerializedName(SERIALIZED_NAME_ACCOUNT_PROPERTIES)
  private AccountPropertiesDTO accountProperties = null;

  public AccountPropertiesInfoDTO accountProperties(AccountPropertiesDTO accountProperties) {
    this.accountProperties = accountProperties;
    return this;
  }

   /**
   * Get accountProperties
   * @return accountProperties
  **/
  @ApiModelProperty(required = true, value = "")
  public AccountPropertiesDTO getAccountProperties() {
    return accountProperties;
  }

  public void setAccountProperties(AccountPropertiesDTO accountProperties) {
    this.accountProperties = accountProperties;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountPropertiesInfoDTO accountPropertiesInfoDTO = (AccountPropertiesInfoDTO) o;
    return Objects.equals(this.accountProperties, accountPropertiesInfoDTO.accountProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountProperties);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountPropertiesInfoDTO {\n");
    sb.append("    accountProperties: ").append(toIndentedString(accountProperties)).append("\n");
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

