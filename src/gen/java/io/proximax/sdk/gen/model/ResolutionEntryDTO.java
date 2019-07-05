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
import io.proximax.sdk.gen.model.SourceDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * ResolutionEntryDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-07-05T16:42:36.122+02:00[Europe/Prague]")
public class ResolutionEntryDTO {
  public static final String SERIALIZED_NAME_SOURCE = "source";
  @SerializedName(SERIALIZED_NAME_SOURCE)
  private SourceDTO source = null;

  public static final String SERIALIZED_NAME_RESOLVED = "resolved";
  @SerializedName(SERIALIZED_NAME_RESOLVED)
  private UInt64DTO resolved = new UInt64DTO();

  public ResolutionEntryDTO source(SourceDTO source) {
    this.source = source;
    return this;
  }

   /**
   * Get source
   * @return source
  **/
  @ApiModelProperty(required = true, value = "")
  public SourceDTO getSource() {
    return source;
  }

  public void setSource(SourceDTO source) {
    this.source = source;
  }

  public ResolutionEntryDTO resolved(UInt64DTO resolved) {
    this.resolved = resolved;
    return this;
  }

   /**
   * Get resolved
   * @return resolved
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getResolved() {
    return resolved;
  }

  public void setResolved(UInt64DTO resolved) {
    this.resolved = resolved;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResolutionEntryDTO resolutionEntryDTO = (ResolutionEntryDTO) o;
    return Objects.equals(this.source, resolutionEntryDTO.source) &&
        Objects.equals(this.resolved, resolutionEntryDTO.resolved);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, resolved);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResolutionEntryDTO {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    resolved: ").append(toIndentedString(resolved)).append("\n");
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

