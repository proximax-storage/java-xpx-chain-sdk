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

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

public class SecretLockWithMetaDTO {
    public static final String SERIALIZED_NAME_META = "meta";
    @SerializedName(SERIALIZED_NAME_META)
    private MetaLockDTO meta = null;

    public static final String SERIALIZED_NAME_LOCK = "lock";
    @SerializedName(SERIALIZED_NAME_LOCK)
    private SecretLockInfoDTO lock = null;

    public SecretLockWithMetaDTO metadataEntry(MetaLockDTO meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Get meta
     * 
     * @return meta
     **/
    @ApiModelProperty(required = true, value = "")
    public MetaLockDTO getMeta() {
        return meta;
    }

    public void setMetadataEntry(MetaLockDTO meta) {
        this.meta = meta;
    }

    public SecretLockWithMetaDTO lock(SecretLockInfoDTO lock) {
        this.lock = lock;
        return this;
    }

    /**
     * Get lock
     * 
     * @return lock
     **/
    @ApiModelProperty(required = true, value = "")
    public SecretLockInfoDTO getLock() {
        return lock;
    }

    public void setLock(SecretLockInfoDTO lock) {
        this.lock = lock;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecretLockWithMetaDTO secretLockWithMeta = (SecretLockWithMetaDTO) o;
        return Objects.equals(this.meta,
                secretLockWithMeta.meta) &&
                Objects.equals(this.lock, secretLockWithMeta.lock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, lock);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SecretLockWithMeta {\n");
        sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
        sb.append("    lock: ").append(toIndentedString(lock)).append("\n");
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