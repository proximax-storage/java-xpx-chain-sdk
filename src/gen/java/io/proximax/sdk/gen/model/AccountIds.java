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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountIds
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-07-05T16:42:36.122+02:00[Europe/Prague]")
public class AccountIds {
  public static final String SERIALIZED_NAME_PUBLIC_KEYS = "publicKeys";
  @SerializedName(SERIALIZED_NAME_PUBLIC_KEYS)
  private List<String> publicKeys = new ArrayList<>();

  public static final String SERIALIZED_NAME_ADDRESSES = "addresses";
  @SerializedName(SERIALIZED_NAME_ADDRESSES)
  private List<String> addresses = new ArrayList<>();

  public AccountIds publicKeys(List<String> publicKeys) {
    this.publicKeys = publicKeys;
    return this;
  }

  public AccountIds addPublicKeysItem(String publicKeysItem) {
    if (this.publicKeys == null) {
      this.publicKeys = new ArrayList<>();
    }
    this.publicKeys.add(publicKeysItem);
    return this;
  }

   /**
   * The array of public keys.
   * @return publicKeys
  **/
  @ApiModelProperty(example = "[\"AC1A6E1D8DE5B17D2C6B1293F1CAD3829EEACF38D09311BB3C8E5A880092DE26\"]", value = "The array of public keys.")
  public List<String> getPublicKeys() {
    return publicKeys;
  }

  public void setPublicKeys(List<String> publicKeys) {
    this.publicKeys = publicKeys;
  }

  public AccountIds addresses(List<String> addresses) {
    this.addresses = addresses;
    return this;
  }

  public AccountIds addAddressesItem(String addressesItem) {
    if (this.addresses == null) {
      this.addresses = new ArrayList<>();
    }
    this.addresses.add(addressesItem);
    return this;
  }

   /**
   * The array of addresses.
   * @return addresses
  **/
  @ApiModelProperty(example = "[\"SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY\",\"SBCPGZ3S2SCC3YHBBTYDCUZV4ZZEPHM2KGCP4QXX\"]", value = "The array of addresses.")
  public List<String> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<String> addresses) {
    this.addresses = addresses;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountIds accountIds = (AccountIds) o;
    return Objects.equals(this.publicKeys, accountIds.publicKeys) &&
        Objects.equals(this.addresses, accountIds.addresses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publicKeys, addresses);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountIds {\n");
    sb.append("    publicKeys: ").append(toIndentedString(publicKeys)).append("\n");
    sb.append("    addresses: ").append(toIndentedString(addresses)).append("\n");
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
