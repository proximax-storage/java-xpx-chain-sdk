/*
 * ProximaX Chain REST API Reference
 * ProximaX Chain REST API Reference
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
import io.proximax.sdk.gen.model.NamespaceTypeEnum;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * RegisterNamespaceTransactionBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-07-05T16:42:36.122+02:00[Europe/Prague]")
public class RegisterNamespaceTransactionBodyDTO {
  public static final String SERIALIZED_NAME_NAMESPACE_TYPE = "namespaceType";
  @SerializedName(SERIALIZED_NAME_NAMESPACE_TYPE)
  private NamespaceTypeEnum namespaceType;

  public static final String SERIALIZED_NAME_DURATION = "duration";
  @SerializedName(SERIALIZED_NAME_DURATION)
  private UInt64DTO duration = new UInt64DTO();

  public static final String SERIALIZED_NAME_NAMESPACE_ID = "namespaceId";
  @SerializedName(SERIALIZED_NAME_NAMESPACE_ID)
  private UInt64DTO namespaceId = new UInt64DTO();

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_PARENT_ID = "parentId";
  @SerializedName(SERIALIZED_NAME_PARENT_ID)
  private UInt64DTO parentId = new UInt64DTO();

  public RegisterNamespaceTransactionBodyDTO namespaceType(NamespaceTypeEnum namespaceType) {
    this.namespaceType = namespaceType;
    return this;
  }

   /**
   * Get namespaceType
   * @return namespaceType
  **/
  @ApiModelProperty(required = true, value = "")
  public NamespaceTypeEnum getNamespaceType() {
    return namespaceType;
  }

  public void setNamespaceType(NamespaceTypeEnum namespaceType) {
    this.namespaceType = namespaceType;
  }

  public RegisterNamespaceTransactionBodyDTO duration(UInt64DTO duration) {
    this.duration = duration;
    return this;
  }

   /**
   * Get duration
   * @return duration
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getDuration() {
    return duration;
  }

  public void setDuration(UInt64DTO duration) {
    this.duration = duration;
  }

  public RegisterNamespaceTransactionBodyDTO namespaceId(UInt64DTO namespaceId) {
    this.namespaceId = namespaceId;
    return this;
  }

   /**
   * Get namespaceId
   * @return namespaceId
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getNamespaceId() {
    return namespaceId;
  }

  public void setNamespaceId(UInt64DTO namespaceId) {
    this.namespaceId = namespaceId;
  }

  public RegisterNamespaceTransactionBodyDTO name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The unique namespace name.
   * @return name
  **/
  @ApiModelProperty(required = true, value = "The unique namespace name.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RegisterNamespaceTransactionBodyDTO parentId(UInt64DTO parentId) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterNamespaceTransactionBodyDTO registerNamespaceTransactionBodyDTO = (RegisterNamespaceTransactionBodyDTO) o;
    return Objects.equals(this.namespaceType, registerNamespaceTransactionBodyDTO.namespaceType) &&
        Objects.equals(this.duration, registerNamespaceTransactionBodyDTO.duration) &&
        Objects.equals(this.namespaceId, registerNamespaceTransactionBodyDTO.namespaceId) &&
        Objects.equals(this.name, registerNamespaceTransactionBodyDTO.name) &&
        Objects.equals(this.parentId, registerNamespaceTransactionBodyDTO.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespaceType, duration, namespaceId, name, parentId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterNamespaceTransactionBodyDTO {\n");
    sb.append("    namespaceType: ").append(toIndentedString(namespaceType)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    namespaceId: ").append(toIndentedString(namespaceId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
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

