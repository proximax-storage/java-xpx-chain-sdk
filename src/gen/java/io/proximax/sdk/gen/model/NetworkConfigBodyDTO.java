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
 * NetworkConfigBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class NetworkConfigBodyDTO {
  public static final String SERIALIZED_NAME_APPLY_HEIGHT_DELTA = "applyHeightDelta";
  @SerializedName(SERIALIZED_NAME_APPLY_HEIGHT_DELTA)
  private UInt64DTO applyHeightDelta = new UInt64DTO();

  public static final String SERIALIZED_NAME_NETWORK_CONFIG = "networkConfig";
  @SerializedName(SERIALIZED_NAME_NETWORK_CONFIG)
  private String networkConfig;

  public static final String SERIALIZED_NAME_SUPPORTED_ENTITY_VERSIONS = "supportedEntityVersions";
  @SerializedName(SERIALIZED_NAME_SUPPORTED_ENTITY_VERSIONS)
  private String supportedEntityVersions;

  public NetworkConfigBodyDTO applyHeightDelta(UInt64DTO applyHeightDelta) {
    this.applyHeightDelta = applyHeightDelta;
    return this;
  }

   /**
   * Get applyHeightDelta
   * @return applyHeightDelta
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getApplyHeightDelta() {
    return applyHeightDelta;
  }

  public void setApplyHeightDelta(UInt64DTO applyHeightDelta) {
    this.applyHeightDelta = applyHeightDelta;
  }

  public NetworkConfigBodyDTO networkConfig(String networkConfig) {
    this.networkConfig = networkConfig;
    return this;
  }

   /**
   * Network config like a raw text.
   * @return networkConfig
  **/
  @ApiModelProperty(required = true, value = "Network config like a raw text.")
  public String getNetworkConfig() {
    return networkConfig;
  }

  public void setNetworkConfig(String networkConfig) {
    this.networkConfig = networkConfig;
  }

  public NetworkConfigBodyDTO supportedEntityVersions(String supportedEntityVersions) {
    this.supportedEntityVersions = supportedEntityVersions;
    return this;
  }

   /**
   * Allowed versions of transaction in json format.
   * @return supportedEntityVersions
  **/
  @ApiModelProperty(required = true, value = "Allowed versions of transaction in json format.")
  public String getSupportedEntityVersions() {
    return supportedEntityVersions;
  }

  public void setSupportedEntityVersions(String supportedEntityVersions) {
    this.supportedEntityVersions = supportedEntityVersions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkConfigBodyDTO networkConfigBodyDTO = (NetworkConfigBodyDTO) o;
    return Objects.equals(this.applyHeightDelta, networkConfigBodyDTO.applyHeightDelta) &&
        Objects.equals(this.networkConfig, networkConfigBodyDTO.networkConfig) &&
        Objects.equals(this.supportedEntityVersions, networkConfigBodyDTO.supportedEntityVersions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applyHeightDelta, networkConfig, supportedEntityVersions);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkConfigBodyDTO {\n");
    sb.append("    applyHeightDelta: ").append(toIndentedString(applyHeightDelta)).append("\n");
    sb.append("    networkConfig: ").append(toIndentedString(networkConfig)).append("\n");
    sb.append("    supportedEntityVersions: ").append(toIndentedString(supportedEntityVersions)).append("\n");
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

