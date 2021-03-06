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
import io.swagger.annotations.ApiModel;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * The supply modification direction: * 0  - Decrease. * 1  - Increase. 
 */
@JsonAdapter(MosaicDirectionEnum.Adapter.class)
public enum MosaicDirectionEnum {
  
  NUMBER_0(0),
  
  NUMBER_1(1);

  private Integer value;

  MosaicDirectionEnum(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static MosaicDirectionEnum fromValue(Integer value) {
    for (MosaicDirectionEnum b : MosaicDirectionEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<MosaicDirectionEnum> {
    @Override
    public void write(final JsonWriter jsonWriter, final MosaicDirectionEnum enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public MosaicDirectionEnum read(final JsonReader jsonReader) throws IOException {
      Integer value = jsonReader.nextInt();
      return MosaicDirectionEnum.fromValue(value);
    }
  }
}

