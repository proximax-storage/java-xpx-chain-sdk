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

import io.proximax.sdk.infrastructure.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;

/**
 * MosaicNameDTO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-29T09:43:14.260+02:00")
public class MosaicNameDTO {
  @SerializedName("parentId")
  private UInt64DTO parentId = null;

  @SerializedName("mosaicId")
  private UInt64DTO mosaicId = null;

  @SerializedName("name")
  private String name = null;

  public MosaicNameDTO parentId(UInt64DTO parentId) {
    this.parentId = parentId;
    return this;
  }

   /**
   * Get parentId
   * @return parentId
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getParentId() {
    return parentId;
  }

  public void setParentId(UInt64DTO parentId) {
    this.parentId = parentId;
  }

  public MosaicNameDTO mosaicId(UInt64DTO mosaicId) {
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

  public MosaicNameDTO name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(required = true, value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MosaicNameDTO mosaicNameDTO = (MosaicNameDTO) o;
    return Objects.equals(this.parentId, mosaicNameDTO.parentId) &&
        Objects.equals(this.mosaicId, mosaicNameDTO.mosaicId) &&
        Objects.equals(this.name, mosaicNameDTO.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parentId, mosaicId, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MosaicNameDTO {\n");
    
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    mosaicId: ").append(toIndentedString(mosaicId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

