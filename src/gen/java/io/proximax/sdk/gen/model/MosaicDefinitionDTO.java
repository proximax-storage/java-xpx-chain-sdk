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
import io.proximax.sdk.gen.model.MosaicPropertiesDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * MosaicDefinitionDTO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-06-17T22:20:38.750+02:00")
public class MosaicDefinitionDTO {
  @SerializedName("mosaicId")
  private UInt64DTO mosaicId = null;

  @SerializedName("supply")
  private UInt64DTO supply = null;

  @SerializedName("height")
  private UInt64DTO height = null;

  @SerializedName("owner")
  private String owner = null;

  @SerializedName("revision")
  private Integer revision = null;

  @SerializedName("properties")
  private MosaicPropertiesDTO properties = null;

  @SerializedName("levy")
  private Object levy = null;

  public MosaicDefinitionDTO mosaicId(UInt64DTO mosaicId) {
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

  public MosaicDefinitionDTO supply(UInt64DTO supply) {
    this.supply = supply;
    return this;
  }

   /**
   * Get supply
   * @return supply
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getSupply() {
    return supply;
  }

  public void setSupply(UInt64DTO supply) {
    this.supply = supply;
  }

  public MosaicDefinitionDTO height(UInt64DTO height) {
    this.height = height;
    return this;
  }

   /**
   * Get height
   * @return height
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getHeight() {
    return height;
  }

  public void setHeight(UInt64DTO height) {
    this.height = height;
  }

  public MosaicDefinitionDTO owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Get owner
   * @return owner
  **/
  @ApiModelProperty(required = true, value = "")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public MosaicDefinitionDTO revision(Integer revision) {
    this.revision = revision;
    return this;
  }

   /**
   * Get revision
   * @return revision
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getRevision() {
    return revision;
  }

  public void setRevision(Integer revision) {
    this.revision = revision;
  }

  public MosaicDefinitionDTO properties(MosaicPropertiesDTO properties) {
    this.properties = properties;
    return this;
  }

   /**
   * Get properties
   * @return properties
  **/
  @ApiModelProperty(required = true, value = "")
  public MosaicPropertiesDTO getProperties() {
    return properties;
  }

  public void setProperties(MosaicPropertiesDTO properties) {
    this.properties = properties;
  }

  public MosaicDefinitionDTO levy(Object levy) {
    this.levy = levy;
    return this;
  }

   /**
   * Get levy
   * @return levy
  **/
  @ApiModelProperty(required = true, value = "")
  public Object getLevy() {
    return levy;
  }

  public void setLevy(Object levy) {
    this.levy = levy;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MosaicDefinitionDTO mosaicDefinitionDTO = (MosaicDefinitionDTO) o;
    return Objects.equals(this.mosaicId, mosaicDefinitionDTO.mosaicId) &&
        Objects.equals(this.supply, mosaicDefinitionDTO.supply) &&
        Objects.equals(this.height, mosaicDefinitionDTO.height) &&
        Objects.equals(this.owner, mosaicDefinitionDTO.owner) &&
        Objects.equals(this.revision, mosaicDefinitionDTO.revision) &&
        Objects.equals(this.properties, mosaicDefinitionDTO.properties) &&
        Objects.equals(this.levy, mosaicDefinitionDTO.levy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mosaicId, supply, height, owner, revision, properties, levy);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MosaicDefinitionDTO {\n");
    
    sb.append("    mosaicId: ").append(toIndentedString(mosaicId)).append("\n");
    sb.append("    supply: ").append(toIndentedString(supply)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    levy: ").append(toIndentedString(levy)).append("\n");
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

