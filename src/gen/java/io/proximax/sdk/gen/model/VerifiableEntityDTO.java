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
 * VerifiableEntityDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class VerifiableEntityDTO {
  public static final String SERIALIZED_NAME_SIGNATURE = "signature";
  @SerializedName(SERIALIZED_NAME_SIGNATURE)
  private String signature;

  public VerifiableEntityDTO signature(String signature) {
    this.signature = signature;
    return this;
  }

   /**
   * The signature of the entity. The signature was generated by the signer and can be used to validate tha the entity data was not modified by a node. 
   * @return signature
  **/
  @ApiModelProperty(example = "D8047EB8285077D9900EDD42F4081070DDB26E08E7F15A3E29642C80CF0C7D68340682DC9868C73EB09744D6298146D8DFC02ED47070FC81FB47D6F73B33EF0A", required = true, value = "The signature of the entity. The signature was generated by the signer and can be used to validate tha the entity data was not modified by a node. ")
  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerifiableEntityDTO verifiableEntityDTO = (VerifiableEntityDTO) o;
    return Objects.equals(this.signature, verifiableEntityDTO.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signature);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerifiableEntityDTO {\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
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

