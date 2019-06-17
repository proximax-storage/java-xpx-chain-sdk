/*
 * ProximaX Chain REST API Reference
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.13
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
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
import io.proximax.sdk.gen.model.AccountPropertyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountPropertiesDTO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-06-17T22:20:38.750+02:00")
public class AccountPropertiesDTO {
  @SerializedName("address")
  private String address = null;

  @SerializedName("properties")
  private List<AccountPropertyDTO> properties = new ArrayList<AccountPropertyDTO>();

  public AccountPropertiesDTO address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(example = "90D91AE6745822A053389598105AAEA846C4F332A9E3095F3E", required = true, value = "")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public AccountPropertiesDTO properties(List<AccountPropertyDTO> properties) {
    this.properties = properties;
    return this;
  }

  public AccountPropertiesDTO addPropertiesItem(AccountPropertyDTO propertiesItem) {
    this.properties.add(propertiesItem);
    return this;
  }

   /**
   * Get properties
   * @return properties
  **/
  @ApiModelProperty(required = true, value = "")
  public List<AccountPropertyDTO> getProperties() {
    return properties;
  }

  public void setProperties(List<AccountPropertyDTO> properties) {
    this.properties = properties;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountPropertiesDTO accountPropertiesDTO = (AccountPropertiesDTO) o;
    return Objects.equals(this.address, accountPropertiesDTO.address) &&
        Objects.equals(this.properties, accountPropertiesDTO.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, properties);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountPropertiesDTO {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

