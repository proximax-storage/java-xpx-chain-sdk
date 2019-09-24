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
import io.proximax.sdk.gen.model.MetadataModificationDTO;
import io.proximax.sdk.gen.model.MetadataTypeEnum;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * NamespaceMetadataBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class NamespaceMetadataBodyDTO {
  public static final String SERIALIZED_NAME_METADATA_ID = "metadataId";
  @SerializedName(SERIALIZED_NAME_METADATA_ID)
  private UInt64DTO metadataId = new UInt64DTO();

  public static final String SERIALIZED_NAME_METADATA_TYPE = "metadataType";
  @SerializedName(SERIALIZED_NAME_METADATA_TYPE)
  private MetadataTypeEnum metadataType;

  public static final String SERIALIZED_NAME_MODIFICATIONS = "modifications";
  @SerializedName(SERIALIZED_NAME_MODIFICATIONS)
  private List<MetadataModificationDTO> modifications = new ArrayList<>();

  public NamespaceMetadataBodyDTO metadataId(UInt64DTO metadataId) {
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

  public NamespaceMetadataBodyDTO metadataType(MetadataTypeEnum metadataType) {
    this.metadataType = metadataType;
    return this;
  }

   /**
   * Get metadataType
   * @return metadataType
  **/
  @ApiModelProperty(required = true, value = "")
  public MetadataTypeEnum getMetadataType() {
    return metadataType;
  }

  public void setMetadataType(MetadataTypeEnum metadataType) {
    this.metadataType = metadataType;
  }

  public NamespaceMetadataBodyDTO modifications(List<MetadataModificationDTO> modifications) {
    this.modifications = modifications;
    return this;
  }

  public NamespaceMetadataBodyDTO addModificationsItem(MetadataModificationDTO modificationsItem) {
    this.modifications.add(modificationsItem);
    return this;
  }

   /**
   * The array of metadata modifications.
   * @return modifications
  **/
  @ApiModelProperty(required = true, value = "The array of metadata modifications.")
  public List<MetadataModificationDTO> getModifications() {
    return modifications;
  }

  public void setModifications(List<MetadataModificationDTO> modifications) {
    this.modifications = modifications;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NamespaceMetadataBodyDTO namespaceMetadataBodyDTO = (NamespaceMetadataBodyDTO) o;
    return Objects.equals(this.metadataId, namespaceMetadataBodyDTO.metadataId) &&
        Objects.equals(this.metadataType, namespaceMetadataBodyDTO.metadataType) &&
        Objects.equals(this.modifications, namespaceMetadataBodyDTO.modifications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadataId, metadataType, modifications);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NamespaceMetadataBodyDTO {\n");
    sb.append("    metadataId: ").append(toIndentedString(metadataId)).append("\n");
    sb.append("    metadataType: ").append(toIndentedString(metadataType)).append("\n");
    sb.append("    modifications: ").append(toIndentedString(modifications)).append("\n");
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

