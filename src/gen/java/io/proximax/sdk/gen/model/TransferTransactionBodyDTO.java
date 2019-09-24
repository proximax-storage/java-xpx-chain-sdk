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
import io.proximax.sdk.gen.model.MessageDTO;
import io.proximax.sdk.gen.model.MosaicDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TransferTransactionBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class TransferTransactionBodyDTO {
  public static final String SERIALIZED_NAME_RECIPIENT = "recipient";
  @SerializedName(SERIALIZED_NAME_RECIPIENT)
  private String recipient;

  public static final String SERIALIZED_NAME_MOSAICS = "mosaics";
  @SerializedName(SERIALIZED_NAME_MOSAICS)
  private List<MosaicDTO> mosaics = new ArrayList<>();

  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private MessageDTO message = null;

  public TransferTransactionBodyDTO recipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

   /**
   * If the bit 0 of byte 0 is not set (like in 0x90), then it is a regular address. Else (e.g. 0x91) it represents a namespace id which starts at byte 1.
   * @return recipient
  **/
  @ApiModelProperty(example = "909B5339654B16CF86F55EAEB1C8708A033286B3EBB314A792", required = true, value = "If the bit 0 of byte 0 is not set (like in 0x90), then it is a regular address. Else (e.g. 0x91) it represents a namespace id which starts at byte 1.")
  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public TransferTransactionBodyDTO mosaics(List<MosaicDTO> mosaics) {
    this.mosaics = mosaics;
    return this;
  }

  public TransferTransactionBodyDTO addMosaicsItem(MosaicDTO mosaicsItem) {
    this.mosaics.add(mosaicsItem);
    return this;
  }

   /**
   * The array of mosaics sent to the recipient. If the most significant bit of byte 0 is set, a namespaceId (alias) is used instead of a instead of a mosaicId corresponds to a mosaicId.
   * @return mosaics
  **/
  @ApiModelProperty(required = true, value = "The array of mosaics sent to the recipient. If the most significant bit of byte 0 is set, a namespaceId (alias) is used instead of a instead of a mosaicId corresponds to a mosaicId.")
  public List<MosaicDTO> getMosaics() {
    return mosaics;
  }

  public void setMosaics(List<MosaicDTO> mosaics) {
    this.mosaics = mosaics;
  }

  public TransferTransactionBodyDTO message(MessageDTO message) {
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(required = true, value = "")
  public MessageDTO getMessage() {
    return message;
  }

  public void setMessage(MessageDTO message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferTransactionBodyDTO transferTransactionBodyDTO = (TransferTransactionBodyDTO) o;
    return Objects.equals(this.recipient, transferTransactionBodyDTO.recipient) &&
        Objects.equals(this.mosaics, transferTransactionBodyDTO.mosaics) &&
        Objects.equals(this.message, transferTransactionBodyDTO.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipient, mosaics, message);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferTransactionBodyDTO {\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    mosaics: ").append(toIndentedString(mosaics)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

