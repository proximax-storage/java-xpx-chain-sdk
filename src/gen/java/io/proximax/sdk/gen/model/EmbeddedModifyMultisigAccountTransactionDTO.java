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
import io.proximax.sdk.gen.model.CosignatoryModificationDTO;
import io.proximax.sdk.gen.model.EmbeddedTransactionDTO;
import io.proximax.sdk.gen.model.EntityTypeEnum;
import io.proximax.sdk.gen.model.ModifyMultisigAccountTransactionBodyDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EmbeddedModifyMultisigAccountTransactionDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-07-05T16:42:36.122+02:00[Europe/Prague]")
public class EmbeddedModifyMultisigAccountTransactionDTO {
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

  public static final String SERIALIZED_NAME_MIN_REMOVAL_DELTA = "minRemovalDelta";
  @SerializedName(SERIALIZED_NAME_MIN_REMOVAL_DELTA)
  private Integer minRemovalDelta;

  public static final String SERIALIZED_NAME_MIN_APPROVAL_DELTA = "minApprovalDelta";
  @SerializedName(SERIALIZED_NAME_MIN_APPROVAL_DELTA)
  private Integer minApprovalDelta;

  public static final String SERIALIZED_NAME_MODIFICATIONS = "modifications";
  @SerializedName(SERIALIZED_NAME_MODIFICATIONS)
  private List<CosignatoryModificationDTO> modifications = new ArrayList<>();

  public EmbeddedModifyMultisigAccountTransactionDTO signer(String signer) {
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

  public EmbeddedModifyMultisigAccountTransactionDTO version(Integer version) {
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

  public EmbeddedModifyMultisigAccountTransactionDTO type(EntityTypeEnum type) {
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

  public EmbeddedModifyMultisigAccountTransactionDTO maxFee(UInt64DTO maxFee) {
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

  public EmbeddedModifyMultisigAccountTransactionDTO deadline(UInt64DTO deadline) {
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

  public EmbeddedModifyMultisigAccountTransactionDTO minRemovalDelta(Integer minRemovalDelta) {
    this.minRemovalDelta = minRemovalDelta;
    return this;
  }

   /**
   * The number of signatures needed to remove a cosignatory. If we are modifying an existing multisig account, this indicates the relative change of the minimum cosignatories. 
   * @return minRemovalDelta
  **/
  @ApiModelProperty(example = "1", required = true, value = "The number of signatures needed to remove a cosignatory. If we are modifying an existing multisig account, this indicates the relative change of the minimum cosignatories. ")
  public Integer getMinRemovalDelta() {
    return minRemovalDelta;
  }

  public void setMinRemovalDelta(Integer minRemovalDelta) {
    this.minRemovalDelta = minRemovalDelta;
  }

  public EmbeddedModifyMultisigAccountTransactionDTO minApprovalDelta(Integer minApprovalDelta) {
    this.minApprovalDelta = minApprovalDelta;
    return this;
  }

   /**
   * The number of signatures needed to approve a transaction. If we are modifying an existing multisig account, this indicates the relative change of the minimum cosignatories. 
   * @return minApprovalDelta
  **/
  @ApiModelProperty(example = "1", required = true, value = "The number of signatures needed to approve a transaction. If we are modifying an existing multisig account, this indicates the relative change of the minimum cosignatories. ")
  public Integer getMinApprovalDelta() {
    return minApprovalDelta;
  }

  public void setMinApprovalDelta(Integer minApprovalDelta) {
    this.minApprovalDelta = minApprovalDelta;
  }

  public EmbeddedModifyMultisigAccountTransactionDTO modifications(List<CosignatoryModificationDTO> modifications) {
    this.modifications = modifications;
    return this;
  }

  public EmbeddedModifyMultisigAccountTransactionDTO addModificationsItem(CosignatoryModificationDTO modificationsItem) {
    this.modifications.add(modificationsItem);
    return this;
  }

   /**
   * The array of cosignatory accounts to add or delete.
   * @return modifications
  **/
  @ApiModelProperty(required = true, value = "The array of cosignatory accounts to add or delete.")
  public List<CosignatoryModificationDTO> getModifications() {
    return modifications;
  }

  public void setModifications(List<CosignatoryModificationDTO> modifications) {
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
    EmbeddedModifyMultisigAccountTransactionDTO embeddedModifyMultisigAccountTransactionDTO = (EmbeddedModifyMultisigAccountTransactionDTO) o;
    return Objects.equals(this.signer, embeddedModifyMultisigAccountTransactionDTO.signer) &&
        Objects.equals(this.version, embeddedModifyMultisigAccountTransactionDTO.version) &&
        Objects.equals(this.type, embeddedModifyMultisigAccountTransactionDTO.type) &&
        Objects.equals(this.maxFee, embeddedModifyMultisigAccountTransactionDTO.maxFee) &&
        Objects.equals(this.deadline, embeddedModifyMultisigAccountTransactionDTO.deadline) &&
        Objects.equals(this.minRemovalDelta, embeddedModifyMultisigAccountTransactionDTO.minRemovalDelta) &&
        Objects.equals(this.minApprovalDelta, embeddedModifyMultisigAccountTransactionDTO.minApprovalDelta) &&
        Objects.equals(this.modifications, embeddedModifyMultisigAccountTransactionDTO.modifications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signer, version, type, maxFee, deadline, minRemovalDelta, minApprovalDelta, modifications);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmbeddedModifyMultisigAccountTransactionDTO {\n");
    sb.append("    signer: ").append(toIndentedString(signer)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    maxFee: ").append(toIndentedString(maxFee)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
    sb.append("    minRemovalDelta: ").append(toIndentedString(minRemovalDelta)).append("\n");
    sb.append("    minApprovalDelta: ").append(toIndentedString(minApprovalDelta)).append("\n");
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

