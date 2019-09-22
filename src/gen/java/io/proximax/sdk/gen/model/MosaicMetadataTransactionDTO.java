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
import io.proximax.sdk.gen.model.EntityTypeEnum;
import io.proximax.sdk.gen.model.MetadataModificationDTO;
import io.proximax.sdk.gen.model.MetadataTypeEnum;
import io.proximax.sdk.gen.model.MosaicMetadataBodyDTO;
import io.proximax.sdk.gen.model.TransactionDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Transaction that addes metadata to mosaic.
 */
@ApiModel(description = "Transaction that addes metadata to mosaic.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class MosaicMetadataTransactionDTO {
  public static final String SERIALIZED_NAME_SIGNATURE = "signature";
  @SerializedName(SERIALIZED_NAME_SIGNATURE)
  private String signature;

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

  public static final String SERIALIZED_NAME_METADATA_ID = "metadataId";
  @SerializedName(SERIALIZED_NAME_METADATA_ID)
  private UInt64DTO metadataId = new UInt64DTO();

  public static final String SERIALIZED_NAME_METADATA_TYPE = "metadataType";
  @SerializedName(SERIALIZED_NAME_METADATA_TYPE)
  private MetadataTypeEnum metadataType;

  public static final String SERIALIZED_NAME_MODIFICATIONS = "modifications";
  @SerializedName(SERIALIZED_NAME_MODIFICATIONS)
  private List<MetadataModificationDTO> modifications = new ArrayList<>();

  public MosaicMetadataTransactionDTO signature(String signature) {
    this.signature = signature;
    return this;
  }

   /**
   * The signature of the entity. The signature was generated by the signer and can be used to validate tha the entity data was not modified by a node. 
   * @return signature
  **/
  @ApiModelProperty(example = "D8047EB8285077D9900EDD42F4081070DDB26E08E7F15A3E29642C80CF0C7D68340682DC9868C73EB09744D6298146D8DFC02ED47070FC81FB47D6F73B33EF0A", required = true, value = "The signature of the entity. The signature was generated by the signer and can be used to validate tha the entity data was not modified by a node. ")
  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public MosaicMetadataTransactionDTO signer(String signer) {
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

  public MosaicMetadataTransactionDTO version(Integer version) {
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

  public MosaicMetadataTransactionDTO type(EntityTypeEnum type) {
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

  public MosaicMetadataTransactionDTO maxFee(UInt64DTO maxFee) {
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

  public MosaicMetadataTransactionDTO deadline(UInt64DTO deadline) {
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

  public MosaicMetadataTransactionDTO metadataId(UInt64DTO metadataId) {
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

  public MosaicMetadataTransactionDTO metadataType(MetadataTypeEnum metadataType) {
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

  public MosaicMetadataTransactionDTO modifications(List<MetadataModificationDTO> modifications) {
    this.modifications = modifications;
    return this;
  }

  public MosaicMetadataTransactionDTO addModificationsItem(MetadataModificationDTO modificationsItem) {
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
    MosaicMetadataTransactionDTO mosaicMetadataTransactionDTO = (MosaicMetadataTransactionDTO) o;
    return Objects.equals(this.signature, mosaicMetadataTransactionDTO.signature) &&
        Objects.equals(this.signer, mosaicMetadataTransactionDTO.signer) &&
        Objects.equals(this.version, mosaicMetadataTransactionDTO.version) &&
        Objects.equals(this.type, mosaicMetadataTransactionDTO.type) &&
        Objects.equals(this.maxFee, mosaicMetadataTransactionDTO.maxFee) &&
        Objects.equals(this.deadline, mosaicMetadataTransactionDTO.deadline) &&
        Objects.equals(this.metadataId, mosaicMetadataTransactionDTO.metadataId) &&
        Objects.equals(this.metadataType, mosaicMetadataTransactionDTO.metadataType) &&
        Objects.equals(this.modifications, mosaicMetadataTransactionDTO.modifications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signature, signer, version, type, maxFee, deadline, metadataId, metadataType, modifications);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MosaicMetadataTransactionDTO {\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    signer: ").append(toIndentedString(signer)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    maxFee: ").append(toIndentedString(maxFee)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
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

