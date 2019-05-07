/*
 * Catapult REST API Reference
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.13
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.proximax.sdk.infrastructure.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.proximax.sdk.infrastructure.model.FieldDTO;
import io.proximax.sdk.infrastructure.model.MetadataDTO;
import io.proximax.sdk.infrastructure.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MosaicMetadataDTO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-29T09:43:14.260+02:00")
public class MosaicMetadataDTO {
  @SerializedName("metadataType")
  private Integer metadataType = null;

  @SerializedName("fields")
  private List<FieldDTO> fields = new ArrayList<FieldDTO>();

  @SerializedName("metadataId")
  private UInt64DTO metadataId = null;

  public MosaicMetadataDTO metadataType(Integer metadataType) {
    this.metadataType = metadataType;
    return this;
  }

   /**
   * Get metadataType
   * @return metadataType
  **/
  @ApiModelProperty(example = "2", required = true, value = "")
  public Integer getMetadataType() {
    return metadataType;
  }

  public void setMetadataType(Integer metadataType) {
    this.metadataType = metadataType;
  }

  public MosaicMetadataDTO fields(List<FieldDTO> fields) {
    this.fields = fields;
    return this;
  }

  public MosaicMetadataDTO addFieldsItem(FieldDTO fieldsItem) {
    this.fields.add(fieldsItem);
    return this;
  }

   /**
   * Get fields
   * @return fields
  **/
  @ApiModelProperty(required = true, value = "")
  public List<FieldDTO> getFields() {
    return fields;
  }

  public void setFields(List<FieldDTO> fields) {
    this.fields = fields;
  }

  public MosaicMetadataDTO metadataId(UInt64DTO metadataId) {
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
    MosaicMetadataDTO mosaicMetadataDTO = (MosaicMetadataDTO) o;
    return Objects.equals(this.metadataType, mosaicMetadataDTO.metadataType) &&
        Objects.equals(this.fields, mosaicMetadataDTO.fields) &&
        Objects.equals(this.metadataId, mosaicMetadataDTO.metadataId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadataType, fields, metadataId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MosaicMetadataDTO {\n");
    
    sb.append("    metadataType: ").append(toIndentedString(metadataType)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
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

