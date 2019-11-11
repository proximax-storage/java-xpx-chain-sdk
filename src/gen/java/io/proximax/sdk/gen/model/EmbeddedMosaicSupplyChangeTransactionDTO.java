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
import io.proximax.sdk.gen.model.EmbeddedTransactionDTO;
import io.proximax.sdk.gen.model.EntityTypeEnum;
import io.proximax.sdk.gen.model.MosaicDirectionEnum;
import io.proximax.sdk.gen.model.MosaicSupplyChangeTransactionBodyDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * EmbeddedMosaicSupplyChangeTransactionDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class EmbeddedMosaicSupplyChangeTransactionDTO {
  public static final String SERIALIZED_NAME_SIGNER = "signer";
  @SerializedName(SERIALIZED_NAME_SIGNER)
  private String signer;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private Integer version;

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private EntityTypeEnum type;

  public static final String SERIALIZED_NAME_MAX_FEE = "max_fee";
  @SerializedName(SERIALIZED_NAME_MAX_FEE)
  private UInt64DTO maxFee = new UInt64DTO();

  public static final String SERIALIZED_NAME_DEADLINE = "deadline";
  @SerializedName(SERIALIZED_NAME_DEADLINE)
  private UInt64DTO deadline = new UInt64DTO();

  public static final String SERIALIZED_NAME_MOSAIC_ID = "mosaicId";
  @SerializedName(SERIALIZED_NAME_MOSAIC_ID)
  private UInt64DTO mosaicId = new UInt64DTO();

  public static final String SERIALIZED_NAME_DIRECTION = "direction";
  @SerializedName(SERIALIZED_NAME_DIRECTION)
  private MosaicDirectionEnum direction;

  public static final String SERIALIZED_NAME_DELTA = "delta";
  @SerializedName(SERIALIZED_NAME_DELTA)
  private UInt64DTO delta = new UInt64DTO();

  public EmbeddedMosaicSupplyChangeTransactionDTO signer(String signer) {
    this.signer = signer;
    return this;
  }

   /**
   * The public key of the entity signer formatted as hexadecimal.
   * @return signer
  **/
  @ApiModelProperty(example = "D799E559AB735A5E62187306E80C1679EE3E1170532280C968D974E351CB412F", required = true, value = "The public key of the entity signer formatted as hexadecimal.")
  public String getSigner() {
    return signer;
  }

  public void setSigner(String signer) {
    this.signer = signer;
  }

  public EmbeddedMosaicSupplyChangeTransactionDTO version(Integer version) {
    this.version = version;
    return this;
  }

   /**
   * The entity version. The higher byte represents the network identifier: * 0x68 (MAIN_NET) - Public main network. * 0x98 (TEST_NET) - Public test network. * 0x60 (MIJIN) - Private network. * 0x90 (MIJIN_TEST) - Private test network. 
   * @return version
  **/
  @ApiModelProperty(example = "36867", required = true, value = "The entity version. The higher byte represents the network identifier: * 0x68 (MAIN_NET) - Public main network. * 0x98 (TEST_NET) - Public test network. * 0x60 (MIJIN) - Private network. * 0x90 (MIJIN_TEST) - Private test network. ")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public EmbeddedMosaicSupplyChangeTransactionDTO type(EntityTypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(required = true, value = "")
  public EntityTypeEnum getType() {
    return type;
  }

  public void setType(EntityTypeEnum type) {
    this.type = type;
  }

  public EmbeddedMosaicSupplyChangeTransactionDTO maxFee(UInt64DTO maxFee) {
    this.maxFee = maxFee;
    return this;
  }

   /**
   * Get maxFee
   * @return maxFee
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getMaxFee() {
    return maxFee;
  }

  public void setMaxFee(UInt64DTO maxFee) {
    this.maxFee = maxFee;
  }

  public EmbeddedMosaicSupplyChangeTransactionDTO deadline(UInt64DTO deadline) {
    this.deadline = deadline;
    return this;
  }

   /**
   * Get deadline
   * @return deadline
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getDeadline() {
    return deadline;
  }

  public void setDeadline(UInt64DTO deadline) {
    this.deadline = deadline;
  }

  public EmbeddedMosaicSupplyChangeTransactionDTO mosaicId(UInt64DTO mosaicId) {
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

  public EmbeddedMosaicSupplyChangeTransactionDTO direction(MosaicDirectionEnum direction) {
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

  public EmbeddedMosaicSupplyChangeTransactionDTO delta(UInt64DTO delta) {
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
    EmbeddedMosaicSupplyChangeTransactionDTO embeddedMosaicSupplyChangeTransactionDTO = (EmbeddedMosaicSupplyChangeTransactionDTO) o;
    return Objects.equals(this.signer, embeddedMosaicSupplyChangeTransactionDTO.signer) &&
        Objects.equals(this.version, embeddedMosaicSupplyChangeTransactionDTO.version) &&
        Objects.equals(this.type, embeddedMosaicSupplyChangeTransactionDTO.type) &&
        Objects.equals(this.maxFee, embeddedMosaicSupplyChangeTransactionDTO.maxFee) &&
        Objects.equals(this.deadline, embeddedMosaicSupplyChangeTransactionDTO.deadline) &&
        Objects.equals(this.mosaicId, embeddedMosaicSupplyChangeTransactionDTO.mosaicId) &&
        Objects.equals(this.direction, embeddedMosaicSupplyChangeTransactionDTO.direction) &&
        Objects.equals(this.delta, embeddedMosaicSupplyChangeTransactionDTO.delta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signer, version, type, maxFee, deadline, mosaicId, direction, delta);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmbeddedMosaicSupplyChangeTransactionDTO {\n");
    sb.append("    signer: ").append(toIndentedString(signer)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    maxFee: ").append(toIndentedString(maxFee)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
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
