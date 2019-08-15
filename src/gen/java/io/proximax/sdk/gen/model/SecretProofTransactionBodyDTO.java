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
import io.proximax.sdk.gen.model.HashAlgorithmEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * SecretProofTransactionBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-08-11T15:04:35.591+02:00[Europe/Prague]")
public class SecretProofTransactionBodyDTO {
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

  public SecretProofTransactionBodyDTO hashAlgorithm(HashAlgorithmEnum hashAlgorithm) {
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

  public SecretProofTransactionBodyDTO secret(String secret) {
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

  public SecretProofTransactionBodyDTO recipient(String recipient) {
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

  public SecretProofTransactionBodyDTO proof(String proof) {
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
    SecretProofTransactionBodyDTO secretProofTransactionBodyDTO = (SecretProofTransactionBodyDTO) o;
    return Objects.equals(this.hashAlgorithm, secretProofTransactionBodyDTO.hashAlgorithm) &&
        Objects.equals(this.secret, secretProofTransactionBodyDTO.secret) &&
        Objects.equals(this.recipient, secretProofTransactionBodyDTO.recipient) &&
        Objects.equals(this.proof, secretProofTransactionBodyDTO.proof);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashAlgorithm, secret, recipient, proof);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecretProofTransactionBodyDTO {\n");
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

