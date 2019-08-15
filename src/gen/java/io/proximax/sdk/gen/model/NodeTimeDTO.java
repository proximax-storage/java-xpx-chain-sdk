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
import io.proximax.sdk.gen.model.CommunicationTimestamps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * NodeTimeDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-08-11T15:04:35.591+02:00[Europe/Prague]")
public class NodeTimeDTO {
  public static final String SERIALIZED_NAME_COMMUNICATION_TIMESTAMPS = "communicationTimestamps";
  @SerializedName(SERIALIZED_NAME_COMMUNICATION_TIMESTAMPS)
  private CommunicationTimestamps communicationTimestamps = null;

  public NodeTimeDTO communicationTimestamps(CommunicationTimestamps communicationTimestamps) {
    this.communicationTimestamps = communicationTimestamps;
    return this;
  }

   /**
   * Get communicationTimestamps
   * @return communicationTimestamps
  **/
  @ApiModelProperty(required = true, value = "")
  public CommunicationTimestamps getCommunicationTimestamps() {
    return communicationTimestamps;
  }

  public void setCommunicationTimestamps(CommunicationTimestamps communicationTimestamps) {
    this.communicationTimestamps = communicationTimestamps;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NodeTimeDTO nodeTimeDTO = (NodeTimeDTO) o;
    return Objects.equals(this.communicationTimestamps, nodeTimeDTO.communicationTimestamps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(communicationTimestamps);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NodeTimeDTO {\n");
    sb.append("    communicationTimestamps: ").append(toIndentedString(communicationTimestamps)).append("\n");
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

