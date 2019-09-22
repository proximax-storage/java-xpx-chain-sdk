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
import io.proximax.sdk.gen.model.MosaicDirectionEnum;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * MosaicSupplyChangeTransactionBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class MosaicSupplyChangeTransactionBodyDTO {
  public static final String SERIALIZED_NAME_MOSAIC_ID = "mosaicId";
  @SerializedName(SERIALIZED_NAME_MOSAIC_ID)
  private UInt64DTO mosaicId = new UInt64DTO();

  public static final String SERIALIZED_NAME_DIRECTION = "direction";
  @SerializedName(SERIALIZED_NAME_DIRECTION)
  private MosaicDirectionEnum direction;

  public static final String SERIALIZED_NAME_DELTA = "delta";
  @SerializedName(SERIALIZED_NAME_DELTA)
  private UInt64DTO delta = new UInt64DTO();

  public MosaicSupplyChangeTransactionBodyDTO mosaicId(UInt64DTO mosaicId) {
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

  public MosaicSupplyChangeTransactionBodyDTO direction(MosaicDirectionEnum direction) {
    this.direction = direction;
    return this;
  }

   /**
   * Get direction
   * @return direction
  **/
  @ApiModelProperty(required = true, value = "")
  public MosaicDirectionEnum getDirection() {
    return direction;
  }

  public void setDirection(MosaicDirectionEnum direction) {
    this.direction = direction;
  }

  public MosaicSupplyChangeTransactionBodyDTO delta(UInt64DTO delta) {
    this.delta = delta;
    return this;
  }

   /**
   * Get delta
   * @return delta
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getDelta() {
    return delta;
  }

  public void setDelta(UInt64DTO delta) {
    this.delta = delta;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MosaicSupplyChangeTransactionBodyDTO mosaicSupplyChangeTransactionBodyDTO = (MosaicSupplyChangeTransactionBodyDTO) o;
    return Objects.equals(this.mosaicId, mosaicSupplyChangeTransactionBodyDTO.mosaicId) &&
        Objects.equals(this.direction, mosaicSupplyChangeTransactionBodyDTO.direction) &&
        Objects.equals(this.delta, mosaicSupplyChangeTransactionBodyDTO.delta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mosaicId, direction, delta);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MosaicSupplyChangeTransactionBodyDTO {\n");
    sb.append("    mosaicId: ").append(toIndentedString(mosaicId)).append("\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
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

