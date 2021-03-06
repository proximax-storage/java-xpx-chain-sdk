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
 * MosaicMetadataDTOAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class MosaicMetadataDTOAllOf {
  public static final String SERIALIZED_NAME_METADATA_TYPE = "metadataType";
  @SerializedName(SERIALIZED_NAME_METADATA_TYPE)
  private Integer metadataType;

  public static final String SERIALIZED_NAME_METADATA_ID = "metadataId";
  @SerializedName(SERIALIZED_NAME_METADATA_ID)
  private UInt64DTO metadataId = new UInt64DTO();

  public MosaicMetadataDTOAllOf metadataType(Integer metadataType) {
    this.metadataType = metadataType;
    return this;
  }

   /**
   * Get metadataType
   * @return metadataType
  **/
  @ApiModelProperty(example = "2", value = "")
  public Integer getMetadataType() {
    return metadataType;
  }

  public void setMetadataType(Integer metadataType) {
    this.metadataType = metadataType;
  }

  public MosaicMetadataDTOAllOf metadataId(UInt64DTO metadataId) {
    this.metadataId = metadataId;
    return this;
  }

   /**
   * Get metadataId
   * @return metadataId
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getMetadataId() {
    return metadataId;
  }

  public void setMetadataId(UInt64DTO metadataId) {
    this.metadataId = metadataId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MosaicMetadataDTOAllOf mosaicMetadataDTOAllOf = (MosaicMetadataDTOAllOf) o;
    return Objects.equals(this.metadataType, mosaicMetadataDTOAllOf.metadataType) &&
        Objects.equals(this.metadataId, mosaicMetadataDTOAllOf.metadataId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadataType, metadataId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MosaicMetadataDTOAllOf {\n");
    sb.append("    metadataType: ").append(toIndentedString(metadataType)).append("\n");
    sb.append("    metadataId: ").append(toIndentedString(metadataId)).append("\n");
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

