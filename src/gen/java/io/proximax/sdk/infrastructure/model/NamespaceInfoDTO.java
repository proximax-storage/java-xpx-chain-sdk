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

import io.proximax.sdk.infrastructure.model.NamespaceDTO;
import io.proximax.sdk.infrastructure.model.NamespaceMetaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;

/**
 * NamespaceInfoDTO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-29T09:43:14.260+02:00")
public class NamespaceInfoDTO {
  @SerializedName("meta")
  private NamespaceMetaDTO meta = null;

  @SerializedName("namespace")
  private NamespaceDTO namespace = null;

  public NamespaceInfoDTO meta(NamespaceMetaDTO meta) {
    this.meta = meta;
    return this;
  }

   /**
   * Get meta
   * @return meta
  **/
  @ApiModelProperty(required = true, value = "")
  public NamespaceMetaDTO getMeta() {
    return meta;
  }

  public void setMeta(NamespaceMetaDTO meta) {
    this.meta = meta;
  }

  public NamespaceInfoDTO namespace(NamespaceDTO namespace) {
    this.namespace = namespace;
    return this;
  }

   /**
   * Get namespace
   * @return namespace
  **/
  @ApiModelProperty(required = true, value = "")
  public NamespaceDTO getNamespace() {
    return namespace;
  }

  public void setNamespace(NamespaceDTO namespace) {
    this.namespace = namespace;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NamespaceInfoDTO namespaceInfoDTO = (NamespaceInfoDTO) o;
    return Objects.equals(this.meta, namespaceInfoDTO.meta) &&
        Objects.equals(this.namespace, namespaceInfoDTO.namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(meta, namespace);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NamespaceInfoDTO {\n");
    
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
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

