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
import io.proximax.sdk.gen.model.HashAlgorithmEnum;
import io.proximax.sdk.gen.model.SecretProofTransactionBodyDTO;
import io.proximax.sdk.gen.model.TransactionDTO;
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Transaction that revealed a proof.
 */
@ApiModel(description = "Transaction that revealed a proof.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class SecretProofTransactionDTO {
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

  public static final String SERIALIZED_NAME_HASH_ALGORITHM = "hashAlgorithm";
  @SerializedName(SERIALIZED_NAME_HASH_ALGORITHM)
  private HashAlgorithmEnum hashAlgorithm;

  public static final String SERIALIZED_NAME_SECRET = "secret";
  @SerializedName(SERIALIZED_NAME_SECRET)
  private String secret;

  public static final String SERIALIZED_NAME_RECIPIENT = "recipient";
  @SerializedName(SERIALIZED_NAME_RECIPIENT)
  private String recipient;

  public static final String SERIALIZED_NAME_PROOF = "proof";
  @SerializedName(SERIALIZED_NAME_PROOF)
  private String proof;

  public SecretProofTransactionDTO signature(String signature) {
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

  public SecretProofTransactionDTO signer(String signer) {
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

  public SecretProofTransactionDTO version(Integer version) {
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

  public SecretProofTransactionDTO type(EntityTypeEnum type) {
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

  public SecretProofTransactionDTO maxFee(UInt64DTO maxFee) {
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

  public SecretProofTransactionDTO deadline(UInt64DTO deadline) {
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

  public SecretProofTransactionDTO hashAlgorithm(HashAlgorithmEnum hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
    return this;
  }

   /**
   * Get hashAlgorithm
   * @return hashAlgorithm
  **/
  @ApiModelProperty(required = true, value = "")
  public HashAlgorithmEnum getHashAlgorithm() {
    return hashAlgorithm;
  }

  public void setHashAlgorithm(HashAlgorithmEnum hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
  }

  public SecretProofTransactionDTO secret(String secret) {
    this.secret = secret;
    return this;
  }

   /**
   * The proof hashed.
   * @return secret
  **/
  @ApiModelProperty(example = "759DE013523D2C36F54BA184611AC887C6C994412E28B35221098E084050249B", required = true, value = "The proof hashed.")
  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public SecretProofTransactionDTO recipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

   /**
   * The address in hexadecimal that received the funds.
   * @return recipient
  **/
  @ApiModelProperty(example = "90829855740901BAA6EF35C91CF00D0059212BF49FBAEC3277", value = "The address in hexadecimal that received the funds.")
  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public SecretProofTransactionDTO proof(String proof) {
    this.proof = proof;
    return this;
  }

   /**
   * The original random set of bytes.
   * @return proof
  **/
  @ApiModelProperty(required = true, value = "The original random set of bytes.")
  public String getProof() {
    return proof;
  }

  public void setProof(String proof) {
    this.proof = proof;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecretProofTransactionDTO secretProofTransactionDTO = (SecretProofTransactionDTO) o;
    return Objects.equals(this.signature, secretProofTransactionDTO.signature) &&
        Objects.equals(this.signer, secretProofTransactionDTO.signer) &&
        Objects.equals(this.version, secretProofTransactionDTO.version) &&
        Objects.equals(this.type, secretProofTransactionDTO.type) &&
        Objects.equals(this.maxFee, secretProofTransactionDTO.maxFee) &&
        Objects.equals(this.deadline, secretProofTransactionDTO.deadline) &&
        Objects.equals(this.hashAlgorithm, secretProofTransactionDTO.hashAlgorithm) &&
        Objects.equals(this.secret, secretProofTransactionDTO.secret) &&
        Objects.equals(this.recipient, secretProofTransactionDTO.recipient) &&
        Objects.equals(this.proof, secretProofTransactionDTO.proof);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signature, signer, version, type, maxFee, deadline, hashAlgorithm, secret, recipient, proof);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecretProofTransactionDTO {\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    signer: ").append(toIndentedString(signer)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    maxFee: ").append(toIndentedString(maxFee)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
    sb.append("    hashAlgorithm: ").append(toIndentedString(hashAlgorithm)).append("\n");
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    proof: ").append(toIndentedString(proof)).append("\n");
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

