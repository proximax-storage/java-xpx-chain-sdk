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
import io.proximax.sdk.gen.model.UInt64DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * BlockchainUpgradeBodyDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-09-22T22:57:50.932+02:00[Europe/Prague]")
public class BlockchainUpgradeBodyDTO {
  public static final String SERIALIZED_NAME_UPGRADE_PERIOD = "upgradePeriod";
  @SerializedName(SERIALIZED_NAME_UPGRADE_PERIOD)
  private UInt64DTO upgradePeriod = new UInt64DTO();

  public static final String SERIALIZED_NAME_NEW_BLOCK_CHAIN_VERSION = "newBlockChainVersion";
  @SerializedName(SERIALIZED_NAME_NEW_BLOCK_CHAIN_VERSION)
  private UInt64DTO newBlockChainVersion = new UInt64DTO();

  public BlockchainUpgradeBodyDTO upgradePeriod(UInt64DTO upgradePeriod) {
    this.upgradePeriod = upgradePeriod;
    return this;
  }

   /**
   * Get upgradePeriod
   * @return upgradePeriod
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getUpgradePeriod() {
    return upgradePeriod;
  }

  public void setUpgradePeriod(UInt64DTO upgradePeriod) {
    this.upgradePeriod = upgradePeriod;
  }

  public BlockchainUpgradeBodyDTO newBlockChainVersion(UInt64DTO newBlockChainVersion) {
    this.newBlockChainVersion = newBlockChainVersion;
    return this;
  }

   /**
   * Get newBlockChainVersion
   * @return newBlockChainVersion
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getNewBlockChainVersion() {
    return newBlockChainVersion;
  }

  public void setNewBlockChainVersion(UInt64DTO newBlockChainVersion) {
    this.newBlockChainVersion = newBlockChainVersion;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockchainUpgradeBodyDTO blockchainUpgradeBodyDTO = (BlockchainUpgradeBodyDTO) o;
    return Objects.equals(this.upgradePeriod, blockchainUpgradeBodyDTO.upgradePeriod) &&
        Objects.equals(this.newBlockChainVersion, blockchainUpgradeBodyDTO.newBlockChainVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(upgradePeriod, newBlockChainVersion);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlockchainUpgradeBodyDTO {\n");
    sb.append("    upgradePeriod: ").append(toIndentedString(upgradePeriod)).append("\n");
    sb.append("    newBlockChainVersion: ").append(toIndentedString(newBlockChainVersion)).append("\n");
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

